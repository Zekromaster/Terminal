package net.zekromaster.minecraft.terminal.nbt;

import net.minecraft.nbt.*;
import net.modificationstation.stationapi.api.util.API;
import org.jetbrains.annotations.NotNull;

/**
 * Reifies the process of converting an Object to and from NBT data
 * @param <T> The object to convert
 */
public final class NbtCodec<T> implements NbtDecoder<T, NbtElement>, NbtEncoder<T, NbtElement> {
    @API public static final NbtCodec<Byte> BYTE = create(NbtByte.class, NbtByte::new, (NbtByte x) -> x.value);
    @API public static final NbtCodec<Short> SHORT = create(NbtShort.class, NbtShort::new, x -> x.value);
    @API public static final NbtCodec<Integer> INT = create(NbtInt.class, NbtInt::new, x -> x.value);
    @API public static final NbtCodec<Long> LONG = create(NbtLong.class, NbtLong::new, x -> x.value);
    @API public static final NbtCodec<Float> FLOAT = create(NbtFloat.class, NbtFloat::new, x -> x.value);
    @API public static final NbtCodec<Double> DOUBLE = create(NbtDouble.class, NbtDouble::new, x -> x.value);
    @API public static final NbtCodec<String> STRING = create(NbtString.class, NbtString::new, (NbtString x) -> x.value);
    @API public static final NbtCodec<Boolean> BOOLEAN = create(NbtByte.class, x -> new NbtByte((byte) (x ? 1 : 0)), x -> x.value != 0);

    private final Class<? extends NbtElement> nbtElementType;
    private final NbtEncoder<T, NbtElement> toNbt;
    private final NbtDecoder<T, NbtElement> fromNbt;

    private NbtCodec(
        Class<? extends NbtElement> nbtElementType,
        NbtEncoder<T, NbtElement> toNbt,
        NbtDecoder<T, NbtElement> fromNbt
    ) {
        this.nbtElementType = nbtElementType;
        this.toNbt = toNbt;
        this.fromNbt = fromNbt;
    }

    static <T, E extends NbtElement> NbtCodec<T> create(
        Class<E> nbtElementType,
        NbtEncoder<T, E> toNbt,
        NbtDecoder<T, E> fromNbt
    ) {
        return new <E> NbtCodec<T>(nbtElementType, (NbtEncoder) toNbt, (NbtDecoder) fromNbt);
    }

    public NbtElement toNbt(@NotNull T object) {
        return toNbt.toNbt(object);
    }

    public @NotNull T fromNbt(NbtElement nbt) {
        if (!nbtElementType.isAssignableFrom(nbt.getClass())) {
            throw new IllegalArgumentException("This codec is unable to process the given NbtElement type");
        }
        return fromNbt.fromNbt(nbt);
    }

}
