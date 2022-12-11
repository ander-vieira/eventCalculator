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

    private static final String SIGNATURE_DELIM = ",";

    public static ItemModel fromItems(List<Item> items) {
        return new ItemModel(items.stream().map(Item::new).toList(), 1, false);
    }

    public ItemModel copy() {
        return new ItemModel(this.items.stream().map(Item::new).toList(), this.chance, this.modified);
    }

    public ItemModel addToItem(int itemId, int amount) {
        Item item = this.items.stream().filter(it -> it.hasId(itemId)).findFirst().orElse(null);

        boolean modified = item != null && item.isIncomplete() && amount != 0;

        if(modified) {
            item.add(amount);
        }

        return new ItemModel(this.items, this.chance, this.modified || modified);
    }

    public ItemModel applyChance(double chance) {
        return new ItemModel(this.items, this.chance*chance, this.modified);
    }

    public ItemModel getChild() {
        return new ItemModel(this.getItems(), 1, false);
    }

    public boolean isComplete() {
        return items.stream().filter(Item::isIncomplete).findAny().isEmpty();
    }

    public String getSignature() {
        return String.join(SIGNATURE_DELIM, items.stream().map(item -> Integer.toString(item.getAmount())).toList());
    }
}
