package com.kumpus.atm.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyNoteQuantity {
    private String currency;
    private int value;
    private int quantity;
}
