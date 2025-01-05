public class Prodotto {
    private String nome;
    private int quantità;
    private String scaffale;
    private String codiceBarre;
    private int soglia;
    private double prezzoAcquisto; // Nuovo attributo
    private double prezzoVendita;   // Nuovo attributo

    public Prodotto(String nome, int quantità, String scaffale, String codiceBarre, double prezzoAcquisto, double prezzoVendita) {
        this.nome = nome;
        this.quantità = quantità;
        this.scaffale = scaffale;
        this.codiceBarre = codiceBarre;
        this.soglia = 0; // Soglia di default
        this.prezzoAcquisto = prezzoAcquisto; // Imposta il prezzo di acquisto
        this.prezzoVendita = prezzoVendita;   // Imposta il prezzo di vendita
    }

    // Getter e Setter per tutti i campi inclusi i nuovi attributi
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantità() {
        return quantità;
    }

    public void setQuantità(int quantità) {
        this.quantità = quantità;
    }

    public String getScaffale() {
        return scaffale;
    }

    public void setScaffale(String scaffale) {
        this.scaffale = scaffale;
    }

    public String getCodiceBarre() {
        return codiceBarre;
    }

    public void setCodiceBarre(String codiceBarre) {
        this.codiceBarre = codiceBarre;
    }

    public int getSoglia() {
        return soglia;
    }

    public void setSoglia(int soglia) {
        this.soglia = soglia;
    }

    public double getPrezzoAcquisto() {
        return prezzoAcquisto; // Aggiunto
    }

    public void setPrezzoAcquisto(double prezzoAcquisto) {
        this.prezzoAcquisto = prezzoAcquisto; // Aggiunto
    }

    public double getPrezzoVendita() {
        return prezzoVendita; // Aggiunto
    }

    public void setPrezzoVendita(double prezzoVendita) {
        this.prezzoVendita = prezzoVendita; // Aggiunto
    }
}

