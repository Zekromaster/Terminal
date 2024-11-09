package net.zekromaster.minecraft.terminal.capabilities;

import net.mine_diver.unsafeevents.Event;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.util.API;

public interface CapabilityEvents {

    class RegisterItemCapabilitiesEvent extends Event {
        @API
        public <T, CTX> void register(ItemCapability<T, CTX> capability, ItemCapability.ItemCapabilityHandler<T, CTX> handler, Item...items) {
            for (var item: items) {
                capability.handlers.put(item, handler);
            }
        }
    }

    class RegisterBlockCapabilitiesEvent extends Event {
        @API
        public <T, CTX> void register(BlockCapability<T, CTX> capability, BlockCapability.BlockCapabilityHandler<T, CTX> handler, Block ...blocks) {
            for (var block: blocks) {
                capability.blockHandlers.put(block, handler);
            }
        }
    }

    class RegisterBlockEntityCapabilitiesEvent extends Event {
        @API
        public <T, CTX> void register(BlockCapability<T, CTX> capability, BlockCapability.BlockEntityCapabilityHandler<T, CTX> handler, String ...blockEntities) {
            for (var be: blockEntities) {
                capability.blockEntityHandlers.put(be, handler);
            }
        }
    }

    class RegisterEntityCapabilitiesEvent extends Event {
        @API
        public <T, CTX> void register(EntityCapability<T, CTX> capability, EntityCapability.EntityCapabilityHandler<T, CTX> handler, String...entities) {
            for (var entity: entities) {
                capability.handlers.put(entity, handler);
            }
        }
    }
}
