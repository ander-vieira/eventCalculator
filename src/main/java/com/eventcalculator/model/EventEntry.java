package com.eventcalculator.model;

import com.eventcalculator.model.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventEntry {
    private final Event event;
    private final double chance;
}
