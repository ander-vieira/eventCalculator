package com.eventcalculator.model.event;

import com.eventcalculator.json.EventDeserializer;
import com.eventcalculator.model.ItemModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(using = EventDeserializer.class)
public interface Event {
    List<ItemModel> happen(ItemModel itemModel);
}
