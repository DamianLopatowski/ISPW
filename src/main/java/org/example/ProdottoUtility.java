package org.example;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ProdottoUtility {

    public static void aggiornaQuantitaProdotto(List<Prodotto> prodotti, String nome, String codiceBarre, int quantita, boolean prezzoAcquistoInserito, double prezzoAcquisto, boolean prezzoVenditaInserito, double prezzoVendita) {
        boolean prodottoEsistente = false;

        for (Prodotto prodotto : prodotti) {
            if ((prodotto.getNome().equalsIgnoreCase(nome) || prodotto.getCodiceBarre().equalsIgnoreCase(codiceBarre))) {
                prodotto.setQuantita(prodotto.getQuantita() + quantita);
                if (prezzoAcquistoInserito) prodotto.setPrezzoAcquisto(prezzoAcquisto);
                if (prezzoVenditaInserito) prodotto.setPrezzoVendita(prezzoVendita);
                prodottoEsistente = true;
                break;
            }
        }

        if (!prodottoEsistente) {
            Prodotto nuovoProdotto = new Prodotto(nome, quantita, "", codiceBarre, prezzoAcquisto, prezzoVendita);
            prodotti.add(nuovoProdotto);
        }
    }

    public static boolean rimuoviQuantitaProdotto(List<Prodotto> prodotti, String nome, String codice, int quantita, String scaffale) {
        for (Prodotto prodotto : prodotti) {
            if ((prodotto.getNome().equalsIgnoreCase(nome) || prodotto.getCodiceBarre().equalsIgnoreCase(codice)) && prodotto.getScaffale().equalsIgnoreCase(scaffale)) {
                if (prodotto.getQuantita() >= quantita) {
                    prodotto.setQuantita(prodotto.getQuantita() - quantita);
                    if (prodotto.getQuantita() < 0) {
                        prodotto.setQuantita(0);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static Map<String, Integer> calcolaQuantitaTotale(List<Prodotto> prodotti) {
        Map<String, Integer> quantitaPerNome = new HashMap<>();

        for (Prodotto prodotto : prodotti) {
            quantitaPerNome.put(prodotto.getNome(), quantitaPerNome.getOrDefault(prodotto.getNome(), 0) + prodotto.getQuantita());
        }

        return quantitaPerNome;
    }

    public static void verificaSoglieProdotti(List<Prodotto> prodotti, Map<String, Integer> quantitaPerNome, StringBuilder avvisi) {
        for (Prodotto prodotto : prodotti) {
            if (quantitaPerNome.get(prodotto.getNome()) < prodotto.getSoglia()) {
                avvisi.append("Attenzione: ").append(prodotto.getNome()).append(" è sotto la soglia minima!\n");
            }
        }
    }
}
