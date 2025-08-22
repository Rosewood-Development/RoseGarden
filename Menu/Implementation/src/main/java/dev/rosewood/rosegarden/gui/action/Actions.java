package dev.rosewood.rosegarden.gui.action;

import dev.rosewood.rosegarden.gui.action.type.CloseMenuAction;
import dev.rosewood.rosegarden.gui.action.type.CommandAction;
import dev.rosewood.rosegarden.gui.action.type.ConditionalAction;
import dev.rosewood.rosegarden.gui.action.type.MessageAction;
import dev.rosewood.rosegarden.gui.action.type.ModifyItemAction;
import dev.rosewood.rosegarden.gui.action.type.NextPageAction;
import dev.rosewood.rosegarden.gui.action.type.OpenMenuAction;
import dev.rosewood.rosegarden.gui.action.type.PlaySoundAction;
import dev.rosewood.rosegarden.gui.action.type.PreviousPageAction;
import dev.rosewood.rosegarden.gui.action.type.RefreshAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.bukkit.configuration.ConfigurationSection;

@SuppressWarnings({"unchecked"})
public final class Actions {

    private static final Map<String, ActionType<?>> REGISTRY = new HashMap<>();

    public static final ActionType<ConditionalAction> REQUIRES_CONDITION = create(ConditionalAction.ID, ConditionalAction::new);
    public static final ActionType<RefreshAction> REFRESH = create(RefreshAction.ID, RefreshAction::new);
    public static final ActionType<CloseMenuAction> CLOSE_MENU = create(CloseMenuAction.ID, CloseMenuAction::new);
    public static final ActionType<OpenMenuAction> OPEN_MENU = create(OpenMenuAction.ID, OpenMenuAction::new);
    public static final ActionType<PlaySoundAction> PLAY_SOUND = create(PlaySoundAction.ID, PlaySoundAction::new);
    public static final ActionType<CommandAction> COMMANDS = create(CommandAction.ID, CommandAction::new);
    public static final ActionType<MessageAction> MESSAGE = create(MessageAction.ID, MessageAction::new);
    public static final ActionType<ModifyItemAction> MODIFY_ITEM = create(ModifyItemAction.ID, ModifyItemAction::new);
    public static final ActionType<NextPageAction> NEXT_PAGE = create(NextPageAction.ID, NextPageAction::new);
    public static final ActionType<PreviousPageAction> PREVIOUS_PAGE = create(PreviousPageAction.ID, PreviousPageAction::new);

    private Actions() {

    }

    public static <T> ActionType<T> create(String name, Function<ConfigurationSection, Action> function) {
        ActionType<T> actionType = new ActionType<>(function);
        REGISTRY.put(name, actionType);
        return actionType;
    }

    public static Map<String, ActionType<?>> getRegistry() {
        return Collections.unmodifiableMap(REGISTRY);
    }

    public static class ActionType<T> {

        private final Function<ConfigurationSection, Action> function;

        public ActionType(Function<ConfigurationSection, Action> function) {
            this.function = function;
        }

        public T create(ConfigurationSection section) {
            return (T) this.function.apply(section);
        }

    }

}
