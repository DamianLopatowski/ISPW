package org.example.dao;

import org.example.model.Prodotto;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class ProdottoDAOImpl implements ProdottoDAO {
    private static final Map<Integer, Prodotto> prodottiOffline = new HashMap<>();
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
            String query = "SELECT id, nome, quantita, scaffale, codiceAbarre, soglia, prezzoAcquisto, prezzoVendita, categoria, immagine FROM prodotti";
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
            prodottiOffline.put(prodotto.getId(), prodotto);
        }
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

    public boolean isOnline() {
        return isOnlineMode;
    }

}
