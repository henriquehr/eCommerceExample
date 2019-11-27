
package cart;

import entity.Product;

public class ShoppingCartItem {

    private short quantity;
    private Product product;

    public ShoppingCartItem(Product product) {
        if (product == null) {
            throw new NullPointerException("Product is null.");
        }
        this.quantity = 1;
        this.product = product;
    }

    public Product getProduct() {
        return this.product;
    }

    public short getQuantity() {
        return this.quantity;
    }

    public void setQuantity(short quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity is <= 0.");
        }
        this.quantity = quantity;
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        this.quantity--;
    }

    public double getTotal() {
        return this.getQuantity() * this.product.getPrice().doubleValue();
    }

}
