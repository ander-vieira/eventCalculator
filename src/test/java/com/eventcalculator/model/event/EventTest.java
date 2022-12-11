package com.eventcalculator.model.event;

import com.eventcalculator.model.EventEntry;
import com.eventcalculator.model.Item;
import com.eventcalculator.model.ItemModel;
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
        Assertions.assertEquals(1.0, result.get(0).getChance(), DELTA);
    }

    @Test
    public void incrementEventTest() {
        final int itemId = 1;
        final int amount = 0;
        final Event event = new IncrementEvent(itemId);
        final ItemModel itemModel = ItemModel.fromItems(List.of(new Item(itemId, amount, amount+1)));

        final List<ItemModel> result = event.happen(itemModel);

        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.get(0).isComplete());
        Assertions.assertEquals(1.0, result.get(0).getChance(), DELTA);
        Assertions.assertEquals(1, result.get(0).getItems().size());
        Assertions.assertEquals(itemId, result.get(0).getItems().get(0).getId());
        Assertions.assertEquals(amount+1, result.get(0).getItems().get(0).getAmount());
    }

    @Test
    public void addRangeEventTest() {
        final int itemId = 1;
        final int maxAmount = 3;
        final double chance = 1.0/(maxAmount+1);
        final Event event = new AddRangeEvent(itemId, 0, maxAmount);
        final ItemModel itemModel = ItemModel.fromItems(List.of(new Item(itemId, 0, 10)));

        List<ItemModel> result = event.happen(itemModel);

        Assertions.assertEquals(maxAmount+1, result.size());
        for(int i = 0 ; i <= maxAmount ; i++) {
            ItemModel model = result.get(i);
            List<Item> items = model.getItems();

            Assertions.assertEquals(1, items.size());
            Assertions.assertEquals(itemId, items.get(0).getId());
            Assertions.assertEquals(i, items.get(0).getAmount());
            Assertions.assertEquals(chance, model.getChance(), DELTA);
        }
    }

    @Test
    public void conditionEventTest() {
        final double chance = 0.5;
        final Event event = new ConditionEvent(mockEvent, chance);
        final ItemModel itemModel = ItemModel.fromItems(List.of(new Item(1, 0, 1)));

        Mockito.doReturn(List.of(itemModel)).when(mockEvent).happen(itemModel);

        final List<ItemModel> result = event.happen(itemModel);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(itemModel.getSignature(), result.get(0).getSignature());
        Assertions.assertEquals(1-chance, result.get(0).getChance(), DELTA);
        Assertions.assertEquals(itemModel.getSignature(), result.get(1).getSignature());
        Assertions.assertEquals(chance, result.get(1).getChance(), DELTA);
    }

    @Test
    public void groupEventTest() {
        final Event[] events = {mockEvent, mockEvent};
        final Event event = new GroupEvent(events);
        final ItemModel itemModel0 = ItemModel.fromItems(List.of(new Item(1, 0, 2)));
        final ItemModel itemModel1 = ItemModel.fromItems(List.of(new Item(1, 1, 2)));
        final ItemModel itemModel2 = ItemModel.fromItems(List.of(new Item(1, 2, 2)));

        Mockito.doReturn(List.of(itemModel0, itemModel1)).when(mockEvent).happen(itemModel0);
        Mockito.doReturn(List.of(itemModel1, itemModel2)).when(mockEvent).happen(itemModel1);

        final List<ItemModel> result = event.happen(itemModel0);

        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals(itemModel0.getSignature(), result.get(0).getSignature());
        Assertions.assertEquals(itemModel1.getSignature(), result.get(1).getSignature());
        Assertions.assertEquals(itemModel1.getSignature(), result.get(2).getSignature());
        Assertions.assertEquals(itemModel2.getSignature(), result.get(3).getSignature());
    }

    @Test
    public void optionEventTest() {
        final double chance = 0.2;
        final EventEntry[] entries = {new EventEntry(mockEvent, chance), new EventEntry(mockEvent, chance)};
        final Event event = new OptionEvent(entries);
        final ItemModel itemModel = ItemModel.fromItems(List.of(new Item(1, 0, 1)));

        Mockito.doReturn(List.of(itemModel)).when(mockEvent).happen(itemModel);

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
