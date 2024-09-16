public class Instrucao {
    private String op;
    private String r1;
    private String r2;
    private String r3;
    private String resultado;

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

    public void setr1(String r1) {
        this.r1 = r1;
    }

    public String getr2() {
        return r2;
    }

    public void setr2(String r2) {
        this.r2 = r2;
    }

    public String getr3() {
        return r3;
    }

    public void setr3(String r3) {
        this.r3 = r3;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getFormattedValues() {
        return String.format("%s %s %s %s", op, r1, r2, r3);
    }

    public boolean podeReceberAdiantamentoDe(Instrucao anterior) {
        if (this.getr1() != null && (this.getr1().equals(anterior.getr2()) || this.getr1().equals(anterior.getr3()))) {
            return true;
        }

        if ((this.getr2() != null && this.getr2().equals(anterior.getr1())) || 
            (this.getr3() != null && this.getr3().equals(anterior.getr1()))) {
            return true;
        }
        return false;
    }

    public void receberAdiantamento(Instrucao anterior) {
        if (this.getr1() != null && this.getr1().equals(anterior.getr2())) {
            this.setr1(anterior.getResultado());
        } else if (this.getr1() != null && this.getr1().equals(anterior.getr3())) {
            this.setr1(anterior.getResultado());
        }

        if (this.getr2() != null && this.getr2().equals(anterior.getr1())) {
            this.setr2(anterior.getResultado());
        }

        if (this.getr3() != null && this.getr3().equals(anterior.getr1())) {
            this.setr3(anterior.getResultado());
        }
    }

    public boolean InstrucaoCarregar() {
        return this.getop().equals("lw") || this.getop().startsWith("ld");
    }

    public boolean temDependenciaDeDados(Instrucao outra) {
        return (this.getr1() != null && (this.getr1().equals(outra.getr2()) || this.getr1().equals(outra.getr3()))) ||
               (this.getr2() != null && (this.getr2().equals(outra.getr1()) || this.getr2().equals(outra.getr3()))) ||
               (this.getr3() != null && (this.getr3().equals(outra.getr1()) || this.getr3().equals(outra.getr2())));
    }
}