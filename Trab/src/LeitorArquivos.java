import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class LeitorArquivos {
    public static List<Instrucao> readInstrucoes(String filePath) throws FileNotFoundException {
        List<Instrucao> instrucoes = new ArrayList<>();
        MipsInstrucao TipoInstrucao = new MipsInstrucao();

        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("Arquivo não encontrado!");
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String linha = fileScanner.nextLine();
                StringTokenizer tokens = new StringTokenizer(linha, ",$() ");

                String parts[] = new String[4];
                int index = 0;
                while(tokens.hasMoreTokens()){
                    parts[index] = tokens.nextToken();
                    index++;
                }

                Instrucao instrucao;
                if(TipoInstrucao.isDestination(parts[0])){
                    instrucao = new Instrucao(parts[0], parts[1], parts[3], parts[2]);
                }else{
                    instrucao = new Instrucao(parts[0], parts[3], parts[1], parts[2]);
                }

                instrucoes.add(instrucao);
            }
        }

        return instrucoes;
    }
}