package org.example.boundary;

import javafx.scene.Parent;
import org.example.bean.ProdottoBean;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface NegozioBoundary {

    enum ConfermaChoice {
        ANNULLA,
        MODIFICA_SPEDIZIONE,
        CONFERMA
    }

    Parent getRoot();
    void setOnInviaOrdine(Runnable action);
    void setOnLogout(Runnable action);
    void setOnProfilo(Runnable action);
    void setOnVisualizzaPagamenti(Runnable action);
    void renderProdotti(List<ProdottoBean> prodotti, BiConsumer<ProdottoBean, Integer> onAddToCart);
    void renderCarrello(Map<ProdottoBean, Integer> carrello,
                        Consumer<ProdottoBean> onPlus,
                        Consumer<ProdottoBean> onMinus,
                        Consumer<ProdottoBean> onRemove);

    void setTotale(double totale);

    void showInfo(String msg);

    Optional<ConfermaChoice> showConfermaOrdine(String riepilogo);
}
