package com.eventcalculator.model;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class AddRangeEvent implements Event {
    private final int itemId;
    private final int minAmount;
    private final int maxAmount;

    public static final String JSON_TYPE = "range";

    @Override
    public List<ItemModel> happen(ItemModel itemModel) {
        List<ItemModel> newModels = new ArrayList<>();

        double resultChance = 1.0/(maxAmount-minAmount+1);

        for(int amount = minAmount ; amount <= maxAmount ; amount++) {
            newModels.add(itemModel.copy().applyChance(resultChance).addToItem(itemId, amount));
        }

        return newModels;
    }
}
