public class HuffmanDecoder {
    static class Buffer {
        private final int SIZE = 256;
        private char[] buffer = new char[SIZE];
        private int index = 0;
        private String fileName;

        Buffer(String fileName) {
            this.fileName = fileName;
        }

        void flush() {
            if (index > 0 && index >= SIZE) {
                FileUtils.writeCharArray(fileName, buffer);
            } else {
                char[] smallBuffer = new char[index];
                for (int i = 0; i < smallBuffer.length; i++) {
                    smallBuffer[i] = buffer[i];
                }
                FileUtils.writeCharArray(fileName, buffer);
            }
            index = 0;
        }

        void add(char c) {
            if (index >= SIZE) {
                flush();
            }
            buffer[index++] = c;
        }
    }
    public static void main(String[] args) {
        ObjectReader reader = new ObjectReader(args[0]);
        BinaryTrie trie = (BinaryTrie) reader.readObject();
        BitSequence in = (BitSequence) reader.readObject();
        int length = in.length();
        Buffer buffer = new Buffer(args[1]);
        while (length > 0) {
            Match match = trie.longestPrefixMatch(in);
            buffer.add(match.getSymbol());
            length -= match.getSequence().length();
        }
        buffer.flush();
    }
}
