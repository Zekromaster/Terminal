package net.zekromaster.minecraft.terminal.attachments;

import net.modificationstation.stationapi.api.event.registry.RegistryEvent;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.API;

public class RegisterAttachmentTypesEvent extends RegistryEvent<Registry<AttachmentType<?>>> {

    public RegisterAttachmentTypesEvent(Registry<AttachmentType<?>> registry) {
        super(registry);
    }

    @API
    public <T> AttachmentType<T> register(AttachmentType<T> attachmentType) {
        return Registry.register(registry, attachmentType.identifier, attachmentType);
    }

}
