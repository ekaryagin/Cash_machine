package com.kumpus.atm.utils;

import com.kumpus.atm.exception.InvalidCommandException;
import com.kumpus.atm.model.CommandValuesDeposit;
import com.kumpus.atm.model.CommandValuesWithdrawal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandValidator {

    private static final String PATTERN_ADD_COMMAND = "^\\+\\s[A-Z]{3}\\s\\d{1,4}\\s\\d+$";
    private static final String PATTERN_GET_CASH_COMMAND = "^-\\s[A-Z]{3}\\s\\d+$";
    private static final Set<Integer> VALID_VALUES =
            new HashSet<>(Arrays.asList(1, 5, 10, 50, 100, 500, 1000, 5000));

    public static CommandValuesDeposit validateDepositCommand(String command) throws InvalidCommandException {
        if (Pattern.matches(PATTERN_ADD_COMMAND, command)) {
            try {
                String[] parts = command.split("\\s+");
                String currency = parts[1];
                int value = Integer.parseInt(parts[2]);
                int quantity = Integer.parseInt(parts[3]);
                if (VALID_VALUES.contains(value)) {
                    return new CommandValuesDeposit(currency, value, quantity);
                }
            } catch (NumberFormatException ex) {
                throw new InvalidCommandException("Invalid number format in deposit command", ex);
            }
        }
        throw new InvalidCommandException("Invalid deposit command");
    }

    public static CommandValuesWithdrawal validateWithdrawalCommand(String command) throws InvalidCommandException {
        if (Pattern.matches(PATTERN_GET_CASH_COMMAND, command)) {
            try {
                String[] parts = command.split("\\s+");
                String currency = parts[1];
                int amount = Integer.parseInt(parts[2]);
                return new CommandValuesWithdrawal(currency, amount);
            } catch (NumberFormatException ex) {
                throw new InvalidCommandException("Invalid number format in deposit command", ex);
            }
        }
        throw new InvalidCommandException("Invalid deposit command");
    }

    public static boolean validateCheckBalanceCommand(String command) {
        return command.equals("?");
    }
}
