package com.eventcalculator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemModel {
    private final List<Item> items;
    private final double chance;
    private final boolean modified;

    public ItemModel copy() {
        return new ItemModel(this.getItems().stream().map(Item::new).toList(), this.getChance(), this.isModified());
    }

    public ItemModel addToItem(int itemId, int amount) {
        Item item = this.items.stream().filter(it -> it.hasId(itemId)).findFirst().orElse(null);

        boolean modified = item != null && amount != 0 && !item.isComplete();

        if(modified) {
            item.add(amount);
        }

        return new ItemModel(this.items, this.chance, this.modified || modified);
    }

    public ItemModel applyChance(double chance) {
        return new ItemModel(this.items, this.chance*chance, this.modified);
    }

    public ItemModel getClean() {
        return new ItemModel(this.getItems(), 1, false);
    }

    public boolean isComplete() {
        return items.stream().filter(item -> !item.isComplete()).findAny().isEmpty();
    }
}
