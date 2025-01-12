package org.example;

public interface ProductActionDelegate {
    void onDeleteProduct(GestisciProdottiPage.Product product);
    void onEditProduct(GestisciProdottiPage.Product product);
}
