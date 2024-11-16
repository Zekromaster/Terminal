package net.zekromaster.minecraft.terminal.mixin.utils;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.zekromaster.minecraft.terminal.utils.DynamicBlastProof;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {

    @Redirect(
        method = "explode",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I")
    )
    int blastResistance(World instance, int x, int y, int z) {
        var blastProofable = DynamicBlastProof.CAPABILITY.get(instance, x, y, z, null);
        if (blastProofable != null && blastProofable.isBlastProof()) {
            return Block.OBSIDIAN.id;
        }
        return instance.getBlockId(x, y, z);
    }

}
