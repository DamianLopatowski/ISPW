package org.example;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ProdottoUtility {

    // Costruttore privato per evitare l'istanza della classe
    private ProdottoUtility() {
        // Costruttore privato
    }

    public static void aggiornaQuantitaProdotto(List<Prodotto> prodotti, ProdottoUpdateRequest request) {
        boolean prodottoEsistente = false;

        for (Prodotto prodotto : prodotti) {
            if (prodotto.getNome().equalsIgnoreCase(request.getNome()) || prodotto.getCodiceBarre().equalsIgnoreCase(request.getCodiceBarre())) {
                prodotto.setQuantita(prodotto.getQuantita() + request.getQuantita());
                if (request.isPrezzoAcquistoInserito()) {
                    prodotto.setPrezzoAcquisto(request.getPrezzoAcquisto());
                }
                if (request.isPrezzoVenditaInserito()) {
                    prodotto.setPrezzoVendita(request.getPrezzoVendita());
                }
                prodottoEsistente = true;
                break;
            }
        }

        if (!prodottoEsistente) {
            // Aggiungi un nuovo prodotto solo se non esiste
            Prodotto nuovoProdotto = new Prodotto(request.getNome(), request.getQuantita(), "", request.getCodiceBarre(), request.getPrezzoAcquisto(), request.getPrezzoVendita());
            prodotti.add(nuovoProdotto);
        }
    }

    public static Map<String, Integer> calcolaQuantitaTotale(List<Prodotto> prodotti) {
        Map<String, Integer> quantitaPerNome = new HashMap<>();

        for (Prodotto prodotto : prodotti) {
            quantitaPerNome.put(prodotto.getNome(), quantitaPerNome.getOrDefault(prodotto.getNome(), 0) + prodotto.getQuantita());
        }

        return quantitaPerNome;
    }

    public static boolean rimuoviQuantitaProdotto(List<Prodotto> prodotti, String nome, String codice, int quantita, String scaffale) {
        for (Prodotto prodotto : prodotti) {
            // Unire le due condizioni "if" in una sola
            if ((prodotto.getNome().equalsIgnoreCase(nome) || prodotto.getCodiceBarre().equalsIgnoreCase(codice)) && prodotto.getScaffale().equalsIgnoreCase(scaffale) && prodotto.getQuantita() >= quantita) {
                prodotto.setQuantita(prodotto.getQuantita() - quantita);
                if (prodotto.getQuantita() < 0) {
                    prodotto.setQuantita(0);
                }
                return true;
            }
        }
        return false;
    }


    public static void verificaSoglieProdotti(List<Prodotto> prodotti, Map<String, Integer> quantitaPerNome, StringBuilder avvisi) {
        for (Prodotto prodotto : prodotti) {
            if (quantitaPerNome.get(prodotto.getNome()) < prodotto.getSoglia()) {
                avvisi.append("Attenzione: ").append(prodotto.getNome()).append(" è sotto la soglia minima!\n");
            }
        }
    }
}
