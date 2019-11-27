
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

    /**
     * Adds a <code>ShoppingCartItem</code> to the <code>ShoppingCart</code>'s
     * <code>items</code> list. If item of the specified <code>product</code>
     * already exists in shopping cart list, the quantity of that item is
     * incremented.
     *
     * @param product the <code>Product</code> that defines the type of shopping cart item
     * @see ShoppingCartItem
     */
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

    /**
     * Updates the <code>ShoppingCartItem</code> of the specified
     * <code>product</code> to the specified quantity. If '<code>0</code>'
     * is the given quantity, the <code>ShoppingCartItem</code> is removed
     * from the <code>ShoppingCart</code>'s <code>items</code> list.
     *
     * @param product the <code>Product</code> that defines the type of shopping cart item
     * @param quantity the number which the <code>ShoppingCartItem</code> is updated to
     * @see ShoppingCartItem
     */
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

    /**
     * Returns the list of <code>ShoppingCartItems</code>.
     *
     * @return the <code>items</code> list
     * @see ShoppingCartItem
     */
    public synchronized List<ShoppingCartItem> getItems() {
        return this.items;
    }

    /**
     * Returns the sum of quantities for all items maintained in shopping cart
     * <code>items</code> list.
     *
     * @return the number of items in shopping cart
     * @see ShoppingCartItem
     */
    public synchronized int getNumberOfItems() {
        this.numberOfItems = 0;
        for (ShoppingCartItem scItem : this.items) {
            this.numberOfItems += scItem.getQuantity();
        }
        return this.numberOfItems;
    }

    /**
     * Returns the sum of the product price multiplied by the quantity for all
     * items in shopping cart list. This is the total cost excluding the surcharge.
     *
     * @return the cost of all items times their quantities
     * @see ShoppingCartItem
     */
    public synchronized double getSubtotal() {
        double amount = 0;
        
        for (ShoppingCartItem scItem : this.items) {
            Product product = scItem.getProduct();
            amount += scItem.getQuantity() * product.getPrice().doubleValue();
        }
        return amount;
    }

    /**
     * Calculates the total cost of the order. This method adds the subtotal to
     * the designated surcharge and sets the <code>total</code> instance variable
     * with the result.
     *
     * @param surcharge the designated surcharge for all orders
     * @see ShoppingCartItem
     */
    public synchronized void calculateTotal(String surcharge) {
        double s = Double.parseDouble(surcharge);
        
        if (s < 0) {
            throw new IllegalArgumentException("Surcharge is negative.");
        }
        this.total = this.getSubtotal() + s;
    }

    /**
     * Returns the total cost of the order for the given
     * <code>ShoppingCart</code> instance.
     *
     * @return the cost of all items times their quantities plus surcharge
     */
    public synchronized double getTotal() {
        return this.total;
    }

    /**
     * Empties the shopping cart. All items are removed from the shopping cart
     * <code>items</code> list, <code>numberOfItems</code> and
     * <code>total</code> are reset to '<code>0</code>'.
     *
     * @see ShoppingCartItem
     */
    public synchronized void clear() {
        this.items.clear();
        this.numberOfItems = 0;
        this.total = 0;
    }

}
