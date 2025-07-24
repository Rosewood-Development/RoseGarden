package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import dev.rosewood.rosegarden.utils.StringPlaceholders;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DurationArgumentHandler extends ArgumentHandler<Duration> {

    private static final Pattern PATTERN = Pattern.compile("(([1-9][0-9]+|[1-9])[dhms])");

    public DurationArgumentHandler() {
        super(Duration.class);
    }

    @Override
    public Duration handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        String input = inputIterator.next().toLowerCase();
        Duration duration = RoseGardenUtils.stringToDuration(input);
        if (duration.isZero()) {
            throw new HandledArgumentException("argument-type-duration", StringPlaceholders.of("input", input));
        }
        
        return duration;
    }

    /**
     * Gets command argument suggestions for the remaining player input.
     *
     * @param context  A readonly command context
     * @param argument The argument being handled
     * @param args     The player input for this argument
     * @return A List of possible argument suggestions
     */
    @Override
    public List<String> suggest(CommandContext context, Argument argument, String[] args) {
        String input = args[0]; // maybe ? 

        if (input.isEmpty()) {
            return IntStream.range(1, 10).boxed()
                    .sorted()
                    .map(String::valueOf)
                    .collect(Collectors.toList());
        }

        // 1d5_, 5d4m2_, etc
        return Stream.of("d", "h", "m", "s")
                .filter(unit -> !input.contains(unit))
                .map(unit -> input + unit)
                .collect(Collectors.toList());
    }
}
