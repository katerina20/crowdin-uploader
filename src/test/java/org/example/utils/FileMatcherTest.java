package org.example.utils;

import org.example.CrowdinUploaderException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileMatcherTest {
    private File tempFile1;
    private File tempFile2;

    @BeforeEach
    void setUp() throws IOException {
        tempFile1 = new File("test1234.txt");
        tempFile2 = new File("demo.xml");
        if (!tempFile1.createNewFile() || !tempFile2.createNewFile()) {
            throw new IOException("Could not create test files");
        }
    }

    @AfterEach
    void tearDown() {
        if (!tempFile1.delete()) System.err.println("Failed to delete " + tempFile1.getName());
        if (!tempFile2.delete()) System.err.println("Failed to delete " + tempFile2.getName());
    }

    @Test
    void testMatch_successTxt() {
        String[] files = FileMatcher.match("test1234.txt");
        assertTrue(files.length > 0);
        assertTrue(contains(files, tempFile1.getName()));
    }

    @Test
    void testMatch_successWildcard() {
        String[] files = FileMatcher.match("*.txt");
        assertTrue(files.length > 0);
        assertTrue(contains(files, tempFile1.getName()));
    }

    @Test
    void testMatch_noMatch() {
        assertThrows(CrowdinUploaderException.class, () -> FileMatcher.match("doesnotexist*.txt"));
    }

    @Test
    void testMatch_invalidWildcard() {
        assertThrows(CrowdinUploaderException.class, () -> FileMatcher.match("invalid*"));
    }

    private boolean contains(String[] array, String value) {
        return Arrays.asList(array).contains(value);
    }
}