package net.zekromaster.minecraft.terminal.capabilities;

import com.google.common.collect.ArrayListMultimap;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.zekromaster.minecraft.terminal.mixin.capabilities.BlockEntityAccessor;
import org.jetbrains.annotations.Nullable;

public final class BlockCapability<T, CTX> {

    final Multimap<String, BlockEntityCapabilityHandler<T, CTX>> blockEntityHandlers = ArrayListMultimap.create();
    final Multimap<Block, BlockCapabilityHandler<T, CTX>> blockHandlers = ArrayListMultimap.create();

    public final Identifier identifier;
    public final Class<T> clazz;
    public final Class<CTX> ctxClass;

    private BlockCapability(Identifier identifier, Class<T> clazz, Class<CTX> ctxClass) {
        this.identifier = identifier;
        this.clazz = clazz;
        this.ctxClass = ctxClass;
    }

    @API
    public static <T, CTX> BlockCapability<T, CTX> create(Identifier identifier, Class<T> clazz, Class<CTX> ctxClass) {
        return new BlockCapability<>(identifier, clazz, ctxClass);
    }

    @API
    public static <T> BlockCapability<T, @Nullable Direction> createSided(Identifier identifier, Class<T> clazz) {
        return new BlockCapability<>(identifier, clazz, Direction.class);
    }

    @API
    public static <T> BlockCapability<T, Void> createVoid(Identifier identifier, Class<T> clazz) {
        return new BlockCapability<>(identifier, clazz, Void.class);
    }

    @API
    public @Nullable T get(World world, BlockPos blockPos, CTX ctx) {
        return this.get(world, blockPos.x, blockPos.y, blockPos.z, ctx);
    }

    @API
    public @Nullable T get(World world, int x, int y, int z, CTX ctx) {
        var blockEntity = world.getBlockEntity(x, y, z);
        if (blockEntity != null) {
            var id = BlockEntityAccessor.getClassToId().get(blockEntity.getClass());
            if (id != null) {
                for (var handler: this.blockEntityHandlers.get(id)) {
                    var value = handler.get(blockEntity, ctx);
                    if (value != null) {
                        return value;
                    }
                }
            }
        }
        var block = world.getBlockState(x, y, z);
        for (var handler: this.blockHandlers.get(block.getBlock())) {
            var value = handler.get(world, new BlockPos(x, y, z), ctx);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @FunctionalInterface
    public interface BlockEntityCapabilityHandler<T, CTX> {
        T get(BlockEntity blockEntity, CTX context);
    }

    @FunctionalInterface
    public interface BlockCapabilityHandler<T, CTX> {
        T get(World world, BlockPos blockPos, CTX context);
    }

}
