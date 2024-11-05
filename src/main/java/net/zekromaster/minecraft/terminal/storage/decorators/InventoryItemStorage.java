package net.zekromaster.minecraft.terminal.storage.decorators;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.zekromaster.minecraft.terminal.storage.ItemStorage;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class InventoryItemStorage<I extends Inventory> implements ItemStorage {

    protected final I inventory;

    public InventoryItemStorage(I inventory) {
        this.inventory = inventory;
    }

    @Override
    public int slots() {
        return inventory.size();
    }

    @Override
    public @Nullable ItemStack slot(int slot) {
        if (slot >= slots()) {
            return null;
        }
        return inventory.getStack(slot);
    }

    @Override
    public @Nullable ItemStack insert(int slot, ItemStack stack, OperationMode operationMode) {
        if (!couldInsert(slot, stack)) {
            return stack;
        }

        var currentContents = slot(slot);
        var maxAmount = Math.min(stack.getMaxCount(), inventory.getMaxCountPerStack());

        var retStack = stack.copy();

        if (currentContents != null) {
            if (!currentContents.isItemEqual(stack) || !Objects.equals(stack.getStationNbt(), currentContents.getStationNbt())) {
                return stack;
            }

            var totalAmount = currentContents.count + stack.count;

            var resultingStack = currentContents.copy();
            resultingStack.count = Math.min(totalAmount, maxAmount);

            if (operationMode == OperationMode.RUN) {
                inventory.setStack(slot, resultingStack);
            }

            retStack.count = totalAmount - resultingStack.count;
        } else {
            var insertedStack = stack.copy();
            insertedStack.count = Math.min(maxAmount, insertedStack.count);
            if (operationMode == OperationMode.RUN) {
                inventory.setStack(slot, insertedStack);
            }
            retStack.count -= insertedStack.count;
        }

        if (retStack.count > 0) {
            return retStack;
        }
        return null;
    }

    @Override
    public @Nullable ItemStack extract(int slot, int amount, OperationMode operationMode) {
        var currentContents = slot(slot);

        if (currentContents == null) {
            return null;
        }

        var extractedStack = currentContents.copy();
        extractedStack.count = Math.min(currentContents.count, amount);
        if (operationMode == OperationMode.RUN) {
            inventory.removeStack(slot, extractedStack.count);
        }
        return extractedStack;
    }

    @Override
    public boolean couldInsert(int slot, ItemStack stack) {
        return slot < slots();
    }
}
