package impl;

import spec.IOProvider;

import java.io.*;
import java.util.*;

/**
 * Created by mmker on 08-Jan-17.
 */
public final class MockIOProviderImpl implements IOProvider {

    // key = path, value = contents
    private final Map<String, String> mockReaderFiles = new HashMap<>();

    // key = path, value = writer
    private final List<Map.Entry<String, TestStringWriter>> mockWriters = new ArrayList<>();

    @Override
    public Reader openReader(String path) throws FileNotFoundException {
        String mockContents = mockReaderFiles.get(path);
        if (mockContents == null) {
            // this mock file was not registered, then simulate that the file
            // was not found
            throw new FileNotFoundException();
        } else {
            return new StringReader(mockContents);
        }
    }

    public List<Map.Entry<String, TestStringWriter>> getMockWriters() {
        return mockWriters;
    }

    @Override
    public Writer openWriter(String path) throws IOException {
        TestStringWriter newWriter = new TestStringWriter();
        getMockWriters().add(new AbstractMap.SimpleEntry<>(path, newWriter));
        return newWriter;
    }

    public static final class TestStringWriter extends StringWriter {
        private boolean closeCalled = false;

        @Override
        public void close() throws IOException {
            this.closeCalled = true;
            super.close();
        }

        public boolean wasCloseCalled() {
            return closeCalled;
        }
    }
}
