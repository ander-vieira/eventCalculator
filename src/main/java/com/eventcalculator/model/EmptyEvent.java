package com.eventcalculator.model;

import java.util.List;

public class EmptyEvent implements Event {
    @Override
    public List<ItemModel> happen(ItemModel itemModel) {
        return List.of(itemModel);
    }
}
