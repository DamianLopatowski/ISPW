package org.example.service;

import org.example.bean.ClienteBean;
import org.example.model.Cliente;

public class ClienteMapper {

    private ClienteMapper() {
        //riempimento
    }
    public static ClienteBean toBean(Cliente cliente) {
        ClienteBean bean = new ClienteBean();
        bean.setUsername(cliente.getUsername());
        bean.setNome(cliente.getNome());
        bean.setCognome(cliente.getCognome());
        bean.setPassword(cliente.getPassword());
        bean.setEmail(cliente.getEmail());
        bean.setPartitaIva(cliente.getPartitaIva());
        bean.setIndirizzo(cliente.getIndirizzo());
        bean.setCivico(cliente.getCivico());
        bean.setCap(cliente.getCap());
        bean.setCitta(cliente.getCitta());
        return bean;
    }
}
