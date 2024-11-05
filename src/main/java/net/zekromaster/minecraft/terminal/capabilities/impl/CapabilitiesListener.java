package net.zekromaster.minecraft.terminal.capabilities.impl;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.MobHandlerRegistryEvent;
import net.zekromaster.minecraft.terminal.capabilities.CapabilityEvents;

public class CapabilitiesListener {

        @EventListener(priority = ListenerPriority.LOWEST)
        static void itemCapabilities(ItemRegistryEvent event) {
            StationAPI.EVENT_BUS.post(new CapabilityEvents.RegisterItemCapabilitiesEvent());
        }

        @EventListener(priority = ListenerPriority.LOWEST)
        static void blockCapabilities(BlockRegistryEvent event) {
            StationAPI.EVENT_BUS.post(new CapabilityEvents.RegisterBlockCapabilitiesEvent());
            StationAPI.EVENT_BUS.post(new CapabilityEvents.RegisterBlockEntityCapabilitiesEvent());
        }

        @EventListener(priority = ListenerPriority.LOWEST)
        static void entityCapabilities(MobHandlerRegistryEvent event) {
            StationAPI.EVENT_BUS.post(new CapabilityEvents.RegisterEntityCapabilitiesEvent());
        }

}
