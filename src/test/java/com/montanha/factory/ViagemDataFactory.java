package com.montanha.factory;

import com.montanha.pojo.Viagem;

public class ViagemDataFactory {
    public static Viagem criarViagemValida(){
        Viagem viagemValida = new Viagem();
        viagemValida.setAcompanhante("Marlon");
        viagemValida.setDataRetorno("2021-10-01");
        viagemValida.setDataPartida("2021-09-30");
        viagemValida.setLocalDeDestino("Itaperuçu");
        viagemValida.setRegiao("Sul");

        return viagemValida;
    }
}
