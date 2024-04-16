package com.kumpus.atm.utils;

import com.kumpus.atm.model.CurrencyNoteQuantity;
import com.kumpus.atm.model.OperationResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageProcessorTest {
    @Test
    void getMessage_SuccessStatus_ReturnsOK() {
        OperationResult operationResult = OperationResult.success();
        assertEquals("OK", MessageProcessor.getMessage(operationResult));
    }

    @Test
    void getMessage_ErrorStatus_ReturnsERROR() {
        OperationResult operationResult = OperationResult.error("some error");
        assertEquals("ERROR", MessageProcessor.getMessage(operationResult));
    }

    @Test
    void getMessage_InfoStatus_ReturnsCheckBalanceInfo() {
        List<CurrencyNoteQuantity> data = new ArrayList<>();
        data.add(new CurrencyNoteQuantity("USD", 10, 5));
        OperationResult operationResult = OperationResult.successCheckBalance(data);
        String expected = "USD 10 5\nOK";
        assertEquals(expected, MessageProcessor.getMessage(operationResult));
    }

    @Test
    void getMessage_WithdrawalStatus_ReturnsWithdrawalInfo() {
        List<CurrencyNoteQuantity> data = new ArrayList<>();
        data.add(new CurrencyNoteQuantity("USD", 10, 5));
        OperationResult operationResult = OperationResult.successWithdrawal(data);
        String expected = "10 5\nOK";
        assertEquals(expected, MessageProcessor.getMessage(operationResult));
    }
}