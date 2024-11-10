package net.zekromaster.minecraft.terminal.mixin.attachments;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.zekromaster.minecraft.terminal.attachments.AttachmentType;
import net.zekromaster.minecraft.terminal.attachments.inject.AttachmentsInjectedStore;
import net.zekromaster.minecraft.terminal.attachments.SimpleAttachmentStore;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements AttachmentsInjectedStore {

    @Shadow public abstract void markDirty();

    @Unique
    private final SimpleAttachmentStore attachments = new SimpleAttachmentStore();

    @Override
    public <T> @Nullable T getDataOrNull(AttachmentType<T> attachmentType) {
        return attachments.getDataOrNull(attachmentType);
    }

    @Override
    public <T> void setData(AttachmentType<T> attachmentType, T data) {
        attachments.setData(attachmentType, data);
        this.markDirty();
    }

    @Override
    public boolean hasData(AttachmentType<?> attachmentType) {
        return attachments.hasData(attachmentType);
    }

    @Inject(method = "writeNbt", at = @At(value = "TAIL"))
    public void writeAttachments(NbtCompound nbt, CallbackInfo ci) {
        nbt.put("attachments", attachments.writeToNbt());
    }

    @Inject(method = "readNbt", at = @At(value = "TAIL"))
    public void readAttachments(NbtCompound nbt, CallbackInfo ci) {
        attachments.readFromNbt(nbt.getCompound("attachments"));
    }
}
