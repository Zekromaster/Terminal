package net.zekromaster.minecraft.terminal.attachments;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public interface AttachmentStore {

    /**
     * Fetches the value of an attachment, or sets it to the default value.
     *
     * @param attachmentType The attachment type to recover
     * @return The value of the attachment
     * @param <T> The type held by the attachment type
     */
    default <T> T getData(AttachmentType<T> attachmentType) {
        if (!this.hasData(attachmentType)) {
            this.setData(attachmentType, attachmentType.defaultValue());
        }
        return this.getDataOrNull(attachmentType);
    }

    /**
     * Fetches the value of an attachment if present, otherwise returns null.
     *
     * @param attachmentType The attachment type to recover
     * @return The value of the attachment
     * @param <T> The type held by the attachment type
     */
    <T> @Nullable T getDataOrNull(AttachmentType<T> attachmentType);

    /**
     * Sets the value of an attachment.
     *
     * @param attachmentType The attachment type to store
     * @param data The data to attach. If null, the attachment is removed.
     * @param <T> The type held by the attachment type
     */
    <T> void setData(AttachmentType<T> attachmentType, T data);

    /**
     * Checks if a certain attachment type is stored in this object.
     *
     * @param attachmentType The attachment type to check for
     * @return if the attachment is present
     */
    boolean hasData(AttachmentType<?> attachmentType);

    /**
     * Applies a mutation to an attachment
     *
     * @param attachmentType The attachment type to mutate
     * @param mutation A function that takes the current value of the attachment (or the default value if absent) and
     *                 returns the new value. If it returns null, the attachment is removed.
     * @param <T> The type held by the attachment type
     */
    default <T> void mutateData(AttachmentType<T> attachmentType, Function<T, T> mutation) {
        this.setData(attachmentType, mutation.apply(this.getData(attachmentType)));
    }

    /**
     * Applies a mutation to an attachment
     *
     * @param attachmentType The attachment type to mutate
     * @param mutation A function that takes the current value of the attachment (or the default value if absent) and returns the new value
     * @param <T> The type held by the attachment type
     */
    default <T> void mutateData(AttachmentType<T> attachmentType, Consumer<T> mutation) {
        this.mutateData(attachmentType, a -> {
            mutation.accept(a);
            return a;
        });
    }
}
