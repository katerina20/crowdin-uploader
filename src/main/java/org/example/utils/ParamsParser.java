package org.example.utils;

import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.example.CrowdinParams;
import org.example.CrowdinUploaderException;

@Log4j2
public class ParamsParser {
    public static CrowdinParams parseArgs(String[] args) {
        if (args.length < 3)
            throw new CrowdinUploaderException(
                    "Not enough arguments. Please specify the project ID, PAT, and wildcard pattern.");
        long projectId = Try.of(() -> Long.parseLong(args[0]))
                            .getOrElseThrow(() -> new CrowdinUploaderException("Invalid project name."));
        return new CrowdinParams(projectId, args[1], args[2]);
    }
}
