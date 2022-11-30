package com.eventcalculator.model;

import com.eventcalculator.json.EventDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(using = EventDeserializer.class)
public interface Event {
    List<ItemModel> happen(ItemModel itemModel);
}
