package com.kumpus.atm.service;

import com.kumpus.atm.dao.CashDAO;
import com.kumpus.atm.exception.InvalidCommandException;
import com.kumpus.atm.model.CommandValuesDeposit;
import com.kumpus.atm.model.CommandValuesWithdrawal;
import com.kumpus.atm.model.CurrencyNoteQuantity;
import com.kumpus.atm.model.OperationResult;
import com.kumpus.atm.model.Status;
import com.kumpus.atm.utils.CommandValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CashService {

    private static final String BAD_COMMAND = "Invalid command";
    private static final String NO_MONEY = "Insufficient funds";
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
            CommandValuesDeposit depositCommandValues = CommandValidator.validateDepositCommand(command);

            return cashDAO.saveCashNote(depositCommandValues.getCurrency(),
                    depositCommandValues.getValue(), depositCommandValues.getQuantity());
        } catch (InvalidCommandException ex) {
            return OperationResult.error(BAD_COMMAND);
        }
    }

    private OperationResult withdrawalOperation(String command) {
        try {
            CommandValuesWithdrawal commandValuesWithdrawal = CommandValidator.validateWithdrawalCommand(command);
            String currency = commandValuesWithdrawal.getCurrency();
            OperationResult operationResult = cashDAO.getCashNotesByCurrency(currency);
            if (operationResult.getStatus() == Status.INFO) {
                List<CurrencyNoteQuantity> currentData = operationResult.getData();
                int requiredAmount = commandValuesWithdrawal.getAmount();

                if (isTotalAvailableNotEnough(currentData, requiredAmount)) {
                    return OperationResult.error(NO_MONEY);
                }

                List<CurrencyNoteQuantity> changes = generateChangesToSave(currentData, requiredAmount);

                if (cashDAO.saveBulkCashNotes(changes).getStatus() == Status.SUCCESS){
                    // в DAO отправляем изменения, но пользователю выводим положительные значения
                    revertChangesToReport(changes);
                    return OperationResult.successWithdrawal(changes);
                }
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
                // поскольку логика DAO настроена на использование merge, формируются именно вносимые изменения
                changes.add(new CurrencyNoteQuantity(currency, noteValue, possibleQuantity * (-1)));
                requiredAmount -= noteValue * possibleQuantity;
            }
        }
        if (requiredAmount != 0) {
            throw new InvalidCommandException(NO_MONEY);
        }
        return changes;
    }

    private boolean isTotalAvailableNotEnough(List<CurrencyNoteQuantity> notes, int amount) {

        // подсчет общей суммы доступных денег и сравнение с запрашиваемой
        int totalAvailable = notes.stream()
                .mapToInt(note -> note.getValue() * note.getQuantity())
                .sum();
        return totalAvailable < amount;
    }

    private void revertChangesToReport(List<CurrencyNoteQuantity> notes){
        for (CurrencyNoteQuantity note : notes){
            note.setQuantity(note.getQuantity() * (-1));
        }
    }

}
