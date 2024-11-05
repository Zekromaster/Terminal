package net.zekromaster.minecraft.terminal.entrypoints;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;

public class EventBusEntrypointListener {

    @EventListener(priority = ListenerPriority.HIGHEST)
    public void preInit(InitEvent event) {
        FabricLoader.getInstance().getEntrypointContainers("terminal:event_bus", Object.class).forEach(EntrypointManager::setup);
        FabricLoader.getInstance().getEntrypointContainers(
            "terminal:event_bus/" +
                switch (FabricLoader.getInstance().getEnvironmentType()) {
                    case CLIENT -> "client";
                    case SERVER -> "server";
                },
            Object.class
        ).forEach(EntrypointManager::setup);
    }
}
