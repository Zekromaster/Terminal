package net.zekromaster.minecraft.terminal.storage.impl;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.zekromaster.minecraft.terminal.capabilities.CapabilityEvents;
import net.zekromaster.minecraft.terminal.storage.ItemStorage;
import net.zekromaster.minecraft.terminal.storage.decorators.InventoryItemStorage;

public class CapabilitiesListener {

    @EventListener
    public void registerBlockCapabilities(CapabilityEvents.RegisterBlockEntityCapabilitiesEvent event) {
        event.registerBlockEntity(
            ItemStorage.BLOCK,
            (be, direction) -> ItemStorage.of((ChestBlockEntity) be),
            "Chest"
        );

        event.registerBlockEntity(
            ItemStorage.BLOCK,
            (be, direction) -> {
                var fullStorage = new FurnaceItemStorage((FurnaceBlockEntity) be);

                if (direction == null) {
                    return fullStorage;
                }
                return switch (direction) {
                    case DOWN -> fullStorage.singleSlot(2).readOnly();
                    case UP -> fullStorage.singleSlot(0);
                    default ->  fullStorage.singleSlot(1);
                };
            },
            "Furnace"
        );

        event.registerBlockEntity(
            ItemStorage.BLOCK,
            (be, direction) -> new JukeboxItemStorage((JukeboxBlockEntity) be),
            "RecordPlayer"
        );

        event.registerBlockEntity(
            ItemStorage.BLOCK,
            (be, direction) -> new InventoryItemStorage<>((DispenserBlockEntity) be),
            "Trap"
        );

    }

}
