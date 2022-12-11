package com.eventcalculator.model.event;

import com.eventcalculator.model.ItemModel;

import java.util.List;

public class EmptyEvent implements Event {
    public static final String JSON_TYPE = "empty";

    @Override
    public List<ItemModel> happen(ItemModel itemModel) {
        return List.of(itemModel);
    }
}
