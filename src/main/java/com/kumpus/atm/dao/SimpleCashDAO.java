package com.kumpus.atm.dao;

import com.kumpus.atm.model.CurrencyNoteQuantity;
import com.kumpus.atm.model.OperationResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SimpleCashDAO implements CashDAO {

    private final Map<String, Map<Integer, Integer>> cashData = new HashMap<>();
    private static final String NOT_FOUND = "Currency note not found";

    @Override
    public OperationResult deleteCashNote(String currency, int value) {
        if (cashData.containsKey(currency)) {
            Map<Integer, Integer> currencyData = cashData.get(currency);
            if (currencyData.containsKey(value)) {
                currencyData.remove(value);
                return OperationResult.success();
            }
        }
        return OperationResult.error(NOT_FOUND);
    }

    @Override
    public OperationResult saveCashNote(String currency, int value, int quantity) {
        cashData.putIfAbsent(currency, new HashMap<>());
        Map<Integer, Integer> currencyData = cashData.get(currency);
        currencyData.merge(value, quantity, Integer::sum);
        return OperationResult.success();
    }

    @Override
    public OperationResult saveBulkCashNotes(List<CurrencyNoteQuantity> currencyNoteQuantityList){
        for(CurrencyNoteQuantity note: currencyNoteQuantityList){
            saveCashNote(note.getCurrency(), note.getValue(), note.getQuantity());
        }
        return OperationResult.success();
    }

    @Override
    public OperationResult getCashNote(String currency, int value) {
        if (cashData.containsKey(currency)) {
            Map<Integer, Integer> currencyData = cashData.get(currency);
            if (currencyData.containsKey(value)) {
                return OperationResult.successCheckBalance(
                        Collections.singletonList(
                                new CurrencyNoteQuantity(currency, value, currencyData.get(value))));
            }
        }
        return OperationResult.error(NOT_FOUND);
    }

    @Override
    public OperationResult getCashNotesByCurrency(String currency) {
        if (cashData.containsKey(currency)) {
            Map<Integer, Integer> currencyData = cashData.get(currency);
            List<CurrencyNoteQuantity> currencyNotes = new ArrayList<>();
            currencyData.forEach((value, quantity) ->
                    currencyNotes.add(new CurrencyNoteQuantity(currency, value, quantity)));
            return OperationResult.successCheckBalance(currencyNotes);
        }
        return OperationResult.error(NOT_FOUND);
    }

    @Override
    public OperationResult getAllCashNotes() {
        List<CurrencyNoteQuantity> allNotes = new ArrayList<>();

        cashData.forEach((currency, notes) ->
                notes.forEach((value, quantity) ->
                        allNotes.add(new CurrencyNoteQuantity(currency, value, quantity))));

        return OperationResult.successCheckBalance(allNotes);
    }
}
