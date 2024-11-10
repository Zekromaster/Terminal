package net.zekromaster.minecraft.terminal.attachments;

import com.mojang.serialization.Codec;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.modificationstation.stationapi.api.nbt.NbtOps;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.mixin.nbt.NbtCompoundAccessor;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Unique;
import java.util.HashMap;
import java.util.Map;

public class SimpleAttachmentStore implements AttachmentStore {

    @SuppressWarnings("LoggerInitializedWithForeignClass")
    @Unique
    private static final Logger LOG = LoggerFactory.getLogger(BlockEntity.class);

    private final Map<AttachmentType<?>, Object> attachments = new HashMap<>();

    @Override
    public <T> @Nullable T getDataOrNull(AttachmentType<T> attachmentType) {
        //noinspection unchecked
        return (T) this.attachments.get(attachmentType);
    }

    @Override
    public <T> void setData(AttachmentType<T> attachmentType, T data) {
        if (data == null) {
            attachments.remove(attachmentType);
        } else {
            attachments.put(attachmentType, data);
        }
    }

    @Override
    public boolean hasData(AttachmentType<?> attachmentType) {
        return this.attachments.containsKey(attachmentType);
    }

    public NbtElement writeToNbt() {
        var allAttachments = attachments.entrySet();
        if (allAttachments.isEmpty()) {
            return new NbtCompound();
        }

        var nbtAttachments = NbtOps.INSTANCE.empty();

        for (var attachment: allAttachments) {
            if (attachment.getKey().codec == null) {
                continue;
            }

            var codec = ((Codec<Object>) attachment.getKey().codec).fieldOf(attachment.getKey().identifier.toString()).codec();

            var res = codec.encode(attachment.getValue(), NbtOps.INSTANCE, nbtAttachments).result();
            nbtAttachments = res.orElseThrow(() -> new Error("Couldn't serialise attachment correctly"));
        }

        return nbtAttachments;
    }

    public void readFromNbt(NbtCompound nbt) {
        Map<String, NbtElement> allAttachments = ((NbtCompoundAccessor) nbt).stationapi$getEntries();

        for (var attachmentNbt: allAttachments.entrySet()) {
            var type = AttachmentTypeRegistry.INSTANCE.get(Identifier.of(attachmentNbt.getKey()));
            if (type == null) {
                LOG.warn(
                    "Invalid attachment {} on block entity. It won't be loaded and will be lost on next world save.",
                    attachmentNbt.getKey()
                );
                continue;
            }
            if (type.codec == null) {
                LOG.warn(
                    "Non-deserialisable attachment {} on block entity. It won't be loaded and will be lost on next world save.",
                    attachmentNbt.getKey()
                );
                continue;
            }

            var value = type.codec.decode(NbtOps.INSTANCE, attachmentNbt.getValue());
            this.setData(
                (AttachmentType) type,
                value.getOrThrow(false, (t) -> {})
            );
        }
    }

}
