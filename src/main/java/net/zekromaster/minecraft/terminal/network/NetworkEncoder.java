package net.zekromaster.minecraft.terminal.network;

import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.IOException;

@FunctionalInterface
public
interface NetworkEncoder<T> {
    void write(@NotNull DataOutputStream stream, @NotNull T object) throws IOException;
}
