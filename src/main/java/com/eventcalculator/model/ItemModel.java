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
    private final int attempt;

    private static final String SIGNATURE_DELIM = ",";

    public static ItemModel fromItems(List<Item> items) {
        return new ItemModel(items.stream().map(Item::new).toList(), 1, false, 0);
    }

    public ItemModel copy() {
        return new ItemModel(this.items.stream().map(Item::new).toList(), this.chance, this.modified, this.attempt);
    }

    public ItemModel addToItem(int itemId, int amount) {
        Item item = this.items.stream().filter(it -> it.hasId(itemId)).findFirst().orElse(null);

        boolean modified = item != null && amount != 0 && !item.isComplete();

        if(modified) {
            item.add(amount);
        }

        return new ItemModel(this.items, this.chance, this.modified || modified, this.attempt);
    }

    public ItemModel applyChance(double chance) {
        return new ItemModel(this.items, this.chance*chance, this.modified, this.attempt);
    }

    public ItemModel getChild() {
        return new ItemModel(this.getItems(), 1, false, this.attempt + 1);
    }

    public boolean isComplete() {
        return items.stream().filter(item -> !item.isComplete()).findAny().isEmpty();
    }

    public String getSignature() {
        return String.join(SIGNATURE_DELIM, items.stream().map(item -> Integer.toString(item.getAmount())).toList());
    }
}
