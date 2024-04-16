package com.kumpus.atm.service;

import com.kumpus.atm.model.OperationResult;
import com.kumpus.atm.utils.MessageProcessor;
import org.springframework.stereotype.Service;

@Service
public class ATM {
    private static final String PROMPT = "Your command: ";
    private final IOService ioService;
    private final CashService cashService;

    public ATM(IOService ioService, CashService cashService) {
        this.ioService = ioService;
        this.cashService = cashService;
    }

    public void run() {
        String command;
        while (true) {
            ioService.outputText(PROMPT);
            command = ioService.inputText();
            if (command.equalsIgnoreCase("exit")) {
                break;
            }

            if (command.length() > 0) {
                OperationResult report = cashService.executeCommand(command);
                String message = MessageProcessor.getMessage(report);
                ioService.outputTextLn(message);
            }
        }
    }
}
