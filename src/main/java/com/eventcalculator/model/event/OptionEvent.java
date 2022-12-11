package com.eventcalculator.model.event;

import com.eventcalculator.model.EventEntry;
import com.eventcalculator.model.ItemModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionEvent implements Event {
    private final EventEntry[] entries;

    public static final String JSON_TYPE = "option";

    public OptionEvent(EventEntry[] entries) {
        double totalChance = Arrays.stream(entries).map(EventEntry::getChance).reduce(0.0, Double::sum);

        if(totalChance != 1) {
            this.entries = new EventEntry[entries.length+1];

            System.arraycopy(entries, 0, this.entries, 0, entries.length);

            this.entries[entries.length] = new EventEntry(new EmptyEvent(), 1-totalChance);
        } else {
            this.entries = entries;
        }
    }

    @Override
    public List<ItemModel> happen(ItemModel itemModel) {
        List<ItemModel> itemModels = new ArrayList<>();

        for(EventEntry entry : entries) {
            itemModels.addAll(entry.getEvent().happen(itemModel).stream().map(model -> model.applyChance(entry.getChance())).toList());
        }

        return itemModels;
    }
}
