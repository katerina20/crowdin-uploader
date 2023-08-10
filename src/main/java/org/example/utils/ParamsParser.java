package org.example.utils;

import org.example.CrowdinParams;

public class ParamsParser {
    public static CrowdinParams parseArgs(String[] args) {
        return new CrowdinParams(args[0], args[1], args[2]);
    }
}
