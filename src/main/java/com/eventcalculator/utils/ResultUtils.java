package com.eventcalculator.utils;

import com.eventcalculator.model.DistributionData;
import com.eventcalculator.model.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResultUtils {
    @Autowired
    private DistributionUtils distributionUtils;

    public ResultData addResultData(ResultData oldData, ResultData newData, double ratio) {
        final double expectedValue;
        if(ratio < 1) {
            expectedValue = oldData.getExpectedValue()*(1-ratio)+ newData.getExpectedValue()*ratio;
        } else {
            expectedValue = newData.getExpectedValue();
        }

        final DistributionData distribution = distributionUtils.addDistribution(oldData.getDistribution(), newData.getDistribution(), ratio);

        return new ResultData(expectedValue, distribution);
    }

    public ResultData convoluteData(ResultData data, double ratio) {
        final double expectedValue = data.getExpectedValue() + 1/ratio;

        final int maxAttempts = data.getDistribution().getValues().length;
        final DistributionData geometricDistribution = distributionUtils.getGeometricDistribution(ratio, maxAttempts);
        final DistributionData distribution = distributionUtils.getConvolution(data.getDistribution(), geometricDistribution);

        return new ResultData(expectedValue, distribution);
    }
}
