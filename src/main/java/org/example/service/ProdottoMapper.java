package org.example.service;

import org.example.bean.ProdottoBean;
import org.example.model.Prodotto;

public class ProdottoMapper {

    private ProdottoMapper() {
        //riempimento
    }
    public static ProdottoBean toBean(Prodotto prodotto) {
        ProdottoBean bean = new ProdottoBean();
        bean.setId(prodotto.getId());
        bean.setNome(prodotto.getNome());
        bean.setQuantita(prodotto.getQuantita());
        bean.setScaffale(prodotto.getScaffale());
        bean.setCodiceAbarre(prodotto.getCodiceAbarre());
        bean.setSoglia(prodotto.getSoglia());
        bean.setPrezzoAcquisto(prodotto.getPrezzoAcquisto());
        bean.setPrezzoVendita(prodotto.getPrezzoVendita());
        bean.setCategoria(prodotto.getCategoria());
        bean.setImmagine(prodotto.getImmagine());
        return bean;
    }

}
