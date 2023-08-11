package org.example;

import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import static org.example.utils.ParamsParser.parseArgs;

@Log4j2
public class CrowdinUploader {
    public static void main(String[] args) {
        log.info("Start...");
        Try.run(() -> {
            CrowdinParams params = parseArgs(args);
            UploaderService service = new UploaderService(params.getToken(), params.getProjectId());
            service.upload(params.getWildcard());
        }).onFailure(CrowdinUploaderException.class, (e) -> log.error(e.getMessage()));
        log.info("Finish!");
    }
}