package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
// class to read 8 byte in each go to the file
public final class ReadBuffer {

    private FileInputStream in;
    private byte[] buffer;     // Buffer to store 8 bytes
    private int bufferIndex;   // Index in the buffer
    private int bitIndex;      // Current bit position within a byte
    private int bytesRead;     // Number of bytes read into the buffer

    public ReadBuffer(File fileName) {
        try {
            in = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        buffer = new byte[8];
        bufferIndex = 0;
        bitIndex = 0;
        bytesRead = 0;
        fillBuffer();
    }

    private void fillBuffer() {
        try {
            bytesRead = in.read(buffer); // Read 8 bytes
            bufferIndex = 0;
            bitIndex = 0;
        } catch (IOException e) {
            System.out.println("EOF");
            bytesRead = -1; // end of file
        }
    }

    public void close() {
        try {
            in.close();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public boolean isEmpty() {
        return bytesRead == -1;
    }

    public boolean readBit() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

        // Extract the current bit
        boolean bit = ((buffer[bufferIndex] >> (7 - bitIndex)) & 1) == 1;
        bitIndex++;

        // Move to the next byte if the current byte is fully processed
        if (bitIndex == 8) {
            bitIndex = 0;
            bufferIndex++;
        }

        // If we've complete reading the buffer, refill it
        if (bufferIndex >= bytesRead) {
            fillBuffer();
        }

        return bit;
    }

    public char readChar() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

        int result = 0;

        // Read 8 bits from the buffer
        for (int i = 0; i < 8; i++) {
            result = (result << 1) | (readBit() ? 1 : 0);
        }

        return (char) result;
    }

    public int readInt() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

        int result = 0;

        // Read 32 bits from the buffer
        for (int i = 0; i < 32; i++) {
            result = (result << 1) | (readBit() ? 1 : 0);
        }

        return result;
    }
    
    public long readLong() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

        long result = 0;

        // Read 64 bits from the buffer
        for (int i = 0; i < 64; i++) {
            result = (result << 1) | (readBit() ? 1 : 0);
        }

        return result;
    }

    public long getFilePosition() throws IOException {
        return in.getChannel().position();
    }

	public int getBufferIndex() {
		return bufferIndex;
	}

	public void setBufferIndex(int bufferIndex) {
		this.bufferIndex = bufferIndex;
	}

	public int getBitIndex() {
		return bitIndex;
	}

	public void setBitIndex(int bitIndex) {
		this.bitIndex = bitIndex;
	}

    
}
