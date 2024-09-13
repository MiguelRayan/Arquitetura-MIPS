public class Instrucao {
    private String op;
    private String r1;
    private String r2;
    private String r3;

    public Instrucao(String op, String r1, String r3, String r2) {
        this.op = op;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
    }

    public String getop() {
        return op;
    }

    public String getr1() {
        return r1;
    }

    public String getr2() {
        return r2;
    }

    public String getr3() {
        return r3;
    }

    public String getFormattedValues() {
        return String.format("%s %s %s %s", op, r1, r2, r3);
    }
}