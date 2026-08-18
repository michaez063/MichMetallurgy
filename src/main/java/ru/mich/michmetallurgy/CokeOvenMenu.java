package ru.mich.michmetallurgy;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class CokeOvenMenu extends AbstractContainerMenu {
    private final CokeOvenBlockEntity blockEntity;
    private final ContainerData data;

    public CokeOvenMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public CokeOvenMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.COKE_OVEN_MENU.get(), containerId);
        this.blockEntity = (CokeOvenBlockEntity) entity;
        this.data = data;

        checkContainerDataCount(data, 2);
        addDataSlots(data);

        // Слот ингредиента — координаты из ТЗ
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 34, 36));
        // Слот результата — координаты из ТЗ
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 1, 107, 36) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    public boolean isLit() {
        return data.get(0) > 0;
    }

    public int getProgress(int pixels) {
        int progress = data.get(0);
        int maxProgress = data.get(1);
        return (maxProgress != 0 && progress != 0) ? progress * pixels / maxProgress : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            if (index >= 0 && index <= 1) {
                if (!this.moveItemStackTo(stackInSlot, 2, 38, true)) return ItemStack.EMPTY;
            } else {
                if (!this.moveItemStackTo(stackInSlot, 0, 1, false)) return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();

            if (stackInSlot.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(player, stackInSlot);
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, ModBlocks.COKE_OVEN.get());
    }

    private void addPlayerInventory(Inventory inv) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(inv, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory inv) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inv, i, 8 + i * 18, 142));
        }
    }
}