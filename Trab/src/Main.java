import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Escolha das técnicas
        System.out.println("Escolha as técnicas de resolução de conflitos (separe os números por espaço):");
        System.out.println("1 - Bolha");
        System.out.println("2 - Adiantamento");
        System.out.println("3 - Reordenamento");

        String input = scanner.nextLine();
        String[] choices = input.split("\\s+");
        List<Tecnica.TecnicaTipo> tecnicaTipos = new ArrayList<>();

        for (String choice : choices) {
            switch (choice) {
                case "1":
                    tecnicaTipos.add(Tecnica.TecnicaTipo.BOLHA);
                    break;
                case "2":
                    tecnicaTipos.add(Tecnica.TecnicaTipo.ADIANTAMENTO);
                    break;
                case "3":
                    tecnicaTipos.add(Tecnica.TecnicaTipo.REORDENAMENTO);
                    break;
                default:
                    System.out.println("Escolha inválida: " + choice + ". Esta escolha será ignorada.");
            }
        }

        String basePath = "C:\\Users\\Migue\\OneDrive\\Documentos\\Code\\z\\"; 
        String filePrefix = "TESTE-";
        String fileSuffix = ".txt";

        for (int i = 1; i < 2; i++) {
            String fileNumber = String.format("%02d", i);
            String inputFilePath = basePath + filePrefix + fileNumber + fileSuffix;
            String outputFilePath = basePath + filePrefix + fileNumber + "-RESULTADO.txt";

            try {
                // Ler as instruções do arquivo de entrada
                List<Instrucao> instrucoes = FileReaderUtil.readInstrucoes(inputFilePath);
                
                // Aplicar todas as técnicas selecionadas
                List<Instrucao> resultado = new ArrayList<>(instrucoes);
                for (Tecnica.TecnicaTipo tecnicaTipo : tecnicaTipos) {
                    Tecnica tecnica = new Tecnica(tecnicaTipo);
                    resultado = tecnica.processarInstrucoes(resultado);
                }
                
                // Gravar o resultado no arquivo de saída
                writeResultado(outputFilePath, resultado);
                
            } catch (IOException e) {
                System.out.println("Erro ao processar o arquivo " + inputFilePath + ": " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void writeResultado(String filePath, List<Instrucao> resultado) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Instrucao instrucao : resultado) {
                writer.write(instrucao.getFormattedValues() + System.lineSeparator());
            }
        }
    }
}
