package com.kumpus.atm.service;

import com.kumpus.atm.dao.CashDAO;
import com.kumpus.atm.model.CurrencyNoteQuantity;
import com.kumpus.atm.model.OperationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


class CashServiceTest {
    private CashService cashService;

    @Mock
    private CashDAO cashDAO;

    @BeforeEach
    void setUp() {
        cashDAO = mock(CashDAO.class);
        cashService = new CashService(cashDAO);
    }

    @Test
    void executeCommand_CheckBalance_OperationResultWithData() {
        String command = "?";
        OperationResult expectedResult = OperationResult.success();
        when(cashDAO.getAllCashNotes()).thenReturn(expectedResult);

        OperationResult result = cashService.executeCommand(command);

        assertTrue(result.isSuccess());
        assertEquals(expectedResult, result);
        verify(cashDAO, times(1)).getAllCashNotes();
    }

    @Test
    void executeCommand_Deposit_ValidCommand_OperationResultWithData() {
        String command = "+ USD 10 5";
        OperationResult expectedResult = OperationResult.success();

        // Устанавливаем поведение для мока cashDAO
        when(cashDAO.saveCashNote("USD", 10, 5)).thenReturn(expectedResult);

        OperationResult result = cashService.executeCommand(command);

        assertTrue(result.isSuccess());
        assertEquals(expectedResult, result);
        verify(cashDAO, times(1)).saveCashNote("USD", 10, 5);
    }

    @Test
    void executeCommand_Withdrawal_ValidCommand_OperationResultWithData() {
        String command = "- USD 10";
        OperationResult expectedResult = OperationResult.success();
        List<CurrencyNoteQuantity> currentData = Collections.singletonList(new CurrencyNoteQuantity("USD", 10, 50));
        when(cashDAO.getCashNotesByCurrency("USD")).thenReturn(OperationResult.successWithData(currentData));

        when(cashDAO.saveBulkCashNotes(Collections.singletonList(new CurrencyNoteQuantity("USD", 10, -1)))).thenReturn(expectedResult);


        OperationResult result = cashService.executeCommand(command);

        assertTrue(result.isSuccess());
        assertEquals(expectedResult, result);
        verify(cashDAO, times(1)).getCashNotesByCurrency("USD");
        verify(cashDAO, times(1)).saveBulkCashNotes(Collections.singletonList(new CurrencyNoteQuantity("USD", 10, -1)));
    }

    @Test
    void executeCommand_InvalidCommand_OperationResultWithError() {
        String command = "invalid command";
        OperationResult expectedResult = OperationResult.error("Invalid command");

        OperationResult result = cashService.executeCommand(command);

        assertFalse(result.isSuccess());
        assertEquals(expectedResult, result);
        verifyNoInteractions(cashDAO);
    }
}