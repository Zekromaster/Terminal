package net.zekromaster.minecraft.terminal.attachments;

import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.registry.Registries;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.registry.SimpleRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

public class AttachmentTypeRegistry extends SimpleRegistry<AttachmentType<?>> {

    public final static RegistryKey<AttachmentTypeRegistry> KEY = RegistryKey.ofRegistry(Identifier.of("attachment_types"));
    public final static AttachmentTypeRegistry INSTANCE = Registries.create(
        KEY,
        new AttachmentTypeRegistry(),
        (r) -> null,
        Lifecycle.experimental()
    );

    public AttachmentTypeRegistry() {
        super(KEY, Lifecycle.experimental());
    }
}
