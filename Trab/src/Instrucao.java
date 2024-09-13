public class Instrucao {
    private String operation;
    private String reg1;
    private String reg2;
    private String reg3;

    public Instrucao(String operation, String reg1, String reg3, String reg2) {
        this.operation = operation;
        this.reg1 = reg1;
        this.reg2 = reg2;
        this.reg3 = reg3;
    }

    public String getOperation() {
        return operation;
    }

    public String getReg1() {
        return reg1;
    }

    public String getReg2() {
        return reg2;
    }

    public String getReg3() {
        return reg3;
    }

    public String getFormattedValues() {
        return String.format("%s %s %s %s", operation, reg1, reg2, reg3);
    }
}