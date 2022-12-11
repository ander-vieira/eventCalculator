package com.eventcalculator.model.event;

import com.eventcalculator.model.ItemModel;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class IncrementEvent implements Event {
    private final int itemId;

    public static final String JSON_TYPE = "increment";

    @Override
    public List<ItemModel> happen(ItemModel itemModel) {
        return List.of(itemModel.copy().addToItem(itemId, 1));
    }
}
