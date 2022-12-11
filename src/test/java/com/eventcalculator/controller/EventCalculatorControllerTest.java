package com.eventcalculator.controller;

import com.eventcalculator.dto.ProcessModelRequest;
import com.eventcalculator.model.Item;
import com.eventcalculator.model.ItemModel;
import com.eventcalculator.model.ResultData;
import com.eventcalculator.model.event.EmptyEvent;
import com.eventcalculator.model.event.Event;
import com.eventcalculator.service.EventCalculatorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class EventCalculatorControllerTest {
    @InjectMocks
    EventCalculatorController eventCalculatorController;
    @Mock
    EventCalculatorService eventCalculatorService;

    @Test
    public void processModelTest() {
        final ProcessModelRequest request = new ProcessModelRequest(List.of(new Item(1, 0, 1)),
                new EmptyEvent(),
                100);
        final ResultData resultData = ResultData.empty(100);

        Mockito.doReturn(resultData)
                .when(eventCalculatorService)
                .getResultData(Mockito.nullable(ItemModel.class), Mockito.nullable(Event.class), Mockito.anyInt());

        final ResultData result = eventCalculatorController.processModel(request);

        Assertions.assertEquals(resultData, result);
    }
}
