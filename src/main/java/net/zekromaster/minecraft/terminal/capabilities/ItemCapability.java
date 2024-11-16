package net.zekromaster.minecraft.terminal.capabilities;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Flexible access to an object of type {@code T} as long as it's attached to an {@link ItemStack}.
 *
 * @param <T> The underlying type
 * @param <CTX> A context object
 */
public final class ItemCapability<T, CTX> {

    final Multimap<Item, ItemCapabilityHandler<T, CTX>> handlers = ArrayListMultimap.create();
    final ArrayList<ItemCapabilityHandler<T, CTX>> fallbacks = new ArrayList<>();

    public final Identifier identifier;
    public final Class<T> clazz;
    public final Class<CTX> ctxClass;

    private ItemCapability(Identifier identifier, Class<T> clazz, Class<CTX> ctxClass) {
        this.identifier = identifier;
        this.clazz = clazz;
        this.ctxClass = ctxClass;
    }

    /**
     * @param identifier A unique identifier
     * @param clazz The underlying type
     * @param ctxClass A context type
     * @return A new item capability with the given parameters
     * @param <T> The underlying type
     * @param <CTX> A context type
     */
    @API
    public static <T, CTX> ItemCapability<T, CTX> create(
        Identifier identifier,
        Class<T> clazz,
        Class<CTX> ctxClass
    ) {
        return new ItemCapability<>(identifier, clazz, ctxClass);
    }

    /**
     * Same as {@link ItemCapability#create(Identifier, Class, Class)}, but the context object is always a
     * {@link Void}
     */
    @API
    public static <T> ItemCapability<T, Void> createVoid(Identifier identifier, Class<T> clazz) {
        return new ItemCapability<>(identifier, clazz, Void.class);
    }

    /**
     * @param itemStack An item stack
     * @param ctx The context object
     * @return An instance of type {@code T}, or null if no handler provides one
     */
    @API
    public @Nullable T get(ItemStack itemStack, CTX ctx) {
        for (var handler: this.handlers.get(itemStack.getItem())) {
            var value = handler.get(itemStack, ctx);
            if (value != null) {
                return value;
            }
        }
        for (var handler: this.fallbacks) {
            var value = handler.get(itemStack, ctx);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @FunctionalInterface
    public interface ItemCapabilityHandler<T, CTX> {
        T get(ItemStack itemStack, CTX context);
    }

}
