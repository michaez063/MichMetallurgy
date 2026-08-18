package ru.mich.michmetallurgy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.ItemStackHandler;

public class PuddlingFurnaceBlockEntity extends BlockEntity implements MenuProvider {

    private static final int BATH_MAX = 64;
    private static final int MELT_TEMP = 1200;
    private static final int MAX_TEMP_HOT_FUEL = 1500;
    private static final int MAX_TEMP_COLD_FUEL = 1000;
    private static final int AMBIENT_TEMP = 20;
    private static final int STEEL_CONVERT_TIME = 500; // 25 секунд

    // Слоты: 0 — топливо, 1 — чугунный слиток, 2 — стальной слиток (результат)
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == 2) return false;
            if (slot == 0) return isFuel(stack);
            if (slot == 1) return stack.is(ModItems.CAST_IRON_INGOT.get());
            return super.isItemValid(slot, stack);
        }
    };

    private int litTime = 0;
    private int litDuration = 0;
    private int temperature = AMBIENT_TEMP;
    private int maxTempTarget = MAX_TEMP_COLD_FUEL;
    private int bathAmount = 0;
    private int steelProgress = 0;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> PuddlingFurnaceBlockEntity.this.litTime;
                case 1 -> PuddlingFurnaceBlockEntity.this.litDuration;
                case 2 -> PuddlingFurnaceBlockEntity.this.temperature;
                case 3 -> PuddlingFurnaceBlockEntity.this.bathAmount;
                case 4 -> PuddlingFurnaceBlockEntity.this.steelProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> PuddlingFurnaceBlockEntity.this.litTime = value;
                case 1 -> PuddlingFurnaceBlockEntity.this.litDuration = value;
                case 2 -> PuddlingFurnaceBlockEntity.this.temperature = value;
                case 3 -> PuddlingFurnaceBlockEntity.this.bathAmount = value;
                case 4 -> PuddlingFurnaceBlockEntity.this.steelProgress = value;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    };

    public PuddlingFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PUDDLING_FURNACE_BE.get(), pos, state);
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    private static boolean isFuel(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.getBurnTime(RecipeType.SMELTING) > 0;
    }

    private boolean isBurning() {
        return this.litTime > 0;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PuddlingFurnaceBlockEntity entity) {
        if (level.isClientSide()) return;

        boolean wasBurning = entity.isBurning();
        boolean setChanged = false;

        if (entity.isBurning()) {
            entity.litTime--;
        }

        // Есть ли смысл жечь топливо: остался ли непереплавленный чугун и место в ванне
        boolean needsHeat = !entity.itemHandler.getStackInSlot(1).isEmpty() && entity.bathAmount < BATH_MAX;

        if (!entity.isBurning() && needsHeat) {
            ItemStack fuelStack = entity.itemHandler.getStackInSlot(0);
            if (isFuel(fuelStack)) {
                int burnTime = fuelStack.getBurnTime(RecipeType.SMELTING);
                entity.litDuration = burnTime;
                entity.litTime = burnTime;

                boolean hotFuel = fuelStack.is(Items.COAL) || fuelStack.is(ModItems.COKE_COAL.get());
                entity.maxTempTarget = hotFuel ? MAX_TEMP_HOT_FUEL : MAX_TEMP_COLD_FUEL;

                fuelStack.shrink(1);
                setChanged = true;
            }
        }

        // Нагрев/остывание — 1 градус за тик
        if (entity.isBurning()) {
            if (entity.temperature < entity.maxTempTarget) {
                entity.temperature = Math.min(entity.maxTempTarget, entity.temperature + 1);
                setChanged = true;
            }
        } else if (entity.temperature > AMBIENT_TEMP) {
            entity.temperature = Math.max(AMBIENT_TEMP, entity.temperature - 1);
            setChanged = true;
        }

        // Плавка чугуна в ванну при достижении 1200 градусов
        if (entity.temperature >= MELT_TEMP) {
            ItemStack ironStack = entity.itemHandler.getStackInSlot(1);
            if (!ironStack.isEmpty() && entity.bathAmount < BATH_MAX) {
                entity.itemHandler.extractItem(1, 1, false);
                entity.bathAmount++;
                setChanged = true;
            }
        }

        // Переплавка расплава в сталь — 1 слиток за 500 тиков
        if (entity.bathAmount > 0) {
            ItemStack potentialSteel = new ItemStack(ModItems.STEEL_INGOT.get());
            if (entity.canInsertOutput(potentialSteel)) {
                entity.steelProgress++;
                if (entity.steelProgress >= STEEL_CONVERT_TIME) {
                    entity.steelProgress = 0;
                    entity.bathAmount--;
                    entity.insertOutput(potentialSteel.copy());
                    setChanged = true;
                }
            }
        } else {
            entity.steelProgress = 0;
        }

        if (wasBurning != entity.isBurning()) {
            setChanged = true;
            state = state.setValue(BlockStateProperties.LIT, entity.isBurning());
            level.setBlock(pos, state, 3);
        }

        if (setChanged) {
            entity.setChanged();
        }
    }

    private boolean canInsertOutput(ItemStack stackToInsert) {
        ItemStack current = this.itemHandler.getStackInSlot(2);
        if (current.isEmpty()) return true;
        return ItemStack.isSameItemSameComponents(current, stackToInsert)
                && current.getCount() + stackToInsert.getCount() <= current.getMaxStackSize();
    }

    private void insertOutput(ItemStack stackToInsert) {
        ItemStack current = this.itemHandler.getStackInSlot(2);
        if (current.isEmpty()) {
            this.itemHandler.setStackInSlot(2, stackToInsert);
        } else {
            current.grow(stackToInsert.getCount());
            this.itemHandler.setStackInSlot(2, current);
        }
    }

    public void drops() {
        if (this.level == null) return;
        int extraSlot = this.bathAmount > 0 ? 1 : 0;
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots() + extraSlot);
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        if (this.bathAmount > 0) {
            inventory.setItem(itemHandler.getSlots(), new ItemStack(ModItems.CAST_IRON_INGOT.get(), this.bathAmount));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.michmetallurgy.puddling_furnace");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new PuddlingFurnaceMenu(id, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", this.itemHandler.serializeNBT(registries));
        tag.putInt("litTime", this.litTime);
        tag.putInt("litDuration", this.litDuration);
        tag.putInt("temperature", this.temperature);
        tag.putInt("maxTempTarget", this.maxTempTarget);
        tag.putInt("bathAmount", this.bathAmount);
        tag.putInt("steelProgress", this.steelProgress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            this.itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        }
        this.litTime = tag.getInt("litTime");
        this.litDuration = tag.getInt("litDuration");
        this.temperature = tag.contains("temperature") ? tag.getInt("temperature") : AMBIENT_TEMP;
        this.maxTempTarget = tag.contains("maxTempTarget") ? tag.getInt("maxTempTarget") : MAX_TEMP_COLD_FUEL;
        this.bathAmount = tag.getInt("bathAmount");
        this.steelProgress = tag.getInt("steelProgress");
    }
}