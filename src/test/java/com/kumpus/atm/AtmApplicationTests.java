package com.kumpus.atm;

import com.kumpus.atm.model.OperationResult;
import com.kumpus.atm.service.ATM;
import com.kumpus.atm.service.CashService;
import com.kumpus.atm.service.IOService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@SpringBootTest
class AtmApplicationTests {

    @Autowired
    private ATM atm;

    @MockBean
    private CashService cashService;

    @MockBean
    private IOService ioService;

    @Test
    void testRun() {
        when(ioService.inputText()).thenReturn("exit");
        when(cashService.executeCommand("exit")).thenReturn(OperationResult.success());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        doAnswer(invocation ->  {
                String arg = invocation.getArgument(0, String.class);
                outputStream.write(arg.getBytes());
                return null;
        }).when(ioService).outputText(anyString());

        atm.run();

        assertEquals("Your command:", outputStream.toString().trim());
    }
}