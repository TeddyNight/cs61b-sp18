public class HuffmanDecoder {
    public static void main(String[] args) {
        ObjectReader reader = new ObjectReader(args[0]);
        BinaryTrie trie = (BinaryTrie) reader.readObject();
        int length = (Integer) reader.readObject();
        BitSequence in = (BitSequence) reader.readObject();
        char[] out = new char[length];
        for (int i = 0; i < length; i++) {
            Match match = trie.longestPrefixMatch(in);
            out[i] = match.getSymbol();
            in = in.allButFirstNBits(match.getSequence().length());
        }
        FileUtils.writeCharArray(args[1], out);
    }
}
