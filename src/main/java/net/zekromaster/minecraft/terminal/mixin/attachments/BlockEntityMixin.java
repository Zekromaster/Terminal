package net.zekromaster.minecraft.terminal.mixin.attachments;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.zekromaster.minecraft.terminal.attachments.AttachmentType;
import net.zekromaster.minecraft.terminal.attachments.inject.AttachmentsInjectedStore;
import net.zekromaster.minecraft.terminal.attachments.SimpleAttachmentStore;
import net.zekromaster.minecraft.terminal.attachments.inject.AttachmentsSetDataSilently;
import net.zekromaster.minecraft.terminal.attachments.packets.UpdateBlockEntityAttachmentPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements AttachmentsInjectedStore, AttachmentsSetDataSilently {

    @Shadow public abstract void markDirty();

    @Shadow public World world;
    @Shadow public int x;
    @Shadow public int y;
    @Shadow public int z;
    @Unique
    private final SimpleAttachmentStore attachments = new SimpleAttachmentStore();

    @Override
    public <T> @Nullable T getDataOrNull(AttachmentType<T> attachmentType) {
        return attachments.getDataOrNull(attachmentType);
    }

    @Override
    public <T> void setData(AttachmentType<T> attachmentType, T data) {
        this.terminal$attachments$setDataSilently(attachmentType, data);
        if (attachmentType.networkCodec != null) {
            var packet = new UpdateBlockEntityAttachmentPacket<>(
                this.x, this.y, this.z, attachmentType, data
            );
            ((List<PlayerEntity>) this.world.players).forEach(x -> PacketHelper.sendTo(x, packet));
        }
    }

    @Override
    public <T> void terminal$attachments$setDataSilently(AttachmentType<T> attachmentType, T data) {
        attachments.setData(attachmentType, data);
        this.markDirty();
    }

    @Override
    public boolean hasData(AttachmentType<?> attachmentType) {
        return attachments.hasData(attachmentType);
    }

    @Inject(method = "writeNbt", at = @At(value = "TAIL"))
    public void writeAttachments(NbtCompound nbt, CallbackInfo ci) {
        nbt.put("attachments", attachments.toNbt());
    }

    @Inject(method = "readNbt", at = @At(value = "TAIL"))
    public void readAttachments(NbtCompound nbt, CallbackInfo ci) {
        attachments.fromNbt(nbt.getCompound("attachments"));
    }
}
