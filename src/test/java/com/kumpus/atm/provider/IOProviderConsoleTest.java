package com.kumpus.atm.provider;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IOProviderConsoleTest {
    @DisplayName("correctly created by the constructor")
    @Test
    void shouldHaveCorrectConstructor() {
        IOProviderConsole ioProvider = new IOProviderConsole();
        assertAll("IOProviderConsole",
                () -> assertEquals(System.in, ioProvider.getIn()),
                () -> assertEquals(System.out, ioProvider.getOut())
        );
    }
}