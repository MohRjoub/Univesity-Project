package application;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
// class to write 8 byte bit in each go to the file
public final class WriteBuffer {
    private FileOutputStream out;     // Output stream
    private byte[] buffer;            // 8-byte buffer
    private int currentByte;            // Index of the next byte to write
    private int curretnBit;             // Bit position in the current byte

    public WriteBuffer(String fileName) throws FileNotFoundException {
        out = new FileOutputStream(fileName);
        buffer = new byte[8];
        currentByte = 0;
        curretnBit = 0;
    }

    //method to writes a single bit to the buffer.
	public void writeBit(boolean bit) throws IOException {
		// if bit is 1 set it else still as the default 0
		if (bit) {
			// Signed Left Shift 1 with the bit index we want to set, start fill from most significant bit
			buffer[currentByte] |= (1 << (7 - curretnBit)); // Set the bit in the current byte
		}
		curretnBit++;

		// Move to the next byte if the current byte is full
		if (curretnBit == 8) {
			curretnBit = 0;
			currentByte++;

			// If the buffer is full, write it to the file
			if (currentByte == 8) {
				flushBuffer();
			}
		}
	}


    //method to writes a single byte to the buffer.
    private void writeByte(int x) throws IOException {
        for (int i = 0; i < 8; i++) { // read least 8 bit from the integer that represent one byte
        	// right shift to extract the bits
            writeBit(((x >> (7 - i)) & 1) == 1);// start write from most bit
        }
    }

    //Flushes the buffer to the file, writing all bytes.
    private void flushBuffer() throws IOException {
            out.write(buffer, 0, currentByte + (curretnBit > 0 ? 1 : 0));
        resetBuffer();
    }

    //Resets the buffer
    private void resetBuffer() {
        buffer = new byte[8];
        currentByte = 0;
        curretnBit = 0;
    }

    //closes the output stream.
    public void close() throws IOException {
        flushBuffer();
        out.close();
    }

    //Writes a 32-bit integer
    public void writeInt(int x) throws IOException {
    	writeByte((x >>> 24) & 0xff); // write the most byte
    	writeByte((x >>> 16) & 0xff);
    	writeByte((x >>> 8) & 0xff);
    	writeByte((x >>> 0) & 0xff); // write the least byte
    }
  //Writes a 64-bit long
    public void writeLong(long x) throws IOException {
        writeByte((int) ((x >>> 56) & 0xff));
        writeByte((int) ((x >>> 48) & 0xff));
        writeByte((int) ((x >>> 40) & 0xff));
        writeByte((int) ((x >>> 32) & 0xff));
        writeByte((int) ((x >>> 24) & 0xff));
        writeByte((int) ((x >>> 16) & 0xff));
        writeByte((int) ((x >>> 8) & 0xff));
        writeByte((int) ((x >>> 0) & 0xff));
    }
  //Writes 8 bit char
    public void writeChar(char x) throws IOException {
        writeByte(x & 0xff); // ensure that only the least 8 sig bit set
    }
}
