import java.io.*;

public class TeeOutputStream extends OutputStream {
    private final OutputStream first;
    private final OutputStream second;

    public TeeOutputStream(OutputStream first, OutputStream second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public void write(int b) throws IOException {
        first.write(b);
        second.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        first.write(b, off, len);
        second.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        first.flush();
        second.flush();
    }

    @Override
    public void close() throws IOException {
        flush();
        second.close();
    }
}
