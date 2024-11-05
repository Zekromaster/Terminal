package net.zekromaster.minecraft.terminal.mixin.storage;

import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FurnaceBlockEntity.class)
public interface FurnaceBlockEntityAccessor {

    @Invoker("getFuelTime")
    int getFuelTime(ItemStack stack);

}
