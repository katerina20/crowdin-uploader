package org.example.utils;

import org.example.CrowdinParams;

public class ParamsParser {
    public static CrowdinParams parseArgs(String[] args) {
        return new CrowdinParams(Long.parseLong(args[0]), args[1], args[2]);
    }
}
