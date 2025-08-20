package dev.rosewood.rosegarden.gui.condition;

import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
import org.bukkit.entity.Player;
import java.util.Optional;

public class PlaceholderCondition implements Condition {

    private String left, right;
    private Operator operator;

    public PlaceholderCondition(String condition) {
        this.init(condition);
    }

    public void init(String condition) {
        if (condition == null || condition.trim().isEmpty())
            return;

        String[] values = condition.split(",");
        this.parseValues(values);
    }


    public boolean parseValues(String[] values) {
        String expression = String.join(",", values);
        if (expression.trim().isEmpty())
            return false;

        char placeholderSymbol = '%';
        outer:
        for (Operator operator : Operator.values()) {
            String symbol = operator.getSymbol();
            boolean inPlaceholder = false;
            StringBuilder buffer = new StringBuilder();
            for (char c : expression.toCharArray()) {
                if (c == placeholderSymbol)
                    inPlaceholder = !inPlaceholder;

                buffer.append(c);
                if (!inPlaceholder && buffer.toString().endsWith(symbol)) {
                    this.left = buffer.substring(0, buffer.length() - symbol.length()).trim();
                    this.operator = operator;
                    this.right = expression.substring(this.left.length() + symbol.length()).trim();
                    break outer;
                }
            }
        }

        if (this.operator == null) {
            this.left = values[0];
            return true;
        }

        return this.left != null && this.right != null;
    }

    @Override
    public boolean check(Context context) {
        Optional<Player> player = context.get(Parameters.PLAYER);

        if (this.left == null || this.right == null) {
            String booleanCondition = this.left != null ? this.left : this.right;
            String result = PlaceholderAPIHook.applyPlaceholders(player.orElse(null), booleanCondition);
            return result.equalsIgnoreCase("yes") || result.equalsIgnoreCase("true");
        }

        String left = PlaceholderAPIHook.applyPlaceholders(player.orElse(null), this.left);
        String right = PlaceholderAPIHook.applyPlaceholders(player.orElse(null), this.right);

        return this.operator.evaluate(left, right);
    }

}
