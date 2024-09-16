import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class LeitorArquivos {
    public static List<Instrucao> leInstrucao(String CaminhoArquivo) throws FileNotFoundException {
        List<Instrucao> instrucoes = new ArrayList<>();
        MipsInstrucao TipoInstrucao = new MipsInstrucao();

        File Arquivo = new File(CaminhoArquivo);
        if (!Arquivo.exists()) {
            throw new FileNotFoundException("Endereço errado");
        }

        try (Scanner fileScanner = new Scanner(Arquivo)) {
            while (fileScanner.hasNextLine()) {
                String linha = fileScanner.nextLine();
                StringTokenizer tokens = new StringTokenizer(linha, ",$() \t");

                String partes[] = new String[4];
                int index = 0;
                while (tokens.hasMoreTokens()) {
                    partes[index] = tokens.nextToken();
                    index++;
                }

                Instrucao instrucao;
                if (TipoInstrucao.deDestino(partes[0])) {
                    instrucao = new Instrucao(partes[0], partes[1], partes[3], partes[2]);
                } else {
                    instrucao = new Instrucao(partes[0], partes[3], partes[1], partes[2]);
                }

                instrucoes.add(instrucao);
            }
        }

        return instrucoes;
    }
}