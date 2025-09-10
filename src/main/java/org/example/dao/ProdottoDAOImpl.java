package org.example.dao;
import org.example.bean.ProdottoBean;
import org.example.model.Prodotto;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class ProdottoDAOImpl implements ProdottoDAO {
    private static final Map<Integer, Prodotto> prodottiOffline = new HashMap<>();
    private static int nextOfflineId = 1;

    private final boolean isOnlineMode;
    private Connection connection;

    public ProdottoDAOImpl(boolean isOnlineMode) {
        this.isOnlineMode = isOnlineMode;
        if (isOnlineMode) {
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream("config.properties")) {
                props.load(fis);
                connection = DriverManager.getConnection(
                        props.getProperty("db.url"),
                        props.getProperty("db.username"),
                        props.getProperty("db.password")
                );
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Prodotto> getAllProdotti() {
        if (isOnlineMode) {
            List<Prodotto> lista = new ArrayList<>();
            String query = "SELECT id, nome, quantita, scaffale, codiceAbarre, soglia, prezzoAcquisto, prezzoVendita, categoria, immagine, ordinato FROM prodotti";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    lista.add(new Prodotto.Builder()
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
                            .ordinato(rs.getBoolean("ordinato"))
                            .build());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return lista;
        } else {
            return new ArrayList<>(prodottiOffline.values());
        }
    }

    @Override
    public List<ProdottoBean> getAllProdottiBean() {
        List<Prodotto> prodotti = getAllProdotti();
        return prodotti.stream().map(p -> {
            ProdottoBean bean = new ProdottoBean();
            bean.setId(p.getId());
            bean.setNome(p.getNome());
            bean.setQuantita(p.getQuantita());
            bean.setSoglia(p.getSoglia());
            bean.setOrdinato(p.isOrdinato()); // Assicurati che Prodotto abbia isOrdinato()
            return bean;
        }).collect(Collectors.toList());
    }


    @Override
    public void aggiornaOrdinato(int id, boolean ordinato) {
        if (isOnlineMode) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE prodotti SET ordinato = ? WHERE id = ?")) {
                stmt.setBoolean(1, ordinato);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Prodotto p = prodottiOffline.get(id);
            if (p != null) {
                p.setOrdinato(ordinato); // mantiene in RAM, dimentica alla chiusura
            }
        }
    }

    @Override
    public void saveProdotto(Prodotto prodotto) {
        if (isOnlineMode) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO prodotti (nome, quantita, scaffale, codiceAbarre, soglia, prezzoAcquisto, prezzoVendita, categoria, immagine) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                stmt.setString(1, prodotto.getNome());
                stmt.setInt(2, prodotto.getQuantita());
                stmt.setString(3, prodotto.getScaffale());
                stmt.setString(4, prodotto.getCodiceAbarre());
                stmt.setInt(5, prodotto.getSoglia());
                stmt.setDouble(6, prodotto.getPrezzoAcquisto());
                stmt.setDouble(7, prodotto.getPrezzoVendita());
                stmt.setString(8, prodotto.getCategoria());
                stmt.setBytes(9, prodotto.getImmagine());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            aggiungiProdottoOffline(prodotto);
        }
    }

    private static void aggiungiProdottoOffline(Prodotto prodotto) {
        int newId = nextOfflineId++;
        Prodotto prodottoConId = new Prodotto.Builder()
                .id(newId)
                .nome(prodotto.getNome())
                .quantita(prodotto.getQuantita())
                .scaffale(prodotto.getScaffale())
                .codiceAbarre(prodotto.getCodiceAbarre())
                .soglia(prodotto.getSoglia())
                .prezzoAcquisto(prodotto.getPrezzoAcquisto())
                .prezzoVendita(prodotto.getPrezzoVendita())
                .categoria(prodotto.getCategoria())
                .immagine(prodotto.getImmagine())
                .build();
        prodottiOffline.put(newId, prodottoConId);
    }

    @Override
    public void riduciQuantita(int id, int quantita) {
        if (isOnlineMode) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE prodotti SET quantita = quantita - ? WHERE id = ?")) {
                stmt.setInt(1, quantita);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Prodotto p = prodottiOffline.get(id);
            if (p != null) {
                p.setQuantita(Math.max(0, p.getQuantita() - quantita));
            }
        }
    }

    @Override
    public void aggiornaQuantita(int id, int nuovaQuantita) {
        if (isOnlineMode) {
            try {
                Prodotto p = getProdottoById(id);
                if (p != null) {
                    boolean resetOrdinato = nuovaQuantita >= p.getSoglia();
                    try (PreparedStatement stmt = connection.prepareStatement(
                            "UPDATE prodotti SET quantita = ?, ordinato = ? WHERE id = ?")) {
                        stmt.setInt(1, nuovaQuantita);
                        stmt.setBoolean(2, !resetOrdinato && p.isOrdinato()); // ✅ corretto
                        stmt.setInt(3, id);
                        stmt.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Prodotto p = prodottiOffline.get(id);
            if (p != null) {
                p.setQuantita(nuovaQuantita);
                if (nuovaQuantita >= p.getSoglia()) {
                    p.setOrdinato(false);
                }
            }
        }
    }

    @Override
    public void rimuoviProdotto(int id) {
        if (isOnlineMode) {
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM prodotti WHERE id = ?")) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            prodottiOffline.remove(id);
        }
    }

    @Override
    public Prodotto getProdottoById(int id) {
        if (isOnlineMode) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, nome, quantita, scaffale, codiceAbarre, soglia, prezzoAcquisto, prezzoVendita, categoria, immagine, ordinato FROM prodotti WHERE id = ?")) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new Prodotto.Builder()
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
                                .ordinato(rs.getBoolean("ordinato"))
                                .build();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return prodottiOffline.get(id);
        }
    }

    @Override
    public boolean isOnline() {
        return isOnlineMode;
    }

    @Override
    public List<Prodotto> getAll() {
        return getAllProdotti();
    }

    @Override
    public void salva(Prodotto prodotto) {
        saveProdotto(prodotto);
    }

    @Override
    public void rimuovi(int id) {
        rimuoviProdotto(id);
    }
}