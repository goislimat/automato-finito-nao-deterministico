package com.company;

import java.util.*;

/**
 * <p>Classe que implementa o autômato finito não-determinístico</p>
 * <p>Quarda todas as informações da tupla M = (∑, Q, δ, S, F) caso venham a ser utilizados</p>
 */
public class AFN extends AutomatoFinito{

    /**
     * <p>Recebe todas as informações necessárias para que se crie um AFN</p>
     *
     * @param alfabeto reconhecido
     * @param estados existentes no autômato
     * @param regrasProducao para cada uma das transições existentes
     * @param estadoInicial único do autômato
     * @param estadosFinais existentes no autômato
     * @throws Exception caso qualquer uma das informações informadas resulte em um autômato inválido
     */
    public AFN(String[] alfabeto, String[] estados,
               ArrayList<RegraProducao> regrasProducao, String estadoInicial,
               String[] estadosFinais) throws Exception {
        super(alfabeto, estados, regrasProducao, estadoInicial, estadosFinais);
    }

    /**
     * <p>Retorna um HashSet com todos os estados que são atingíveis partindo-se dos estados atuais e
     * levando em conta o símbolo que se quer ler</p>
     *
     * @param estadosAtuais reflete todos os estados correntes na computação
     * @param simboloLido é o símbolo que atualmente se encontra em fase de processamento
     * @return um HashSet com todos os estados que foram atingidos através dessas transições
     */
    @Override
    public HashSet<String> avanca(HashSet<String> estadosAtuais, String simboloLido) throws Exception {

        String resultado = "";

        for (String estado : estadosAtuais) {
            for (RegraProducao regra : mRegrasProducao) {
                if(estado.equals(regra.getOrigem()) && simboloLido.equals(regra.getSimbolo())) {
                    for (String estadoDestino : regra.getDestinos()) {
                        resultado += estadoDestino + ",";
                    }
                }
            }
        }

        resultado = ajustaResultado(resultado, estadosAtuais, simboloLido);

        estadosAtuais = new HashSet<String>();
        estadosAtuais.addAll(Arrays.asList(resultado.split(",")));

        return estadosAtuais;
    }

    /**
     * <p>Formata a String para que não haja problemas no momento de definiro HashSet</p>
     * <p>Analisa se não foram obtidas nenhuma transiçã ou se há transições indefinidas que foram lidas das regras de
     * produção</p>
     * <p>Devolvendo uma String perfeitamente formatada e pronta para ser adicionada ao HashSet</p>
     *
     * @param result recebida do processo de leitura dos dados das regras de produção
     * @param estadosAtuais para os quais desejamos saber para onde vamos casa um simbolo esteja sendo lido
     * @param simboloLido da cadeia de símbolos informadas como entrada
     * @return String formatada pronta para ser consumida pelo HashSet
     * @throws Exception caso não haja nenhum estado a ser atingido a partir dos estados atuais para esse determinado
     * símbolo
     */
    private String ajustaResultado(String result, HashSet<String> estadosAtuais, String simboloLido) throws Exception {
        if(result.equals("-,")) {
            String mensagem = "Para o(s) estado(s) ";

            for (String estado : estadosAtuais) {
                mensagem += estado + ", ";
            }

            mensagem += "xxx a transição lê '" + simboloLido + "' é indefinida. Portanto a palavra é REJEITADA!";
            mensagem = mensagem.replace(", xxx", "");

            throw new Exception(mensagem);
        }

        result = result.replace("-", "");
        result = result.replace(",,", ",");

        if(result.charAt(0) == ',') {
            result = result.substring(1);
        }

        result = result.substring(0, result.length() - 1);

        return result;
    }
}
