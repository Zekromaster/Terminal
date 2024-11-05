package net.zekromaster.minecraft.terminal.network;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class NetworkCodec<T> {

    private final NetworkDecoder<T> decoder;
    private final NetworkEncoder<T> encoder;
    private final NetworkSizeEstimator<T> sizeEstimator;

    private NetworkCodec(
        NetworkDecoder<T> decoder,
        NetworkEncoder<T> encoder,
        NetworkSizeEstimator<T> sizeEstimator
    ) {
        this.decoder = decoder;
        this.encoder = encoder;
        this.sizeEstimator = sizeEstimator;
    }

    public @NotNull T read(DataInputStream input) throws IOException {
        return this.decoder.read(input);
    }

    public void write(@NotNull DataOutputStream stream, @NotNull T object) throws IOException {
        this.encoder.write(stream, object);
    }

    public int size(@NotNull T object) {
        return this.sizeEstimator.size(object);
    }

    static <T> NetworkCodec<T> create(
        NetworkDecoder<T> decoder,
        NetworkEncoder<T> encoder,
        NetworkSizeEstimator<T> sizeEstimator
    ) {
        return new NetworkCodec<>(decoder, encoder, sizeEstimator);
    }


}
