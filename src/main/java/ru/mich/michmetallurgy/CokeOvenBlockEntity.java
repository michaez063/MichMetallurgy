package ru.mich.michmetallurgy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.ItemStackHandler;

public class CokeOvenBlockEntity extends BlockEntity implements MenuProvider {

    public static final int LOG_TO_CHARCOAL_TIME = 400; // 20 секунд
    public static final int COAL_TO_COKE_TIME = 600;    // 30 секунд

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == 1) return false; // Слот результата — только автоматически
            return isValidInput(stack);
        }
    };

    private int progress = 0;
    private int maxProgress = 0;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> CokeOvenBlockEntity.this.progress;
                case 1 -> CokeOvenBlockEntity.this.maxProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> CokeOvenBlockEntity.this.progress = value;
                case 1 -> CokeOvenBlockEntity.this.maxProgress = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public CokeOvenBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COKE_OVEN_BE.get(), pos, state);
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    public static boolean isValidInput(ItemStack stack) {
        return stack.is(ItemTags.LOGS) || stack.is(Items.COAL);
    }

    private static int getRequiredTime(ItemStack stack) {
        if (stack.is(ItemTags.LOGS)) return LOG_TO_CHARCOAL_TIME;
        if (stack.is(Items.COAL)) return COAL_TO_COKE_TIME;
        return 0;
    }

    private static ItemStack getResult(ItemStack stack) {
        if (stack.is(ItemTags.LOGS)) return new ItemStack(Items.CHARCOAL);
        if (stack.is(Items.COAL)) return new ItemStack(ModItems.COKE_COAL.get());
        return ItemStack.EMPTY;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CokeOvenBlockEntity entity) {
        if (level.isClientSide()) return;

        ItemStack input = entity.itemHandler.getStackInSlot(0);
        boolean wasLit = entity.progress > 0;
        boolean setChanged = false;

        if (!input.isEmpty() && isValidInput(input)) {
            ItemStack result = getResult(input);
            boolean canInsert = entity.canInsertOutput(result);

            if (canInsert) {
                entity.maxProgress = getRequiredTime(input);
                entity.progress++;

                if (entity.progress >= entity.maxProgress) {
                    entity.progress = 0;
                    input.shrink(1);
                    entity.insertOutput(result.copy());
                    setChanged = true;
                }
            } else {
                entity.progress = 0;
            }
        } else {
            entity.progress = 0;
        }

        boolean isLit = entity.progress > 0;
        if (wasLit != isLit) {
            setChanged = true;
            state = state.setValue(BlockStateProperties.LIT, isLit);
            level.setBlock(pos, state, 3);
        }

        if (setChanged) {
            entity.setChanged();
        }
    }

    private boolean canInsertOutput(ItemStack result) {
        ItemStack current = this.itemHandler.getStackInSlot(1);
        if (current.isEmpty()) return true;
        return ItemStack.isSameItemSameComponents(current, result)
                && current.getCount() + result.getCount() <= current.getMaxStackSize();
    }

    private void insertOutput(ItemStack result) {
        ItemStack current = this.itemHandler.getStackInSlot(1);
        if (current.isEmpty()) {
            this.itemHandler.setStackInSlot(1, result);
        } else {
            current.grow(result.getCount());
            this.itemHandler.setStackInSlot(1, current);
        }
    }

    public void drops() {
        if (this.level == null) return;
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.michmetallurgy.coke_oven");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new CokeOvenMenu(id, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", this.itemHandler.serializeNBT(registries));
        tag.putInt("progress", this.progress);
        tag.putInt("maxProgress", this.maxProgress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            this.itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        }
        this.progress = tag.getInt("progress");
        this.maxProgress = tag.getInt("maxProgress");
    }
}