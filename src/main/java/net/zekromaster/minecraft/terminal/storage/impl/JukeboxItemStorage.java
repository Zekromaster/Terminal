package net.zekromaster.minecraft.terminal.storage.impl;

import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.zekromaster.minecraft.terminal.storage.ItemStorage;
import org.jetbrains.annotations.Nullable;

public class JukeboxItemStorage implements ItemStorage {
    private final JukeboxBlockEntity jukebox;

    public JukeboxItemStorage(JukeboxBlockEntity jukebox) {
        this.jukebox = jukebox;
    }

    @Override
    public int slots() {
        return 1;
    }

    @Override
    public @Nullable ItemStack slot(int slot) {
        if (slot != 0) {
            return null;
        }
        return new ItemStack(jukebox.recordId, 1, 0);
    }

    @Override
    public @Nullable ItemStack insert(int slot, ItemStack stack, OperationMode operationMode) {
        if (!couldInsert(slot, stack)) {
            return stack;
        }

        if (jukebox.recordId > 0) {
            return stack;
        }

        if (operationMode == OperationMode.RUN) {
            jukebox.recordId = stack.getItem().id;
            jukebox.markDirty();
            jukebox.world.setBlockMeta(jukebox.x, jukebox.y, jukebox.z, 1);
        }

        var retStack = stack.copy();
        retStack.count--;
        return retStack;
    }

    @Override
    public @Nullable ItemStack extract(int slot, int amount, OperationMode operationMode) {
        if (jukebox.recordId == 0 || slot != 0 || amount < 1) {
            return null;
        }
        var previousId = jukebox.recordId;


        jukebox.world.worldEvent(1005, jukebox.x, jukebox.y, jukebox.z, 0);
        jukebox.world.playStreaming(null, jukebox.x, jukebox.y, jukebox.z);
        jukebox.recordId = 0;
        jukebox.markDirty();
        jukebox.world.setBlockMeta(jukebox.x, jukebox.y, jukebox.z, 0);

        return new ItemStack(
            previousId,
            1,
            0
        );
    }

    @Override
    public boolean couldInsert(int slot, ItemStack stack) {
        return slot == 0 && stack.getItem() instanceof MusicDiscItem;
    }
}
