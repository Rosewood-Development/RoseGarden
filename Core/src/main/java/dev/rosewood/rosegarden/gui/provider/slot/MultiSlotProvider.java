package dev.rosewood.rosegarden.gui.provider.slot;

import org.bukkit.configuration.ConfigurationSection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MultiSlotProvider extends AbstractSlotProvider {

    public static final String ID = "slots";

    // Code Constructors

    public MultiSlotProvider(Integer... slots) {
        super(ID, null);

        this.slots.addAll(Arrays.asList(slots));
    }

    public MultiSlotProvider(List<Integer> slots) {
        super(ID, null);

        this.slots.addAll(slots);
    }

    // Config Constructors

    public MultiSlotProvider(String key, ConfigurationSection section) {
        super(key, section);

        List<String> slots = section.getStringList(this.getKey());
        for (String slot : slots)
            this.add(slot);
    }

    public MultiSlotProvider add(Integer... slots) {
        this.slots.addAll(Arrays.asList(slots));
        return this;
    }

    public MultiSlotProvider add(List<Integer> slots) {
        this.slots.addAll(slots);
        return this;
    }

    public MultiSlotProvider add(String... slots) {
        for (String slot : slots) {
            if (slot.contains("-")) {
                String[] slotsSplit = slot.split("-");
                int first = Integer.parseInt(slotsSplit[0]);
                int second = Integer.parseInt(slotsSplit[1]);

                List<Integer> collected = new ArrayList<>();
                for (int i = first; i < second + 1; i++)
                    collected.add(i);

                this.slots.addAll(collected);
            } else {
                this.slots.add(Integer.parseInt(slot));
            }
        }

        return this;
    }

    public MultiSlotProvider addRange(int min, int max) {
        for (int i = min; i < max + 1; i++)
            this.slots.add(i);

        return this;
    }

    @Override
    public void write(ConfigurationSection section) {
        Collections.sort(this.slots);
        List<String> slotsAsStrings = new ArrayList<>();

        int start = this.slots.get(0);
        int end = this.slots.get(0);
        for (int i = 1; i < this.slots.size(); i++) {
            int current = slots.get(i);

            if (current != end + 1) {
                if (start == end) {
                    slotsAsStrings.add(String.valueOf(start));
                } else {
                    slotsAsStrings.add(start + "-" + end);
                }

                start = current;
            }

            end = current;
        }

        if (start == end) {
            slotsAsStrings.add(String.valueOf(start));
        } else {
            slotsAsStrings.add(start + "-" + end);
        }

        section.set(this.getKey(), slotsAsStrings);
    }

    // Static Constructors

    public static MultiSlotProvider of(Integer... slots) {
        return new MultiSlotProvider(slots);
    }

    public static MultiSlotProvider of(String... slots) {
        MultiSlotProvider provider = new MultiSlotProvider();
        provider.add(slots);
        return provider;
    }

    public static MultiSlotProvider range(int min, int max) {
        MultiSlotProvider provider = new MultiSlotProvider();
        provider.addRange(min, max);
        return provider;
    }

}
