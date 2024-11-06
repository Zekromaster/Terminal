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

    /**
     * @return How many slots this ItemHandler has
     */
    @API
    int slots();

    /**
     * @param slot A slot
     * @return The contents of the slot. May be {@code null} if the slot is either empty or invalid
     */
    @API
    @Nullable ItemStack slot(int slot);

    /**
     * @param matcher A predicate that evaluates an ItemStack
     * @return The slots matching the predicate
     */
    @API
    default int[] slotsOf(Predicate<@Nullable ItemStack> matcher) {
        return IntStream.range(0, slots()).filter(
            x -> matcher.test(slot(x))
        ).toArray();
    }

    /**
     * @param stack An ItemStack
     * @return The slots containing an identical ItemStack
     */
    @API
    default int[] slotsOf(ItemStack stack) {
        return slotsOf(s -> ItemStack.areEqual(s, stack));
    }

    /**
     * @param item An Item
     * @return The slots containing that item
     */
    @API
    default int[] slotsOf(Item item) {
        if (item == null) {
            return slotsOf(Objects::isNull);
        }
        return slotsOf(s -> s != null && s.isOf(item));
    }

    /**
     * Inserts an ItemStack in a slot
     * @param slot The slot on which to operate
     * @param stack The ItemStack to insert
     * @param operationMode How this operation should impact the underlying inventory
     * @return A remainder (i.e. empty buckets, or simply part of the original ItemStack if it exceeds the slot's maximum
     * size)
     */
    @API
    @Nullable ItemStack insert(int slot, ItemStack stack, OperationMode operationMode);

    /**
     * Inserts an ItemStack in a slot
     * @param slot The slot on which to operate
     * @param stack The ItemStack to insert
     * @return A remainder (i.e. empty buckets, or simply part of the original ItemStack if it exceeds the slot's maximum
     * size)
     */
    @API
    default @Nullable ItemStack insert(int slot, ItemStack stack) {
        return insert(slot, stack, OperationMode.RUN);
    }

    /**
     * Extract some items from a slot
     * @param slot The slot
     * @param amount The maximum amount to extract
     * @param operationMode How this operation should impact the underlying inventory
     * @return Whatever is extracted
     */
    @API
    @Nullable ItemStack extract(int slot, int amount, OperationMode operationMode);

    /**
     * Extract some items from a slot
     * @param slot The slot
     * @param amount The maximum amount to extract
     * @return Whatever is extracted
     */
    @API
    default @Nullable ItemStack extract(int slot, int amount) {
        return extract(slot, amount, OperationMode.RUN);
    }

    /**
     * Checks if a slot could theoretically hold an item - this simply validates the ItemStack, without checking if it would
     * fit into the slot or generally considering the slot's contents
     * @param slot The slot
     * @param stack The item stack to insert
     * @return If the slot could hold the given ItemStack, ignoring its current state
     */
    @API
    boolean couldInsert(int slot, ItemStack stack);

    // FACTORY METHODS

    /**
     * @param inventory An inventory to wrap
     * @return An {@link ItemStorage} that operates on the given {@link Inventory}
     */
    @API
    static ItemStorage of(Inventory inventory) {
        return new InventoryItemStorage<>(inventory);
    }

    /**
     * @return An {@link ItemStorage} that wraps the current one with no-op insertion operations
     */
    @API
    default ItemStorage extractOnly() {
        return new ReadOnlyItemStorage(this);
    }

    /**
     * @param slot A slot
     * @return An {@link ItemStorage} that wraps the current one into a one-slot {@link ItemStorage} that acts as a proxy
     * for the given slot.
     */
    @API
    default ItemStorage singleSlot(int slot) {
        return new SingleSlotItemStorage(this, slot);
    }

    /**
     * @return An {@link ItemStorage} that wraps the current one with no-op extraction operations
     */
    @API
    default ItemStorage insertOnly() {
        return new WriteOnlyItemStorage(this);
    }

    /**
     * Describes the way an operation is executed on an ItemStorage
     */
    enum OperationMode {
        /**
         * Actually run the operation
         */
        RUN,
        /**
         * Simulate the operation, without actually impacting the world
         */
        SIMULATE
    }
}
