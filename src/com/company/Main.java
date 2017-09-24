package com.company;

import java.util.*;

import static java.lang.System.exit;

/**
 * <p>Classe principal da aplicação onde a leitura dos dados é feita</p>
 *
 * @author goislimat
 */
public class Main {

    /** Leitor da entrada padrão do usuário */
    private Scanner s = new Scanner(System.in);

    /** Vetor de símbolos */
    private String[] mAlfabeto;

    /** Vetor de estados */
    private String[] mEstados;

    /** Lista de regras de produção */
    private ArrayList<RegraProducao> mRegrasProducao;

    /** Estao inicial */
    private String mEstadoInicial;

    /** Vetor de estados finais */
    private String[] mEstadosFinais;

    /** Automato criado com base nas informações passadas */
    private AutomatoFinito mAutomato;

    /** Cor padrão do texto */
    public static final String ANSI_RESET = "\u001B[0m";

    /** Cor vermelha para realçar mensagens de erro */
    public static final String ANSI_RED = "\u001B[31m";

    /** Comando para retornar ao menu principal */
    private static final String REFEFINIR_AUTOMATO = "<<";

    /** Comando para sair do programa */
    private static final String ENCERRAR_PROGRAMA = "exit";

    /**
     * <p>Inicializa a aplicação</p>
     *
     * @param args
     */
    public static void main(String[] args) {
        new Main().iniciaPrograma();
    }

    /**
     * <p>Representa o fluxo de apresentação e leitura de dados do usuário sobre o autômato que tem um exemplo de sua
     * tupla de composição mostrada abaixo.</p>
     * <p>M = (∑, Q, δ, S, F)</p>
     * <p>Todos os dados serão lidos como strings e divididos em vetores baseado no padrão observado na entrada.</p>
     * <p>No caso, o padrão adotado será que todos os valores a serem denotados em conjuntos deverão ser separados
     * por vírgula (,) com ou sem espaços.</p>
     *
     * <p>Esse método tem a única responsabilidade de organizar o fluxo de leitura dos dados passados pelo
     * usuário do sistema. E é nele que o fluxo do programa volta sempre que há a necessidade de repassar os
     * parâmetros do autômato</p>
     */
    private void iniciaPrograma() {
        boolean automatoValido = false;

        do {
            mensagemInicial();
            leAlfabeto();
            leEstados();
            leTransicoes();
            leEstadoInicial();
            leEstadosFinais();

            try {
                criaAFN();
                automatoValido = true;
            } catch (Exception e) { }
        } while(!automatoValido);

        for(;;) {
            lePalavra();
        }
    }

    /**
     * <p>Exibe a primeira mensagem da aplicação oferendo um menu de ajuda ou a possibilidade de entrar diretamente com
     * os parâmetros do autômato</p>
     */
    private void mensagemInicial() {
        System.out.print("\nEntre com 'h' para ver a ajuda ou [ENTER] para continuar: ");

        if("h".equals(s.nextLine())){
            ajuda();
        }
    }

    /**
     * <p>Exibe uma ajuda rápida de como a aplicação funciona</p>
     */
    private void ajuda() {
        System.out.println("INSTRUÇÕES:");
        System.out.println("\n1- O início do programa consiste em um pré-processamento onde as informações" +
                "do autômato devem ser inseridas.");
        System.out.println("\n2- Toda vez que for utilizar um conjunto, separe os itens com ',' vírugla (não" +
                "são necessários espaços).\nEx: q0,q1,q2 ou q0, q1, q2");
        System.out.println("\n3- Quando for preencher as regras de produção entre com todos os estados atingíveis" +
                "dada a regra de produção descrita (não são necessários espaços).\nEx: δ(q0, a) = q0,q1 ou q0, q1");
        System.out.println("\n4- Se o estado tiver uma transição indefinida para o símbolo preencha com '-'.\n" +
                "Ex: δ(q0, a) =  -");
        System.out.print("\nPressione [ENTER] para sair.");
        s.nextLine();
    }

    /**
     * <p>Lê da entrada padrão o alfabeto que o usuário vai ter que interpretar com o autômato</p>
     * <p>Cada símbolo é inserido em uma posição do array</p>
     */
    private void leAlfabeto() {
        System.out.print("Entre com os símbolos do alfabeto ∑ = ");
        mAlfabeto = s.nextLine().replace(" ", "").split(",");

    }

    /**
     * <p>Lê o conjunto de estados que fazem parte do autômato, nesse momento ainda não há distinção entre os
     * estados</p>
     * <p>Cada estado é inserido em uma posição do array</p>
     */
    private void leEstados() {
        System.out.print("Entre com o conjunto de estados Q = ");
        mEstados = s.nextLine().replace(" ", "").split(",");
    }

    /**
     * <p>Lê todas as transições do autômato. Todas as transições possíveis são apresentadas uma a uma para o usuário, que
     * deve informar quais são os estados atingíveis a partir do estado apresentado lendo o símbolo informado pela
     * aplicação</p>
     *
     * <p>Para cada transição inserida, um novo objeto RegraProducao é criado passando-se as informações sobre quais o
     * nó de origem, o símbolo lido e quais estados são resultantes dessa transição em forma de array</p>
     */
    private void leTransicoes() {
        System.out.println("Entre com as regras de produção para cada estado (siga a ordem exibida):");

        mRegrasProducao = new ArrayList<RegraProducao>();

        for (int i = 0; i < mEstados.length; i++) {
            for (int j = 0; j < mAlfabeto.length; j++) {
                System.out.print("δ(" + mEstados[i] + ", " + mAlfabeto[j] + ") = ");
                String[] producoes = s.nextLine().replace(" ", "").split(",");

                mRegrasProducao.add(new RegraProducao(mEstados[i], mAlfabeto[j], producoes));
            }
        }
    }

    /**
     * <p>Lê o estado inicial do autômato</p>
     */
    private void leEstadoInicial() {
        System.out.print("Entre com o estado inicial S = ");
        mEstadoInicial = s.nextLine();
    }

    /**
     * <p>Lê todos os estados que formam o conjunto de estados finais do autômato</p>
     * <p>Cada estado final é inserido em uma posição do array</p>
     */
    private void leEstadosFinais() {
        System.out.print("Entre com o conjunto de estados finais F = ");
        mEstadosFinais = s.nextLine().replace(" ", "").split(",");
    }

    /**
     * <p>Com os parâmetros que foram inseridos nos passos anteriores, a aplicação tenta criar um autômato</p>
     *
     * @throws Exception caso alguma informação esteja errada e seja impossível criar o autômato
     */
    private void criaAFN() throws Exception {
        try {
            mAutomato = new AFN(mAlfabeto, mEstados, mRegrasProducao, mEstadoInicial, mEstadosFinais);
        } catch (Exception e) {
            System.out.println(Main.ANSI_RED + e.getMessage() + Main.ANSI_RESET);
            System.out.println(Main.ANSI_RED + "Você deve informar os parâmetros do autômato novamente!" + Main.ANSI_RESET);
            throw new Exception();
        }
    }

    /**
     * <p>Objetivo principal é ler a entrada que o autômato deve processar</p>
     * <p>Adicionalmente, verifica se o usuário deseja redefinir um novo autômato para ser processado pela aplicação ou
     * se quer encerrar a aplicação</p>
     */
    private void lePalavra() {
        System.out.print("\nInforme" +
                "\nA palavra a ser computada pelo autômato ou " +
                "\nEntre com '<<' para informar novamente os parâmetros do autômato ou" +
                "\nEntre com 'exit' para sair do programa: ");
        String palavra = s.nextLine();

        if(palavra.equals(Main.REFEFINIR_AUTOMATO)) {
            iniciaPrograma();
        } else if(palavra.equals(Main.ENCERRAR_PROGRAMA)) {
            exit(0);
        }

        new ComputaAFN().processaEntrada(mAutomato, palavra);
    }
}
