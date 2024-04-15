package com.kumpus.atm.dao;

import com.kumpus.atm.model.CurrencyNoteQuantity;
import com.kumpus.atm.model.OperationResult;

import java.util.List;

public interface CashDAO {

    OperationResult deleteCashNote(String currency, int value);

    OperationResult saveCashNote(String currency, int value, int quantity);
    OperationResult saveBulkCashNotes(List<CurrencyNoteQuantity> currencyNoteQuantityList);

    OperationResult getCashNote(String currency, int value);

    OperationResult getCashNotesByCurrency(String currency);

    OperationResult getAllCashNotes();
}
