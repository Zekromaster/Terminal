package net.zekromaster.minecraft.terminal.network;

import net.modificationstation.stationapi.api.util.API;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Reifies the process of writing and reading an object from/to a Stream for network packet purposes
 * @param <T> The object to serialise/deserialise
 */
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

    @API
    public static <T> NetworkCodec<T> create(
        NetworkDecoder<T> decoder,
        NetworkEncoder<T> encoder,
        NetworkSizeEstimator<T> sizeEstimator
    ) {
        return new NetworkCodec<>(decoder, encoder, sizeEstimator);
    }


}
