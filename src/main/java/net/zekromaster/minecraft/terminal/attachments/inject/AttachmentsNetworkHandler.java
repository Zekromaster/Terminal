package net.zekromaster.minecraft.terminal.attachments.inject;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Util;

public interface AttachmentsNetworkHandler {
    default World terminal$attachments$getWorld() {
        return Util.assertImpl();
    }

    default Entity terminal$attachments$getEntity(int id) {
        return Util.assertImpl();
    }
}
