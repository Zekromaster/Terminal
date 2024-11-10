package net.zekromaster.minecraft.terminal.attachments.impl;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.zekromaster.minecraft.terminal.attachments.AttachmentTypeRegistry;
import net.zekromaster.minecraft.terminal.attachments.RegisterAttachmentTypesEvent;

public class AttachmentsListener {

    @EventListener
    public void attachmentRegistry(InitEvent event) {
        StationAPI.EVENT_BUS.post(new RegisterAttachmentTypesEvent(AttachmentTypeRegistry.INSTANCE));
    }

}
