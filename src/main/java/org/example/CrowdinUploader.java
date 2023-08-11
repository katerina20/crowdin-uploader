package org.example;

import static org.example.utils.ParamsParser.parseArgs;

public class CrowdinUploader {
    public static void main(String[] args) {
        CrowdinParams params = parseArgs(args);
        UploaderService service = new UploaderService(params.getToken(), params.getProjectId());
        service.upload(params.getWildcard());
    }
}