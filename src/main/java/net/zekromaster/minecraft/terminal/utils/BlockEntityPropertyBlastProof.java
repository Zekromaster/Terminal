package net.zekromaster.minecraft.terminal.utils;

import net.minecraft.block.entity.BlockEntity;

import java.util.function.BiConsumer;
import java.util.function.Function;

class BlockEntityPropertyBlastProof<B extends BlockEntity> implements DynamicBlastProof {

    private final B blockEntity;
    private final Function<B, Boolean> getter;
    private final BiConsumer<B, Boolean> setter;

    BlockEntityPropertyBlastProof(B blockEntity, Function<B, Boolean> getter, BiConsumer<B, Boolean> setter) {
        this.blockEntity = blockEntity;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public boolean isBlastProof() {
        return this.getter.apply(blockEntity);
    }

    @Override
    public void setBlastProof(boolean value) {
        this.setter.accept(blockEntity, value);
    }
}
