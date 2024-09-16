import java.util.HashSet;
import java.util.Set;

public class MipsInstrucao {
    private Set<String> operaDestino;
    private Set<String> operaDado;
    private Set<String> operaMemoria;

    public MipsInstrucao(){
        operaDestino = new HashSet<>();
        operaDado = new HashSet<>();
        operaMemoria = new HashSet<>();

        initoperaDestino();
        initoperaDado();
        initoperaMemoria();
    }

    private void initoperaDestino(){
        String[] ops = {
            "lb", "lh", "lwl", "lw", "lbu", "lhu", "lwr", "add", "addu", "sub", "subu",
            "and", "or", "xor", "nor", "slt", "sltu", "addi", "addiu", "slti", "sltiu",
            "andi", "ori", "xori", "lui", "sll", "srl", "sra", "sllv", "srlv", "srav",
            "mfhi", "mflo", "jalr"
        };
        for(String op : ops){
            operaDestino.add(op);
        }
    }

    private void initoperaDado(){
        String[] ops = {
            "sw", "sb", "sh", "swl", "swr", "mthi", "mtlo", "mult", "multu", "div", "divu",
            "jr", "bltz", "bgez", "bltzal", "bgezal", "j", "jal"
        };
        for(String op : ops){
            operaDado.add(op);
        }
    }

    private void initoperaMemoria(){
        String[] ops = {
            "lb", "lh", "lwl", "lw", "lbu", "lhu", "lwr", "sb", "sh", "swl", "sw", "swr",
            "j", "jal", "jr", "jalr", "sll", "srl", "sra", "sllv", "srlv", "srav"
        };
        for(String op : ops){
            operaMemoria.add(op);
        }
    }

    public boolean deDestino(String operation){
        return operaDestino.contains(operation);
    }

    public boolean deMemoria(String operation){
        return operaMemoria.contains(operation);
    }
}