package net.zekromaster.minecraft.terminal.attachments;

import com.google.common.base.Suppliers;
import net.modificationstation.stationapi.api.util.Identifier;
import net.zekromaster.minecraft.terminal.nbt.NbtCodec;
import net.zekromaster.minecraft.terminal.network.NetworkCodec;
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
    public final Class<T> clazz;

    /**
     * A unique namespaced identifier for this AttachmentType
     */
    public final Identifier identifier;

    /**
     * An optional codec to persist this in NBT form. If absent, the attachment might not be persisted. Individual
     * AttachmentStores might handle this differently - i.e. ItemStacks won't even accept attachments without an NBT
     * codec.
     */
    public final @Nullable NbtCodec<T> nbtCodec;

    /**
     * An optional codec to send this over the network. If absent, the attachment might not be synchronised. Individual
     * AttachmentStores might handle this differently - i.e. ItemStacks will always sync without ever using this codec,
     * because attachments are stored in the ItemStack NBT.
     */
    public final @Nullable NetworkCodec<T> networkCodec;

    private AttachmentType(
        Identifier identifier,
        Class<T> clazz,
        Supplier<T> defaultValue,
        @Nullable NbtCodec<T> nbtCodec,
        @Nullable NetworkCodec<T> networkCodec
        ) {
        this.identifier = identifier;
        this.clazz = clazz;
        this.defaultValue = defaultValue;
        this.nbtCodec = nbtCodec;
        this.networkCodec = networkCodec;
    }

    /**
     *
     * @return The default value for this attachment
     */
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
        private @Nullable NbtCodec<T> nbtCodec;
        private @Nullable NetworkCodec<T> networkCodec;

        private AttachmentTypeBuilder(
            Identifier identifier,
            Class<T> clazz,
            Supplier<T> defaultValue
        ) {
            this.identifier = identifier;
            this.clazz = clazz;
            this.defaultValue = defaultValue;
            this.nbtCodec = null;
            this.networkCodec = null;
        }

        /**
         *
         * @param codec An optional codec to persist this in NBT form. If absent, the attachment might not be persisted.
         *              Individual AttachmentStores might handle this differently - i.e. ItemStacks won't even accept
         *              attachments without an NBT codec.
         * @return This builder
         */
        public AttachmentTypeBuilder<T> nbt(@Nullable NbtCodec<T> codec) {
            this.nbtCodec = codec;
            return this;
        }

        /**
         *
         * @param codec An optional codec to send this over the network. If absent, the attachment might not be
         *              synchronised. Individual AttachmentStores might handle this differently - i.e. ItemStacks will
         *              always sync without ever using this codec, because attachments are stored in the ItemStack NBT.
         * @return This builder
         */
        public AttachmentTypeBuilder<T> sync(@Nullable NetworkCodec<T> codec) {
            this.networkCodec = codec;
            return this;
        }

        /**
         *
         * @return An AttachmentType configured by the builder
         */
        public AttachmentType<T> build() {
            return new AttachmentType<>(
                identifier,
                clazz,
                defaultValue,
                nbtCodec,
                networkCodec
            );
        }
    }

}
