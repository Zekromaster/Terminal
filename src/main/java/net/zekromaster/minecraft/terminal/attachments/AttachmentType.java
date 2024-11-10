package net.zekromaster.minecraft.terminal.attachments;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 *
 * @param <T> The type of object this AttachmentType holds
 */
public final class AttachmentType<T> {

    private final Supplier<T> defaultValue;

    /**
     * The class of object this AttachmentType stores
     */
    @API
    public final Class<T> clazz;

    /**
     * A unique namespaced identifier for this AttachmentType
     */
    @API
    public final Identifier identifier;

    /**
     * An optional codec to serialise the attachment type.
     */
    @API
    public final @Nullable Codec<T> codec;

    private AttachmentType(
        Identifier identifier,
        Class<T> clazz,
        Supplier<T> defaultValue,
        @Nullable Codec<T> codec
    ) {
        this.identifier = identifier;
        this.clazz = clazz;
        this.defaultValue = defaultValue;
        this.codec = codec;
    }

    /**
     *
     * @return The default value for this attachment
     */
    @API
    public T defaultValue() {
        return this.defaultValue.get();
    }

    /**
     * Starts building an AttachmentType
     * @param identifier A unique namespaced identifier for this AttachmentType
     * @param clazz The class of object this AttachmentType stores
     * @param defaultValue A supplier generating a default value for this attachment
     * @return An AttachmentTypeBuilder that allows configuring optional parameters
     * @param <T> The type of object this AttachmentType stores
     */
    @API
    public static <T> AttachmentTypeBuilder<T> create(
        Identifier identifier,
        Class<T> clazz,
        Supplier<T> defaultValue
    ) {
        return new AttachmentTypeBuilder<>(identifier, clazz, defaultValue);
    }

    /**
     * Starts building an AttachmentType
     * @param identifier A unique namespaced identifier for this AttachmentType
     * @param clazz The class of object this AttachmentType stores
     * @param defaultValue A default value for this attachment
     * @return An AttachmentTypeBuilder that allows configuring optional parameters
     * @param <T> The type of object this AttachmentType stores
     */
    @API
    public static <T> AttachmentTypeBuilder<T> create(
        Identifier identifier,
        Class<T> clazz,
        T defaultValue
    ) {
        return create(identifier, clazz, Suppliers.ofInstance(defaultValue));
    }

    public static final class AttachmentTypeBuilder<T> {

        private final Class<T> clazz;
        private final Identifier identifier;
        private final Supplier<T> defaultValue;
        private @Nullable Codec<T> codec;

        private AttachmentTypeBuilder(
            Identifier identifier,
            Class<T> clazz,
            Supplier<T> defaultValue
        ) {
            this.identifier = identifier;
            this.clazz = clazz;
            this.defaultValue = defaultValue;
            this.codec = null;
        }

        /**
         *
         * @param codec An optional codec to serialise the attachment type.
         * @return This builder
         */
        @API
        public AttachmentTypeBuilder<T> codec(@Nullable Codec<T> codec) {
            this.codec = codec;
            return this;
        }

        /**
         *
         * @return An AttachmentType configured by the builder
         */
        @API
        public AttachmentType<T> build() {
            return new AttachmentType<>(
                identifier,
                clazz,
                defaultValue,
                codec
            );
        }
    }

}
