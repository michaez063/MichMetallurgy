package ru.mich.michmetallurgy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;
import java.util.Optional;

import static net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity.isFuel;

public class BigBlastFurnaceBlockEntity extends BlockEntity implements MenuProvider {

    // Слоты: 0-1 (Топливо), 2-5 (Ингредиенты), 6-7 (Результат)
    private final ItemStackHandler itemHandler = new ItemStackHandler(8) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            // Слоты 6 и 7 — слоты результата (руками или воронкой вложить ничего нельзя)
            if (slot == 6 || slot == 7) {
                return false;
            }

            // Слоты 0 и 1 — ТОЛЬКО для топлива
            if (slot == 0 || slot == 1) {
                return isFuel(stack);
            }

            // Слоты 2, 3, 4, 5 — ТОЛЬКО для сырья (топливо сюда положить нельзя)
            if (slot >= 2 && slot <= 5) {
                return !isFuel(stack);
            }

            return super.isItemValid(slot, stack);
        }
    };

    private boolean isFuel(ItemStack stack) {
        if (stack.isEmpty()) return false;
        // В NeoForge 1.21.1 время горения у ItemStack запрашивается прямо из предмета:
        return stack.getBurnTime(RecipeType.BLASTING) > 0;
    }

    @Override
    public Component getDisplayName() {
        // Вставь сюда ключ перевода твоего блока
        return Component.translatable("block.michmetallurgy.big_blast_furnace");
    }

    // Переменные для синхронизации и логики печи
    private int litTime = 0;
    private int litDuration = 0;
    private int progress = 0;
    private int maxProgress = 66; // Значение по умолчанию
    private float storedExperience = 0;

    // Синхронизация данных с клиентом (интерфейсом)
    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> BigBlastFurnaceBlockEntity.this.litTime;
                case 1 -> BigBlastFurnaceBlockEntity.this.litDuration;
                case 2 -> BigBlastFurnaceBlockEntity.this.progress;
                case 3 -> BigBlastFurnaceBlockEntity.this.maxProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> BigBlastFurnaceBlockEntity.this.litTime = value;
                case 1 -> BigBlastFurnaceBlockEntity.this.litDuration = value;
                case 2 -> BigBlastFurnaceBlockEntity.this.progress = value;
                case 3 -> BigBlastFurnaceBlockEntity.this.maxProgress = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public BigBlastFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BIG_BLAST_FURNACE_BE.get(), pos, state);
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    // Проверка, горит ли сейчас печь
    private boolean isBurning() {
        return this.litTime > 0;
    }

    // === ГЛАВНЫЙ ТИК СЕРВЕРА (Логика работы) ===
    // Убедись, что метод стал static и принимает entity четвертым аргументом
    public static void tick(Level level, BlockPos pos, BlockState state, BigBlastFurnaceBlockEntity entity) {
        if (level.isClientSide()) return;

        boolean wasBurning = entity.isBurning();
        boolean setChanged = false;

        if (entity.isBurning()) {
            entity.litTime--;
        }

        boolean hasAnyRecipe = entity.hasRecipe();

// 1. Получаем результат текущего рецепта
// (Замени entity.getRecipeOutput() на твой метод получения ItemStack результата рецепта)
        ItemStack resultStack = entity.getCurrentResultStack();
        boolean canInsert = entity.canInsertResult(resultStack);

// Добавляем проверку canInsert во все ключевые условия:
        if (entity.isBurning() || (entity.hasFuelInSlots() && hasAnyRecipe && canInsert)) {

            // Сжигаем топливо, ТОЛЬКО если есть рецепт И результат помещается
            if (!entity.isBurning() && hasAnyRecipe && canInsert) {
                entity.consumeFuel();
                setChanged = true;
            }

            // Плавим, ТОЛЬКО если печь горит, есть рецепт И результат помещается
            if (entity.isBurning() && hasAnyRecipe && canInsert) {
                entity.maxProgress = entity.calculateMaxProgress();
                entity.progress++;

                if (entity.progress >= entity.maxProgress) {
                    entity.progress = 0;
                    entity.smeltAllItems();
                    setChanged = true;
                }
            } else {
                // Если слоты вывода забиты или нет рецепта — сбрасываем прогресс
                entity.progress = 0;
            }
        } else if (!entity.isBurning() && entity.progress > 0) {
            entity.progress = Math.max(0, entity.progress - 2);
        }

        if (wasBurning != entity.isBurning()) {
            setChanged = true;

            // Меняем состояние блока (LIT) на true или false в зависимости от того, горит ли печь
            state = state.setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT, entity.isBurning());

            // Обновляем блок в мире (флаг 3 означает, что мы обновляем и сервер, и клиент)
            level.setBlock(pos, state, 3);
        }

        if (setChanged) {
            entity.setChanged(); // Сохраняем изменения в мире
        }
    }

    // === НОВАЯ МЕХАНИКА: Динамический расчет времени ===
    private int calculateMaxProgress() {
        if (this.level == null) return 66;

        // 1. Ищем рецепт сплава
        var customRecipe = this.level.getRecipeManager().getRecipeFor(
                ModRecipes.BIG_BLAST_FURNACE_TYPE.get(),
                getRecipeInput(),
                this.level
        );

        // 2. Если сплав найден, берем время плавки из самого рецепта (сгенерированного через ModRecipeProvider)
        if (customRecipe.isPresent()) {
            return customRecipe.get().value().getCookingTime();
        }

        // 3. Для всех одиночных рецептов (ванильная плавильня) всегда возвращаем 66 тиков
        return 66;
    }

    // === ТВОЙ ОРИГИНАЛЬНЫЙ МЕТОД ПЛАВКИ (из скриншота) ===
    public void smeltAllItems() {
        if (level == null) return;

        // 1. Сначала пытаемся сварить кастомный сплав
        var customRecipeOpt = level.getRecipeManager().getRecipeFor(ModRecipes.BIG_BLAST_FURNACE_TYPE.get(), getRecipeInput(), level);
        if (customRecipeOpt.isPresent()) {
            BigBlastFurnaceRecipe recipe = customRecipeOpt.get().value();
            ItemStack resultStack = recipe.getResultItem(level.registryAccess());

            // Проверяем, влезет ли результат в слоты 6 или 7
            if (canInsertIntoOutput(resultStack)) {
                // Забираем нужное количество ингредиентов из слотов 2-5
                if (consumeRecipeIngredients(recipe)) {
                    insertIntoOutput(resultStack.copy());
                    this.storedExperience += recipe.getExperience();
                    return;
                }
            }
            return;
        }

        // 2. Если это не сплав, плавим обычную ванильную руду через RecipeType.BLASTING
        for (int i = 2; i <= 5; i++) {
            ItemStack stack = this.itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                SingleRecipeInput input = new SingleRecipeInput(stack);
                var vanillaRecipe = level.getRecipeManager().getRecipeFor(RecipeType.BLASTING, input, level);

                if (vanillaRecipe.isPresent()) {
                    ItemStack result = vanillaRecipe.get().value().getResultItem(level.registryAccess());

                    if (canInsertIntoOutput(result)) {
                        this.itemHandler.extractItem(i, 1, false);
                        insertIntoOutput(result.copy());
                        this.storedExperience += vanillaRecipe.get().value().getExperience();
                        return;
                    }
                }
            }
        }
    }

    public ItemStack getCurrentResultStack() {
        if (this.level == null) return ItemStack.EMPTY;

        // 1. Проверяем кастомный сплав
        var customRecipeOpt = this.level.getRecipeManager().getRecipeFor(ModRecipes.BIG_BLAST_FURNACE_TYPE.get(), getRecipeInput(), this.level);
        if (customRecipeOpt.isPresent()) {
            return customRecipeOpt.get().value().getResultItem(this.level.registryAccess());
        }

        // 2. Проверяем ванильную руду в слотах 2-5
        for (int i = 2; i <= 5; i++) {
            ItemStack stack = this.itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                SingleRecipeInput input = new SingleRecipeInput(stack);
                var vanillaRecipe = this.level.getRecipeManager().getRecipeFor(RecipeType.BLASTING, input, this.level);
                if (vanillaRecipe.isPresent()) {
                    return vanillaRecipe.get().value().getResultItem(this.level.registryAccess());
                }
            }
        }

        return ItemStack.EMPTY;
    }

    public void drops() {
        if (this.level == null) return;
        net.minecraft.world.SimpleContainer inventory = new net.minecraft.world.SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        net.minecraft.world.Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    // Метод проверки наличия любого доступного рецепта (сплава или ванильного)
    public boolean hasRecipe() {
        // 1. Сначала проверяем, собран ли кастомный сплав (как было раньше)
        var customRecipe = level.getRecipeManager().getRecipeFor(ModRecipes.BIG_BLAST_FURNACE_TYPE.get(), getRecipeInput(), level);
        if (customRecipe.isPresent()) {
            return true;
        }

        // 2. Если сплава нет, ищем ванильные рецепты плавильни в слотах входа (2, 3, 4, 5)
        for (int i = 2; i <= 5; i++) {
            ItemStack stack = this.itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                // Оборачиваем предмет в SingleRecipeInput (стандарт 1.21)
                SingleRecipeInput input = new SingleRecipeInput(stack);
                var vanillaRecipe = level.getRecipeManager().getRecipeFor(RecipeType.BLASTING, input, level);

                if (vanillaRecipe.isPresent()) {
                    return true; // Нашли обычную руду!
                }
            }
        }

        return false;
    }

    // Заглушка: твои методы для работы с рецептами (должны быть реализованы в твоем коде)
    private Optional<RecipeHolder<BigBlastFurnaceRecipe>> findMatchingAlloyRecipe() {
        if (level == null) return Optional.empty();
        return level.getRecipeManager().getRecipeFor(ModRecipes.BIG_BLAST_FURNACE_TYPE.get(), getRecipeInput(), level);
    }

    private boolean hasFuelInSlots() {
        return !itemHandler.getStackInSlot(0).isEmpty() || !itemHandler.getStackInSlot(1).isEmpty();
    }

    private void consumeFuel() {
        if (this.level == null) return;

        for (int i = 0; i <= 1; i++) {
            ItemStack fuelStack = this.itemHandler.getStackInSlot(i);
            if (!fuelStack.isEmpty()) {
                // Получаем время горения предмета
                int burnTime = fuelStack.getBurnTime(RecipeType.BLASTING);

                if (burnTime > 0) {
                    this.litDuration = burnTime;
                    this.litTime = burnTime;

                    fuelStack.shrink(1);
                    if (fuelStack.isEmpty()) {
                        this.itemHandler.setStackInSlot(i, ItemStack.EMPTY);
                    }
                    return;
                }
            }
        }
    }

    private boolean canInsertResult(ItemStack result) {
        if (result.isEmpty()) return true;

        int remaining = result.getCount();

        for (int i = 6; i <= 7; i++) {
            ItemStack current = this.itemHandler.getStackInSlot(i);

            // Если слот пустой, туда точно влезает результат
            if (current.isEmpty()) {
                return true;
            }

            // Если предметы одинаковые, считаем сколько свободного места в слоте
            if (ItemStack.isSameItemSameComponents(current, result)) {
                int space = current.getMaxStackSize() - current.getCount();
                remaining -= space;

                // Если свободного места хватило на весь результат — возвращаем true
                if (remaining <= 0) {
                    return true;
                }
            }
        }

        // Если прошли оба слота и место не хватило — плавку начинать нельзя
        return false;
    }

    private void insertIntoOutput(ItemStack stackToInsert) {
        for (int i = 6; i <= 7; i++) {
            if (stackToInsert.isEmpty()) break;

            ItemStack current = this.itemHandler.getStackInSlot(i);

            // Если слот пустой, кладем туда предмет напрямую (игнорируя запрет isItemValid)
            if (current.isEmpty()) {
                this.itemHandler.setStackInSlot(i, stackToInsert.copy());
                stackToInsert.setCount(0);
                return;
            }

            // Если в слоте уже лежит такой же металл, просто увеличиваем его количество
            if (ItemStack.isSameItemSameComponents(current, stackToInsert)) {
                int space = current.getMaxStackSize() - current.getCount();
                int transfer = Math.min(space, stackToInsert.getCount());

                if (transfer > 0) {
                    current.grow(transfer);
                    this.itemHandler.setStackInSlot(i, current); // Принудительно обновляем слот
                    stackToInsert.shrink(transfer);
                }
            }
        }
    }

    private boolean consumeRecipeIngredients(BigBlastFurnaceRecipe recipe) {
        // Простой и надежный забор ингредиентов по их типам и количеству
        int needed1 = recipe.getCount1();
        int needed2 = recipe.getCount2();

        // Сначала проверяем, хватает ли вообще предметов в слотах 2-5
        // (на случай если что-то изменилось)

        for (int i = 2; i <= 5; i++) {
            ItemStack stack = this.itemHandler.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (needed1 > 0 && recipe.getIngredient1().test(stack)) {
                int extract = Math.min(needed1, stack.getCount());
                this.itemHandler.extractItem(i, extract, false);
                needed1 -= extract;
            } else if (needed2 > 0 && recipe.getIngredient2().test(stack)) {
                int extract = Math.min(needed2, stack.getCount());
                this.itemHandler.extractItem(i, extract, false);
                needed2 -= extract;
            }
        }
        return needed1 <= 0 && needed2 <= 0;
    }

    private BigBlastFurnaceRecipeInput createRecipeInput() {
        List<ItemStack> inputs = new java.util.ArrayList<>();
        // Собираем предметы из входных слотов (с 2 по 5)
        for (int i = 2; i <= 5; i++) {
            inputs.add(itemHandler.getStackInSlot(i));
        }
        return new BigBlastFurnaceRecipeInput(inputs);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BigBlastFurnaceMenu(id, inventory, this, this.data);
    }

    public void dropStoredExperience(Player player) {
        if (this.level != null && !this.level.isClientSide() && this.storedExperience > 0) {
            int i = (int) this.storedExperience;
            float f = this.storedExperience - i;
            if (f > 0.0F && Math.random() < (double) f) {
                i++;
            }

            // Опыт появится прямо там, где стоит игрок
            net.minecraft.world.entity.ExperienceOrb.award(
                    (net.minecraft.server.level.ServerLevel) this.level,
                    player.position(),
                    i
            );

            this.storedExperience = 0;
        }
    }

    private BigBlastFurnaceRecipeInput getRecipeInput() {
        // Оборачиваем 4 предмета в один список (List.of)
        return new BigBlastFurnaceRecipeInput(java.util.List.of(
                this.itemHandler.getStackInSlot(2),
                this.itemHandler.getStackInSlot(3),
                this.itemHandler.getStackInSlot(4),
                this.itemHandler.getStackInSlot(5)
        ));
    }

    private boolean canInsertIntoOutput(ItemStack stackToInsert) {
        // Проверяем слоты 6 и 7
        for (int i = 6; i <= 7; i++) {
            ItemStack current = this.itemHandler.getStackInSlot(i);
            if (current.isEmpty()) return true;
            if (ItemStack.isSameItemSameComponents(current, stackToInsert) &&
                    current.getCount() + stackToInsert.getCount() <= current.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        // Сохраняем содержимое слотов (инвентарь)
        tag.put("inventory", this.itemHandler.serializeNBT(registries));

        // Сохраняем состояние горения, прогресса и опыта
        tag.putInt("litTime", this.litTime);
        tag.putInt("litDuration", this.litDuration);
        tag.putInt("progress", this.progress);
        tag.putInt("maxProgress", this.maxProgress);
        tag.putFloat("storedExperience", this.storedExperience);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        // Восстанавливаем предметы в слотах
        if (tag.contains("inventory")) {
            this.itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        }

        // Восстанавливаем состояние горения, прогресса и опыта
        this.litTime = tag.getInt("litTime");
        tag.contains("litDuration"); // Безопасное чтение
        this.litDuration = tag.getInt("litDuration");
        this.progress = tag.getInt("progress");
        this.maxProgress = tag.getInt("maxProgress");
        this.storedExperience = tag.getFloat("storedExperience");
    }
}