package net.zekromaster.minecraft.terminal.mixin.attachments;

import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.world.ClientWorld;
import net.minecraft.world.World;
import net.zekromaster.minecraft.terminal.attachments.inject.AttachmentsNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientNetworkHandler.class)
public abstract class ClientNetworkHandlerMixin implements AttachmentsNetworkHandler {

    @Shadow private ClientWorld world;

    @Override
    public World terminal$attachments$getWorld() {
        return this.world;
    }

    @Override
    public Entity terminal$attachments$getEntity(int id) {
        return this.world.getEntity(id);
    }
}
