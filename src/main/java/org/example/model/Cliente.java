package org.example.model;

public class Cliente {
    private final String username;
    private final String nome;
    private final String cognome;
    private final String password;
    private final String email;
    private final String partitaIva;
    private final String indirizzo;
    private final String civico;
    private final String cap;
    private final String citta;

    //Costruttore Privato: Solo il Builder può creare un Cliente
    private Cliente(Builder builder) {
        this.username = builder.username;
        this.nome = builder.nome;
        this.cognome = builder.cognome;
        this.password = builder.password;
        this.email = builder.email;
        this.partitaIva = builder.partitaIva;
        this.indirizzo = builder.indirizzo;
        this.civico = builder.civico;
        this.cap = builder.cap;
        this.citta = builder.citta;
    }

    //Getter per tutti i campi
    public String getUsername() { return username; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getPartitaIva() { return partitaIva; }
    public String getIndirizzo() { return indirizzo; }
    public String getCivico() { return civico; }
    public String getCap() { return cap; }
    public String getCitta() { return citta; }

    //Classe Builder interna per costruire oggetti Cliente
    public static class Builder {
        private String username;
        private String nome;
        private String cognome;
        private String password;
        private String email;
        private String partitaIva;
        private String indirizzo;
        private String civico;
        private String cap;
        private String citta;

        //Metodi Fluent per impostare i campi
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }
        public Builder cognome(String cognome) {
            this.cognome = cognome;
            return this;
        }
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        public Builder partitaIva(String partitaIva) {
            this.partitaIva = partitaIva;
            return this;
        }
        public Builder indirizzo(String indirizzo) {
            this.indirizzo = indirizzo;
            return this;
        }
        public Builder civico(String civico) {
            this.civico = civico;
            return this;
        }
        public Builder cap(String cap) {
            this.cap = cap;
            return this;
        }
        public Builder citta(String citta) {
            this.citta = citta;
            return this;
        }

        //Metodo finale per creare l'oggetto Cliente
        public Cliente build() {
            return new Cliente(this);
        }
    }

}
