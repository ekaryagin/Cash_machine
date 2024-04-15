package com.kumpus.atm.model;

import lombok.Data;

@Data
public class DepositCommandValues {
    private final String currency;
    private final int value;
    private final int quantity;
}
