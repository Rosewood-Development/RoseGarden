package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentParser;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentHandler;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentInfo;
import dev.rosewood.rosegarden.command.framework.types.GreedyString;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GreedyStringArgumentHandler extends RoseCommandArgumentHandler<GreedyString> {

    public GreedyStringArgumentHandler(RosePlugin rosePlugin) {
        super(rosePlugin, GreedyString.class);
    }

    @Override
    protected GreedyString handleInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        List<String> inputs = new ArrayList<>();
        while (argumentParser.hasNext())
            inputs.add(argumentParser.next());

        String combined = String.join(" ", inputs);
        if (combined.isEmpty())
            throw new HandledArgumentException("argument-handler-string");
        return new GreedyString(combined);
    }

    @Override
    protected List<String> suggestInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        while (argumentParser.hasNext())
            argumentParser.next();
        return Collections.singletonList(argumentInfo.toString());
    }

}
