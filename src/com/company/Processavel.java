package com.company;

/**
 * <p>Toda classe que for computar um autômato finito deve implementar essa classe</p>
 *
 * @author goislimat
 */
public interface Processavel {
    /**
     * <p>Processa a entrada que deve ser validada pelo autômato</p>
     *
     * @param automato a ser utilizado
     * @param palavra a ser testada
     */
    void processaEntrada(AutomatoFinito automato, String palavra);
}
