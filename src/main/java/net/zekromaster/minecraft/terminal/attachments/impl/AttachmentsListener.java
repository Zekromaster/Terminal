package net.zekromaster.minecraft.terminal.attachments.impl;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.zekromaster.minecraft.terminal.attachments.AttachmentTypeRegistry;
import net.zekromaster.minecraft.terminal.attachments.RegisterAttachmentTypesEvent;
import net.zekromaster.minecraft.terminal.attachments.packets.UpdateBlockEntityAttachmentPacket;
import net.zekromaster.minecraft.terminal.attachments.packets.UpdateEntityAttachmentPacket;

public class AttachmentsListener {

    @EventListener
    public void attachmentRegistry(InitEvent event) {
        StationAPI.EVENT_BUS.post(new RegisterAttachmentTypesEvent(AttachmentTypeRegistry.INSTANCE));
    }

    @EventListener
    public void packetRegistry(PacketRegisterEvent event) {
        IdentifiablePacket.register(
            UpdateBlockEntityAttachmentPacket.ID,
            true,
            true,
            UpdateBlockEntityAttachmentPacket::new
        );
        IdentifiablePacket.register(
            UpdateEntityAttachmentPacket.ID,
            true,
            true,
            UpdateEntityAttachmentPacket::new
        );
    }

}
