package com.c3transcribe.core.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@Getter
@Setter
public class ReusableBufferedInputStream extends BufferedInputStream {
    @Value("${app.io.readbuffer.size.default: 32768}")
    private int BUFFER_SIZE ;

    private byte[] buffer = null;
    private int writeIndex = 0;
    private int readIndex = 0;
    private InputStream source = null;

    public ReusableBufferedInputStream(InputStream source) {
        super(source);
        this.setSource(source);
    }
    public ReusableBufferedInputStream setSource(InputStream source) {
        if (!source.markSupported()) {
            throw new IllegalArgumentException("marking not supported");
        }
        this.source = source;
        this.writeIndex = 0;
        this.readIndex = 0;
        return this;
    }

    @Override
    public int read() throws IOException {

        if (readIndex == writeIndex) {
            if (writeIndex == buffer.length) {
                writeIndex = 0;
                readIndex = 0;
            }
            //data should be read into buffer.
            int bytesRead = readBytesIntoBuffer();
            while (bytesRead == 0) {
                //continue until you actually get some bytes !
                bytesRead = readBytesIntoBuffer();
            }

            //if no more data could be read in, return -1;
            if (bytesRead == -1) {
                return -1;
            }
        }

        return 255 & this.buffer[readIndex++];
    }

    private int readBytesIntoBuffer() throws IOException {
        int bytesRead = this.source.read(this.buffer, this.writeIndex, this.buffer.length - this.writeIndex);
        writeIndex += bytesRead;
        return bytesRead;
    }

    @Override
    public void close() throws java.io.IOException {
        this.source.close();
    }
}
