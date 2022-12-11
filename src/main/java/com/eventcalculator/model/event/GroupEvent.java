package com.eventcalculator.model.event;

import com.eventcalculator.model.ItemModel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class GroupEvent implements Event {
    private final Event[] events;

    public static final String JSON_TYPE = "group";

    @Override
    public List<ItemModel> happen(ItemModel itemModel) {
        return getEntryResults(itemModel, 0);
    }

    private List<ItemModel> getEntryResults(ItemModel itemModel, int eventId) {
        if(eventId < events.length) {
            Event event = events[eventId];

            List<ItemModel> models = event.happen(itemModel);

            List<ItemModel> itemModels = new ArrayList<>();
            for(ItemModel model : models) {
                itemModels.addAll(getEntryResults(model, eventId+1));
            }

            return itemModels;
        } else {
            return List.of(itemModel);
        }
    }
}
