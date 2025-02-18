package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
// class for compress and decompress files using huffman coding
public class Huffman {
	private WriteBuffer writeBuffer;
	private WriteBuffer headerWriter;
	private ReadBuffer readBuffer;
	private int[] freq;
	private long headerSize;
	private String[] codes;
	private String fileType;
	private StringBuilder header;
	private long orginalSize;
	private long compressedSize;
	
	
	public void compress(File inputFile) throws IOException {
		// Read input
			header = new StringBuilder();
			headerWriter = new WriteBuffer("header.huf");
			orginalSize = inputFile.length();
			String fileName = inputFile.getName();

			// Find the last dot in the file name
			int dotIndex = fileName.lastIndexOf('.');
            String parentDirectory = inputFile.getParent();
			
			// extract the file name without extension
            String newFileName = parentDirectory + "\\" + fileName.substring(0,
					dotIndex > 0 && dotIndex < fileName.length() - 1 ? dotIndex : fileName.length()) + ".huf";
			setWriteBuffer(new WriteBuffer(newFileName));
			// used to read the file to be compressed
			FileInputStream fileInputStream = new FileInputStream(inputFile);
			// Create a buffer to hold 8 bytes
			byte[] buffer = new byte[8];
			int bytesRead = 0;
			long totalBytes = 0; // total bytes in the file
			freq = new int[256]; // frequency array
			// Read the file 8 bytes at a time
			while ((bytesRead = fileInputStream.read(buffer)) != -1) {
				totalBytes += bytesRead;
				// calculate frequency
				for (int i = 0; i < bytesRead; i++)
					freq[buffer[i] & 0xFF]++;
			}

			// Build Huffman code tree
			Node root = buildTree(freq);
			// Build code table
			codes = new String[256];
			buildCode(codes, root, "");
			
			String fileEx ="";
			// write file extension
			if (dotIndex > 0 && dotIndex < fileName.length()) {
				fileEx = fileName.substring(dotIndex);
				fileType = fileEx;
			}
			// write file extension length
			writeBuffer.writeInt(fileEx.length());
			headerWriter.writeInt(fileEx.length());
			for (int i = 0; i < fileEx.length(); i++) {
				writeBuffer.writeChar(fileEx.charAt(i));
				headerWriter.writeChar(fileEx.charAt(i));
			}

			// write the tree on the compressed file
			writeTree(root);

			// write number of chars
			writeBuffer.writeLong(totalBytes);
			headerWriter.writeLong(totalBytes);
			
			// Use Huffman code to encode input.
			fileInputStream.close();
			fileInputStream = new FileInputStream(inputFile);
			while ((bytesRead = fileInputStream.read(buffer)) != -1) {
				for (int i = 0; i < bytesRead; i++) {
					// get the code for the current byte
					String code = codes[buffer[i] & 0xFF];
					// write the code into the file
					for (int j = 0; j < code.length(); j++)
						if (code.charAt(j) == '1') {
							writeBuffer.writeBit(true);
						} else {
							writeBuffer.writeBit(false);
						}
				}
			}
			writeBuffer.close();
			headerWriter.close();
			fileInputStream.close();
			fileInputStream = new FileInputStream("header.huf");
			headerSize = new File("header.huf").length();
			byte[] headerBytes = fileInputStream.readAllBytes();
			for (int i = 0; i < headerBytes.length; i++) {
				header.append((char)headerBytes[i]);
			}

			compressedSize = new File(newFileName).length();
			fileInputStream.close();
	}

	 // method to build the code table from tree
	private void buildCode(String[] st, Node x, String s) {
		if (x.isLeaf()) {
			st[x.ch] = s;
			return;
		}
		buildCode(st, x.left, s + '0'); // append 0 to the code if go left
		buildCode(st, x.right, s + '1'); // append 1 to the code if go right
	}

	// method to build the huffman tree
	private Node buildTree(int[] freq) {
		// Initialize min heap with initial capacity 1
		MinHeap<Node> pq = new MinHeap<>();
		// fill the heap with each character node
		for (char c = 0; c < 256; c++) {
			if (freq[c] > 0)
				pq.insert(new Node(c, freq[c], null, null));
		}
		
		// Merge two smallest trees.
		while (pq.size() > 1) {
			Node left = pq.delMin();
			Node right = pq.delMin();
			Node parent = new Node('\0', left.freq + right.freq, left, right);
			pq.insert(parent);
		}
		return pq.delMin();
	}

	// method to write the tree into the file using preorder traversal
	private void writeTree(Node root) throws IOException {
		// write a single 1 bit when visits an leaf node
		if (root.isLeaf()) {
			writeBuffer.writeBit(true);
			writeBuffer.writeChar(root.ch);
			headerWriter.writeBit(true);
			headerWriter.writeChar(root.ch);
			return;
		}
		// write a single 0 bit when visits an internal node
		writeBuffer.writeBit(false);
		headerWriter.writeBit(false);
		writeTree(root.left);
		writeTree(root.right);
	}

	// method to read the tree from the file and build it
	private Node readTree() {
		// if read 1 means leaf node
		if (readBuffer.readBit())
			return new Node(readBuffer.readChar(), 0, null, null);
		return new Node('\0', 0, readTree(), readTree());
	}

	// method to decompress files
	public void decompress(File file) throws IOException {
		// initialize read buffer
		setReadBuffer(new ReadBuffer(file));
		// read file extension length
		int exLength = readBuffer.readInt();
		String fileEx = "";
		// read file extension
		for (int i = 0; i < exLength; i++) {
			fileEx += readBuffer.readChar();
		}
		// initialize write buffer
		setWriteBuffer(new WriteBuffer(file.getParent() + "\\" + file.getName().replace(".huf", "decompressed" + fileEx)));
		// read the huffman tree
		Node root = readTree();
		// read total bytes in the original file
		long totalBytes = readBuffer.readLong();
		/*
		 * read the encoded file, starting at the root, go down the tree 
		 * move left if it is 0, and move right if it is 1.
		 * when arrive a leaf, write the character at that node to the decompresed file and
		 * restart at the root
		 */
		for (long i = 0; i < totalBytes; i++) {
			Node x = root;
			while (!x.isLeaf()) {
				if (readBuffer.readBit())
					x = x.right;
				else
					x = x.left;
			}	
			writeBuffer.writeChar(x.ch);
		}
		readBuffer.close();
		writeBuffer.close();
	}
	

	public WriteBuffer getWriteBuffer() {
		return writeBuffer;
	}

	public void setWriteBuffer(WriteBuffer writeBuffer) {
		this.writeBuffer = writeBuffer;
	}

	public ReadBuffer getReadBuffer() {
		return readBuffer;
	}

	public void setReadBuffer(ReadBuffer readBuffer) {
		this.readBuffer = readBuffer;
	}

	public int[] getFrequencyArray() {
		return freq;
	}
	
    public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public ObservableList<Frequency> getFrequencyData() {
        ObservableList<Frequency> frequencies = FXCollections.observableArrayList();
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] > 0) {
                frequencies.add(new Frequency((char) i, freq[i], codes[i], codes[i].length()));
            }
        }
        FXCollections.sort(frequencies, Comparator.reverseOrder());
        return frequencies;
    }
	
	public StringBuilder getHeader() {
		return header;
	}

	public long getOrginalSize() {
		return orginalSize;
	}

	public long getCompressedSize() {
		return compressedSize;
	}
	
	public long getHeaderSize() {
		return headerSize;
	}

	public void setHeaderSize(long headerSize) {
		this.headerSize = headerSize;
	}



	// Huffman tree node
	private static class Node implements Comparable<Node> {
		private char ch; 
		private int freq; // char frequency
		private final Node left, right;

		Node(char ch, int freq, Node left, Node right) {
			this.ch = ch;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}

		public boolean isLeaf() {
			return left == null && right == null;
		}

		public int compareTo(Node that) {
			return this.freq - that.freq;
		}
	}
}
