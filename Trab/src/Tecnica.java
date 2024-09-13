import java.util.ArrayList;
import java.util.List;

public class Tecnica {

    // Enum para as técnicas disponíveis
    public enum TecnicaTipo {
        BOLHA, ADIANTAMENTO, REORDENAMENTO
    }

    private TecnicaTipo tecnicaTipo;

    // Construtor para definir a técnica a ser utilizada
    public Tecnica(TecnicaTipo tecnicaTipo) {
        this.tecnicaTipo = tecnicaTipo;
    }

    public List<Instrucao> processarInstrucoes(List<Instrucao> pipeline) {
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

    private List<Instrucao> aplicarTecnicaBolha(List<Instrucao> pipeline) {
        List<Instrucao> result = new ArrayList<>();
        List<Instrucao> nops = new ArrayList<>();

        for (int i = 0; i < pipeline.size() - 1; i++) {
            Instrucao first = pipeline.get(i);
            Instrucao second = pipeline.get(i + 1);
            Instrucao third = (i < pipeline.size() - 2) ? pipeline.get(i + 2) : null;

            int nopCount = countNops(first, second, third);
            Instrucao[] nopInstrucaos = generateNops(pipeline, i);

            addInstrucaosToResult(first, nopCount, result, nopInstrucaos, nops);
        }

        result.add(pipeline.get(pipeline.size() - 1));
        reorderInstrucaos(result, nops);

        return result;
    }

    private List<Instrucao> aplicarTecnicaAdiamento(List<Instrucao> pipeline) {
        // Implemente a lógica específica para a técnica de adiamento aqui
        return pipeline; // Placeholder
    }

    private List<Instrucao> aplicarTecnicaReordenamento(List<Instrucao> pipeline) {
        // Implemente a lógica específica para a técnica de reordenamento aqui
        return pipeline; // Placeholder
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

    private void reorderInstrucaos(List<Instrucao> result, List<Instrucao> nops) {
        for (int i = 0; i < result.size(); i++) {
            Instrucao current = result.get(i);
            if (current.getOperation() == null || hasDependency(i, result)) {
                continue;
            }

            for (int j = 0; j < nops.size(); j++) {
                Instrucao nop = nops.get(j);

                if (nop.getOperation() != null || 
                    !nop.getReg1().contains(current.getReg1()) &&
                    !nop.getReg3().contains(current.getReg2()) &&
                    !nop.getReg3().contains(current.getReg3())) {
                    continue;
                }

                result.remove(current);
                nops.set(j, current);
                i--;
                break;
            }
        }

        for (int i = 0; i < result.size(); i++) {
            Instrucao current = result.get(i);
            if (current.getOperation() != null) {
                continue;
            }
            for (Instrucao nop : nops) {
                if (nop.getOperation() == null) {
                    result.set(i, nop);
                    nops.remove(nop);
                    break;
                }
            }
        }
    }

    private boolean hasDependency(int index, List<Instrucao> result) {
        Instrucao current = result.get(index);
        int start = Math.max(0, index - 15);
        int end = Math.min(result.size() - 1, index + 15);

        for (int i = start; i <= end; i++) {
            if (i == index) {
                continue;
            }

            Instrucao other = result.get(i);
            if (other.getOperation() == null) {
                continue;
            }

            if (other.getReg2().matches("^\\d+$")) {
                if (current.getReg1().equals(other.getReg1()) || current.getReg1().equals(other.getReg3())) {
                    return true;
                }
            } else {
                if (current.getReg1().equals(other.getReg1()) ||
                    current.getReg1().equals(other.getReg3()) ||
                    current.getReg2().equals(other.getReg1()) ||
                    current.getReg2().equals(other.getReg3()) ||
                    current.getReg3().equals(other.getReg1()) ||
                    current.getReg3().equals(other.getReg3())) {
                    return true;
                }
            }
        }

        return false;
    }

    private int countNops(Instrucao first, Instrucao second, Instrucao third) {
        int count = 0;
        if (third != null && third.getOperation() != null) {
            count++;
        }
        if (second.getOperation() != null) {
            count++;
        }
        return count;
    }

    private Instrucao[] generateNops(List<Instrucao> pipeline, int index) {
        Instrucao nop1 = new Instrucao("nop", "", "", "");
        Instrucao nop2 = new Instrucao("nop", "", "", "");
        Instrucao nop3 = new Instrucao("nop", "", "", "");

        if (index < pipeline.size()) {
            return new Instrucao[]{nop1, nop2, nop3};
        }

        return new Instrucao[]{nop1, nop2, nop3};
    }
}