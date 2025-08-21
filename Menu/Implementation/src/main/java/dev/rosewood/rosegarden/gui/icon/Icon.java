package dev.rosewood.rosegarden.gui.icon;

import dev.rosewood.rosegarden.gui.EditType;
import dev.rosewood.rosegarden.gui.action.Action;
import dev.rosewood.rosegarden.gui.item.Item;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.provider.Provider;
import dev.rosewood.rosegarden.gui.provider.Providers;
import dev.rosewood.rosegarden.gui.provider.item.ItemProvider;
import dev.rosewood.rosegarden.gui.provider.trigger.TriggerProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An icon that can be placed in an inventory.
 * Icons may have multiple providers.
 */
@SuppressWarnings("unchecked")
public class Icon {

    private final Map<String, Provider<?>> providers;
    private EditType editType;
    private Boolean persistent;

    public Icon() {
        this.providers = new HashMap<>();
        this.editType = EditType.NONE;
        this.persistent = null;
    }

    public Icon(Icon icon) {
        this.editType = icon.editType;
        this.persistent = icon.persistent;
        this.providers = new HashMap<>(icon.providers);
    }

    public Icon(Provider<?> provider) {
        this();

        this.providers.put(provider.getType(), provider);
    }

    public Icon(Item item) {
        this();

        ItemProvider provider = new ItemProvider(item);
        this.providers.put(provider.getType(), provider);
    }

    /**
     * Adds a {@link Provider} to this icon.
     * @param provider The {@link Provider} to add.
     * @return This {@link Icon}.
     */
    public Icon addProvider(Provider<?> provider) {
        this.providers.put(provider.getType(), provider);
        return this;
    }

    /**
     * @param providerType The {@link dev.rosewood.rosegarden.gui.provider.Providers.ProviderType} that this icon may hold.
     * @return The provider if it exists.
     */
    public <T> Optional<T> getProvider(Providers.ProviderType<? extends T> providerType) {
        return Optional.ofNullable((T) this.providers.get(providerType.getType()));
    }

    /**
     * Calls a {@link TriggerProvider} on an action.
     * @param id The ID of the trigger to call.
     * @param actionContext {@link Context} to be passed to the action.
     */
    public void call(String id, Context actionContext) {
        TriggerProvider provider = (TriggerProvider) this.providers.get(id);
        if (provider == null)
            return;

        provider.call(actionContext);
    }

    /**
     * Adds a {@link TriggerProvider} and an {@link Action} to this icon, to run when the icon is triggered.
     * @param trigger The {@link TriggerProvider} that will be activated.
     * @param action The {@link Action} to run when activated.
     * @return This {@link Icon}.
     */
    public Icon on(Providers.ProviderType<TriggerProvider> trigger, Action action) {
        Optional<TriggerProvider> current = this.getProvider(trigger);
        if (current.isPresent()) {
            current.get().add(action);
        } else {
            this.providers.put(trigger.getType(), new TriggerProvider(trigger.getKey(), action));
        }

        return this;
    }

    /**
     * Marks this icon as editable.
     * Players will be able to take this icon from the inventory.
     * @param editable True if the icon should be edited.
     * @return This {@link Icon}.
     */
    public Icon setEditable(boolean editable) {
        this.editType = editable ? EditType.REPLACE : EditType.NONE;
        return this;
    }

    /**
     * Sets the {@link EditType} for this icon.
     * Some icons may only be able to be taken, or placed.
     * @param editType The {@link EditType} to use.
     * @return This {@link Icon}.
     */
    public Icon setEditType(EditType editType) {
        this.editType = editType;
        return this;
    }

    /**
     * @return The current {@link EditType} of this icon.
     */
    public EditType getEditType() {
        return this.editType;
    }

    /**
     * NOT YET IMPLEMENTED
     *
     * Marks this icon as persistent.
     * The state of this icon will be saved when a player interacts with it.
     * The state of this icon will be loaded when the inventory is opened.
     * @param persistent True if this icon should be persistent.
     * @return This {@link Icon}.
     */
    public Icon markPersistent(boolean persistent) {
        this.persistent = persistent;
        return this;
    }

    /**
     * @return Whether this icon is persistent.
     */
    public boolean isPersistent() {
        return this.persistent != null && this.persistent;
    }

    /**
     * @return A map of all the providers that this icon holds.
     */
    public Map<String, Provider<?>> getProviders() {
        return this.providers;
    }

}
