package com.eventcalculator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventEntry {
    private final Event event;
    private final double chance;
}
