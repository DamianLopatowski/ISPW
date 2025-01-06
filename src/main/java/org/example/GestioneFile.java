package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestioneFile {
    private final String filePath;

    public GestioneFile(String filePath) {
        this.filePath = filePath;
    }

    public List<Prodotto> leggiProdotti() {
        List<Prodotto> prodotti = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] dati = linea.split(",");
                if (dati.length == 7) { // Modificato da 6 a 7 per includere la soglia
                    String nome = dati[0];
                    int quantita = Integer.parseInt(dati[1]);
                    String scaffale = dati[2];
                    String codiceBarre = dati[3];
                    double prezzoAcquisto = Double.parseDouble(dati[4]);
                    double prezzoVendita = Double.parseDouble(dati[5]);
                    int soglia = Integer.parseInt(dati[6]); // Leggi la soglia
                    Prodotto prodotto = new Prodotto(nome, quantita, scaffale, codiceBarre, prezzoAcquisto, prezzoVendita);
                    prodotto.setSoglia(soglia); // Imposta la soglia
                    prodotti.add(prodotto);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prodotti;
    }

    public void scriviProdotti(List<Prodotto> prodotti) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (Prodotto prodotto : prodotti) {
                pw.println(prodotto.getNome() + "," +
                        prodotto.getQuantita() + "," +
                        prodotto.getScaffale() + "," +
                        prodotto.getCodiceBarre() + "," +
                        prodotto.getPrezzoAcquisto() + "," +
                        prodotto.getPrezzoVendita() + "," + // Scrivi la soglia
                        prodotto.getSoglia()); // Aggiungi la soglia
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
