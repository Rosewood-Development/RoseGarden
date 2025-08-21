package dev.rosewood.rosegarden.gui.condition.predicate;

import dev.rosewood.rosegarden.gui.condition.Condition;
import dev.rosewood.rosegarden.gui.parameter.Context;

public class InvertedCondition implements Condition {

    private final Condition condition;

    public InvertedCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public boolean check(Context context) {
        return !this.condition.check(context);
    }

}
