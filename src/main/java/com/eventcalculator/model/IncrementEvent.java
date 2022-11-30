package com.eventcalculator.model;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class IncrementEvent implements Event {
    private final int itemId;

    @Override
    public List<ItemModel> happen(ItemModel itemModel) {
        return List.of(itemModel.copy().addToItem(itemId, 1));
    }
}
