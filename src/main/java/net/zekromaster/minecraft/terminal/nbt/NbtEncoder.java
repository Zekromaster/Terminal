package net.zekromaster.minecraft.terminal.nbt;

import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NbtEncoder<T, E extends NbtElement> {
    E toNbt(@NotNull T object);
}
