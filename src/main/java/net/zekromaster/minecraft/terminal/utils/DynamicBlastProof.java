package net.zekromaster.minecraft.terminal.utils;

import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.state.property.Property;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.api.util.Namespace;
import net.zekromaster.minecraft.terminal.capabilities.BlockCapability;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface DynamicBlastProof {

    BlockCapability<DynamicBlastProof, Void> CAPABILITY = BlockCapability.create(
        Namespace.MINECRAFT.id("blast_proof"),
        DynamicBlastProof.class,
        Void.class
    );

    @API
    boolean isBlastProof();

    @API
    void setBlastProof(boolean value);

    @API
    static BlockCapability.BlockCapabilityHandler<DynamicBlastProof, Void> blockState(Property<Boolean> property) {
        return (world, blockPos, context) -> new BlockStateBasedBlastProof(world, blockPos, property);
    }

    @API
    static <B extends BlockEntity> BlockCapability.BlockEntityCapabilityHandler<DynamicBlastProof, Void> blockEntityProperty(
        Class<B> blockEntityClass,
        Function<B, Boolean> getter,
        BiConsumer<B, Boolean> setter
    ) {
        return (blockEntity, context) -> {
            if (blockEntityClass.isInstance(blockEntity)) {
                new BlockEntityPropertyBlastProof<>((B) blockEntity, getter, setter);
            }
            return null;
        };
    }

}
