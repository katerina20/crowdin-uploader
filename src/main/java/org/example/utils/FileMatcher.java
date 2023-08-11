package org.example.utils;

import org.example.CrowdinUploaderException;

import java.io.File;

import static java.util.Objects.isNull;

public class FileMatcher {
    public static String[] match(String wildcard) {
        File directory = new File(".");
        validate(wildcard);
        String regex = wildcardToRegex(wildcard);
        String[] files = directory.list((dir, name) -> name.matches(regex));
        if (isNull(files) || files.length == 0)
            throw new CrowdinUploaderException("No files were found that match the wildcard.");
        return files;
    }

    private static String wildcardToRegex(String wildcard) {
        return wildcard.replace(".", "\\.")
                       .replace("*", ".+");
    }

    private static void validate(String wildcard) {
        if (!wildcard.contains(".")) throw new CrowdinUploaderException("Invalid wildcard. Extension is must.");
    }
}
