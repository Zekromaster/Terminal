package net.zekromaster.minecraft.terminal.mixin.attachments;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.modificationstation.stationapi.mixin.nbt.NbtCompoundAccessor;
import net.zekromaster.minecraft.terminal.attachments.AttachmentType;
import net.zekromaster.minecraft.terminal.attachments.inject.AttachmentsInjectedStore;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Map;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements AttachmentsInjectedStore {

    @Override
    public <T> @Nullable T getDataOrNull(AttachmentType<T> attachmentType) {
        if (attachmentType.nbtCodec == null) {
            throw new IllegalStateException("Attempted to read attachment without NBT Codec on ItemStack");
        }

        var nbt = ((ItemStack) (Object) this).getStationNbt().getCompound("attachments");
        Map<String, NbtElement> nbtEntries = ((NbtCompoundAccessor) nbt).stationapi$getEntries();

        if (nbtEntries.containsKey(attachmentType.identifier.toString())) {
            return attachmentType.nbtCodec.fromNbt(nbtEntries.get(attachmentType.identifier.toString()));
        }

        return null;
    }

    @Override
    public <T> void setData(AttachmentType<T> attachmentType, T data) {
        if (attachmentType.nbtCodec == null) {
            throw new IllegalStateException("Attempted to add attachment without NBT Codec on ItemStack");
        }

        var nbt = ((ItemStack) (Object) this).getStationNbt();
        var attachments = nbt.getCompound("attachments");

        var individualAttachment = attachmentType.nbtCodec.toNbt(data);
        attachments.put(attachmentType.identifier.toString(), individualAttachment);
        nbt.put("attachments", attachments);
    }

    @Override
    public boolean hasData(AttachmentType<?> attachmentType) {
        return ((NbtCompoundAccessor)((ItemStack) (Object) this).getStationNbt().getCompound("attachments"))
            .stationapi$getEntries()
            .containsKey(attachmentType.identifier.toString());
    }

}
