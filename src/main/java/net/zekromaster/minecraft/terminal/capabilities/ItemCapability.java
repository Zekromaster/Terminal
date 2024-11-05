package net.zekromaster.minecraft.terminal.capabilities;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

public final class ItemCapability<T, CTX> {

    final Multimap<Item, ItemCapabilityHandler<T, CTX>> handlers = ArrayListMultimap.create();

    public final Identifier identifier;
    public final Class<T> clazz;
    public final Class<CTX> ctxClass;

    private ItemCapability(Identifier identifier, Class<T> clazz, Class<CTX> ctxClass) {
        this.identifier = identifier;
        this.clazz = clazz;
        this.ctxClass = ctxClass;
    }

    @API
    public static <T, CTX> ItemCapability<T, CTX> create(
        Identifier identifier,
        Class<T> clazz,
        Class<CTX> ctxClass
    ) {
        return new ItemCapability<>(identifier, clazz, ctxClass);
    }

    @API
    public static <T> ItemCapability<T, Void> createVoid(Identifier identifier, Class<T> clazz) {
        return new ItemCapability<>(identifier, clazz, Void.class);
    }

    @API
    public @Nullable T get(ItemStack itemStack, CTX ctx) {
        for (var handler: this.handlers.get(itemStack.getItem())) {
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
