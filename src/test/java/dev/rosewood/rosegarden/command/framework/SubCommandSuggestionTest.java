package dev.rosewood.rosegarden.command.framework;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.command.sub.TestSubCommand;
import dev.rosewood.rosegarden.command.framework.model.TestEnum;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SubCommandSuggestionTest {

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
        BaseRoseCommand command = new TestSubCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "";

        List<String> suggestions = commandWrapper.tabComplete(this.sender, command.getName(), this.splitInput(input));

        assertEquals(Stream.of(TestEnum.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()), suggestions);
    }

    @Test
    public void testSuggestion_arg1_partial() {
        BaseRoseCommand command = new TestSubCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "val";

        List<String> suggestions = commandWrapper.tabComplete(this.sender, command.getName(), this.splitInput(input));

        assertEquals(Arrays.asList("value_1", "value_2"), suggestions);
    }

    @Test
    public void testSuggestion_arg1_complete() {
        BaseRoseCommand command = new TestSubCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "value_1";

        List<String> suggestions = commandWrapper.tabComplete(this.sender, command.getName(), this.splitInput(input));

        assertTrue(suggestions.isEmpty());
    }

    @Test
    public void testSuggestion_arg2_empty() {
        BaseRoseCommand command = new TestSubCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "value_1 ";

        List<String> suggestions = commandWrapper.tabComplete(this.sender, command.getName(), this.splitInput(input));

        assertEquals(Arrays.asList("option1", "option2", "secret-option3"), suggestions);
    }

    @Test
    public void testSuggestion_arg2_partial() {
        BaseRoseCommand command = new TestSubCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "value_1 opt";

        List<String> suggestions = commandWrapper.tabComplete(this.sender, command.getName(), this.splitInput(input));

        assertEquals(Arrays.asList("option1", "option2"), suggestions);
    }

    @Test
    public void testSuggestion_arg2_complete() {
        BaseRoseCommand command = new TestSubCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "value_1 option_1";

        List<String> suggestions = commandWrapper.tabComplete(this.sender, command.getName(), this.splitInput(input));

        assertTrue(suggestions.isEmpty());
    }

    @Test
    public void testSuggestion_arg3_complete_space() {
        BaseRoseCommand command = new TestSubCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "value_1 secret-option3 ";

        List<String> suggestions = commandWrapper.tabComplete(this.sender, command.getName(), this.splitInput(input));

        assertEquals(Arrays.asList("true", "false"), suggestions);
    }

    @Test
    public void testSuggestion_arg3_complete_partial() {
        BaseRoseCommand command = new TestSubCommand(this.rosePlugin);
        RoseCommandWrapper commandWrapper = new RoseCommandWrapper(this.rosePlugin, command);

        String input = "value_1 secret-option3 t";

        List<String> suggestions = commandWrapper.tabComplete(this.sender, command.getName(), this.splitInput(input));

        assertEquals(Collections.singletonList("true"), suggestions);
    }

    private String[] splitInput(String input) {
        return input.split(" ", -1);
    }

}
