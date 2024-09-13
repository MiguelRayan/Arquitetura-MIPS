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
        switch (tecnicaTipo) {
            case BOLHA:
                return aplicarTecnicaBolha(pipeline);
            case ADIANTAMENTO:
                return aplicarTecnicaAdiant(pipeline);
            case REORDENAMENTO:
                return aplicarTecnicaReordenamento(pipeline);
            default:
                throw new IllegalArgumentException("Técnica não reconhecida: " + tecnicaTipo);
        }
    }

    private List<Instrucao> aplicarTecnicaBolha(List<Instrucao> pipeline) {
        List<Instrucao> result = new ArrayList<>();
        List<Instrucao> nops = new ArrayList<>();

        for(int i=0; i< pipeline.size()-1; i++){
            Instrucao first = pipeline.get(i);
            Instrucao second = pipeline.get(i + 1);
            Instrucao third = (i < pipeline.size() - 2) ? pipeline.get(i + 2) : null;

            int nopCount = NOPS(first, second, third);
            Instrucao[] nopInstrucaos = criaNOPS(pipeline, i);

            addInstrucaosToResult(first, nopCount, result, nopInstrucaos, nops);
        }

        result.add(pipeline.get(pipeline.size() - 1));
        reord(result, nops);

        return result;
    }

    private List<Instrucao> aplicarTecnicaAdiant(List<Instrucao> pipeline){
        List<Instrucao> result = new ArrayList<>(pipeline);

        for(int i=0; i<result.size()-1; i++){
            Instrucao atual = result.get(i);
            Instrucao proxima = result.get(i + 1);

            if(atual.getr1() != null && (atual.getr1().equals(proxima.getr2()) || atual.getr1().equals(proxima.getr3()))){
                // Encontrar uma instrução futura que seja independente e possa ser adiantada
                for(int j=i+2; j<result.size(); j++){
                    Instrucao instrucaoIndependente = result.get(j);

                    // Verificação de dependência: Se a instrução não depende da atual nem da
                    // próxima
                    if(!instrucaoIndependente.temDependenciaDeDados(atual)
                            && !instrucaoIndependente.temDependenciaDeDados(proxima)){
                        // Troca a próxima instrução pela instrução independente
                        result.set(i + 1, instrucaoIndependente);
                        result.set(j, proxima);

                        System.out.println("Adiantamento aplicado: " + instrucaoIndependente.getFormattedValues() +
                                " adiantada para antes de " + proxima.getFormattedValues());
                        break;
                    }
                }
            }
        }
        return result;
    }

    private List<Instrucao> aplicarTecnicaReordenamento(List<Instrucao> pipeline) {
        List<Instrucao> result = new ArrayList<>(pipeline);

        for(int i=0; i<result.size()-1; i++){
            Instrucao atual = result.get(i);
            Instrucao proxima = result.get(i + 1);

            // Verifica se r1, r2 e r3 não são nulos antes de fazer comparações
            if(proxima.getr1() != null && (proxima.getr1().equals(atual.getr2()) || proxima.getr1().equals(atual.getr3()))) {
                // Procura por uma instrução independente para reordenar
                for(int j=i+2; j<result.size(); j++){
                    Instrucao terceira = result.get(j);

                    if(terceira != null && !terceira.temDependenciaDeDados(atual)
                            && !terceira.temDependenciaDeDados(proxima)) {
                        result.set(i + 1, terceira);
                        result.set(j, proxima);
                        System.out.println("Reordenamento aplicado: " + terceira.getFormattedValues()
                                + " movida para antes de " + proxima.getFormattedValues());
                        break;
                    }
                }
            }
        }

        return result;
    }

    private void addInstrucaosToResult(Instrucao instrucao, int nopCount, List<Instrucao> result,
            Instrucao[] nopInstrucaos, List<Instrucao> nops) {
        if(nopCount == 0){
            result.add(instrucao);
        }else if(nopCount == 2){
            result.add(instrucao);
            result.add(nopInstrucaos[0]);
            result.add(nopInstrucaos[1]);
            nops.add(nopInstrucaos[0]);
            nops.add(nopInstrucaos[1]);
        }else if(nopCount == 1){
            result.add(instrucao);
            result.add(nopInstrucaos[2]);
            nops.add(nopInstrucaos[2]);
        }
    }

    private void reord(List<Instrucao> result, List<Instrucao> nops){
        for(int i=0; i<result.size(); i++){
            Instrucao media = result.get(i);
            if(media.getop() == null || verificaDependencia(i, result)){
                continue;
            }

            for(int j=0; j<nops.size(); j++){
                Instrucao nop = nops.get(j);

                if(nop.getop() != null || !nop.getr1().contains(media.getr1()) && !nop.getr3().contains(media.getr2()) &&
                        !nop.getr3().contains(media.getr3())) {
                    continue;
                }

                result.remove(media);
                nops.set(j, media);
                i--;
                break;
            }
        }

        for(int i=0; i<result.size(); i++){
            Instrucao media = result.get(i);
            if(media.getop() != null){
                continue;
            }
            for(Instrucao nop : nops){
                if(nop.getop() == null){
                    result.set(i, nop);
                    nops.remove(nop);
                    break;
                }
            }
        }
    }

    private boolean verificaDependencia(int index, List<Instrucao> result){
        Instrucao media = result.get(index);
        int inicia = Math.max(0, index - 15);
        int fim = Math.min(result.size() - 1, index + 15);

        for(int i = inicia; i <= fim; i++){
            if(i == index){
                continue;
            }

            Instrucao other = result.get(i);
            if(other.getop() == null){
                continue;
            }

            if(other.getr2() != null && other.getr2().matches("^\\d+$")){
                if((media.getr1() != null && media.getr1().equals(other.getr1())) ||
                        (media.getr1() != null && media.getr1().equals(other.getr3()))) {
                    return true;
                }
            }else{
                if((media.getr1() != null && media.getr1().equals(other.getr1())) ||
                        (media.getr1() != null && media.getr1().equals(other.getr3())) ||
                        (media.getr2() != null && media.getr2().equals(other.getr1())) ||
                        (media.getr2() != null && media.getr2().equals(other.getr3())) ||
                        (media.getr3() != null && media.getr3().equals(other.getr1())) ||
                        (media.getr3() != null && media.getr3().equals(other.getr3()))) {
                    return true;
                }
            }
        }
        return false;
    }

    private int NOPS(Instrucao first, Instrucao second, Instrucao third){
        int count = 0;
        if(third != null && third.getop() != null){
            count++;
        }
        if(second.getop() != null){
            count++;
        }
        return count;
    }

    private Instrucao[] criaNOPS(List<Instrucao> pipeline, int index){
        Instrucao nop1 = new Instrucao("nop", "", "", "");
        Instrucao nop2 = new Instrucao("nop", "", "", "");
        Instrucao nop3 = new Instrucao("nop", "", "", "");

        if(index < pipeline.size()){
            return new Instrucao[] { nop1, nop2, nop3 };
        }

        return new Instrucao[] { nop1, nop2, nop3 };
    }
}