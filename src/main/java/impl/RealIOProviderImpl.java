package impl;

import spec.IOProvider;

import java.io.*;

/**
 * Created by mmker on 08-Jan-17.
 */
public final class RealIOProviderImpl implements IOProvider {
    @Override
    public Reader openReader(String path) throws FileNotFoundException{
        return new FileReader(path);
    }

    @Override
    public Writer openWriter(String path) throws IOException {
        return new FileWriter(path);
    }
}
