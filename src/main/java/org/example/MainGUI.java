package org.example;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainGUI extends JFrame {
    private JTextField nomeField;
    private JTextField quantitaField;
    private JTextField scaffaleField;
    private JTextField codiceBarreField;
    private JTextField sogliaField; // Campo per la soglia
    private JTextField prezzoAcquistoField; // Nuovo campo per prezzo acquisto
    private JTextField prezzoVenditaField; // Nuovo campo per prezzo vendita
    private JTextArea avvisiArea; // Area di testo per gli avvisi
    private DefaultTableModel tableModel; // Modello per la tabella
    private JTable table; // Dichiarazione della tabella
    private GestioneFile gestioneFile;
    private List<Prodotto> prodotti;

    private JButton aggiornaPrezzoButton; // Pulsante per aggiornare il prezzo


    public MainGUI(String filePath, String sogliaFilePath) {
        gestioneFile = new GestioneFile(filePath);
        prodotti = gestioneFile.leggiProdotti();

        // Imposta la finestra
        setTitle("Gestione Prodotti");
        setSize(800, 500); // Modificato per adattarsi a più colonne
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Pannello di input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

// Inizializza i campi di input
        nomeField = new JTextField(15);
        quantitaField = new JTextField(5);
        scaffaleField = new JTextField(10);
        codiceBarreField = new JTextField(10);
        sogliaField = new JTextField(); // Inizializza il campo della soglia
        prezzoAcquistoField = new JTextField(10); // Nuovo campo
        prezzoVenditaField = new JTextField(10); // Nuovo campo


// Pulsanti esistenti
        JButton aggiungiButton = new JButton("Aggiungi Prodotto");
        JButton rimuoviButton = new JButton("Rimuovi Prodotto");
        JButton cercaButton = new JButton("Cerca Prodotto");
        JButton aggiornaSogliaButton = new JButton("Aggiorna Soglia");

// Pulsanti aggiuntivi vuoti
        JButton aggiornaPrezzoButton = new JButton("Aggiorna Prezzo");
        JButton vuoto2Button = new JButton("2");
        JButton vuoto3Button = new JButton("3");
        JButton vuoto4Button = new JButton("4");

// Configura il layout per i pulsanti
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Quantità:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(quantitaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Scaffale:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(scaffaleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Codice a barre:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(codiceBarreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Soglia per avviso:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(sogliaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("Prezzo Acquisto:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(prezzoAcquistoField, gbc); // Aggiungi campo prezzo acquisto

        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(new JLabel("Prezzo Vendita:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(prezzoVenditaField, gbc); // Aggiungi campo prezzo vendita

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(aggiungiButton, gbc);
        gbc.gridy = 8;
        inputPanel.add(rimuoviButton, gbc);
        gbc.gridy = 9;
        inputPanel.add(cercaButton, gbc);
        gbc.gridy = 10;
        inputPanel.add(aggiornaSogliaButton, gbc); // Aggiungi il pulsante per aggiornare la soglia

// Aggiungi i nuovi pulsanti a destra
        gbc.gridx = 2; // Sposta nella terza colonna
        gbc.gridwidth = 1; // Resetta la larghezza della griglia
        gbc.gridy = 7;
        inputPanel.add(aggiornaPrezzoButton, gbc);
        gbc.gridy = 8;
        inputPanel.add(vuoto2Button, gbc);
        gbc.gridy = 9;
        inputPanel.add(vuoto3Button, gbc);
        gbc.gridy = 10;
        inputPanel.add(vuoto4Button, gbc);
        // Aggiungi il pulsante per aggiornare la soglia

        add(inputPanel, BorderLayout.NORTH);

        // Crea la tabella
        String[] columnNames = {"Nome", "Quantità", "Scaffale", "Codice a barre", "Prezzo Acquisto", "Prezzo Vendita"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendere le celle non modificabili
            }
        };
        table = new JTable(tableModel) { // Assegna alla variabile d'istanza
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                String nomeProdotto = (String) getValueAt(row, 0);
                int totaleQuantità = getTotalQuantityByName(nomeProdotto);

                if (totaleQuantità < getThresholdForProduct(nomeProdotto)) {
                    c.setBackground(Color.RED); // Colora di rosso se sotto soglia
                    c.setForeground(Color.WHITE); // Testo bianco per contrasto
                } else {
                    c.setBackground(Color.WHITE); // Colore normale se non sotto soglia
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Permetti solo una selezione alla volta
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Area di avvisi
        avvisiArea = new JTextArea(5, 30);
        avvisiArea.setEditable(false);
        add(new JScrollPane(avvisiArea), BorderLayout.SOUTH);

        // Aggiungi i listener ai pulsanti
        aggiungiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String scaffale = scaffaleField.getText();
                String codiceBarre = codiceBarreField.getText();
                int quantità;

                // Valida la quantità
                try {
                    quantità = Integer.parseInt(quantitaField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Inserisci una quantità valida!");
                    return;
                }

                // Valida il prezzo di acquisto e vendita
                double prezzoAcquisto;
                double prezzoVendita;
                try {
                    prezzoAcquisto = Double.parseDouble(prezzoAcquistoField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Inserisci un prezzo di acquisto valido!");
                    return;
                }
                try {
                    prezzoVendita = Double.parseDouble(prezzoVenditaField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Inserisci un prezzo di vendita valido!");
                    return;
                }

                // Cerca il prodotto per nome e scaffale
                boolean prodottoEsistente = false;

                for (Prodotto prodotto : prodotti) {
                    if ((prodotto.getNome().equalsIgnoreCase(nome) || prodotto.getCodiceBarre().equalsIgnoreCase(codiceBarre))
                            && prodotto.getScaffale().equalsIgnoreCase(scaffale)) {
                        prodotto.setQuantita(prodotto.getQuantita() + quantità); // Aggiungi la quantità
                        prodottoEsistente = true;
                        break; // Esci dal ciclo
                    }
                }

                if (!prodottoEsistente) {
                    // Aggiungi un nuovo prodotto
                    Prodotto nuovoProdotto = new Prodotto(nome, quantità, scaffale, codiceBarre, prezzoAcquisto, prezzoVendita);
                    // Imposta la soglia se fornita
                    try {
                        int soglia = Integer.parseInt(sogliaField.getText());
                        nuovoProdotto.setSoglia(soglia);
                    } catch (NumberFormatException ex) {
                        // La soglia rimane a 0 se non è valida
                    }
                    prodotti.add(nuovoProdotto); // Aggiungi il nuovo prodotto
                }

                gestioneFile.scriviProdotti(prodotti); // Scrivi i prodotti nel file
                updateProdotti(); // Aggiorna la tabella
                clearFields(); // Pulisci i campi
                verificaAvvisi(); // Verifica avvisi
            }
        });

        rimuoviButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String codice = codiceBarreField.getText();
                String scaffale = scaffaleField.getText();
                int quantità;

                try {
                    quantità = Integer.parseInt(quantitaField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Inserisci una quantità valida!");
                    return;
                }

                boolean prodottoTrovato = false;

                // Cerca il prodotto per nome o codice e scaffale
                for (Prodotto prodotto : prodotti) {
                    if ((prodotto.getNome().equalsIgnoreCase(nome) || prodotto.getCodiceBarre().equalsIgnoreCase(codice))
                            && prodotto.getScaffale().equalsIgnoreCase(scaffale)) {
                        prodottoTrovato = true;
                        if (prodotto.getQuantita() >= quantità) {
                            prodotto.setQuantita(prodotto.getQuantita() - quantità);
                            if (prodotto.getQuantita() < 0) {
                                prodotto.setQuantita(0); // Non dovrebbe mai succedere
                            }
                            break; // Esci dal ciclo
                        } else {
                            JOptionPane.showMessageDialog(null, "Quantità da rimuovere superiore alla quantità disponibile!");
                            return; // Esci se non c'è abbastanza quantità
                        }
                    }
                }
                if (!prodottoTrovato) {
                    JOptionPane.showMessageDialog(null, "Prodotto non trovato!");
                } else {
                    // Salva i cambiamenti nel file
                    gestioneFile.scriviProdotti(prodotti);
                    updateProdotti(); // Aggiorna la tabella
                    clearFields(); // Pulisci i campi
                    verificaAvvisi(); // Verifica avvisi
                }
            }
        });

        cercaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String codice = codiceBarreField.getText();
                StringBuilder risultati = new StringBuilder();

                // Cerca il prodotto per nome o codice a barre
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
        });

        aggiornaSogliaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String codice = codiceBarreField.getText();

                // Verifica se il prodotto esiste per nome o codice
                for (Prodotto prodotto : prodotti) {
                    if (prodotto.getNome().equalsIgnoreCase(nome) || prodotto.getCodiceBarre().equalsIgnoreCase(codice)) {
                        // Aggiorna la soglia per questo prodotto
                        try {
                            int nuovaSoglia = Integer.parseInt(sogliaField.getText());
                            prodotto.setSoglia(nuovaSoglia); // Imposta la nuova soglia
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Inserisci una soglia valida!");
                            return;
                        }
                    }
                }

                gestioneFile.scriviProdotti(prodotti); // Scrivi i prodotti nel file
                updateProdotti(); // Aggiorna la tabella
                clearFields(); // Pulisci i campi
                verificaAvvisi(); // Verifica avvisi
            }
        });

        aggiornaPrezzoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String codice = codiceBarreField.getText();

                // Inizializza le variabili per i prezzi
                double nuovoPrezzoAcquisto = 0;
                double nuovoPrezzoVendita = 0;
                boolean prezzoAcquistoAggiornato = false;
                boolean prezzoVenditaAggiornato = false;

                // Aggiorna il prezzo di acquisto se il campo non è vuoto
                try {
                    if (!prezzoAcquistoField.getText().isEmpty()) {
                        nuovoPrezzoAcquisto = Double.parseDouble(prezzoAcquistoField.getText());
                        prezzoAcquistoAggiornato = true;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Inserisci un prezzo di acquisto valido!");
                    return;
                }

                // Aggiorna il prezzo di vendita se il campo non è vuoto
                try {
                    if (!prezzoVenditaField.getText().isEmpty()) {
                        nuovoPrezzoVendita = Double.parseDouble(prezzoVenditaField.getText());
                        prezzoVenditaAggiornato = true;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Inserisci un prezzo di vendita valido!");
                    return;
                }

                // Cerca il prodotto per nome o codice
                for (Prodotto prodotto : prodotti) {
                    if (prodotto.getNome().equalsIgnoreCase(nome) || prodotto.getCodiceBarre().equalsIgnoreCase(codice)) {
                        // Aggiorna i prezzi corrispondenti
                        if (prezzoAcquistoAggiornato) {
                            prodotto.setPrezzoAcquisto(nuovoPrezzoAcquisto);
                        }
                        if (prezzoVenditaAggiornato) {
                            prodotto.setPrezzoVendita(nuovoPrezzoVendita);
                        }
                        break; // Esci dal ciclo dopo aver aggiornato i prezzi
                    }
                }

                gestioneFile.scriviProdotti(prodotti); // Scrivi i prodotti nel file
                updateProdotti(); // Aggiorna la tabella
                clearFields(); // Pulisci i campi
                verificaAvvisi(); // Verifica avvisi
            }
        });

        // Aggiungi un listener alla tabella per selezionare un prodotto
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        nomeField.setText((String) tableModel.getValueAt(selectedRow, 0));
                        quantitaField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 1)));
                        scaffaleField.setText((String) tableModel.getValueAt(selectedRow, 2));
                        codiceBarreField.setText((String) tableModel.getValueAt(selectedRow, 3));
                        prezzoAcquistoField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 4))); // Aggiunto
                        prezzoVenditaField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 5))); // Aggiunto
                    }
                }
            }
        });

        updateProdotti(); // Carica i prodotti all'avvio
        verificaAvvisi(); // Verifica gli avvisi iniziali
    }

    private void updateProdotti() {
        tableModel.setRowCount(0); // Pulisce il modello della tabella
        prodotti.forEach(this::aggiungiRigaTabella);
        table.repaint(); // Rinfresca la tabella per applicare i colori
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
        avvisiArea.setText(""); // Pulisci gli avvisi
        Map<String, Integer> quantitàPerNome = new HashMap<>(); // Mappa per aggregare le quantità

        // Aggrega le quantità per nome
        for (Prodotto prodotto : prodotti) {
            // Usa il nome per aggregare
            quantitàPerNome.put(prodotto.getNome(),
                    quantitàPerNome.getOrDefault(prodotto.getNome(), 0) + prodotto.getQuantita());
        }

        // Controlla la soglia per ogni prodotto aggregato
        for (Prodotto prodotto : prodotti) {
            if (quantitàPerNome.get(prodotto.getNome()) < prodotto.getSoglia()) {
                avvisiArea.append("Attenzione: " + prodotto.getNome() + " è sotto la soglia minima!\n");
            }
        }
    }

    private void clearFields() {
        nomeField.setText("");
        quantitaField.setText("");
        scaffaleField.setText("");
        codiceBarreField.setText("");
        sogliaField.setText("");
        prezzoAcquistoField.setText(""); // Aggiunto
        prezzoVenditaField.setText(""); // Aggiunto
    }

    private int getTotalQuantityByName(String nome) {
        int totale = 0;
        for (Prodotto prodotto : prodotti) {
            if (prodotto.getNome().equalsIgnoreCase(nome)) {
                totale += prodotto.getQuantita();
            }
        }
        return totale;
    }

    private int getThresholdForProduct(String nome) {
        for (Prodotto prodotto : prodotti) {
            if (prodotto.getNome().equalsIgnoreCase(nome)) {
                return prodotto.getSoglia();
            }
        }
        return Integer.MAX_VALUE; // Valore di soglia molto alto se non trovato
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI("prodotti.txt", "soglie.txt").setVisible(true));
    }
}
