package com.eventcalculator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Item {
    private int id;
    private int amount;
    private int neededAmount;

    public Item(Item item) {
        this.id = item.getId();
        this.amount = item.getAmount();
        this.neededAmount = item.getNeededAmount();
    }

    public void add(int amount) {
        this.amount += amount;
    }

    public boolean hasId(int itemId) {
        return this.id == itemId;
    }

    public boolean isIncomplete() {
        return this.amount < this.neededAmount;
    }
}
