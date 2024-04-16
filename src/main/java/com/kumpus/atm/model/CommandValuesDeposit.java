package com.kumpus.atm.model;

import lombok.Data;

@Data
public class CommandValuesDeposit {
    private final String currency;
    private final int value;
    private final int quantity;
}
