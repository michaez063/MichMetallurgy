package ru.mich.michmetallurgy;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class PuddlingFurnaceMenu extends AbstractContainerMenu {
    private final PuddlingFurnaceBlockEntity blockEntity;
    private final ContainerData data;

    public PuddlingFurnaceMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(5));
    }

    public PuddlingFurnaceMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.PUDDLING_FURNACE_MENU.get(), containerId);
        this.blockEntity = (PuddlingFurnaceBlockEntity) entity;
        this.data = data;

        checkContainerDataCount(data, 5);
        addDataSlots(data);

        // Индексы 0,1,2 в меню — печные слоты (для quickMoveStack)
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 1, 28, 28));
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 72, 64));
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 2, 148, 33) {
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

    public int getFlameHeight(int pixels) {
        int duration = data.get(1);
        if (duration == 0) duration = 200;
        return data.get(0) * pixels / duration;
    }

    public int getTemperature() {
        return data.get(2);
    }

    public int getBathLevel(int pixels) {
        return data.get(3) * pixels / 64;
    }

    public int getArrowWidth(int pixels) {
        return data.get(4) * pixels / 500;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            if (index >= 0 && index <= 2) {
                if (!this.moveItemStackTo(stackInSlot, 3, 39, true)) return ItemStack.EMPTY;
            } else if (this.blockEntity.getItemHandler().isItemValid(1, stackInSlot)) {
                if (!this.moveItemStackTo(stackInSlot, 0, 1, false)) return ItemStack.EMPTY;
            } else if (this.blockEntity.getItemHandler().isItemValid(0, stackInSlot)) {
                if (!this.moveItemStackTo(stackInSlot, 1, 2, false)) return ItemStack.EMPTY;
            } else {
                return ItemStack.EMPTY;
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
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, ModBlocks.PUDDLING_FURNACE.get());
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