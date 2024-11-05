package net.zekromaster.minecraft.terminal.network;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;

@FunctionalInterface
public
interface NetworkDecoder<T> {
    @NotNull T read(DataInputStream input) throws IOException;
}
