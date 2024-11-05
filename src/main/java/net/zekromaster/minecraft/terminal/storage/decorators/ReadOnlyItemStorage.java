package net.zekromaster.minecraft.terminal.storage.decorators;

import net.minecraft.item.ItemStack;
import net.zekromaster.minecraft.terminal.storage.ItemStorage;
import org.jetbrains.annotations.Nullable;

public class ReadOnlyItemStorage implements ItemStorage {

    private final ItemStorage innerStorage;

    public ReadOnlyItemStorage(ItemStorage innerStorage) {
        this.innerStorage = innerStorage;
    }

    @Override
    public int slots() {
        return innerStorage.slots();
    }

    @Override
    public @Nullable ItemStack slot(int slot) {
        return innerStorage.slot(slot);
    }

    @Override
    public @Nullable ItemStack insert(int slot, ItemStack stack, OperationMode operationMode) {
        return stack;
    }

    @Override
    public @Nullable ItemStack extract(int slot, int amount, OperationMode operationMode) {
        return innerStorage.extract(slot, amount, operationMode);
    }

    @Override
    public boolean couldInsert(int slot, ItemStack stack) {
        return false;
    }
}
