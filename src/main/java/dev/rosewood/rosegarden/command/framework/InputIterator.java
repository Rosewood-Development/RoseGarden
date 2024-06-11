package dev.rosewood.rosegarden.command.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class InputIterator implements Iterator<String>, Cloneable {

    private List<String> input;
    private List<String> stack;

    public InputIterator(Collection<String> input) {
        this.input = new LinkedList<>(input);
        this.stack = new ArrayList<>(input.size());
    }

    /**
     * @return true if there is another argument available, false otherwise
     */
    @Override
    public boolean hasNext() {
        return !this.input.isEmpty();
    }

    /**
     * @return pops the next available argument, or an empty string if none are available
     */
    @Override
    public String next() {
        if (!this.hasNext())
            return "";
        String value = this.input.remove(0);
        this.stack.add(value);
        return value;
    }

    /**
     * Pops the next available argument <code>amount</code> times.
     *
     * @param amount the number of arguments to pop
     * @return the next <code>amount</code> arguments, missing arguments will be filled with empty strings
     */
    public String[] next(int amount) {
        String[] result = new String[amount];
        for (int i = 0; i < amount; i++)
            result[i] = this.next();
        return result;
    }

    /**
     * @return peeks the next available argument, or an empty string if none are available
     */
    public String peek() {
        if (!this.hasNext())
            return "";
        return this.input.get(0);
    }

    /**
     * Peeks the next available argument <code>amount</code> times.
     *
     * @param amount the number of arguments to peek
     * @return the next <code>amount</code> arguments, missing arguments will be filled with empty strings
     */
    public String[] peek(int amount) {
        String[] result = new String[amount];
        InputIterator it = this.clone();
        for (int i = 0; i < amount; i++)
            result[i] = it.next();
        return result;
    }

    protected List<String> getStack() {
        return Collections.unmodifiableList(this.stack);
    }

    protected void clearStack() {
        this.stack.clear();
    }

    protected void restore(InputIterator other) {
        this.input = new LinkedList<>(other.input);
        this.stack = new ArrayList<>(other.stack);
    }

    @Override
    public InputIterator clone() {
        try {
            InputIterator clone = (InputIterator) super.clone();
            clone.restore(this);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
