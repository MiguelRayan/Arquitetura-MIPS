import java.util.ArrayList;
import java.util.List;

public class Tecnicas {

    // Enum para as técnicas disponíveis
    public enum TecnicaTipo {
        BOLHA, ADIANTAMENTO, REORDENAMENTO
    }

    private TecnicaTipo tecnicaTipo;

    // Construtor para definir a técnica a ser utilizada
    public Tecnicas(TecnicaTipo tecnicaTipo) {
        this.tecnicaTipo = tecnicaTipo;
    }

    public List<Instrucao> processarInstrucoes(List<Instrucao> pipeline){
        switch(tecnicaTipo){
            case BOLHA:
                return aplicarTecnicaBolha(pipeline);
            case ADIANTAMENTO:
                return aplicarTecnicaAdiantamento(pipeline);
            case REORDENAMENTO:
                return aplicarTecnicaReordenamento(pipeline);
            default:
                throw new IllegalArgumentException("Técnica não reconhecida: "+tecnicaTipo);
        }
    }

    private List<Instrucao> aplicarTecnicaBolha(List<Instrucao> pipeline) {
        List<Instrucao> resultado = new ArrayList<>();

        for (int i = 0; i < pipeline.size() - 1; i++) {
            Instrucao atual = pipeline.get(i);
            Instrucao proxima = pipeline.get(i + 1);

            // Verificar se há dependência de dados entre a instrução atual e a próxima
            if (temDependenciaDeDados(atual, proxima)) {
                // Inserir NOP se houver dependência de dados
                resultado.add(atual);
                resultado.add(new Instrucao("NOP", "", "", ""));
            } else {
                resultado.add(atual);
            }
        }

        // Adicionar a última instrução ao resultado
        resultado.add(pipeline.get(pipeline.size() - 1));
        return resultado;
    }

    private List<Instrucao> aplicarTecnicaAdiantamento(List<Instrucao> pipeline) {
        List<Instrucao> resultado = new ArrayList<>(pipeline);

        for(int i=0; i<resultado.size()-1; i++){
            Instrucao atual = resultado.get(i);
            Instrucao proxima = resultado.get(i + 1);

            if(atual.temDependenciaDeDados(proxima) && !atual.isLoadInstruction()){
                if(proxima.podeReceberAdiantamentoDe(atual)){
                    proxima.receberAdiantamento(atual);
                }else{
                    for(int j=i+2; j<resultado.size(); j++){
                        Instrucao independente = resultado.get(j);
                        if(!independente.temDependenciaDeDados(atual) && !independente.temDependenciaDeDados(proxima)){
                            resultado.set(i + 1, independente);
                            resultado.set(j, proxima);
                            break;
                        }
                    }
                }
            }
        }
        return resultado;
    }

    private List<Instrucao> aplicarTecnicaReordenamento(List<Instrucao> pipeline) {
        List<Instrucao> resultado = new ArrayList<>(pipeline);
        resultado.removeIf(instrucao -> "NOP".equals(instrucao.getop()));

        boolean[][] dependencias = calcularDependencias(resultado);

        for(int i=0; i<resultado.size() - 1; i++){
            if(!dependencias[i][i + 1]){
                continue;
            }

            for(int j=i+2; j<resultado.size(); j++){
                Instrucao futura = resultado.get(j);
                if(!dependencias[i][j] && !dependencias[i + 1][j]){
                    resultado.set(i + 1, futura);
                    resultado.set(j, resultado.get(i + 1));
                    break;
                }
            }
        }
        return resultado;
    }

    private boolean[][] calcularDependencias(List<Instrucao> pipeline) {
        int tamanho = pipeline.size();
        boolean[][] dependencias = new boolean[tamanho][tamanho];

        for(int i=0; i<tamanho; i++){
            for (int j = i + 1; j < tamanho; j++) {
                dependencias[i][j] = pipeline.get(j).temDependenciaDeDados(pipeline.get(i));
            }
        }

        return dependencias;
    }

    private void adicionarAoResultado(Instrucao instrucao, int quantidadeNops, List<Instrucao> resultado, Instrucao[] nops, List<Instrucao> nopsList) {
        resultado.add(instrucao);
        if(quantidadeNops > 0){
            resultado.add(nops[quantidadeNops - 1]);
            nopsList.add(nops[quantidadeNops - 1]);
        }
    }

    private void reordenarResultado(List<Instrucao> resultado, List<Instrucao> nops) {
        for(int i=0; i<resultado.size(); i++){
            Instrucao instrucao = resultado.get(i);
            if (instrucao.getop() == null || !temDependencia(i, resultado)) {
                continue;
            }

            for(int j=0; j<nops.size(); j++){
                Instrucao nop = nops.get(j);
                if (nop.getop() == null) {
                    resultado.set(i, nop);
                    nops.set(j, instrucao);
                    break;
                }
            }
        }
    }

    private boolean temDependencia(int indice, List<Instrucao> resultado) {
        Instrucao atual = resultado.get(indice);
        for(int i=0; i<resultado.size(); i++){
            if(i != indice && resultado.get(i).temDependenciaDeDados(atual)) {
                return true;
            }
        }
        return false;
    }

    private boolean temDependenciaDeDados(Instrucao atual, Instrucao proxima) {
        String regEscritaAtual = atual.getr1(); // Registrador escrito pela primeira instrução
        String regLidoProxima1 = proxima.getr2(); // Primeiro registrador lido pela próxima instrução
        String regLidoProxima2 = proxima.getr3(); // Segundo registrador lido pela próxima instrução

        // Verifica se o registrador escrito pela atual é lido pela próxima instrução
        return regEscritaAtual.equals(regLidoProxima1) || regEscritaAtual.equals(regLidoProxima2);
    }

    private Instrucao[] criarNops() {
        return new Instrucao[] {
            new Instrucao("NOP", "", "", ""),
            new Instrucao("NOP", "", "", ""),
            new Instrucao("NOP", "", "", "")
        };
    }
}