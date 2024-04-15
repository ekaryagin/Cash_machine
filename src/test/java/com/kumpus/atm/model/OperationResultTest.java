package com.kumpus.atm.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OperationResultTest {

    @Test
    void success_ShouldCreateOperationResultWithSuccessAndNoData() {
        OperationResult result = OperationResult.success();

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getData()).isEmpty();
        assertThat(result.getErrorMessage()).isEqualTo("NO_ERROR");
    }

    @Test
    void successWithData_ShouldCreateOperationResultWithSuccessAndData() {
        List<CurrencyNoteQuantity> testData = new ArrayList<>();
        testData.add(new CurrencyNoteQuantity("USD", 10, 100));

        OperationResult result = OperationResult.successWithData(testData);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getData()).isEqualTo(testData);
        assertThat(result.getErrorMessage()).isEqualTo("NO_ERROR");
    }

    @Test
    void error_ShouldCreateOperationResultWithErrorAndNoData() {
        OperationResult result = OperationResult.error("ERROR_MESSAGE");

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getData()).isEmpty();
        assertThat(result.getErrorMessage()).isEqualTo("ERROR_MESSAGE");
    }
}
