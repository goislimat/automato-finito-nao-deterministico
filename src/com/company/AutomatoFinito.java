package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * <p>Superclasse abstrata de qualquer autômato finito</p>
 *
 * @author goslimat
 */
public abstract class AutomatoFinito {

    /** Alfabeto do autômato */
    protected String[] mAlfabeto;

    /** Conjunto de estados */
    protected String[] mEstados;

    /** Conjunto das resgras de produção */
    protected ArrayList<RegraProducao> mRegrasProducao;

    /** Estado inicial */
    protected String mEstadoIncial;

    /** Estados finais */
    protected String[] mEstadosFinais;

    /** Armazena uma lista de estados sendo validada */
    protected List<String> mListaEstados;

    /**
     * <p>Recebe todas as informações necessárias para que se crie um Autômato Finito</p>
     * <p>Ao final da inserção dessas informações, o autômato é validado para garantir a integridade da execução desse
     * autômato quando as palavras forem inseridas pelo usuário</p>
     *
     * @param alfabeto reconhecido
     * @param estados existentes no autômato
     * @param regrasProducao para cada uma das transições existentes
     * @param estadoInicial único do autômato
     * @param estadosFinais existentes no autômato
     * @throws Exception caso qualquer uma das informações informadas resulte em um autômato inválido
     */
    public AutomatoFinito(String[] alfabeto, String[] estados,
               ArrayList<RegraProducao> regrasProducao, String estadoInicial,
               String[] estadosFinais) throws Exception {
        mAlfabeto = alfabeto;
        mEstados = estados;
        mRegrasProducao = regrasProducao;
        mEstadoIncial = estadoInicial;
        mEstadosFinais = estadosFinais;

        validarAutomato();
    }

    /**
     * <p>Deixa para que cada tipo de autômato finito diga como vai avançar na sua computação</p>
     *
     * @param estadosAtuais reflete todos os estados correntes na computação
     * @param simboloLido é o símbolo que atualmente se encontra em fase de processamento
     * @return um HashSet com todos os estados que foram atingidos através dessas transições
     */
    public abstract HashSet<String> avanca(HashSet<String> estadosAtuais, String simboloLido) throws Exception;

    /**
     * <p>Verifica a validade do autômato passado por parâmetro de acordo com os padrões analisados</p>
     *
     * @throws Exception lançada de cada uma das verificações feitas
     */
    private void validarAutomato() throws Exception {
        validaEstadoInicial();
        validaEstadosFinais();
        validaTransicoes();
    }

    /**
     * <p>Verifica se o estado inicial existe no conjunto de estados informados para o autômato</p>
     *
     * @throws Exception caso o estado inicial informado não exista no conjunto de todos os estados do autômato
     */
    private void validaEstadoInicial() throws Exception {
        mListaEstados = new ArrayList<String>(Arrays.asList(mEstados));
        if(!mListaEstados.contains(mEstadoIncial)) {
            throw new Exception("O estado inicial " + mEstadoIncial + " não faz parte do conjunto de " +
                    "estados Q listados");
        }
    }

    /**
     * <p>Valida o conjunto de estados finais informado</p>
     *
     * @throws Exception caso ao menos um dos estados finais não exista no conjunto de todos os estados do autômato
     */
    private void validaEstadosFinais() throws Exception {
        for (String estado : mEstadosFinais) {
            if(!mListaEstados.contains(estado)) {
                throw new Exception("O estado final " + estado + " não faz parte do conjunto de " +
                        "estados Q listados");
            }
        }
    }

    /**
     * <p>Valida cada uma das transições definidas no autômato</p>
     *
     * @throws Exception caso ao menos uma das regras de produção tente acessar um estado que não foi listado no
     * conjunto de todos os estados do autômato
     */
    private void validaTransicoes() throws Exception {
        for (RegraProducao regra : mRegrasProducao) {
            for (String estado : regra.getDestinos()) {
                if(!mListaEstados.contains(estado) && !estado.equals("-")) {
                    throw new Exception("O estado " + estado + " na regra de produção " + regra.getOrigem() +
                            " lê " + regra.getSimbolo() + ", não faz parte do conjunto de estados Q listados");
                }
            }
        }
    }

    /**
     * <p>Resgata o estado inicial</p>
     *
     * @return o estado inicial
     */
    public String getEstadoIncial() {
        return mEstadoIncial;
    }

    /**
     * <p>Resgata o vetor de estados finais</p>
     *
     * @return os estados finais do autômato
     */
    public String[] getEstadosFinais() { return mEstadosFinais; }
}
