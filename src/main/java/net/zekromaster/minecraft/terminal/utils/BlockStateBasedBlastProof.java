package net.zekromaster.minecraft.terminal.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.state.property.Property;

class BlockStateBasedBlastProof implements DynamicBlastProof {
    private final World world;
    private final BlockPos pos;
    private final Property<Boolean> property;

    BlockStateBasedBlastProof(World world, BlockPos pos, Property<Boolean> property) {
        this.world = world;
        this.pos = pos;
        this.property = property;
    }

    @Override
    public boolean isBlastProof() {
        var blockState = world.getBlockState(pos);
        return blockState.contains(property) && blockState.get(property);
    }

    @Override
    public void setBlastProof(boolean value) {
        var blockState = world.getBlockState(pos);
        if (blockState.contains(property)) {
            world.setBlockStateWithNotify(pos, blockState.with(property, value));
            world.setBlockDirty(pos.x, pos.y, pos.z);
        }
    }

}
