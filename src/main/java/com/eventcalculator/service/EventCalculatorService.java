package com.eventcalculator.service;

import com.eventcalculator.model.Event;
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

    public ResultData getResultStats(ItemModel itemModel, Event event, int maxAttempts) {
        final Map<String, ResultData> cachedResults = new HashMap<>();

        return this.getResultStats(itemModel, event, maxAttempts, cachedResults);
    }

    private ResultData getResultStats(ItemModel itemModel, Event event, int maxAttempts, Map<String, ResultData> cachedResults) {
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

    private ResultData getCachedResult(ItemModel itemModel, Event event, int maxAttempts, Map<String, ResultData> cachedResults) {
        final ResultData result;
        final String signature = itemModel.getSignature();

        if(cachedResults.containsKey(signature)) {
            result = cachedResults.get(signature);
        } else {
            result = this.getResultStats(itemModel.getChild(), event, maxAttempts, cachedResults);
            cachedResults.put(signature, result);
        }

        return result;
    }
}
