package org.example.dao;

import org.example.model.Prodotto;

import java.util.List;

public interface ProdottoDAO {
    List<Prodotto> getAllProdotti();
    void saveProdotto(Prodotto prodotto);
    void riduciQuantita(int id, int quantita);
}
