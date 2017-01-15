package mmkeri;

import java.io.*;
import java.util.*;

/**
 * Created by mmker on 08-Jan-17.
 */
public class MockIOProviderImpl implements IOProvider {

    // key = path, value = contents
    public final Map<String, String> mockReaderFiles = new HashMap<>();

    // key = path, value = writer
    public final List<Map.Entry<String, TestStringWriter>> mockWriters = new ArrayList<>();

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

    public static class TestStringWriter extends StringWriter {
        public boolean wasCloseCalled = false;

        @Override
        public void close() throws IOException {
            this.wasCloseCalled = true;
            super.close();
        }
    }

    @Override
    public Writer openWriter(String path) throws IOException {
        TestStringWriter newWriter = new TestStringWriter();
        mockWriters.add(new AbstractMap.SimpleEntry<>(path, newWriter));
        return newWriter;
    }
}
