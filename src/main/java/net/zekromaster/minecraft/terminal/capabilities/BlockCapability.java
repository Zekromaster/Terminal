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

import java.util.ArrayList;
import java.util.List;

/**
 * Flexible access to an object of type {@code T} as long as it's attached to a Block or {@link BlockEntity}.
 *
 * @param <T> The underlying type
 * @param <CTX> A context object
 */
public final class BlockCapability<T, CTX> {

    final Multimap<String, BlockEntityCapabilityHandler<T, CTX>> blockEntityHandlers = ArrayListMultimap.create();
    final Multimap<Block, BlockCapabilityHandler<T, CTX>> blockHandlers = ArrayListMultimap.create();

    final List<BlockEntityCapabilityHandler<T, CTX>> blockEntityFallbacks = new ArrayList<>();
    final List<BlockCapabilityHandler<T, CTX>> blockFallbacks = new ArrayList<>();

    public final Identifier identifier;
    public final Class<T> clazz;
    public final Class<CTX> ctxClass;

    private BlockCapability(Identifier identifier, Class<T> clazz, Class<CTX> ctxClass) {
        this.identifier = identifier;
        this.clazz = clazz;
        this.ctxClass = ctxClass;
    }

    /**
     * @param identifier A unique identifier
     * @param clazz The underlying type
     * @param ctxClass A context type
     * @return A new block capability with the given parameters
     * @param <T> The underlying type
     * @param <CTX> A context type
     */
    @API
    public static <T, CTX> BlockCapability<T, CTX> create(Identifier identifier, Class<T> clazz, Class<CTX> ctxClass) {
        return new BlockCapability<>(identifier, clazz, ctxClass);
    }

    /**
     * Same as {@link BlockCapability#create(Identifier, Class, Class)}, but the context object is always a
     * {@link Direction}
     */
    @API
    public static <T> BlockCapability<T, @Nullable Direction> createSided(Identifier identifier, Class<T> clazz) {
        return new BlockCapability<>(identifier, clazz, Direction.class);
    }

    /**
     * Same as {@link BlockCapability#create(Identifier, Class, Class)}, but the context object is always a
     * {@link Void} and thus is always null
     */
    @API
    public static <T> BlockCapability<T, Void> createVoid(Identifier identifier, Class<T> clazz) {
        return new BlockCapability<>(identifier, clazz, Void.class);
    }

    /**
     * Same as {@link BlockCapability#get(World, int, int, int, Object)} but using a BlockPos.
     */
    @API
    public @Nullable T get(World world, BlockPos blockPos, CTX ctx) {
        return this.get(world, blockPos.x, blockPos.y, blockPos.z, ctx);
    }

    /**
     *
     * @param world The world
     * @param x The x coordinate of the block
     * @param y The y coordinate of the block
     * @param z The z coordinate of the block
     * @param ctx The context object
     * @return An instance of type {@code T}, or null if no handler provides one
     */
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
                for (var handler: this.blockEntityFallbacks) {
                    var value = handler.get(blockEntity, ctx);
                    if (value != null) {
                        return value;
                    }
                }
            }
        }
        var block = world.getBlockState(x, y, z);
        var blockPos = new BlockPos(x, y, z);
        for (var handler: this.blockHandlers.get(block.getBlock())) {
            var value = handler.get(world, blockPos, ctx);
            if (value != null) {
                return value;
            }
        }
        for (var handler: this.blockFallbacks) {
            var value = handler.get(world, blockPos, ctx);
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
