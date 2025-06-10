package org.example.dao;

import org.example.controllerapplicativo.SessionController;
import org.example.model.Cliente;
import org.example.model.Ordine;
import org.example.model.Prodotto;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class OrdineDAOImpl implements OrdineDAO {
    private static final Logger LOGGER = Logger.getLogger(OrdineDAOImpl.class.getName());

    private final boolean isOnlineMode;
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public OrdineDAOImpl(boolean isOnlineMode) {
        this.isOnlineMode = isOnlineMode;
        if (isOnlineMode) {
            loadDatabaseConfig();
        }
    }

    private void loadDatabaseConfig() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            this.dbUrl = properties.getProperty("db.url");
            this.dbUsername = properties.getProperty("db.username");
            this.dbPassword = properties.getProperty("db.password");
            LOGGER.info("Configurazione database caricata con successo!");
        } catch (IOException e) {
            LOGGER.severe("Errore nel caricamento del file di configurazione: " + e.getMessage());
        }
    }

    @Override
    public void salvaOrdine(Ordine ordine) {
        if (isOnlineMode) {
            try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                 PreparedStatement stmt = connection.prepareStatement(
                         "INSERT INTO ordini (cliente_username, data, totale) VALUES (?, ?, ?)",
                         Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, ordine.getCliente().getUsername());
                stmt.setTimestamp(2, Timestamp.valueOf(ordine.getData()));
                stmt.setDouble(3, ordine.getTotale());
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int ordineId = generatedKeys.getInt(1);
                        ordine.setId(ordineId);

                        // Inseriamo ordineId direttamente nella query per evitare il warning java:S6909
                        String queryProdotti = "INSERT INTO ordine_prodotti (ordine_id, prodotto_id, quantita) VALUES (" + ordineId + ", ?, ?)";
                        try (PreparedStatement psProdotti = connection.prepareStatement(queryProdotti)) {

                            for (Map.Entry<Prodotto, Integer> entry : ordine.getProdotti().entrySet()) {
                                psProdotti.setInt(1, entry.getKey().getId());    // prodotto_id
                                psProdotti.setInt(2, entry.getValue());          // quantita
                                psProdotti.addBatch();
                            }

                            psProdotti.executeBatch();
                        }

                        LOGGER.info("Ordine salvato con successo nel database.");
                    }
                }

            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Errore nel salvataggio dell ordine: {0}", e.getMessage());
            }
        } else {
            if (ordine.getCliente() == null || ordine.getCliente().getUsername() == null) {
                LOGGER.warning("Ordine offline senza cliente associato. Ignorato.");
                return;
            }

            SessionController.salvaOrdineOffline(ordine);

            LOGGER.log(Level.INFO,
                    "Ordine salvato in modalità offline per cliente: {0}", ordine.getCliente().getUsername());
        }
    }

    @Override
    public List<Ordine> getOrdiniPerCliente(String username) {
        if (!isOnlineMode) {
            return SessionController.getOrdiniOffline().stream()
                    .filter(o -> o.getCliente() != null && o.getCliente().getUsername().equalsIgnoreCase(username))
                    .collect(Collectors.toList());
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            Map<Integer, Ordine> ordiniMappa = recuperaOrdiniBase(conn, username);
            if (ordiniMappa.isEmpty()) return new ArrayList<>();

            Map<Integer, Map<Integer, Integer>> prodottiPerOrdine = recuperaProdottiPerOrdine(conn, ordiniMappa.keySet());
            Map<Integer, Prodotto> dettagliProdotti = recuperaDettagliProdotti(conn, prodottiPerOrdine);

            return costruisciOrdiniComposti(ordiniMappa, prodottiPerOrdine, dettagliProdotti);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante getOrdiniPerCliente", e);
            return new ArrayList<>();
        }
    }

    private Map<Integer, Ordine> recuperaOrdiniBase(Connection conn, String username) throws SQLException {
        Map<Integer, Ordine> ordini = new HashMap<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id, data, totale FROM ordini WHERE cliente_username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ordine ordine = new Ordine();
                ordine.setId(rs.getInt("id"));
                ordine.setData(rs.getTimestamp("data").toLocalDateTime());
                ordine.setTotale(rs.getDouble("totale"));
                ordine.setCliente(new Cliente.Builder().username(username).build());
                ordini.put(ordine.getId(), ordine);
            }
        }
        return ordini;
    }

    private Map<Integer, Map<Integer, Integer>> recuperaProdottiPerOrdine(Connection conn, Set<Integer> ordineIds) throws SQLException {
        Map<Integer, Map<Integer, Integer>> prodottiPerOrdine = new HashMap<>();
        if (ordineIds.isEmpty()) return prodottiPerOrdine;

        String query = "SELECT ordine_id, prodotto_id, quantita FROM ordine_prodotti WHERE ordine_id IN (" +
                String.join(",", Collections.nCopies(ordineIds.size(), "?")) + ")";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            int index = 1;
            for (Integer id : ordineIds) {
                ps.setInt(index++, id);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int ordineId = rs.getInt("ordine_id");
                int prodottoId = rs.getInt("prodotto_id");
                int quantita = rs.getInt("quantita");

                prodottiPerOrdine.computeIfAbsent(ordineId, k -> new HashMap<>()).put(prodottoId, quantita);
            }
        }
        return prodottiPerOrdine;
    }

    private Map<Integer, Prodotto> recuperaDettagliProdotti(Connection conn, Map<Integer, Map<Integer, Integer>> prodottiPerOrdine) throws SQLException {
        Set<Integer> prodottoIds = new HashSet<>();
        for (Map<Integer, Integer> prodotti : prodottiPerOrdine.values()) {
            prodottoIds.addAll(prodotti.keySet());
        }
        if (prodottoIds.isEmpty()) return Collections.emptyMap();

        Map<Integer, Prodotto> prodotti = new HashMap<>();
        String query = "SELECT id, nome, quantita, scaffale, codiceAbarre, soglia, prezzoAcquisto, prezzoVendita, categoria, immagine " +
                "FROM prodotti WHERE id IN (" + String.join(",", Collections.nCopies(prodottoIds.size(), "?")) + ")";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            int index = 1;
            for (Integer id : prodottoIds) {
                ps.setInt(index++, id);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Prodotto prodotto = new Prodotto.Builder()
                        .id(rs.getInt("id"))
                        .nome(rs.getString("nome"))
                        .quantita(rs.getInt("quantita"))
                        .scaffale(rs.getString("scaffale"))
                        .codiceAbarre(rs.getString("codiceAbarre"))
                        .soglia(rs.getInt("soglia"))
                        .prezzoAcquisto(rs.getDouble("prezzoAcquisto"))
                        .prezzoVendita(rs.getDouble("prezzoVendita"))
                        .categoria(rs.getString("categoria"))
                        .immagine(rs.getBytes("immagine"))
                        .build();
                prodotti.put(prodotto.getId(), prodotto);
            }
        }
        return prodotti;
    }

    private List<Ordine> costruisciOrdiniComposti(Map<Integer, Ordine> ordiniMappa,
                                                  Map<Integer, Map<Integer, Integer>> prodottiPerOrdine,
                                                  Map<Integer, Prodotto> prodottiDettagliati) {
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : prodottiPerOrdine.entrySet()) {
            Ordine ordine = ordiniMappa.get(entry.getKey());
            Map<Prodotto, Integer> prodotti = new HashMap<>();
            for (Map.Entry<Integer, Integer> prodottoEntry : entry.getValue().entrySet()) {
                Prodotto prodotto = prodottiDettagliati.get(prodottoEntry.getKey());
                if (prodotto != null) {
                    prodotti.put(prodotto, prodottoEntry.getValue());
                }
            }
            ordine.setProdotti(prodotti);
        }
        return new ArrayList<>(ordiniMappa.values());
    }

    @Override
    public void aggiornaStatoSpedizione(Ordine ordine) {
        if (!SessionController.getIsOnlineModeStatic()) {
            SessionController.aggiornaOrdineOffline(ordine);
            return;
        }

        String sql = "UPDATE ordini SET spedito = ?, codice_spedizione = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, ordine.isSpedito());
            ps.setString(2, ordine.getCodiceSpedizione());
            ps.setInt(3, ordine.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            LOGGER.severe("Errore aggiornamento stato spedizione: " + e.getMessage());
        }
    }

    @Override
    public List<Ordine> getTuttiGliOrdini() {
        if (!SessionController.getIsOnlineModeStatic()) return SessionController.getOrdiniOffline();

        Map<Integer, Ordine> ordiniMappa = new HashMap<>();

        String sql = "SELECT o.*, c.nome, c.cognome, c.email, c.indirizzo, c.civico, c.cap, c.citta " +
                "FROM ordini o " +
                "JOIN usercliente c ON o.cliente_username = c.username";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente.Builder()
                        .username(rs.getString("cliente_username"))
                        .nome(rs.getString("nome"))
                        .cognome(rs.getString("cognome"))
                        .email(rs.getString("email"))
                        .indirizzo(rs.getString("indirizzo"))
                        .civico(rs.getString("civico"))
                        .cap(rs.getString("cap"))
                        .citta(rs.getString("citta"))
                        .build();

                Ordine ordine = new Ordine();
                ordine.setId(rs.getInt("id"));
                ordine.setData(rs.getTimestamp("data").toLocalDateTime());
                ordine.setTotale(rs.getDouble("totale"));
                ordine.setSpedito(rs.getBoolean("spedito"));
                ordine.setCodiceSpedizione(rs.getString("codice_spedizione"));
                ordine.setCliente(cliente);

                ordiniMappa.put(ordine.getId(), ordine);
            }

            // ↪ Recupera i prodotti associati a ciascun ordine
            Map<Integer, Map<Integer, Integer>> prodottiPerOrdine = recuperaProdottiPerOrdine(conn, ordiniMappa.keySet());
            Map<Integer, Prodotto> prodottiDettagliati = recuperaDettagliProdotti(conn, prodottiPerOrdine);

            // ↪ Associa prodotti agli ordini
            for (Map.Entry<Integer, Map<Integer, Integer>> entry : prodottiPerOrdine.entrySet()) {
                Ordine ordine = ordiniMappa.get(entry.getKey());
                Map<Prodotto, Integer> prodotti = new HashMap<>();
                for (Map.Entry<Integer, Integer> prodottoEntry : entry.getValue().entrySet()) {
                    Prodotto prodotto = prodottiDettagliati.get(prodottoEntry.getKey());
                    if (prodotto != null) {
                        prodotti.put(prodotto, prodottoEntry.getValue());
                    }
                }
                if (ordine != null) ordine.setProdotti(prodotti);
            }

        } catch (SQLException e) {
            LOGGER.severe("Errore caricamento ordini: " + e.getMessage());
        }

        return new ArrayList<>(ordiniMappa.values());
    }

    @Override
    public List<String> getTuttiClientiConOrdini() {
        List<String> clienti = new ArrayList<>();

        if (!SessionController.getIsOnlineModeStatic()) {
            // Recupera i clienti dagli ordini salvati in RAM
            for (Ordine ordine : SessionController.getOrdiniOffline()) {
                if (ordine.getCliente() != null && ordine.getCliente().getUsername() != null) {
                    String username = ordine.getCliente().getUsername();
                    if (!clienti.contains(username)) {
                        clienti.add(username);
                    }
                }
            }
            return clienti;
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT cliente_username FROM ordini")) {

            while (rs.next()) {
                clienti.add(rs.getString("cliente_username"));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel recuperare i clienti con ordini", e);
        }

        return clienti;
    }

}
