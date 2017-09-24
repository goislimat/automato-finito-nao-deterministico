package com.company;

/**
 * <p>Classe que representa a estrutura de cada regra de produção</p>
 *
 * @author goislimat
 */
public class RegraProducao {

    /** Estado de origem */
    private String mOrigem;

    /** Vetor de estados destino */
    private String[] mDestinos;

    /** Símbolos lidos */
    private String mSimbolo;

    /**
     * <p>Cria uma nova transição com base nas informações passadas</p>
     *
     * @param origem da transição
     * @param simbolo lido a partir da origem
     * @param destinos alcançados depois de feita a leitura
     */
    public RegraProducao(String origem, String simbolo, String[] destinos) {
        mOrigem = origem;
        mSimbolo = simbolo;
        mDestinos = destinos;
    }

    /**
     * <p>Resgata o estado de origem da transição</p>
     *
     * @return o estado de origem
     */
    public String getOrigem() { return mOrigem; }

    /**
     * <p>Resgata o símbolo lido a partir da origem da transição</p>
     *
     * @return o símbolo lido
     */
    public String getSimbolo() {
        return mSimbolo;
    }

    /**
     * <p>Resgata o vetor de estados de destino da transição</p>
     *
     * @return o vetor de estados de destino
     */
    public String[] getDestinos() {
        return mDestinos;
    }
}
