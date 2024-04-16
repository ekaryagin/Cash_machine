package com.kumpus.atm.dao;

import com.kumpus.atm.model.CurrencyNoteQuantity;
import com.kumpus.atm.model.OperationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleCashDAOTest {
    private SimpleCashDAO cashDAO;

    @BeforeEach
    void setUp() {
        cashDAO = new SimpleCashDAO();
    }

    @Test
    void deleteCashNote_ShouldRemoveNoteFromCashData_WhenNoteExists() {
        cashDAO.saveCashNote("USD", 10, 100);

        OperationResult result = cashDAO.deleteCashNote("USD", 10);

        //assertThat(result.isSuccess()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("NO_ERROR");
    }

    @Test
    void deleteCashNote_ShouldReturnError_WhenNoteDoesNotExist() {
        OperationResult result = cashDAO.deleteCashNote("EUR", 20);

        //assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrorMessage()).isEqualTo("Currency note not found");
    }

    @Test
    void saveCashNote_ShouldAddNoteToCashData() {
        OperationResult result = cashDAO.saveCashNote("USD", 5, 50);

        //assertThat(result.isSuccess()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("NO_ERROR");
    }

    @Test
    void saveBulkCashNotes_ShouldAddNotesToCashData() {
        List<CurrencyNoteQuantity> notes = List.of(
                new CurrencyNoteQuantity("USD", 5, 50),
                new CurrencyNoteQuantity("EUR", 10, 100)
        );

        OperationResult result = cashDAO.saveBulkCashNotes(notes);

        //assertThat(result.isSuccess()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("NO_ERROR");
    }

    @Test
    void getCashNote_ShouldReturnNote_WhenNoteExists() {
        cashDAO.saveCashNote("USD", 10, 100);

        OperationResult result = cashDAO.getCashNote("USD", 10);

        //assertThat(result.isSuccess()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("NO_ERROR");
        assertThat(result.getData()).hasSize(1);
        CurrencyNoteQuantity note = result.getData().get(0);
        assertThat(note.getCurrency()).isEqualTo("USD");
        assertThat(note.getValue()).isEqualTo(10);
        assertThat(note.getQuantity()).isEqualTo(100);
    }

    @Test
    void getCashNote_ShouldReturnError_WhenNoteDoesNotExist() {
        OperationResult result = cashDAO.getCashNote("EUR", 20);

        //assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrorMessage()).isEqualTo("Currency note not found");
        assertThat(result.getData()).isEmpty();
    }

    @Test
    void getCashNotesByCurrency_ShouldReturnNotes_WhenCurrencyExists() {
        cashDAO.saveCashNote("USD", 10, 100);

        OperationResult result = cashDAO.getCashNotesByCurrency("USD");

        //assertThat(result.isSuccess()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("NO_ERROR");
        assertThat(result.getData()).hasSize(1);
        CurrencyNoteQuantity note = result.getData().get(0);
        assertThat(note.getCurrency()).isEqualTo("USD");
        assertThat(note.getValue()).isEqualTo(10);
        assertThat(note.getQuantity()).isEqualTo(100);
    }

    @Test
    void getCashNotesByCurrency_ShouldReturnError_WhenCurrencyDoesNotExist() {
        OperationResult result = cashDAO.getCashNotesByCurrency("EUR");

        //assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrorMessage()).isEqualTo("Currency note not found");
        assertThat(result.getData()).isEmpty();
    }

    @Test
    void getAllCashNotes_ShouldReturnAllNotes() {

        cashDAO.saveCashNote("USD", 10, 100);
        cashDAO.saveCashNote("USD", 5, 50);
        cashDAO.saveCashNote("EUR", 20, 200);

        OperationResult result = cashDAO.getAllCashNotes();

        //assertThat(result.isSuccess()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("NO_ERROR");
        assertThat(result.getData()).hasSize(3);
    }
}