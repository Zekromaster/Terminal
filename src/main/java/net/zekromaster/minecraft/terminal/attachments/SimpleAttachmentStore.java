package net.zekromaster.minecraft.terminal.attachments;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.mixin.nbt.NbtCompoundAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SimpleAttachmentStore implements AttachmentStore {

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

    public NbtCompound toNbt() {
        var compound = new NbtCompound();
        for (AttachmentType attachmentType: attachments.keySet()) {
            if (attachmentType.nbtCodec == null) {
                continue;
            }
            var individualAttachment = attachmentType.nbtCodec.toNbt(this.getData(attachmentType));
            compound.put(attachmentType.identifier.toString(), individualAttachment);
        }

        return compound;
    }

    public void fromNbt(NbtCompound nbt) {
        Map<String, NbtElement> allAttachments = ((NbtCompoundAccessor) nbt).stationapi$getEntries();

        for (var attachment: allAttachments.entrySet()) {
            var attachmentType = AttachmentTypeRegistry.INSTANCE.get(Identifier.of(attachment.getKey()));
            if (attachmentType != null && attachmentType.nbtCodec != null) {
                var attachmentValue = attachmentType.nbtCodec.fromNbt(allAttachments.get(attachmentType.identifier.toString()));
                attachments.put(attachmentType, attachmentValue);
            }
        }
    }

}
