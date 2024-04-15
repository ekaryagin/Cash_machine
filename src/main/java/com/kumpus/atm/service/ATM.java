package com.kumpus.atm.service;

import org.springframework.stereotype.Service;

@Service
public class ATM {
    private final IOService ioService;
    private final CashService cashService;

    public ATM(IOService ioService, CashService cashService) {
        this.ioService = ioService;
        this.cashService = cashService;
    }

    public void run() {
        String command;
        while (true) {
            ioService.outputText("Your command: ");
            command = ioService.inputText();
            if (command.equalsIgnoreCase("exit")) {
                break;
            }
            // TODO исправить формат вывода когда происходит снятие наличных (наверное надо будет поменять логику OperationResult)
            // TODO необходимо добавить некий месседж провайдер
            // TODO вынести настройки в ямл
            // TODO вынести сообщения и шаблоны в проперти
            // TODO подумать легко ли добавлять новые команды
            // TODO подумать легко ли изменять стратегию выдачи купюр
            // TODO интеграционное тестирование
            if (command.length() > 0) {
                ioService.outputText(cashService.executeCommand(command).toString());
            }
        }
    }
}
