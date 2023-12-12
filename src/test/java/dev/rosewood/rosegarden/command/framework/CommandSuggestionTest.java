package dev.rosewood.rosegarden.command.framework;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.command.suggest.SuggestionCommand;
import dev.rosewood.rosegarden.command.framework.handler.TestArgumentHandler;
import dev.rosewood.rosegarden.command.framework.model.TestEnum;
import java.util.List;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandSuggestionTest {

    private RosePlugin rosePlugin;
    private CommandSender sender;

    @BeforeEach
    public void setup() {
        this.rosePlugin = mock(RosePlugin.class);
        this.sender = mock(CommandSender.class);

        when(this.rosePlugin.getName()).thenReturn("TestPlugin");
    }

    @Test
    public void testSuggestion_arg1_empty() {
        BaseRoseCommand command = new SuggestionCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "";

        List<String> suggestions = commandWrapper.tabComplete(this.sender, command.getName(), this.splitInput(input));

        assertEquals(TestArgumentHandler.SUGGESTIONS, suggestions);
    }

    @Test
    public void testSuggestion_arg2_empty() {
        BaseRoseCommand command = new SuggestionCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "apple ";

        List<String> suggestions = commandWrapper.tabComplete(this.sender, command.getName(), this.splitInput(input));

        assertEquals(Stream.of(TestEnum.values()).map(Enum::name).map(String::toLowerCase).toList(), suggestions);
    }

    @Test
    public void testSuggestion_arg1_partial() {
        BaseRoseCommand command = new SuggestionCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "app";

        List<String> suggestions = commandWrapper.tabComplete(this.sender, command.getName(), this.splitInput(input));

        assertEquals(List.of("apple", "applause"), suggestions);
    }

    @Test
    public void testSuggestion_arg2_partial() {
        BaseRoseCommand command = new SuggestionCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "apple val";

        List<String> suggestions = commandWrapper.tabComplete(this.sender, command.getName(), this.splitInput(input));

        assertEquals(List.of("value_1", "value_2"), suggestions);
    }

    @Test
    public void testSuggestion_arg1_complete() {
        BaseRoseCommand command = new SuggestionCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "apple";

        List<String> suggestions = commandWrapper.tabComplete(this.sender, command.getName(), this.splitInput(input));

        assertEquals(List.of(), suggestions);
    }

    @Test
    public void testSuggestion_arg2_complete() {
        BaseRoseCommand command = new SuggestionCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "apple value_1";

        List<String> suggestions = commandWrapper.tabComplete(this.sender, command.getName(), this.splitInput(input));

        assertEquals(List.of(), suggestions);
    }

    private String[] splitInput(String input) {
        return input.split(" ", -1);
    }

}
