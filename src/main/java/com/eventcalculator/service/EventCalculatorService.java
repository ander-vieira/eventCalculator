package com.eventcalculator.service;

import com.eventcalculator.model.Event;
import com.eventcalculator.model.ItemModel;
import com.eventcalculator.model.ResultStats;
import com.eventcalculator.utils.StatsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventCalculatorService {
    @Autowired
    private StatsUtils distributionUtils;

    public ResultStats getResultStats(ItemModel itemModel, Event event, int maxAttempts) {
        if(itemModel.isComplete()) {
            double[] distribution = new double[maxAttempts];
            double remainder = 0;

            if(itemModel.getAttempt() < maxAttempts) {
                distribution[itemModel.getAttempt()] = 1;
            } else {
                remainder = 1;
            }

            return new ResultStats(distribution, remainder, 0);
        } else {
            List<ItemModel> newModels = event.happen(itemModel);
            List<ItemModel> modifiedModels = newModels.stream().filter(ItemModel::isModified).toList();
            double totalChance = modifiedModels.stream().map(ItemModel::getChance).reduce(0.0, Double::sum);

            if(totalChance != 0) {
                ResultStats result = new ResultStats(new double[maxAttempts], 0, 1/totalChance);

                for(ItemModel model : modifiedModels) {
                    ResultStats modelResult = this.getResultStats(model.getChild(), event, maxAttempts);
                    result = distributionUtils.addResults(result, modelResult, model.getChance()/totalChance);
                }

                return distributionUtils.geometricExpansion(result, totalChance);
            } else {
                return new ResultStats(new double[maxAttempts], 1, Double.POSITIVE_INFINITY);
            }
        }
    }
}
