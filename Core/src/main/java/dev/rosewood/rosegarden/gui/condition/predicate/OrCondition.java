package dev.rosewood.rosegarden.gui.condition.predicate;

import dev.rosewood.rosegarden.gui.condition.Condition;
import dev.rosewood.rosegarden.gui.parameter.Context;

public class OrCondition implements Condition {

    private final Condition left, right;

    public OrCondition(Condition right, Condition left) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean check(Context context) {
        return this.left.check(context) || this.right.check(context);
    }

}
