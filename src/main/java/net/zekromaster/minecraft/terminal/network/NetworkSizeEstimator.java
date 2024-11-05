package net.zekromaster.minecraft.terminal.network;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NetworkSizeEstimator<T> {
    int size(@NotNull T object);
}
