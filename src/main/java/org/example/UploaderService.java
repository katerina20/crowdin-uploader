package org.example;

import org.example.utils.FileMatcher;

public class UploaderService {
    public void upload(CrowdinParams params) {
        String[] match = FileMatcher.match(params.getWildcard());
    }
}
