package net.zekromaster.minecraft.terminal.mixin.attachments;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.zekromaster.minecraft.terminal.attachments.AttachmentType;
import net.zekromaster.minecraft.terminal.attachments.inject.AttachmentsInjectedStore;
import net.zekromaster.minecraft.terminal.attachments.SimpleAttachmentStore;
import net.zekromaster.minecraft.terminal.attachments.inject.AttachmentsSetDataSilently;
import net.zekromaster.minecraft.terminal.attachments.packets.UpdateEntityAttachmentPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(Entity.class)
public abstract class EntityMixin implements AttachmentsInjectedStore, AttachmentsSetDataSilently {

    @Shadow public int id;
    @Shadow public World world;
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
            var packet = new UpdateEntityAttachmentPacket<>(
                this.id, attachmentType, data
            );
            ((List<PlayerEntity>) this.world.players).forEach(x -> PacketHelper.sendTo(x, packet));
        }
    }

    @Override
    public <T> void terminal$attachments$setDataSilently(AttachmentType<T> attachmentType, T data) {
        attachments.setData(attachmentType, data);
    }

    @Override
    public boolean hasData(AttachmentType<?> attachmentType) {
        return attachments.hasData(attachmentType);
    }

    @Inject(
        method = "write",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;writeNbt(Lnet/minecraft/nbt/NbtCompound;)V")
    )
    public void writeAttachments(NbtCompound nbt, CallbackInfo ci) {
        nbt.put("attachments", attachments.toNbt());
    }

    @Inject(
        method = "read",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;readNbt(Lnet/minecraft/nbt/NbtCompound;)V")
    )
    public void readAttachments(NbtCompound nbt, CallbackInfo ci) {
        attachments.fromNbt(nbt.getCompound("attachments"));
    }

}
