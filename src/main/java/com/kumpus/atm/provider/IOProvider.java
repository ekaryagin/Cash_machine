package com.kumpus.atm.provider;

import java.io.InputStream;
import java.io.PrintStream;

public interface IOProvider {

    InputStream getIn();

    PrintStream getOut();
}
