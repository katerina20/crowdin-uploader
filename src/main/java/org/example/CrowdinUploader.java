package org.example;

import static org.example.utils.ParamsParser.parseArgs;

public class CrowdinUploader {
    public static void main(String[] args) {
        UploaderService service = new UploaderService();
        service.upload(parseArgs(args));
    }
}