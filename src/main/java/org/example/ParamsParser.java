package org.example;

public class ParamsParser {
    public static CrowdinParams parseArgs(String[] args) {
        return new CrowdinParams(args[0], args[1], args[2]);
    }
}
