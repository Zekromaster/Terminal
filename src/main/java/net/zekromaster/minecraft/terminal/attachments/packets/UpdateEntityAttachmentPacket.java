package net.zekromaster.minecraft.terminal.attachments.packets;

import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Identifier;
import net.zekromaster.minecraft.terminal.attachments.AttachmentType;
import net.zekromaster.minecraft.terminal.attachments.AttachmentTypeRegistry;
import net.zekromaster.minecraft.terminal.attachments.inject.AttachmentsNetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UpdateEntityAttachmentPacket<T> extends Packet implements IdentifiablePacket {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateEntityAttachmentPacket.class);
    public static final Identifier ID = Identifier.of("terminal:update_entity_attachments");

    private int entityId;
    private AttachmentType<T> attachmentType;
    private T attachment;

    public UpdateEntityAttachmentPacket() {
        this(0, null, null);
    }

    public UpdateEntityAttachmentPacket(
        int entityId,
        AttachmentType<T> attachmentType,
        T attachment
    ) {
        this.entityId = 0;
        this.attachmentType = attachmentType;
        this.attachment = attachment;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.entityId = stream.readInt();
            this.attachmentType = (AttachmentType<T>) AttachmentTypeRegistry.INSTANCE.get(
                Identifier.of(readString(stream, 128))
            );
            if (attachmentType == null) {
                LOG.error("Attempting to receive non-existing attachment over the network");
                return;

            }
            if (attachmentType.networkCodec == null) {
                LOG.error("Attempting to receive non-syncable attachment over the network");
                return;
            }
            this.attachment = attachmentType.networkCodec.read(stream);
        } catch (IOException e) {
            LOG.error("Caught exception while trying to synchronise attachments");
            LOG.error("e: ", e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(entityId);
            if (attachmentType == null) {
                return;
            }
            writeString(this.attachmentType.identifier.toString(), stream);
            if (attachmentType.networkCodec == null || attachment == null) {
                LOG.error("Attempting to send non-syncable attachment over the network");
                return;
            }
            attachmentType.networkCodec.write(stream, attachment);
        } catch (IOException e) {
            LOG.error("Caught exception while trying to synchronise attachments");
            LOG.error("e: ", e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        var entity = ((AttachmentsNetworkHandler) networkHandler).terminal$attachments$getEntity(entityId);
        entity.terminal$attachments$setDataSilently(attachmentType, attachment);
    }

    @Override
    public int size() {
        if (this.attachmentType.networkCodec == null) {
            return 4 + this.attachmentType.identifier.toString().length();
        }
        return 4 + this.attachmentType.identifier.toString().length() + this.attachmentType.networkCodec.size(this.attachment);
    }

    @Override
    public Identifier getId() {
        return ID;
    }
}
