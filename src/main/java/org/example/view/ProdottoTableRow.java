package org.example.view;

import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.model.Prodotto;

import java.io.ByteArrayInputStream;

public class ProdottoTableRow {
    private final Prodotto prodottoOriginale;

    private final StringProperty nome;
    private final StringProperty quantita;
    private final StringProperty scaffale;
    private final StringProperty codiceAbarre;
    private final StringProperty soglia;
    private final StringProperty prezzoAcquisto;
    private final StringProperty prezzoVendita;
    private final StringProperty categoria;
    private final ObjectProperty<ImageView> immagine;

    public ProdottoTableRow(Prodotto prodotto) {
        this.prodottoOriginale = prodotto;

        this.nome = new SimpleStringProperty(prodotto.getNome());
        this.quantita = new SimpleStringProperty(String.valueOf(prodotto.getQuantita()));
        this.scaffale = new SimpleStringProperty(prodotto.getScaffale());
        this.codiceAbarre = new SimpleStringProperty(prodotto.getCodiceAbarre());
        this.soglia = new SimpleStringProperty(String.valueOf(prodotto.getSoglia()));
        this.prezzoAcquisto = new SimpleStringProperty(String.format("%.2f", prodotto.getPrezzoAcquisto()));
        this.prezzoVendita = new SimpleStringProperty(String.format("%.2f", prodotto.getPrezzoVendita()));
        this.categoria = new SimpleStringProperty(prodotto.getCategoria());

        ImageView imgView = new ImageView();
        if (prodotto.getImmagine() != null) {
            imgView.setImage(new Image(new ByteArrayInputStream(prodotto.getImmagine())));
        }
        imgView.setFitWidth(50);
        imgView.setFitHeight(50);
        imgView.setPreserveRatio(true);
        this.immagine = new SimpleObjectProperty<>(imgView);
    }

    public StringProperty nomeProperty() { return nome; }
    public StringProperty quantitaProperty() { return quantita; }
    public StringProperty scaffaleProperty() { return scaffale; }
    public StringProperty codiceAbarreProperty() { return codiceAbarre; }
    public StringProperty sogliaProperty() { return soglia; }
    public StringProperty prezzoAcquistoProperty() { return prezzoAcquisto; }
    public StringProperty prezzoVenditaProperty() { return prezzoVendita; }
    public StringProperty categoriaProperty() { return categoria; }
    public ObjectProperty<ImageView> immagineProperty() { return immagine; }

    public Prodotto getProdottoOriginale() { return prodottoOriginale; }
}
