package net.zekromaster.minecraft.terminal.mixin.capabilities;

import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(BlockEntity.class)
public interface BlockEntityAccessor {
    @Accessor("classToId")
    static Map<Class<?>, String> getClassToId() {
        return Util.assertImpl();
    }
}
