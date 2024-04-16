package com.kumpus.atm.utils;

import com.kumpus.atm.exception.InvalidCommandException;
import com.kumpus.atm.model.CommandValuesDeposit;
import com.kumpus.atm.model.CommandValuesWithdrawal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class CommandValidatorTest {

    @Test
    void validateDepositCommand_ValidCommand_ReturnsDepositCommandValues() {
        String command = "+ USD 10 5";
        CommandValuesDeposit depositCommandValues = null;
        try {
            depositCommandValues = CommandValidator.validateDepositCommand(command);
        } catch (InvalidCommandException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertNotNull(depositCommandValues);
        assertEquals("USD", depositCommandValues.getCurrency());
        assertEquals(10, depositCommandValues.getValue());
        assertEquals(5, depositCommandValues.getQuantity());
    }

    @Test
    void validateDepositCommand_InvalidCurrency_ThrowsInvalidCommandException() {
        String command = "+ ABC 100 " + Long.MAX_VALUE;
        assertThrows(InvalidCommandException.class, () -> CommandValidator.validateDepositCommand(command));
    }

    @Test
    void validateDepositCommand_InvalidValue_ThrowsInvalidCommandException() {
        String command = "+ USD 10000 5";
        assertThrows(InvalidCommandException.class, () -> CommandValidator.validateDepositCommand(command));
    }

    @Test
    void validateDepositCommand_InvalidFormat_ThrowsInvalidCommandException() {
        String command = "+ USD 10";
        assertThrows(InvalidCommandException.class, () -> CommandValidator.validateDepositCommand(command));
    }

    @Test
    void validateWithdrawalCommand_ValidCommand_ReturnsWithdrawalCommandValues() {
        String command = "- USD 50";
        CommandValuesWithdrawal commandValuesWithdrawal = null;
        try {
            commandValuesWithdrawal = CommandValidator.validateWithdrawalCommand(command);
        } catch (InvalidCommandException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertNotNull(commandValuesWithdrawal);
        assertEquals("USD", commandValuesWithdrawal.getCurrency());
        assertEquals(50, commandValuesWithdrawal.getAmount());
    }

    @Test
    void validateWithdrawalCommand_InvalidCurrency_ThrowsInvalidCommandException() {
        String command = "- ABC " + Long.MAX_VALUE;
        assertThrows(InvalidCommandException.class, () -> CommandValidator.validateWithdrawalCommand(command));
    }

    @Test
    void validateWithdrawalCommand_InvalidFormat_ThrowsInvalidCommandException() {
        String command = "- USD";
        assertThrows(InvalidCommandException.class, () -> CommandValidator.validateWithdrawalCommand(command));
    }

    @Test
    void validateCheckBalanceCommand_ValidCommand_ReturnsTrue() {
        String command = "?";
        assertTrue(CommandValidator.validateCheckBalanceCommand(command));
    }

    @Test
    void validateCheckBalanceCommand_InvalidCommand_ReturnsFalse() {
        String command = "? ";
        assertFalse(CommandValidator.validateCheckBalanceCommand(command));
    }
}