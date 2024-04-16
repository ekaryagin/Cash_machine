package com.kumpus.atm.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class OperationResult {
    private static final String NO_ERROR = "NO_ERROR";
    private final Status status;
    // используется для предоставления списка доступных купюр и списка выдачи наличности
    private final List<CurrencyNoteQuantity> data;
    private final String errorMessage;

    private OperationResult(Status status,
                            List<CurrencyNoteQuantity> data,
                            String errorMessage) {
        this.status = status;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public static OperationResult success() {
        return new OperationResult(Status.SUCCESS, Collections.emptyList(), NO_ERROR);
    }

    public static OperationResult successCheckBalance(List<CurrencyNoteQuantity> data) {
        return new OperationResult(Status.INFO, data, NO_ERROR);
    }

    public static OperationResult successWithdrawal(List<CurrencyNoteQuantity> data) {
        return new OperationResult(Status.WITHDRAWAL, data, NO_ERROR);
    }

    public static OperationResult error(String errorMessage) {
        return new OperationResult(Status.ERROR, Collections.emptyList(), errorMessage);
    }

    public Status getStatus() {
        return status;
    }

    public List<CurrencyNoteQuantity> getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}