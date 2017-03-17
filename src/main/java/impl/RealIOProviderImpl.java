package impl;

import spec.IOProvider;

import java.io.*;

/**
 * Created by mmker on 08-Jan-17.
 */

/**
 * An instantiation of the abstract class IOProvider.
 */
public final class RealIOProviderImpl implements IOProvider {
    /**
     * Creates a new FileReader, given the name of the file to read from
     * @param path the name of the file to read from
     * @return An instance of a FileReader
     * @throws FileNotFoundException
     */
    @Override
    public Reader openReader(String path) throws FileNotFoundException{
        return new FileReader(path);
    }

    /**
     * Creates a new FileWriter object given a file name
     * @param path the name of the file to write to
     * @return An instance of a FileWriter
     * @throws IOException
     */
    @Override
    public Writer openWriter(String path) throws IOException {
        return new FileWriter(path);
    }
}
