package org.example.controllergrafici;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import org.example.bean.ProdottoBean;
import org.example.controllerapplicativo.SessionController;
import org.example.dao.ProdottoDAO;
import org.example.dao.ProdottoDAOImpl;
import org.example.service.NavigationService;
import org.example.service.ProdottoRow;
import org.example.view.SogliaView;

import java.util.List;
import java.util.stream.Collectors;

public class SogliaController {
    private final SogliaView view;
    private final Stage stage;
    private final NavigationService navigationService;

    public SogliaController(Stage stage, NavigationService navigationService) {
        this.stage = stage;
        this.navigationService = navigationService;
        this.view = new SogliaView();
        setup();
    }

    private void setup() {
        ProdottoDAO dao = new ProdottoDAOImpl(SessionController.getIsOnlineModeStatic());

        List<ProdottoBean> prodotti = dao.getAllProdottiBean(); // usa i bean

        List<ProdottoRow> sottoSoglia = prodotti.stream()
                .filter(p -> p.getQuantita() < p.getSoglia())
                .map(p -> new ProdottoRow(
                        p.getId(),
                        p.getNome(),
                        p.getQuantita(),
                        p.getSoglia(),
                        p.isOrdinato()
                ))
                .collect(Collectors.toList());

        // Imposta la tabella come editabile
        view.getTabella().setEditable(true);

        // Aggiunta colonna checkbox "Ordinato"
        TableColumn<ProdottoRow, Boolean> ordinatoCol = new TableColumn<>("Ordinato");
        ordinatoCol.setCellValueFactory(cellData -> cellData.getValue().ordinatoProperty());
        ordinatoCol.setCellFactory(CheckBoxTableCell.forTableColumn(ordinatoCol));
        ordinatoCol.setEditable(true);
        view.getTabella().getColumns().add(ordinatoCol);

        view.getTabella().getItems().addAll(sottoSoglia);

        // Listener per aggiornare lo stato "ordinato"
        view.getTabella().getItems().forEach(row -> {
            row.ordinatoProperty().addListener((obs, oldVal, newVal) -> dao.aggiornaOrdinato(row.getId(), newVal));

            // Listener per rimuovere dalla tabella se la quantità supera la soglia
            row.quantitaProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.intValue() >= row.getSoglia()) {
                    view.getTabella().getItems().remove(row);
                }
            });
        });

        // Bottone "Indietro"
        view.getIndietroButton().setOnAction(e -> {
            Parent gestioneRoot = navigationService.navigateToGestioneView(
                    !SessionController.getIsOnlineModeStatic(),
                    SessionController.getIsInterfaccia1Static()
            );
            stage.setScene(new Scene(gestioneRoot, 1100, 700));
        });
    }

    public Parent getRoot() {
        return view.getRoot();
    }
}
