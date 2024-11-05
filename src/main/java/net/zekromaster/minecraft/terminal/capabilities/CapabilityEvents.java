package net.zekromaster.minecraft.terminal.capabilities;

import net.mine_diver.unsafeevents.Event;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public interface CapabilityEvents {

    class RegisterItemCapabilitiesEvent extends Event {
        public <T, CTX> void registerItem(ItemCapability<T, CTX> capability, ItemCapability.ItemCapabilityHandler<T, CTX> handler, Item...items) {
            for (var item: items) {
                capability.handlers.put(item, handler);
            }
        }
    }

    class RegisterBlockCapabilitiesEvent extends Event {
        public <T, CTX> void registerBlock(BlockCapability<T, CTX> capability, BlockCapability.BlockCapabilityHandler<T, CTX> handler, Block ...blocks) {
            for (var block: blocks) {
                capability.blockHandlers.put(block, handler);
            }
        }
    }

    class RegisterBlockEntityCapabilitiesEvent extends Event {
        public <T, CTX> void registerBlockEntity(BlockCapability<T, CTX> capability, BlockCapability.BlockEntityCapabilityHandler<T, CTX> handler, String ...blockEntities) {
            for (var be: blockEntities) {
                capability.blockEntityHandlers.put(be, handler);
            }
        }
    }

    class RegisterEntityCapabilitiesEvent extends Event {
        public <T, CTX> void registerEntity(EntityCapability<T, CTX> capability, EntityCapability.EntityCapabilityHandler<T, CTX> handler, String...entities) {
            for (var entity: entities) {
                capability.handlers.put(entity, handler);
            }
        }
    }
}
