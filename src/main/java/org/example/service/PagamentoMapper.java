package org.example.service;

import org.example.bean.PagamentoBean;
import org.example.model.Pagamento;

public class PagamentoMapper {

    private PagamentoMapper() {
        //riempimento
    }

    public static PagamentoBean toBean(Pagamento pagamento) {
        PagamentoBean bean = new PagamentoBean();
        bean.setClienteUsername(pagamento.getClienteUsername());
        bean.setImporto(pagamento.getImporto());
        bean.setDataPagamento(pagamento.getDataPagamento());
        return bean;
    }

    public static Pagamento toModel(PagamentoBean bean) {
        Pagamento pagamento = new Pagamento();
        pagamento.setClienteUsername(bean.getClienteUsername());
        pagamento.setImporto(bean.getImporto());
        pagamento.setDataPagamento(bean.getDataPagamento());
        return pagamento;
    }
}
