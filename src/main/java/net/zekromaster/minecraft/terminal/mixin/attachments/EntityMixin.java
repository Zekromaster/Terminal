package net.zekromaster.minecraft.terminal.mixin.attachments;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.zekromaster.minecraft.terminal.attachments.AttachmentType;
import net.zekromaster.minecraft.terminal.attachments.inject.AttachmentsInjectedStore;
import net.zekromaster.minecraft.terminal.attachments.SimpleAttachmentStore;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(Entity.class)
public abstract class EntityMixin implements AttachmentsInjectedStore {

    @Unique
    private final SimpleAttachmentStore attachments = new SimpleAttachmentStore();

    @Override
    public <T> @Nullable T getDataOrNull(AttachmentType<T> attachmentType) {
        return attachments.getDataOrNull(attachmentType);
    }

    @Override
    public <T> void setData(AttachmentType<T> attachmentType, T data) {
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
        nbt.put("attachments", attachments.writeToNbt());
    }

    @Inject(
        method = "read",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;readNbt(Lnet/minecraft/nbt/NbtCompound;)V")
    )
    public void readAttachments(NbtCompound nbt, CallbackInfo ci) {
        attachments.readFromNbt(nbt.getCompound("attachments"));
    }

}
