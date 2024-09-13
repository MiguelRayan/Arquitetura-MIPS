import java.util.HashSet;
import java.util.Set;

public class MipsInstrucao {
    private Set<String> destinationOps;
    private Set<String> sourceOps;
    private Set<String> memoryOps;

    public MipsInstrucao() {
        destinationOps = new HashSet<>();
        sourceOps = new HashSet<>();
        memoryOps = new HashSet<>();

        initDestinationOps();
        initSourceOps();
        initMemoryOps();
    }

    private void initDestinationOps() {
        String[] ops = {
            "lb", "lh", "lwl", "lw", "lbu", "lhu", "lwr", "add", "addu", "sub", "subu",
            "and", "or", "xor", "nor", "slt", "sltu", "addi", "addiu", "slti", "sltiu",
            "andi", "ori", "xori", "lui", "sll", "srl", "sra", "sllv", "srlv", "srav",
            "mfhi", "mflo", "jalr"
        };
        for (String op : ops) {
            destinationOps.add(op);
        }
    }

    private void initSourceOps() {
        String[] ops = {
            "sw", "sb", "sh", "swl", "swr", "mthi", "mtlo", "mult", "multu", "div", "divu",
            "jr", "bltz", "bgez", "bltzal", "bgezal", "j", "jal"
        };
        for (String op : ops) {
            sourceOps.add(op);
        }
    }

    private void initMemoryOps() {
        String[] ops = {
            "lb", "lh", "lwl", "lw", "lbu", "lhu", "lwr", "sb", "sh", "swl", "sw", "swr",
            "j", "jal", "jr", "jalr", "sll", "srl", "sra", "sllv", "srlv", "srav"
        };
        for (String op : ops) {
            memoryOps.add(op);
        }
    }

    public boolean isDestination(String operation) {
        return destinationOps.contains(operation);
    }

    public boolean isMemoryOperation(String operation) {
        return memoryOps.contains(operation);
    }
}