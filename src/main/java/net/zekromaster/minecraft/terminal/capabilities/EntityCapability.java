package net.zekromaster.minecraft.terminal.capabilities;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

public final class EntityCapability<T, CTX> {

    final Multimap<String, EntityCapabilityHandler<T, CTX>> handlers = ArrayListMultimap.create();

    public final Identifier identifier;
    public final Class<T> clazz;
    public final Class<CTX> ctxClass;

    private EntityCapability(
        Identifier identifier,
        Class<T> clazz,
        Class<CTX> ctxClass
    ) {
        this.identifier = identifier;
        this.clazz = clazz;
        this.ctxClass = ctxClass;
    }

    @API
    public static <T, CTX> EntityCapability<T, CTX> create(Identifier identifier, Class<T> clazz, Class<CTX> ctxClass) {
        return new EntityCapability<>(identifier, clazz, ctxClass);
    }

    @API
    public static <T> EntityCapability<T, Void> createVoid(Identifier identifier, Class<T> clazz) {
        return new EntityCapability<>(identifier, clazz, Void.class);
    }

    @API
    public @Nullable T get(Entity entity, CTX ctx) {
        for (var handler: this.handlers.get(EntityRegistry.getId(entity))) {
            var value = handler.get(entity, ctx);
            if (value != null) {
                return value;
            }
        }
        return null;
    }


    @FunctionalInterface
    public interface EntityCapabilityHandler<T, CTX> {
        T get(Entity entity, CTX context);
    }

}
