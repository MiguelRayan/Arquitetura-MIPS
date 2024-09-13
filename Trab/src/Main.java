import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escolha as técnicas de resolução de conflitos (separe os números por espaço):");
        System.out.println("1 - Bolha");
        System.out.println("2 - Adiantamento");
        System.out.println("3 - Reordenamento");

        String tecnicasConflito = scanner.nextLine();
        String escolhas[] = tecnicasConflito.split("\\s+");
        List<Tecnicas.TecnicaTipo> tecnicaTipos = new ArrayList<>();

        for(String escolha : escolhas){
            switch(escolha){
                case "1":
                    tecnicaTipos.add(Tecnicas.TecnicaTipo.BOLHA);
                    break;
                case "2":
                    tecnicaTipos.add(Tecnicas.TecnicaTipo.ADIANTAMENTO);
                    break;
                case "3":
                    tecnicaTipos.add(Tecnicas.TecnicaTipo.REORDENAMENTO);
                    break;
                default:
                    System.out.println("Escolha inválida: "+escolha+". Esta escolha será ignorada.");
            }
        }

        String base = "C:\\Users\\Migue\\OneDrive\\Documentos\\Code\\z\\"; 
        String prefixo = "TESTE-";
        String sufixo = ".txt";

        for(int i=1; i<2; i++){
            String numArquivo = String.format("%02d", i);
            String tecnicasConflitoCaminho = base + prefixo + numArquivo + sufixo;
            String outputFilePath = base + prefixo + numArquivo + "-RESULTADO.txt";

            try{
                // Ler as instruções do arquivo de entrada
                List<Instrucao> instrucoes = LeitorArquivos.leInstrucao(tecnicasConflitoCaminho);
                
                // Aplicar todas as técnicas selecionadas
                List<Instrucao> resultado = new ArrayList<>(instrucoes);
                for(Tecnicas.TecnicaTipo tecnicaTipo : tecnicaTipos){
                    Tecnicas tecnica = new Tecnicas(tecnicaTipo);
                    resultado = tecnica.processarInstrucoes(resultado);
                }
                
                // Gravar o resultado no arquivo de saída
                writeResultado(outputFilePath, resultado);
                
            }catch (IOException e){
                System.out.println("Erro ao processar o arquivo "+tecnicasConflitoCaminho+": "+e.getMessage());
            }
        }

        scanner.close();
    }

    private static void writeResultado(String filePath, List<Instrucao> resultado) throws IOException {
        try(FileWriter writer = new FileWriter(filePath)){
            for(Instrucao instrucao : resultado){
                writer.write(instrucao.getFormattedValues() + System.lineSeparator());
            }
        }
    }
}
