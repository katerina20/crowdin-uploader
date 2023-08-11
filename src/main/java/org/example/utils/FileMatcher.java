package org.example.utils;

import java.io.File;

import static java.util.Objects.nonNull;

public class FileMatcher {
    public static String[] match(String wildcard) {
        File directory = new File(".");
        String regex = wildcardToRegex(wildcard);
        String[] files = directory.list((dir, name) -> name.matches(regex));
        return nonNull(files) ? files : new String[0];
    }

    private static String wildcardToRegex(String wildcard) {
        return wildcard.replace(".", "\\.")
                       .replace("*", ".+");
    }
}
