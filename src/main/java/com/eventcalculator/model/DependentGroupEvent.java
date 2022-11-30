package com.eventcalculator.model;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class DependentGroupEvent implements Event {
    private final EventEntry[] entries;

    @Override
    public List<ItemModel> happen(ItemModel itemModel) {
        List<ItemModel> itemModels = new ArrayList<>();

        for(EventEntry entry : entries) {
            itemModels.addAll(entry.getEvent().happen(itemModel).stream().map(model -> model.applyChance(entry.getChance())).toList());
        }

        return itemModels;
    }
}
