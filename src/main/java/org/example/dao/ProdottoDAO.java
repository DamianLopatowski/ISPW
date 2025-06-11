package org.example.dao;

import org.example.bean.ProdottoBean;
import org.example.model.Prodotto;

import java.util.List;

public interface ProdottoDAO {

    List<Prodotto> getAllProdotti();
    List<ProdottoBean> getAllProdottiBean();

    void saveProdotto(Prodotto prodotto);

    void riduciQuantita(int id, int quantita);

    void rimuoviProdotto(int id);
    void aggiornaQuantita(int id, int nuovaQuantita);

    Prodotto getProdottoById(int id);

    boolean isOnline();

    default List<Prodotto> getAll() {
        return getAllProdotti();
    }
    void aggiornaOrdinato(int id, boolean ordinato);

    default void salva(Prodotto prodotto) {
        saveProdotto(prodotto);
    }

    default void rimuovi(int id) {
        rimuoviProdotto(id);
    }
}