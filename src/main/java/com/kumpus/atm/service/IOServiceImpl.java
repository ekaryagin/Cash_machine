package com.kumpus.atm.service;

import com.kumpus.atm.provider.IOProvider;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class IOServiceImpl implements IOService {

    private final IOProvider ioProvider;
    private final Scanner scanner;

    public IOServiceImpl(IOProvider ioProvider) {
        this.ioProvider = ioProvider;
        this.scanner = new Scanner(ioProvider.getIn());
    }

    @Override
    public void outputTextLn(String text) {
        ioProvider.getOut().println(text);
    }

    @Override
    public void outputText(String text) {
        ioProvider.getOut().print(text);
    }

    @Override
    public String inputText() {
        return scanner.nextLine();
    }
}
