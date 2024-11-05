package net.zekromaster.minecraft.terminal.attachments.inject;

import net.modificationstation.stationapi.api.util.Util;
import net.zekromaster.minecraft.terminal.attachments.AttachmentType;

public interface AttachmentsSetDataSilently {

    default <T> void terminal$attachments$setDataSilently(AttachmentType<T> attachmentType, T data) {
        Util.assertImpl();
    }
    
}
