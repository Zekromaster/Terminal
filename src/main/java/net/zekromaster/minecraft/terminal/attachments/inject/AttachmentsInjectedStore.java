package net.zekromaster.minecraft.terminal.attachments.inject;

import net.modificationstation.stationapi.api.util.Util;
import net.zekromaster.minecraft.terminal.attachments.AttachmentStore;
import net.zekromaster.minecraft.terminal.attachments.AttachmentType;
import org.jetbrains.annotations.Nullable;

public interface AttachmentsInjectedStore extends AttachmentStore {

    @Override
    default <T> @Nullable T getDataOrNull(AttachmentType<T> attachmentType) {
        return Util.assertImpl();
    }

    @Override
    default <T> void setData(AttachmentType<T> attachmentType, T data) {
        Util.assertImpl();
    }

    @Override
    default boolean hasData(AttachmentType<?> attachmentType) {
        return Util.assertImpl();
    }

}
