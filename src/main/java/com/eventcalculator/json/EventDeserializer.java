package com.eventcalculator.json;

import com.eventcalculator.model.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EventDeserializer extends JsonDeserializer<Event> {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Event deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        String type = node.get("type").textValue();
        return switch (type) {
            case EmptyEvent.JSON_TYPE -> new EmptyEvent();
            case IncrementEvent.JSON_TYPE -> new IncrementEvent(node.get("itemId").asInt());
            case AddRangeEvent.JSON_TYPE -> new AddRangeEvent(node.get("itemId").asInt(),
                    node.get("minAmount").asInt(),
                    node.get("maxAmount").asInt());
            case ConditionEvent.JSON_TYPE -> new ConditionEvent(objectMapper.treeToValue(node.get("event"), Event.class),
                    node.get("chance").asDouble());
            case GroupEvent.JSON_TYPE -> new GroupEvent(objectMapper.treeToValue(node.get("entries"), EventEntry[].class));
            case DependentGroupEvent.JSON_TYPE ->
                    new DependentGroupEvent(objectMapper.treeToValue(node.get("entries"), EventEntry[].class));
            default -> null;
        };
    }
}
