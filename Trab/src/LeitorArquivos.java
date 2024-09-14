import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class LeitorArquivos {
    public static List<Instrucao> leInstrucao(String filePath) throws FileNotFoundException {
        List<Instrucao> instrucoes = new ArrayList<>();
        MipsInstrucao TipoInstrucao = new MipsInstrucao();

        File Arquivo = new File(filePath);
        if(!Arquivo.exists()){
            throw new FileNotFoundException("Endereço errado");
        }

        try(Scanner fileScanner = new Scanner(Arquivo)){
            while (fileScanner.hasNextLine()) {
                String linha = fileScanner.nextLine();
                StringTokenizer tokens = new StringTokenizer(linha, ",$() \t");

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