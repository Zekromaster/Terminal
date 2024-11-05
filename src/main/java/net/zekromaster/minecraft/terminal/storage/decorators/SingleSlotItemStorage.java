package net.zekromaster.minecraft.terminal.storage.decorators;

import net.minecraft.item.ItemStack;
import net.zekromaster.minecraft.terminal.storage.ItemStorage;
import org.jetbrains.annotations.Nullable;

public class SingleSlotItemStorage implements ItemStorage {

    private final ItemStorage innerStorage;
    private final int inventorySlot;

    public SingleSlotItemStorage(ItemStorage innerStorage, int slot) {
        this.innerStorage = innerStorage;
        this.inventorySlot = slot;
    }

    @Override
    public int slots() {
        return 1;
    }

    @Override
    public @Nullable ItemStack slot(int slot) {
        if (slot != 0) {
            return null;
        }
        return innerStorage.slot(this.inventorySlot);
    }

    @Override
    public @Nullable ItemStack insert(int slot, ItemStack stack, OperationMode operationMode) {
        if (slot != 0) {
            return null;
        }
        return innerStorage.insert(inventorySlot, stack, operationMode);
    }

    @Override
    public @Nullable ItemStack extract(int slot, int amount, OperationMode operationMode) {
        if (slot != 0) {
            return null;
        }
        return innerStorage.extract(slot, amount, operationMode);
    }

    @Override
    public boolean couldInsert(int slot, ItemStack stack) {
        return slot == 0 && innerStorage.couldInsert(slot, stack);
    }
}
