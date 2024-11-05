package net.zekromaster.minecraft.terminal.mixin.attachments;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.zekromaster.minecraft.terminal.attachments.inject.AttachmentsNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerNetworkHandlerMixin implements AttachmentsNetworkHandler {

    @Shadow private ServerPlayerEntity player;
    @Shadow private MinecraftServer server;

    @Override
    public ServerWorld terminal$attachments$getWorld() {
        return this.server.getWorld(this.player.dimensionId);
    }

    @Override
    public Entity terminal$attachments$getEntity(int id) {
        return this.terminal$attachments$getWorld().getEntity(id);
    }


}
