package ru.mich.michmetallurgy;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import ru.mich.michmetallurgy.BigBlastFurnaceBlockEntity;

public class BigBlastFurnaceMenu extends AbstractContainerMenu {
    private final BigBlastFurnaceBlockEntity blockEntity;
    private final ContainerData data;

    public BigBlastFurnaceMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public BigBlastFurnaceMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.BIG_BLAST_FURNACE_MENU.get(), containerId);
        this.blockEntity = (BigBlastFurnaceBlockEntity) entity;
        this.data = data;

        checkContainerDataCount(data, 4);
        addDataSlots(data);

        // 1. Топливо (0, 1)
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 10, 18));
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 1, 10, 36));

        // 2. Ингредиенты (2, 3, 4, 5)
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 2, 35, 18));
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 3, 53, 18));
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 4, 35, 36));
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 5, 53, 36));

        // 3. Результат (6, 7)
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 6, 109, 36) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
            @Override
            public void onTake(Player player, ItemStack stack) {
                blockEntity.dropStoredExperience(player);
                super.onTake(player, stack);
            }
        });
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 7, 127, 36) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
            @Override
            public void onTake(Player player, ItemStack stack) {
                blockEntity.dropStoredExperience(player);
                super.onTake(player, stack);
            }
        });

        // 4. Инвентарь игрока
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    public boolean isLit() {
        return data.get(0) > 0;
    }

    public int getLitProgress(int pixels) {
        int duration = data.get(1);
        if (duration == 0) duration = 200;
        return data.get(0) * pixels / duration;
    }

    public int getBurnProgress(int pixels) {
        int progress = data.get(2);
        int maxProgress = data.get(3);
        return (maxProgress != 0 && progress != 0) ? progress * pixels / maxProgress : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            if (index >= 0 && index <= 7) {
                if (!this.moveItemStackTo(stackInSlot, 8, 44, true)) return ItemStack.EMPTY;
            } else {
                if (this.blockEntity.getItemHandler().isItemValid(0, stackInSlot)) {
                    if (!this.moveItemStackTo(stackInSlot, 0, 2, false)) return ItemStack.EMPTY;
                } else {
                    if (!this.moveItemStackTo(stackInSlot, 2, 6, false)) return ItemStack.EMPTY;
                }
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
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, ModBlocks.BIG_BLAST_FURNACE.get());
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