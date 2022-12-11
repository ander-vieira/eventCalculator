package com.eventcalculator.dto;

import com.eventcalculator.model.event.Event;
import com.eventcalculator.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProcessModelRequest {
    private final List<Item> items;
    private final Event event;
    private final int maxAttempts;
}
