import java.io.*;

// =========================================
// Tee Output Stream Utility
// =========================================
//
// This utility forwards compiler output
// to both console and output log files.
//
// Used for:
// - console logging
// - file logging
// - synchronized compiler output
//
// =========================================

public class TeeOutputStream extends OutputStream {

    // =========================================
    // Output Stream References
    // =========================================

    private final OutputStream first;

    private final OutputStream second;

    // =========================================
    // Constructor
    // =========================================

    public TeeOutputStream(
            OutputStream first,
            OutputStream second
    ) {

        this.first = first;
        this.second = second;
    }

    // =========================================
    // Single Byte Write
    // =========================================

    @Override
    public void write(int b)
            throws IOException {

        first.write(b);

        second.write(b);
    }

    // =========================================
    // Buffered Write
    // =========================================

    @Override
    public void write(
            byte[] b,
            int off,
            int len
    ) throws IOException {

        first.write(b, off, len);

        second.write(b, off, len);
    }

    // =========================================
    // Stream Flush Handling
    // =========================================

    @Override
    public void flush()
            throws IOException {

        first.flush();

        second.flush();
    }

    // =========================================
    // Stream Close Handling
    // =========================================

    @Override
    public void close()
            throws IOException {

        flush();

        second.close();
    }
}