package net.zekromaster.minecraft.terminal.storage.impl;

import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.zekromaster.minecraft.terminal.mixin.storage.FurnaceBlockEntityAccessor;
import net.zekromaster.minecraft.terminal.storage.decorators.InventoryItemStorage;
import org.jetbrains.annotations.Nullable;

public class FurnaceItemStorage extends InventoryItemStorage<FurnaceBlockEntity> {
    public FurnaceItemStorage(FurnaceBlockEntity furnace) {
        super(furnace);
    }

    @Override
    public @Nullable ItemStack insert(int slot, ItemStack stack, OperationMode operationMode) {
        if (slot == 2) {
            return stack;
        }
        return super.insert(slot, stack, operationMode);
    }

    @Override
    public boolean couldInsert(int slot, ItemStack stack) {
        if (slot == 1) {
            return super.couldInsert(slot, stack) && ((FurnaceBlockEntityAccessor) this.inventory).getFuelTime(stack) > 0;
        }
        if (slot == 2) {
            return false;
        }
        return super.couldInsert(slot, stack);
    }

}
