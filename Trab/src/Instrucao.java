public class Instrucao {
    private String op;
    private String r1;
    private String r2;
    private String r3;

    public Instrucao(String op, String r1, String r3, String r2){
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

    public boolean temDependenciaDeDados(Instrucao outra){
        // Verifica se os registradores de leitura/escrita de 'outra' causam dependência com esta instrução
        return (this.getr1() != null && (this.getr1().equals(outra.getr2()) || this.getr1().equals(outra.getr3()))) ||
               (this.getr2() != null && (this.getr2().equals(outra.getr1()) || this.getr2().equals(outra.getr3()))) ||
               (this.getr3() != null && (this.getr3().equals(outra.getr1()) || this.getr3().equals(outra.getr2())));
    }    
}