package dev.rosewood.rosegarden.command.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class InputIterator implements Iterator<String>, Cloneable {

    private List<String> input;
    private final List<String> stack;

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
     * @return peeks the next available argument, or an empty string if none are available
     */
    public String peek() {
        if (!this.hasNext())
            return "";
        return this.input.get(0);
    }

    protected List<String> getStack() {
        return List.copyOf(this.stack);
    }

    protected void clearStack() {
        this.stack.clear();
    }

    @Override
    public InputIterator clone() {
        try {
            InputIterator clone = (InputIterator) super.clone();
            clone.input = new LinkedList<>(this.input);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
