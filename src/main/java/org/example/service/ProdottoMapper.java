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

    public static Prodotto toModel(ProdottoBean bean) {
        return new Prodotto.Builder()
                .id(bean.getId())
                .nome(bean.getNome())
                .quantita(bean.getQuantita())
                .scaffale(bean.getScaffale())
                .codiceAbarre(bean.getCodiceAbarre())
                .soglia(bean.getSoglia())
                .prezzoAcquisto(bean.getPrezzoAcquisto())
                .prezzoVendita(bean.getPrezzoVendita())
                .categoria(bean.getCategoria())
                .immagine(bean.getImmagine())
                .build();
    }
}
