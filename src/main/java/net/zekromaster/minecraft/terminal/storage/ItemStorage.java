package net.zekromaster.minecraft.terminal.storage;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.zekromaster.minecraft.terminal.capabilities.BlockCapability;
import net.zekromaster.minecraft.terminal.capabilities.EntityCapability;
import net.zekromaster.minecraft.terminal.capabilities.ItemCapability;
import net.zekromaster.minecraft.terminal.storage.decorators.InventoryItemStorage;
import net.zekromaster.minecraft.terminal.storage.decorators.ReadOnlyItemStorage;
import net.zekromaster.minecraft.terminal.storage.decorators.SingleSlotItemStorage;
import net.zekromaster.minecraft.terminal.storage.decorators.WriteOnlyItemStorage;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public interface ItemStorage {

    @API
    BlockCapability<ItemStorage, @Nullable Direction> BLOCK = BlockCapability.createSided(
        Namespace.MINECRAFT.id("item_storage"),
        ItemStorage.class
    );

    @API
    ItemCapability<ItemStorage, Void> ITEM = ItemCapability.createVoid(
        Namespace.MINECRAFT.id("item_storage"),
        ItemStorage.class
    );

    @API
    EntityCapability<ItemStorage, Void> ENTITY = EntityCapability.createVoid(
        Namespace.MINECRAFT.id("item_storage"),
        ItemStorage.class
    );

    @API
    int slots();

    @API
    @Nullable ItemStack slot(int slot);

    @API
    default int[] slotsOf(Predicate<@Nullable ItemStack> matcher) {
        return IntStream.range(0, slots()).filter(
            x -> matcher.test(slot(x))
        ).toArray();
    }

    @API
    default int[] slotsOf(ItemStack stack) {
        return slotsOf(s -> ItemStack.areEqual(s, stack));
    }

    @API
    default int[] slotsOf(Item item) {
        if (item == null) {
            return slotsOf(Objects::isNull);
        }
        return slotsOf(s -> s != null && s.isOf(item));
    }

    @API
    @Nullable ItemStack insert(int slot, ItemStack stack, OperationMode operationMode);

    @API
    default @Nullable ItemStack insert(int slot, ItemStack stack) {
        return insert(slot, stack, OperationMode.RUN);
    }

    @API
    @Nullable ItemStack extract(int slot, int amount, OperationMode operationMode);

    @API
    default @Nullable ItemStack extract(int slot, int amount) {
        return extract(slot, amount, OperationMode.RUN);
    }

    @API
    boolean couldInsert(int slot, ItemStack stack);

    // FACTORY METHODS

    @API
    static ItemStorage of(Inventory inventory) {
        return new InventoryItemStorage<>(inventory);
    }

    @API
    default ItemStorage readOnly() {
        return new ReadOnlyItemStorage(this);
    }

    @API
    default ItemStorage singleSlot(int slot) {
        return new SingleSlotItemStorage(this, slot);
    }

    @API
    default ItemStorage writeOnly() {
        return new WriteOnlyItemStorage(this);
    }

    enum OperationMode {
        RUN,
        SIMULATE
    }
}
