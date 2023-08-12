package org.example.utils;

import org.example.CrowdinParams;
import org.example.CrowdinUploaderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParamsParserTest {

    @Test
    void testParseArgs_success() {
        String[] args = {"12345", "1q2w3e", "*.txt"};
        CrowdinParams params = ParamsParser.parseArgs(args);
        assertEquals(12345L, params.getProjectId());
        assertEquals("1q2w3e", params.getToken());
        assertEquals("*.txt", params.getWildcard());
    }

    @Test
    void testParseArgs_invalidProjectId() {
        String[] args = {"invalidId", "1q2w3e", "*.txt"};
        assertThrows(CrowdinUploaderException.class, () -> ParamsParser.parseArgs(args));
    }

    @Test
    void testParseArgs_notEnoughArgs() {
        String[] args = {"12345", "1q2w3e"};
        assertThrows(CrowdinUploaderException.class, () -> ParamsParser.parseArgs(args));
    }

    @Test
    void testParseArgs_tooManyArgs() {
        String[] args = {"12345", "1q2w3e", "*.txt", "extraArg"};
        CrowdinParams params = ParamsParser.parseArgs(args);
        assertEquals(12345L, params.getProjectId());
        assertEquals("1q2w3e", params.getToken());
        assertEquals("*.txt", params.getWildcard());
    }
}