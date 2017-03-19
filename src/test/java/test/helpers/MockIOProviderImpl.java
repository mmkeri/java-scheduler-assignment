package test.helpers;

import spec.IOProvider;

import java.io.*;
import java.util.*;

/**
 * Created by mmker on 08-Jan-17.
 */

/**
 * A mock IO provider created for the purposes of testing
 */
public final class MockIOProviderImpl implements IOProvider {

    /**
     * A HashMap composed of file paths in the form of a String and the file
     * contents also as a String
     */
    private final Map<String, String> mockReaderFiles = new HashMap<>();

    /**
     * An ArrayList composed of a sequence of Map objects which are themselves composed of
     * a String representing the file path to be written to and a TestStringWriter object
     */
    private final List<Map.Entry<String, TestStringWriter>> mockWriters = new ArrayList<>();

    /**
     * Returns a StringReader object that contains mockContents of the file
     * @param path file name as a String
     * @return A StringReader with mock contents attached
     * @throws FileNotFoundException
     */
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

    /**
     * @return an instance of the mockWriters list
     */
    public List<Map.Entry<String, TestStringWriter>> getMockWriters() {
        return mockWriters;
    }

    /**
     * Creates a mock writer object for the purposes of testing.
     * @param path file path to where the file will be stored in the form of a String
     * @return a new TestStringWriter object
     * @throws IOException
     */
    @Override
    public Writer openWriter(String path) throws IOException {
        TestStringWriter newWriter = new TestStringWriter();
        getMockWriters().add(new AbstractMap.SimpleEntry<>(path, newWriter));
        return newWriter;
    }

    /**
     * A mock StringWriter object that has only one function, to signal whether the file
     * was closed or not in the form of a boolean value
     */
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
