package com.eventcalculator.service;

import com.eventcalculator.model.event.Event;
import com.eventcalculator.model.ItemModel;
import com.eventcalculator.model.ResultData;
import com.eventcalculator.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventCalculatorService {
    @Autowired
    private ResultUtils resultUtils;

    /**
     * Get the expected value and distribution of the number of attempts needed to
     * reach the specified required number of items.
     * This method runs the item model through the event repeatedly until the model satisfies the requirements,
     * then uses the resulting map of possible states to calculate the statistical parameters.
     *
     * @param itemModel The item model to be modified by the event
     * @param event The event to run the model through
     * @param maxAttempts The maximum number of attempts to generate the distribution for
     * @return The statistical parameters for the number of attempts needed
     */
    public ResultData getResultData(ItemModel itemModel, Event event, int maxAttempts) {
        final Map<String, ResultData> cachedResults = new HashMap<>();

        return this.getResultData(itemModel, event, maxAttempts, cachedResults);
    }

    /**
     * Get the expected value and distribution of the number of attempts needed to
     * reach the specified required number of items.
     * This method runs the item model through the event repeatedly until the model satisfies the requirements,
     * then uses the resulting map of possible states to calculate the statistical parameters.
     *
     * @param itemModel The item model to be modified by the event
     * @param event The event to run the model through
     * @param maxAttempts The maximum number of attempts to generate the distribution for
     * @param cachedResults A map of results for already calculated states of the model
     * @return The statistical parameters for the number of attempts needed
     */
    private ResultData getResultData(ItemModel itemModel, Event event, int maxAttempts, Map<String, ResultData> cachedResults) {
        if(itemModel.isComplete()) {
            return ResultData.finished(maxAttempts);
        } else {
            final List<ItemModel> models = event.happen(itemModel).stream().filter(ItemModel::isModified).toList();

            ResultData partialResult = ResultData.empty(maxAttempts);
            double totalChance = 0;

            for(ItemModel model : models) {
                totalChance += model.getChance();

                final ResultData modelResult = this.getCachedResult(model, event, maxAttempts, cachedResults);

                partialResult = resultUtils.addResultData(partialResult, modelResult, model.getChance()/totalChance);
            }

            return resultUtils.convoluteData(partialResult, totalChance);
        }
    }

    /**
     * Check if the specified model state has already been processed.
     * If so, get the cached results to improve performance.
     * Otherwise, process the state by recursively calling getResultData.
     *
     * @param itemModel The item model to be modified by the event
     * @param event The event to run the model through
     * @param maxAttempts The maximum number of attempts to generate the distribution for
     * @param cachedResults A map of results for already calculated states of the model
     * @return The statistical parameters for the number of attempts needed
     */
    private ResultData getCachedResult(ItemModel itemModel, Event event, int maxAttempts, Map<String, ResultData> cachedResults) {
        final ResultData result;
        final String signature = itemModel.getSignature();

        if(cachedResults.containsKey(signature)) {
            result = cachedResults.get(signature);
        } else {
            result = this.getResultData(itemModel.getChild(), event, maxAttempts, cachedResults);
            cachedResults.put(signature, result);
        }

        return result;
    }
}
