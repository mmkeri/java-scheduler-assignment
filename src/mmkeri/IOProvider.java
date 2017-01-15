package mmkeri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Created by mmker on 08-Jan-17.
 */
public interface IOProvider {
    Reader openReader(String path) throws FileNotFoundException;
    Writer openWriter(String path) throws IOException;
}
