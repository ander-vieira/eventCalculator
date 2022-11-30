package com.eventcalculator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemModel {
    private double chance;
    private List<Item> items;

    public ItemModel(ItemModel itemModel) {
        this.chance = itemModel.getChance();
        this.items = itemModel.getItems().stream().map(Item::new).toList();
    }

    public ItemModel addToItem(int itemId, int amount) {
        items.stream().filter(item -> item.hasId(itemId)).findFirst().ifPresent(item -> item.add(amount));

        return this;
    }

    public ItemModel applyChance(double chance) {
        this.chance *= chance;

        return this;
    }

    public ItemModel copy() {
        return new ItemModel(this);
    }

    public boolean isComplete() {
        return items.stream().filter(item -> !item.isComplete()).findAny().isEmpty();
    }
}
