
package cart;

import entity.Product;
import java.util.ArrayList;
import java.util.List;


public class ShoppingCart {

    private double total;
    private int numberOfItems;
    private List<ShoppingCartItem> items;

    public ShoppingCart() {
        this.total = 0;
        this.numberOfItems = 0;
        this.items = new ArrayList<>();
    }

    public synchronized void addItem(Product product) {
        boolean newItem = true;
        
        for (ShoppingCartItem scItem : this.items) {
            if (scItem.getProduct().getId().equals(product.getId())) {
                newItem = false;
                scItem.incrementQuantity();
            }
        }
        if (newItem) {
            ShoppingCartItem item = new ShoppingCartItem(product);
            this.items.add(item);
        }
    }

    public synchronized void update(Product product, String quantity) {
        if (product == null) {
            throw new NullPointerException("Product is null");
        }
        if (quantity.isBlank()) {
            return;
        }
        try {
            short temp = Short.parseShort(quantity);
        } catch (NumberFormatException e) {
            return;
        }
                
        short qty = Short.parseShort(quantity);
        ShoppingCartItem item = null;
        
        if (qty >= 0) {
            for (ShoppingCartItem scItem : this.items) {
                if (scItem.getProduct().getId().equals(product.getId())) {
                    if (qty != 0) {
                        scItem.setQuantity(qty);
                    } else {
                        item = scItem;
                        break;
                    }
                }
            }
            if (item != null) {
                this.items.remove(item);
            }
        }
    }

    public synchronized List<ShoppingCartItem> getItems() {
        return this.items;
    }

    public synchronized int getNumberOfItems() {
        this.numberOfItems = 0;
        for (ShoppingCartItem scItem : this.items) {
            this.numberOfItems += scItem.getQuantity();
        }
        return this.numberOfItems;
    }

    public synchronized double getSubtotal() {
        double amount = 0;
        
        for (ShoppingCartItem scItem : this.items) {
            Product product = scItem.getProduct();
            amount += scItem.getQuantity() * product.getPrice().doubleValue();
        }
        return amount;
    }

    public synchronized void calculateTotal(String surcharge) {
        double s = Double.parseDouble(surcharge);
        
        if (s < 0) {
            throw new IllegalArgumentException("Surcharge is negative.");
        }
        this.total = this.getSubtotal() + s;
    }

    public synchronized double getTotal() {
        return this.total;
    }

    public synchronized void clear() {
        this.items.clear();
        this.numberOfItems = 0;
        this.total = 0;
    }

}
