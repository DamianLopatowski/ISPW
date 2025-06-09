package org.example.service;

import org.example.bean.OrdineBean;
import org.example.bean.ProdottoBean;
import org.example.model.Ordine;
import org.example.model.Prodotto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class OrdineMapper {

    private OrdineMapper() {
        //riempimento
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
        return bean;
    }

    public static Ordine toModel(OrdineBean bean) {
        Ordine ordine = new Ordine();
        ordine.setId(bean.getId());
        if (bean.getCliente() != null) {
            ordine.setCliente(bean.getCliente().toCliente());
        }
        ordine.setData(bean.getData() != null ? bean.getData() : LocalDateTime.now());
        if (bean.getProdotti() != null) {
            Map<Prodotto, Integer> prodottiModel = new HashMap<>();
            for (Map.Entry<ProdottoBean, Integer> entry : bean.getProdotti().entrySet()) {
                prodottiModel.put(entry.getKey().toProdotto(), entry.getValue());
            }
            ordine.setProdotti(prodottiModel);
        }
        ordine.setTotale(bean.getTotale());
        return ordine;
    }
}
