package com.company;

import java.util.HashSet;

/**
 * <p>Classe que computa e exibe a mensagem formatada descrevendo a computação extendida de uma palavra
 * fornecida pelo usuário</p>
 *
 * <p>Imprementa processável, pois como qualquer outro autômato, é capaz de processar uma informação
 * bastando ter o autômato a ser usado e a palavra a ser computada</p>
 *
 * <p>Implementa a interface que dita as regras para processar um autômato finito</p>
 *
 * @author goislimat
 */
public class ComputaAFN implements Processavel {

    /** Estados atuais da computação */
    private HashSet<String> mEstadosAtuais;

    /** Autômato sendo utilizado */
    private AFN mAfn;

    /** Mensagem para o usuário */
    private String mMensagem;

    /** Palavra sendo processada */
    private String mPalavra;

    /** Símbolo sendo atualmente lido */
    private String mSimbolo;

    /** Tamanho da palavra que ainda falta a ser lida */
    private int mPalavraLen;

    /** Constante com o tempo a ser aguardado para exibição da próxima mensagem */
    private final int TEMPO_ESPERA = 1000;

    /**
     * <p>O objetivo desse método, é mostrar o resultado do passo-a-passo da computação de uma palavra
     * dada pelo usuário junto ao autômato que deve processar essa entrada</p>
     *
     * <p>Começa verificando se a palavra passada foi a palavra vazia, caso não tenho sido, passa para
     * a inicialização das variáveis e um primeiro processamento partindo do símbolo inicial e depois a
     * iteração é feita a partir dos estados alcançados depois da primeira iteração</p>
     *
     * <p>O modelo do processamento é:</p>
     * <p>δ*({q0}, babba) =</p>
     * <p>δ*(δ(q0, b), abba) =</p>
     *
     * @param automato que deve ser usado para processar a entrada
     * @param palavra que deve ser processada
     */
    @Override
    public void processaEntrada(AutomatoFinito automato, String palavra) {

        mAfn = (AFN) automato;

        try {
            if(verificaSePalavraVazia(palavra)) return;

            mPalavra = palavra;
            mPalavraLen = mPalavra.length();
            mMensagem = "";

            mEstadosAtuais = new HashSet<String>();

            parteDoSimboloInicial();

            while (mPalavraLen > 0) iteraRestantePalavra();

            mostraResultado();
            testaSucesso();
        } catch (Exception e) { e.getMessage(); }

    }

    /**
     * <p>Verifica se a palavra passada é ou não aceita pelo autômato</p>
     *
     * @param palavra que está sendo processada
     * @throws Exception caso a palavra seja a palavra vazia e a mesma não é reconhecida pelo autômato
     */
    private boolean verificaSePalavraVazia(String palavra) throws Exception {
        if(palavra.equals("ε")) {
            for (String estadoFinal:
                    mAfn.getEstadosFinais()) {
                if(mAfn.getEstadoIncial().equals(estadoFinal)) {
                    System.out.println("A palavra vazia 'ε' é RECONHECIDA pelo autômato!");
                    return true;
                }
            }
            System.out.println(mensagemErroPadrao(new Exception("A palavra vazia 'ε' é REJEITADA pelo autômato!")));
            throw new Exception();
        }

        return false;
    }

    /**
     * <p>Partindo do símbolo inicial</p>
     * <p>1- Monta a hash de estados atuais passando o estado inicial</p>
     * <p>2- Mostra a mensagem de pré-processamento δ*({qo,q2}, babba) =</p>
     * <p>3- Dorme para dar um tempo de ler a mensagem</p>
     * <p>4- Remove um símbolo da palavra para que o mesmo seja processado</p>
     * <p>5- Mostra a mensagem de pós-processamento δ*(δ(q0, b) ∪ δ(q2, b), abba) =</p>
     * <p>6- Dorme para dar um tempo de ler a mensagem</p>
     */
    private void parteDoSimboloInicial() {

        mEstadosAtuais.add(mAfn.getEstadoIncial());

        mMensagem = preMensagem(mEstadosAtuais, mPalavra);
        System.out.println(mMensagem);
        sleep();

        removeUmSimbolo();

        mMensagem = posMensagem(mEstadosAtuais, mPalavra, mSimbolo);
        System.out.println(mMensagem);
        sleep();
    }

    /**
     * <p>Partindo do símbolo inicial</p>
     * <p>1- Monta a hash de estados atuais passando os estados que foram obtidos a partir do processamento do
     * primeiro símbolo a partir do estado inicial</p>
     * <p>2- Mostra a mensagem de pré-processamento δ*({qo,q2}, babba) =</p>
     * <p>3- Dorme para dar um tempo de ler a mensagem</p>
     * <p>4- Remove um símbolo da palavra para que o mesmo seja processado</p>
     * <p>5- Mostra a mensagem de pós-processamento δ*(δ(q0, b) ∪ δ(q2, b), abba) =</p>
     * <p>6- Dorme para dar um tempo de ler a mensagem</p>
     *
     * @throws Exception caso tenhamos caído em apenas transições inválidas
     */
    private void iteraRestantePalavra() throws Exception {
        try {
            mEstadosAtuais = mAfn.avanca(mEstadosAtuais, mSimbolo);
        } catch (Exception e) {
            System.out.println(mensagemErroPadrao(e));
            throw new Exception();
        }

        mMensagem = preMensagem(mEstadosAtuais, mPalavra);
        System.out.println(mMensagem);
        sleep();

        removeUmSimbolo();

        mMensagem = posMensagem(mEstadosAtuais, mPalavra, mSimbolo);
        System.out.println(mMensagem);
        sleep();
    }

    /**
     * <p>Processa o último símbolo para cada um dos estados</p>
     *
     * @throws Exception caso tenhamos caído em apenas transições inválidas
     */
    private void mostraResultado() throws Exception {
        try {
            mEstadosAtuais = mAfn.avanca(mEstadosAtuais, mSimbolo);
        } catch (Exception e) {
            System.out.println(mensagemErroPadrao(e));
            throw new Exception();
        }

        mMensagem = preMensagem(mEstadosAtuais, mPalavra);
        System.out.println(mMensagem);
        sleep();
    }

    /**
     * <p>Testa se no conjunto de estados onde o autômato parou, se há ao menos um estado inicial</p>
     * <p>1- Mostra uma mensagem de resultado</p>
     * <p>2- Verifica se os estados onde paramos tem ao menos um representante nos estados finais</p>
     * <p>3- Mostra a mensagem de resultado de palavra aceita ou rejeitada</p>
     *
     * @throws Exception caso tenhamos caído em apenas transições inválidas
     */
    private void testaSucesso() throws Exception {

        boolean sucesso = false;

        mMensagem = preMensagemFinal();
        System.out.print(mMensagem);
        sleep();

        for (String estadoFinal : mAfn.getEstadosFinais()) {
            if(mEstadosAtuais.contains(estadoFinal)) {
                sucesso = true;
                break;
            }
        }

        try {
            mMensagem = posMensagemFinal(sucesso);
            System.out.println(mMensagem);
        } catch (Exception e) {
            System.out.println(mensagemErroPadrao(e));
            throw new Exception();
        }

    }

    /**
     * <p>Remove um símbolo da palavra, caso seja o último caractere coloca a palavra vazia na palavra sendo precessada
     * pelo autômato</p>
     */
    private void removeUmSimbolo() {
        if (mPalavraLen == 1) {
            mSimbolo = mPalavra;
            mPalavra = "ε";
        } else {
            mSimbolo = mPalavra.substring(0, 1);
            mPalavra = mPalavra.substring(1);
        }

        mPalavraLen--;
    }

    /**
     * <p>Mostra as mensagens de pré-processamento como mostrado abaixo</p>
     * <p>δ*({qo,q1}, babba) =</p>
     */
    private String preMensagem(HashSet<String> estadosCorrentes, String palavra) {

        String mensagem = "δ*({";

        for (String estado :
                estadosCorrentes) {
            mensagem += estado + ",";
        }

        mensagem += "}, " + palavra + ") =";

        mensagem = mensagem.replace(",}", "}");

        return mensagem;
    }

    /**
     * <p>Mostra as mensagens de pós-processamento como mostrado abaixo</p>
     * <p>δ*(δ(q0, b) ∪ δ(q1, b), abba) =</p>
     */
    private String posMensagem(HashSet<String> estadosCorrentes, String palavra, String simbolo) {

        String mensagem = "δ*(δ(";

        for (String estado :
                estadosCorrentes) {
            mensagem += estado + ", " + simbolo + ") ∪ δ(";
        }

        mensagem += ", " + palavra + ") =";

        mensagem = mensagem.replace(" ∪ δ(,", ",");

        return mensagem;
    }

    /**
     * <p>Monta a String de verificação da notação de conjuntos</p>
     * <p>{q0, q1, qf} ∩ {qf}</p>
     *
     * @return a String da mensagem de verificação de estados finais
     */
    private String preMensagemFinal() {
        String mensagem = "{";

        for (String estado :
                mEstadosAtuais) {
            mensagem += estado + ", ";
        }

        mensagem += "} ∩ {";

        for (String estadoFinal :
                mAfn.getEstadosFinais()) {
            mensagem += estadoFinal + ", ";
        }

        mensagem += "}";

        mensagem = mensagem.replace(", }", "}");

        return mensagem;
    }

    /**
     * <p>Lança as mensagens devidas após a leitura da palavra</p>
     * <p>= Ø ou ≠ Ø</p>
     *
     * @return a String com a mensagem resultante
     * @throws Exception caso a palavra tenha sido rejeitada
     */
    private String posMensagemFinal(boolean sucesso) throws Exception {
        String mensagem;

        if(sucesso) {
            mensagem = " ≠ Ø >>>> PALAVRA ACEITA";
        } else {
            mensagem = " = Ø >>>> PALAVRA REJEITADA";
            throw new Exception(mensagem);
        }

        return mensagem;
    }

    /**
     * <p>Mensagem padronizada de erro</p>
     *
     * @param e exception contendo a mensagem a ser exibida
     * @return a mensagem em vermelho como texto do erro lançado
     */
    private String mensagemErroPadrao(Exception e) {
        return Main.ANSI_RED + e.getMessage() + Main.ANSI_RESET;
    }

    /**
     * <p>Coloca o método para dormir e dar um prazo para que o usuário possa ler o resultado</p>
     */
    private void sleep() {
        try {
            Thread.sleep(TEMPO_ESPERA);
        } catch (InterruptedException e) {
            System.out.println(mensagemErroPadrao(e));
        }
    }
}
