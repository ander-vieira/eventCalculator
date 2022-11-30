package com.eventcalculator.service;

import com.eventcalculator.model.Event;
import com.eventcalculator.model.Item;
import com.eventcalculator.model.ItemModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventCalculatorService {
    public double getExpectedAttempts(List<Item> items, Event event) {
        ItemModel itemModel = new ItemModel(items, 1, false);

        return this.getExpectedAttempts(itemModel, event);
    }

    private double getExpectedAttempts(ItemModel itemModel, Event event) {
        if(itemModel.isComplete()) {
            return 0;
        } else {
            List<ItemModel> newModels = event.happen(itemModel);
            double result = 1;

            List<ItemModel> modifiedModels = newModels.stream().filter(ItemModel::isModified).toList();

            double totalChance = 0;
            for(ItemModel model : modifiedModels) {
                result += model.getChance()*this.getExpectedAttempts(model.getClean(), event);
                totalChance += model.getChance();
            }

            if(totalChance != 0) {
                result /= totalChance;

                return result;
            } else {
                return Double.POSITIVE_INFINITY;
            }

        }

    }
}
