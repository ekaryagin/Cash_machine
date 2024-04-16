package com.kumpus.atm.model;

import lombok.Data;

@Data
public class CommandValuesWithdrawal {
    private final String currency;
    private final int amount;
}
