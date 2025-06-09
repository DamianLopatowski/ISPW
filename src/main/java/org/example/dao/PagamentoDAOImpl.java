package org.example.dao;

import org.example.model.Pagamento;
import org.example.controllerapplicativo.SessionController;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PagamentoDAOImpl implements PagamentoDAO {
    private static final Logger LOGGER = Logger.getLogger(PagamentoDAOImpl.class.getName());
    private final boolean isOnline;

    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public PagamentoDAOImpl(boolean isOnline) {
        this.isOnline = isOnline;
        loadDatabaseConfig();
    }

    private void loadDatabaseConfig() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            this.dbUrl = properties.getProperty("db.url");
            this.dbUsername = properties.getProperty("db.username");
            this.dbPassword = properties.getProperty("db.password");
            LOGGER.info("Configurazione database caricata per PagamentoDAOImpl.");
        } catch (IOException e) {
            LOGGER.severe("Errore caricamento config PagamentoDAOImpl: " + e.getMessage());
        }
    }

    @Override
    public void registraPagamento(Pagamento pagamento) {
        if (!isOnline) {
            SessionController.salvaPagamentoOffline(pagamento); // 🔄 Salvataggio in SessionController
            LOGGER.log(Level.INFO, "Pagamento salvato in RAM per {0}: €{1}",
                    new Object[]{pagamento.getClienteUsername(), pagamento.getImporto()});
            return;
        }

        String sql = "INSERT INTO pagamenti_cliente (cliente_username, importo) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pagamento.getClienteUsername());
            ps.setDouble(2, pagamento.getImporto());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                LOGGER.log(Level.INFO, "Pagamento registrato nel database: {0} → €{1}",
                        new Object[]{pagamento.getClienteUsername(), pagamento.getImporto()});
            } else {
                LOGGER.warning("Nessuna riga inserita per pagamento.");
            }

        } catch (SQLException e) {
            LOGGER.severe("Errore SQL durante salvataggio pagamento: " + e.getMessage());
        }
    }

    @Override
    public List<Pagamento> getPagamentiPerCliente(String username) {
        if (!isOnline) {
            LOGGER.log(Level.INFO, "Recupero pagamenti da RAM per: {0}", username);
            return SessionController.getPagamentiOfflinePer(username); //Recupero da SessionController
        }

        List<Pagamento> pagamenti = new ArrayList<>();
        String sql = "SELECT cliente_username, importo, data_pagamento FROM pagamenti_cliente WHERE cliente_username = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Pagamento p = new Pagamento();
                p.setClienteUsername(rs.getString("cliente_username"));
                p.setImporto(rs.getDouble("importo"));
                p.setDataPagamento(rs.getTimestamp("data_pagamento").toLocalDateTime());
                pagamenti.add(p);
            }

            LOGGER.log(Level.INFO, "{0} pagamenti recuperati per {1}", new Object[]{pagamenti.size(), username});

        } catch (SQLException e) {
            LOGGER.severe("Errore durante recupero pagamenti per cliente: " + e.getMessage());
        }

        return pagamenti;
    }
}
