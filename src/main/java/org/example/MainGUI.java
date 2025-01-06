package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class MainGUI extends JFrame {
    private JTextField nomeField;
    private JTextField quantitaField;
    private JTextField scaffaleField;
    private JTextField codiceBarreField;
    private JTextField sogliaField;
    private JTextField prezzoAcquistoField;
    private JTextField prezzoVenditaField;
    private JTextArea avvisiArea;
    private DefaultTableModel tableModel;
    private JTable table;
    private transient GestioneFile gestioneFile;
    private transient List<Prodotto> prodotti;

    private JButton aggiungiButton;
    private JButton rimuoviButton;
    private JButton cercaButton;
    private JButton aggiornaSogliaButton;
    private JButton aggiornaPrezzoButton;

    public MainGUI(String filePath) {
        gestioneFile = new GestioneFile(filePath);
        prodotti = gestioneFile.leggiProdotti();

        tableModel = new DefaultTableModel(
                new String[]{"Nome", "Quantità", "Scaffale", "Codice a Barre", "Prezzo Acquisto", "Prezzo Vendita"},
                0
        );
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                String nomeProdotto = (String) getValueAt(row, 0);
                int totaleQuantita = ProdottoUtility.calcolaQuantitaTotale(prodotti).getOrDefault(nomeProdotto, 0);

                if (totaleQuantita < getThresholdForProduct(nomeProdotto)) {
                    c.setBackground(Color.RED);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                nomeField.setText((String) tableModel.getValueAt(table.getSelectedRow(), 0));
                quantitaField.setText(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 1)));
                scaffaleField.setText((String) tableModel.getValueAt(table.getSelectedRow(), 2));
                codiceBarreField.setText((String) tableModel.getValueAt(table.getSelectedRow(), 3));
                prezzoAcquistoField.setText(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 4)));
                prezzoVenditaField.setText(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 5)));
            }
        });

        setTitle("Gestione Prodotti");
        setSize(800, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        avvisiArea = new JTextArea(5, 30);
        avvisiArea.setEditable(false);
        add(new JScrollPane(avvisiArea), BorderLayout.SOUTH);

        setupListeners();
        updateProdotti();
        verificaAvvisi();
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        addFieldToPanel(inputPanel, gbc, "Nome:", nomeField = new JTextField(15), 0);
        addFieldToPanel(inputPanel, gbc, "Quantità:", quantitaField = new JTextField(5), 1);
        addFieldToPanel(inputPanel, gbc, "Scaffale:", scaffaleField = new JTextField(10), 2);
        addFieldToPanel(inputPanel, gbc, "Codice a barre:", codiceBarreField = new JTextField(10), 3);
        addFieldToPanel(inputPanel, gbc, "Soglia per avviso:", sogliaField = new JTextField(), 4);
        addFieldToPanel(inputPanel, gbc, "Prezzo Acquisto:", prezzoAcquistoField = new JTextField(10), 5);
        addFieldToPanel(inputPanel, gbc, "Prezzo Vendita:", prezzoVenditaField = new JTextField(10), 6);

        addButtonsToPanel(inputPanel, gbc);

        return inputPanel;
    }

    private void addFieldToPanel(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int gridY) {
        gbc.gridx = 0;
        gbc.gridy = gridY;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void addButtonsToPanel(JPanel panel, GridBagConstraints gbc) {
        aggiungiButton = new JButton("Aggiungi Prodotto");
        rimuoviButton = new JButton("Rimuovi Prodotto");
        cercaButton = new JButton("Cerca Prodotto");
        aggiornaSogliaButton = new JButton("Aggiorna Soglia");
        aggiornaPrezzoButton = new JButton("Aggiorna Prezzo");

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(aggiungiButton, gbc);

        gbc.gridy = 8;
        panel.add(rimuoviButton, gbc);

        gbc.gridy = 9;
        panel.add(cercaButton, gbc);

        gbc.gridy = 10;
        panel.add(aggiornaSogliaButton, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.gridy = 7;
        panel.add(aggiornaPrezzoButton, gbc);
    }

    private void setupListeners() {
        aggiungiButton.addActionListener(e -> aggiungiProdotto());
        rimuoviButton.addActionListener(e -> rimuoviProdotto());
        cercaButton.addActionListener(e -> cercaProdotto());
        aggiornaSogliaButton.addActionListener(e -> aggiornaSoglia());
        aggiornaPrezzoButton.addActionListener(e -> aggiornaPrezzo());
    }

    private void aggiungiProdotto() {
        String nome = nomeField.getText();
        String codiceBarre = codiceBarreField.getText();
        int quantita;

        try {
            quantita = Integer.parseInt(quantitaField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Inserisci una quantità valida!");
            return;
        }

        double prezzoAcquisto = 0;
        double prezzoVendita = 0;
        boolean prezzoAcquistoInserito = !prezzoAcquistoField.getText().isEmpty();
        boolean prezzoVenditaInserito = !prezzoVenditaField.getText().isEmpty();

        if (prezzoAcquistoInserito) {
            try {
                prezzoAcquisto = Double.parseDouble(prezzoAcquistoField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Inserisci un prezzo di acquisto valido!");
                return;
            }
        }

        if (prezzoVenditaInserito) {
            try {
                prezzoVendita = Double.parseDouble(prezzoVenditaField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Inserisci un prezzo di vendita valido!");
                return;
            }
        }

        ProdottoUtility.aggiornaQuantitaProdotto(prodotti, nome, codiceBarre, quantita, prezzoAcquistoInserito, prezzoAcquisto, prezzoVenditaInserito, prezzoVendita);
        gestioneFile.scriviProdotti(prodotti);
        updateProdotti();
        clearFields();
        verificaAvvisi();
    }

    private void rimuoviProdotto() {
        String nome = nomeField.getText();
        String codice = codiceBarreField.getText();
        String scaffale = scaffaleField.getText();
        int quantita;

        try {
            quantita = Integer.parseInt(quantitaField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Inserisci una quantità valida!");
            return;
        }

        if (!ProdottoUtility.rimuoviQuantitaProdotto(prodotti, nome, codice, quantita, scaffale)) {
            JOptionPane.showMessageDialog(null, "Prodotto non trovato o quantità insufficiente!");
        } else {
            gestioneFile.scriviProdotti(prodotti);
            updateProdotti();
            clearFields();
            verificaAvvisi();
        }
    }

    private void cercaProdotto() {
        String nome = nomeField.getText();
        String codice = codiceBarreField.getText();
        StringBuilder risultati = new StringBuilder();

        for (Prodotto prodotto : prodotti) {
            if (prodotto.getNome().equalsIgnoreCase(nome) || prodotto.getCodiceBarre().equalsIgnoreCase(codice)) {
                risultati.append("Prodotto: ").append(prodotto.getNome())
                        .append(", Quantità: ").append(prodotto.getQuantita())
                        .append(", Scaffale: ").append(prodotto.getScaffale())
                        .append(", Codice a barre: ").append(prodotto.getCodiceBarre())
                        .append(", Prezzo Acquisto: ").append(prodotto.getPrezzoAcquisto())
                        .append(", Prezzo Vendita: ").append(prodotto.getPrezzoVendita()).append("\n");
            }
        }

        if (risultati.length() == 0) {
            risultati.append("Prodotto non trovato!");
        }

        JOptionPane.showMessageDialog(null, risultati.toString());
    }

    private void aggiornaSoglia() {
        String nome = nomeField.getText();
        String codice = codiceBarreField.getText();

        for (Prodotto prodotto : prodotti) {
            if (prodotto.getNome().equalsIgnoreCase(nome) || prodotto.getCodiceBarre().equalsIgnoreCase(codice)) {
                try {
                    int nuovaSoglia = Integer.parseInt(sogliaField.getText());
                    prodotto.setSoglia(nuovaSoglia);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Inserisci una soglia valida!");
                    return;
                }
            }
        }

        gestioneFile.scriviProdotti(prodotti);
        updateProdotti();
        clearFields();
        verificaAvvisi();
    }

    private void aggiornaPrezzo() {
        String nome = nomeField.getText();
        String codice = codiceBarreField.getText();

        double nuovoPrezzoAcquisto = 0;
        double nuovoPrezzoVendita = 0;
        boolean prezzoAcquistoAggiornato = false;
        boolean prezzoVenditaAggiornato = false;

        try {
            if (!prezzoAcquistoField.getText().isEmpty()) {
                nuovoPrezzoAcquisto = Double.parseDouble(prezzoAcquistoField.getText());
                prezzoAcquistoAggiornato = true;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Inserisci un prezzo di acquisto valido!");
            return;
        }

        try {
            if (!prezzoVenditaField.getText().isEmpty()) {
                nuovoPrezzoVendita = Double.parseDouble(prezzoVenditaField.getText());
                prezzoVenditaAggiornato = true;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Inserisci un prezzo di vendita valido!");
            return;
        }

        for (Prodotto prodotto : prodotti) {
            if (prodotto.getNome().equalsIgnoreCase(nome) || prodotto.getCodiceBarre().equalsIgnoreCase(codice)) {
                if (prezzoAcquistoAggiornato) {
                    prodotto.setPrezzoAcquisto(nuovoPrezzoAcquisto);
                }
                if (prezzoVenditaAggiornato) {
                    prodotto.setPrezzoVendita(nuovoPrezzoVendita);
                }
                break;
            }
        }

        gestioneFile.scriviProdotti(prodotti);
        updateProdotti();
        clearFields();
        verificaAvvisi();
    }

    private void updateProdotti() {
        tableModel.setRowCount(0);
        prodotti.forEach(this::aggiungiRigaTabella);
        table.repaint();
    }

    private void aggiungiRigaTabella(Prodotto prodotto) {
        tableModel.addRow(new Object[]{
                prodotto.getNome(),
                prodotto.getQuantita(),
                prodotto.getScaffale(),
                prodotto.getCodiceBarre(),
                prodotto.getPrezzoAcquisto(),
                prodotto.getPrezzoVendita()
        });
    }

    private void verificaAvvisi() {
        avvisiArea.setText("");
        Map<String, Integer> quantitaPerNome = ProdottoUtility.calcolaQuantitaTotale(prodotti);
        StringBuilder avvisi = new StringBuilder();
        ProdottoUtility.verificaSoglieProdotti(prodotti, quantitaPerNome, avvisi);
        avvisiArea.setText(avvisi.toString());
    }

    private void clearFields() {
        nomeField.setText("");
        quantitaField.setText("");
        scaffaleField.setText("");
        codiceBarreField.setText("");
        sogliaField.setText("");
        prezzoAcquistoField.setText("");
        prezzoVenditaField.setText("");
    }

    private int getThresholdForProduct(String nome) {
        for (Prodotto prodotto : prodotti) {
            if (prodotto.getNome().equalsIgnoreCase(nome)) {
                return prodotto.getSoglia();
            }
        }
        return Integer.MAX_VALUE;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI("prodotti.txt").setVisible(true));
    }
}
