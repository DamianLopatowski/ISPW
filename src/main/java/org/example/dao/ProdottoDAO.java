package org.example.dao;

import org.example.bean.ProdottoBean;
import org.example.model.Prodotto;

import java.util.List;

public interface ProdottoDAO {

    // Usato per mostrare tutti i prodotti (interfaccia cliente e gestione)
    List<Prodotto> getAllProdotti();
    List<ProdottoBean> getAllProdottiBean();

    // Usato per salvare un nuovo prodotto
    void saveProdotto(Prodotto prodotto);

    // Usato per ridurre la quantità di un prodotto
    void riduciQuantita(int id, int quantita);

    // Usato per rimuovere un prodotto
    void rimuoviProdotto(int id);
    void aggiornaQuantita(int id, int nuovaQuantita);


    // Usato per ottenere un singolo prodotto per ID
    Prodotto getProdottoById(int id);

    // Controllo modalità (online/offline)
    boolean isOnline();

    // ALTRI METODI RICHIESTI DAL CONTROLLER GESTIONE (alias)

    // Alias di getAllProdotti() richiesto da GestioneProdottiController
    default List<Prodotto> getAll() {
        return getAllProdotti();
    }
    void aggiornaOrdinato(int id, boolean ordinato);


    // Alias di saveProdotto() richiesto da GestioneProdottiController
    default void salva(Prodotto prodotto) {
        saveProdotto(prodotto);
    }

    // Alias di rimuoviProdotto() richiesto da GestioneProdottiController
    default void rimuovi(int id) {
        rimuoviProdotto(id);
    }
}
