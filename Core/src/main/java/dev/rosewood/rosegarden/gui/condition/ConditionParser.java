package dev.rosewood.rosegarden.gui.condition;

import dev.rosewood.rosegarden.gui.condition.predicate.AndCondition;
import dev.rosewood.rosegarden.gui.condition.predicate.InvertedCondition;
import dev.rosewood.rosegarden.gui.condition.predicate.OrCondition;
import java.util.ArrayDeque;
import java.util.Deque;

public final class ConditionParser {

    private static final String TOKEN_REGEX = "(?<=&&)|(?=&&)|(?<=\\|\\|)|(?=\\|\\|)|(?<=(?<=^|\\s|\\()!)|(?=(?<=^|\\s|\\()!)|(?<=\\()|(?=\\()|(?<=\\))|(?=\\))";

    private ConditionParser() {

    }

    public static Condition parse(String condition) {
        condition = condition.replaceAll("\\s+", "");

        try {
            String[] tokens = condition.split(TOKEN_REGEX);

            Deque<Condition> conditions = new ArrayDeque<>();
            Deque<String> operators = new ArrayDeque<>();
            for (String token : tokens) {
                switch (token) {
                    case "(":
                        operators.push(token);
                        break;
                    case ")":
                        while (!operators.peek().equals("("))
                            conditions.push(getCondition(operators.pop(), conditions));
                        operators.pop();
                        break;
                    case "&&":
                    case "||":
                    case "!":
                        while (!operators.isEmpty() && hasPrecedence(token, operators.peek()))
                            conditions.push(getCondition(operators.pop(), conditions));
                        operators.push(token);
                        break;
                    default:
                        conditions.push(new PlaceholderCondition(token));
                        break;
                }
            }

            while (!operators.isEmpty())
                conditions.push(getCondition(operators.pop(), conditions));

            return conditions.pop();
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean hasPrecedence(String op1, String op2) {
        if (op2.equals("(") || op2.equals(")"))
            return false;
        return (!op1.equals("&&") && !op1.equals("||")) || !op2.equals("!");
    }

    private static Condition getCondition(String operator, Deque<Condition> conditions) {
        switch (operator) {
            case "&&":
                return new AndCondition(conditions.pop(), conditions.pop());
            case "||":
                return new OrCondition(conditions.pop(), conditions.pop());
            case "!":
                return new InvertedCondition(conditions.pop());
            default:
                throw new IllegalArgumentException("Invalid operator");
        }
    }

}
