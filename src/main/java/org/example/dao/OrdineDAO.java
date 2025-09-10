package org.example.dao;
import org.example.model.Ordine;

import java.util.List;

public interface OrdineDAO {

    void salvaOrdine(Ordine ordine);

    List<Ordine> getOrdiniPerCliente(String username);

    List<Ordine> getTuttiGliOrdini();
    List<String> getTuttiClientiConOrdini();


    void aggiornaStatoSpedizione(Ordine ordine);
}