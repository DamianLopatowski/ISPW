package org.example.service;

import org.example.bean.OrdineBean;
import org.example.bean.ProdottoBean;
import org.example.model.Ordine;
import org.example.model.Prodotto;
import java.util.HashMap;
import java.util.Map;

public class OrdineMapper {

    private OrdineMapper() {
        // Costruttore privato per evitare istanziazione
    }
    public static OrdineBean toBean(Ordine ordine) {
        OrdineBean bean = new OrdineBean();
        bean.setId(ordine.getId());

        if (ordine.getCliente() != null) {
            bean.setCliente(ClienteMapper.toBean(ordine.getCliente()));
        }

        bean.setData(ordine.getData());

        if (ordine.getProdotti() != null) {
            Map<ProdottoBean, Integer> prodottiBean = new HashMap<>();
            for (Map.Entry<Prodotto, Integer> entry : ordine.getProdotti().entrySet()) {
                prodottiBean.put(ProdottoMapper.toBean(entry.getKey()), entry.getValue());
            }
            bean.setProdotti(prodottiBean);
        }

        bean.setTotale(ordine.getTotale());

        bean.setSpedito(ordine.isSpedito());
        bean.setCodiceSpedizione(ordine.getCodiceSpedizione());

        return bean;
    }
}
