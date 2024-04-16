package com.kumpus.atm.utils;

import com.kumpus.atm.model.CurrencyNoteQuantity;
import com.kumpus.atm.model.OperationResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageProcessor {
    public static String getMessage(OperationResult operationResult){
        return switch (operationResult.getStatus()){
            case SUCCESS -> "OK";
            case ERROR ->  "ERROR";
            case INFO -> getCheckBalanceInfo(operationResult.getData());
            case WITHDRAWAL -> getWithdrawalInfo(operationResult.getData());
        };
    }

    private static String getCheckBalanceInfo(List<CurrencyNoteQuantity> data){
        StringBuilder result = new StringBuilder();
        for (CurrencyNoteQuantity note : data) {
            result.append(note.getCurrency())
                    .append(" ")
                    .append(note.getValue())
                    .append(" ")
                    .append(note.getQuantity())
                    .append("\n");
        }
        return result + "OK";
    }

    private static String getWithdrawalInfo(List<CurrencyNoteQuantity> data){
        StringBuilder result = new StringBuilder();
        for (CurrencyNoteQuantity note : data) {
            result.append(note.getValue())
                    .append(" ")
                    .append(note.getQuantity())
                    .append("\n");
        }
        return result + "OK";
    }
}
