package com.jminiapp.examples.ceasar;

import com.jminiapp.core.engine.JMiniAppRunner;

public class CeasarAppRunner {
    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(CeasarApp.class)
            .withState(CeasarCipherState.class)
            .withAdapters(new CeasarJSONAdapter())
            .named("CeasarCipher")
            .run(args);
    }
}
