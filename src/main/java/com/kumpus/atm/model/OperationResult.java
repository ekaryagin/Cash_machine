package com.kumpus.atm.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class OperationResult {
    private final boolean success;
    private final List<CurrencyNoteQuantity> data;
    private final String errorMessage;

    private OperationResult(boolean success,
                            List<CurrencyNoteQuantity> data,
                            String errorMessage) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public static OperationResult success() {
        return new OperationResult(true, Collections.emptyList(), "NO_ERROR");
    }

    public static OperationResult successWithData(List<CurrencyNoteQuantity> data) {
        return new OperationResult(true, data, "NO_ERROR");
    }

    public static OperationResult error(String errorMessage) {
        return new OperationResult(false, Collections.emptyList(), errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public List<CurrencyNoteQuantity> getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        String result = "";
        if (success){
            for (CurrencyNoteQuantity note: data){
                result += note.getCurrency() + " " + note.getValue() + " " + note.getQuantity() + "\n";
            }
            return result + "OK\n";
        }
        return "ERROR\n";
    }
}