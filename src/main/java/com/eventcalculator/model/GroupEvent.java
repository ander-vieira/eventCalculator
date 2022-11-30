package com.eventcalculator.model;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class GroupEvent implements Event {
    private final EventEntry[] entries;

    @Override
    public List<ItemModel> happen(ItemModel itemModel) {
        return getEntryResults(itemModel, 0);
    }

    private List<ItemModel> getEntryResults(ItemModel itemModel, int entryId) {
        if(entryId < entries.length) {
            EventEntry entry = entries[entryId];

            ItemModel modelFalse = itemModel.copy().applyChance(1-entry.getChance());
            List<ItemModel> modelsTrue = entry.getEvent()
                    .happen(itemModel)
                    .stream()
                    .map(model -> model.applyChance(entry.getChance()))
                    .toList();

            List<ItemModel> itemModels = new ArrayList<>(getEntryResults(modelFalse, entryId + 1));
            for(ItemModel modelTrue : modelsTrue) {
                itemModels.addAll(getEntryResults(modelTrue, entryId+1));
            }

            return itemModels;
        } else {
            return List.of(itemModel);
        }
    }
}
