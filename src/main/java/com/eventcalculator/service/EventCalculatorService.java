package com.eventcalculator.service;

import com.eventcalculator.model.Event;
import com.eventcalculator.model.Item;
import com.eventcalculator.model.ItemModel;
import com.eventcalculator.utils.DistributionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventCalculatorService {
    @Autowired
    private DistributionUtils distributionUtils;

    public double getExpectedAttempts(List<Item> items, Event event) {
        ItemModel itemModel = ItemModel.fromItems(items);

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
                result += model.getChance()*this.getExpectedAttempts(model.getChild(), event);
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

    public double[] getAttemptDistribution(List<Item> items, Event event, int numAttempts) {
        return getAttemptDistribution(ItemModel.fromItems(items), event, numAttempts, 1);
    }

    private double[] getAttemptDistribution(ItemModel itemModel, Event event, int numAttempts, double currentChance) {
        double[] result = new double[numAttempts];

        if(itemModel.isComplete()) {
            result[itemModel.getAttempt()] = currentChance*itemModel.getChance();
        } else {
            List<ItemModel> newModels = event.happen(itemModel);
            List<ItemModel> modifiedModels = newModels.stream().filter(ItemModel::isModified).toList();

            double totalChance = 0;
            for(ItemModel model : modifiedModels) {
                double[] modelResult = getAttemptDistribution(model.getChild(), event, numAttempts, currentChance*model.getChance());
                distributionUtils.addArray(result, modelResult);
                totalChance += model.getChance();
            }

            result = distributionUtils.geometricExpansion(result, 1-totalChance);
        }

        return result;
    }
}
