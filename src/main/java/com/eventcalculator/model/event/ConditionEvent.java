package com.eventcalculator.model.event;

import com.eventcalculator.model.ItemModel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ConditionEvent implements Event {
    private final Event event;
    private final double chance;

    public static final String JSON_TYPE = "condition";

    @Override
    public List<ItemModel> happen(ItemModel itemModel) {
        final List<ItemModel> newModels = new ArrayList<>();

        newModels.add(itemModel.copy().applyChance(1-chance));
        newModels.addAll(event.happen(itemModel).stream().map(model -> model.applyChance(chance)).toList());

        return newModels;
    }
}
