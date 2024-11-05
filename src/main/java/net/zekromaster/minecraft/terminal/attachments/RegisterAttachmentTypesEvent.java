package net.zekromaster.minecraft.terminal.attachments;

import net.modificationstation.stationapi.api.event.registry.RegistryEvent;
import net.modificationstation.stationapi.api.registry.Registry;

public class RegisterAttachmentTypesEvent extends RegistryEvent<Registry<AttachmentType<?>>> {

    public RegisterAttachmentTypesEvent(Registry<AttachmentType<?>> registry) {
        super(registry);
    }

}
