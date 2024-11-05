package net.zekromaster.minecraft.terminal.nbt;

import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NbtDecoder<T, E extends NbtElement> {
    @NotNull T fromNbt(@NotNull E nbt);
}
