import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> table = new HashMap<>();
        for (char c: inputSymbols) {
            if (table.get(c) == null) {
                table.put(c, 1);
            }
            table.put(c, table.get(c) + 1);
        }
        return table;
    }
    public static void main(String[] args) {
        char[] in = FileUtils.readFile(args[0]);
        ObjectWriter writer = new ObjectWriter(args[0] + ".huf");
        Map<Character, Integer> frequencyTable = buildFrequencyTable(in);
        BinaryTrie trie = new BinaryTrie(frequencyTable);
        writer.writeObject(trie);
        Map<Character, BitSequence> lookupTable = trie.buildLookupTable();
        List<BitSequence> out = new LinkedList<>();
        for (char c: in) {
            out.add(lookupTable.get(c));
        }
        writer.writeObject(BitSequence.assemble(out));
    }
}
