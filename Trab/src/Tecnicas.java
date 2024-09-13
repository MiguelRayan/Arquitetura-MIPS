import java.util.ArrayList;
import java.util.List;

public class Tecnicas {

    // Enum para as técnicas disponíveis
    public enum TecnicaTipo {
        BOLHA, ADIANTAMENTO, REORDENAMENTO
    }

    private TecnicaTipo tecnicaTipo;

    // Construtor para definir a técnica a ser utilizada
    public Tecnicas(TecnicaTipo tecnicaTipo){
        this.tecnicaTipo = tecnicaTipo;
    }

    public List<Instrucao> processarInstrucoes(List<Instrucao> pipeline){
        switch (tecnicaTipo) {
            case BOLHA:
                return aplicarTecnicaBolha(pipeline);
            case ADIANTAMENTO:
                return aplicarTecnicaAdiamento(pipeline);
            case REORDENAMENTO:
                return aplicarTecnicaReordenamento(pipeline);
            default:
                throw new IllegalArgumentException("Técnica não reconhecida: " + tecnicaTipo);
        }
    }

    private List<Instrucao> aplicarTecnicaBolha(List<Instrucao> pipeline){
        List<Instrucao> result = new ArrayList<>();
        List<Instrucao> nops = new ArrayList<>();

        for (int i=0; i<pipeline.size()-1; i++){
            Instrucao first = pipeline.get(i);
            Instrucao second = pipeline.get(i + 1);
            Instrucao third = (i < pipeline.size() - 2) ? pipeline.get(i + 2) : null;

            int nopCount = NOPS (first, second, third);
            Instrucao[] nopInstrucaos = geraNOPS (pipeline, i);

            addInstrucaosToResult(first, nopCount, result, nopInstrucaos, nops);
        }

        result.add(pipeline.get(pipeline.size() - 1));
        reord(result, nops);

        return result;
    }

    private List<Instrucao> aplicarTecnicaAdiamento(List<Instrucao> pipeline) {
        // Implemente a lógica específica para a técnica de adiamento aqui
        return pipeline;
    }

    private List<Instrucao> aplicarTecnicaReordenamento(List<Instrucao> pipeline) {
        // Implemente a lógica específica para a técnica de reordenamento aqui
        return pipeline;
    }

    private void addInstrucaosToResult(Instrucao instrucao, int nopCount, List<Instrucao> result,
                                       Instrucao[] nopInstrucaos, List<Instrucao> nops) {
        if (nopCount == 0) {
            result.add(instrucao);
        } else if (nopCount == 2) {
            result.add(instrucao);
            result.add(nopInstrucaos[0]);
            result.add(nopInstrucaos[1]);
            nops.add(nopInstrucaos[0]);
            nops.add(nopInstrucaos[1]);
        } else if (nopCount == 1) {
            result.add(instrucao);
            result.add(nopInstrucaos[2]);
            nops.add(nopInstrucaos[2]);
        }
    }

    private void reord (List<Instrucao> result, List<Instrucao> nops){
        for(int i=0; i<result.size(); i++){
            Instrucao media = result.get(i);
            if(media.getop() == null || depfimencia (i, result)){
                continue;
            }

            for(int j=0; j<nops.size(); j++){
                Instrucao nop = nops.get(j);

                if(nop.getop() != null || 
                    !nop.getr1().contains(media.getr1()) &&
                    !nop.getr3().contains(media.getr2()) &&
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

    private boolean depfimencia (int index, List<Instrucao> result){
        Instrucao media = result.get(index);
        int inicia = Math.max(0, index - 15);
        int fim = Math.min(result.size() - 1, index + 15);

        for(int i=inicia; i<=fim; i++){
            if(i == index){
                continue;
            }

            Instrucao other = result.get(i);
            if(other.getop() == null){
                continue;
            }

            if(other.getr2().matches("^\\d+$")){
                if(media.getr1().equals(other.getr1()) || media.getr1().equals(other.getr3())){
                    return true;
                }
            }else{
                if (media.getr1().equals(other.getr1()) ||
                    media.getr1().equals(other.getr3()) ||
                    media.getr2().equals(other.getr1()) ||
                    media.getr2().equals(other.getr3()) ||
                    media.getr3().equals(other.getr1()) ||
                    media.getr3().equals(other.getr3())) {
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

    private Instrucao[] geraNOPS (List<Instrucao> pipeline, int index){
        Instrucao nop1 = new Instrucao("nop", "", "", "");
        Instrucao nop2 = new Instrucao("nop", "", "", "");
        Instrucao nop3 = new Instrucao("nop", "", "", "");

        if(index < pipeline.size()){
            return new Instrucao[]{nop1, nop2, nop3};
        }

        return new Instrucao[]{nop1, nop2, nop3};
    }
}