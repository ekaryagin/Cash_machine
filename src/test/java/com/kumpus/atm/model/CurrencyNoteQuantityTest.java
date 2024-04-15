package com.kumpus.atm.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CurrencyNoteQuantityTest {
    @Test
    void createCurrencyNoteQuantity_ShouldCreateObjectWithGivenValues() {
        String currency = "USD";
        int value = 10;
        int quantity = 100;

        CurrencyNoteQuantity noteQuantity = new CurrencyNoteQuantity(currency, value, quantity);

        assertThat(noteQuantity.getCurrency()).isEqualTo(currency);
        assertThat(noteQuantity.getValue()).isEqualTo(value);
        assertThat(noteQuantity.getQuantity()).isEqualTo(quantity);
    }

    @Test
    void currencyNoteQuantity_EqualsAndHashCode_ShouldWorkProperly() {
        CurrencyNoteQuantity note1 = new CurrencyNoteQuantity("USD", 10, 100);
        CurrencyNoteQuantity note2 = new CurrencyNoteQuantity("USD", 10, 100);
        CurrencyNoteQuantity note3 = new CurrencyNoteQuantity("EUR", 10, 100);
        CurrencyNoteQuantity note4 = new CurrencyNoteQuantity("USD", 5, 100);
        CurrencyNoteQuantity note5 = new CurrencyNoteQuantity("USD", 10, 200);

        assertThat(note1).isEqualTo(note2)
                .hasSameHashCodeAs(note2)
                .isNotEqualTo(note3)
                .isNotEqualTo(note4)
                .isNotEqualTo(note5);
    }


}