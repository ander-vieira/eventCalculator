package com.eventcalculator.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class EventTest {
    @Value("${test.delta}")
    private double DELTA;

    @Mock
    private Event mockEvent;

    @Test
    public void emptyEventTest() {
        final Event event = new EmptyEvent();
        final ItemModel itemModel = ItemModel.fromItems(List.of(new Item(1, 0, 1)));

        final List<ItemModel> result = event.happen(itemModel);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(itemModel.getSignature(), result.get(0).getSignature());
        Assertions.assertEquals(1, result.get(0).getChance(), DELTA);
    }

    @Test
    public void incrementEventTest() {
        final Event event = new IncrementEvent(1);
        final ItemModel itemModel = ItemModel.fromItems(List.of(new Item(1, 0, 1)));

        final List<ItemModel> result = event.happen(itemModel);

        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.get(0).isComplete());
        Assertions.assertEquals(1, result.get(0).getChance(), DELTA);
    }

    @Test
    public void conditionEventTest() {
        final double chance = 0.5;
        final Event event = new ConditionEvent(mockEvent, chance);
        final ItemModel itemModel = ItemModel.fromItems(List.of(new Item(1, 0, 1)));

        Mockito.when(mockEvent.happen(itemModel)).thenReturn(List.of(itemModel));

        final List<ItemModel> result = event.happen(itemModel);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(itemModel.getSignature(), result.get(0).getSignature());
        Assertions.assertEquals(1-chance, result.get(0).getChance(), DELTA);
        Assertions.assertEquals(itemModel.getSignature(), result.get(1).getSignature());
        Assertions.assertEquals(chance, result.get(1).getChance(), DELTA);
    }

    @Test
    public void optionEventTest() {
        final double chance = 0.2;
        final EventEntry[] entries = {new EventEntry(mockEvent, chance), new EventEntry(mockEvent, chance)};
        final Event event = new OptionEvent(entries);
        final ItemModel itemModel = ItemModel.fromItems(List.of(new Item(1, 0, 1)));

        Mockito.when(mockEvent.happen(itemModel)).thenReturn(List.of(itemModel)).thenReturn(List.of(itemModel));

        final List<ItemModel> result = event.happen(itemModel);

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(itemModel.getSignature(), result.get(0).getSignature());
        Assertions.assertEquals(chance, result.get(0).getChance(), DELTA);
        Assertions.assertEquals(itemModel.getSignature(), result.get(1).getSignature());
        Assertions.assertEquals(chance, result.get(1).getChance(), DELTA);
        Assertions.assertEquals(itemModel.getSignature(), result.get(2).getSignature());
        Assertions.assertEquals(1-2*chance, result.get(2).getChance(), DELTA);
    }
}
