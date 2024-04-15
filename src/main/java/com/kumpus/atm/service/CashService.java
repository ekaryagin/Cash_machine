package com.kumpus.atm.service;

import com.kumpus.atm.dao.CashDAO;
import com.kumpus.atm.exception.InvalidCommandException;
import com.kumpus.atm.model.CurrencyNoteQuantity;
import com.kumpus.atm.model.DepositCommandValues;
import com.kumpus.atm.model.OperationResult;
import com.kumpus.atm.model.WithdrawalCommandValues;
import com.kumpus.atm.utils.CommandValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CashService {

    private static final String BAD_COMMAND = "Invalid command";
    private final CashDAO cashDAO;

    public CashService(CashDAO cashDAO) {
        this.cashDAO = cashDAO;
    }

    public OperationResult executeCommand(String command) {
        return switch (command.charAt(0)) {
            case '?' -> checkBalanceOperation(command);
            case '+' -> depositOperation(command);
            case '-' -> withdrawalOperation(command);
            default -> OperationResult.error(BAD_COMMAND);
        };
    }

    private OperationResult checkBalanceOperation(String command) {
        if (!CommandValidator.validateCheckBalanceCommand(command)) {
            return OperationResult.error(BAD_COMMAND);
        }
        return cashDAO.getAllCashNotes();
    }

    private OperationResult depositOperation(String command) {
        try {
            DepositCommandValues depositCommandValues = CommandValidator.validateDepositCommand(command);

            return cashDAO.saveCashNote(depositCommandValues.getCurrency(),
                    depositCommandValues.getValue(), depositCommandValues.getQuantity());
        } catch (InvalidCommandException ex) {
            return OperationResult.error(BAD_COMMAND);
        }
    }


    private OperationResult withdrawalOperation(String command) {
        try {
            WithdrawalCommandValues withdrawalCommandValues = CommandValidator.validateWithdrawalCommand(command);
            String currency = withdrawalCommandValues.getCurrency();
            OperationResult operationResult = cashDAO.getCashNotesByCurrency(currency);
            if (operationResult.isSuccess()) {
                List<CurrencyNoteQuantity> currentData = operationResult.getData();
                int requiredAmount = withdrawalCommandValues.getAmount();

                if (isTotalAvailableNotEnough(currentData, requiredAmount)) {
                    return OperationResult.error("Insufficient funds");
                }

                return cashDAO.saveBulkCashNotes(
                        generateChangesToSave(currentData, requiredAmount));
            }
            return OperationResult.error(BAD_COMMAND);
        } catch (InvalidCommandException ex) {
            return OperationResult.error(BAD_COMMAND);
        }
    }

    private List<CurrencyNoteQuantity> generateChangesToSave(
                List<CurrencyNoteQuantity> currentData, int requiredAmount)
                throws InvalidCommandException {
        List<CurrencyNoteQuantity> changes = new ArrayList<>();
        String currency = currentData.get(0).getCurrency();

        // Сортируем список банкнот в порядке убывания их номиналов,
        // чтобы использовать наибольшие номиналы в первую очередь при выдаче средств.
        currentData.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));


        for (CurrencyNoteQuantity note : currentData) {
            if (requiredAmount <= 0) {
                break;
            }
            int noteValue = note.getValue();
            int possibleQuantity = Math.min(requiredAmount / noteValue, note.getQuantity());
            if (possibleQuantity > 0) {
                changes.add(new CurrencyNoteQuantity(currency, noteValue, possibleQuantity * (-1)));
                requiredAmount -= noteValue * possibleQuantity;
            }
        }
        if (requiredAmount != 0){
            throw new InvalidCommandException("Invalid deposit command");
        }
        return changes;
    }

    private boolean isTotalAvailableNotEnough(List<CurrencyNoteQuantity> notes, int amount){
        int totalAvailable = notes.stream()
                .mapToInt(note -> note.getValue() * note.getQuantity())
                .sum();
        return totalAvailable < amount;
    }

}
