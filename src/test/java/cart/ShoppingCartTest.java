
package cart;

import entity.Product;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ShoppingCartTest {
    
    ShoppingCart instance;   
    Product product;
    
    public ShoppingCartTest() {
    }
    
    @BeforeEach
    public void setUp() {
        this.instance = new ShoppingCart();
        this.product = new Product(1, "productName", new BigDecimal(1.23), new Date(0));
        this.instance.addItem(this.product);
    }
    
    @AfterEach
    public void tearDown() {
    }


    @Test
    public void whenAddItem_shouldItemsContainsShoppingCartItemWithSameId() {
        Integer expected = this.product.getId();
        for (ShoppingCartItem scItem : this.instance.getItems()) {
            Integer actual = scItem.getProduct().getId();
            assertEquals(expected, actual);
        }
    }
   

    @Test
    public void whenUpdateWithPosiveQuantity_shouldShoppingCartItemHaveTheSameQuantity() {
        String quantity = "2";
        short expected = Short.parseShort(quantity);
        this.instance.update(this.product, quantity);
        for (ShoppingCartItem scItem : this.instance.getItems()) {
            if (scItem.getProduct().getId().equals(this.product.getId())) {
                short actual = scItem.getQuantity();
                assertEquals(expected, actual);
            }
        }
    }
    
    @Test
    public void whenUpdateWithZero_shouldRemoveTheItemWithSameId() {
        
        String quantity = "0";
        this.instance.update(this.product, quantity);
        Integer unexpected = this.product.getId();
        for (ShoppingCartItem scItem : this.instance.getItems()) {
            Integer actual = scItem.getProduct().getId();
            assertNotEquals(unexpected, actual);
        }
    }
    
    @Test
    public void whenUpdateWithNegativeQuantity_shouldNotChangeTheShoppingCartItemQuantity() {
        String quantity = "-1";
        short expected = 1;
        this.instance.update(this.product, quantity);
        for (ShoppingCartItem scItem : this.instance.getItems()) {
            if (scItem.getProduct().getId().equals(this.product.getId())) {
                short actual = scItem.getQuantity();
                assertEquals(expected, actual);
            }
        }
    }
    
    @Test
    public void whenUpdateWithEmptyQuantity_shouldNotChangeTheShoppingCartItemQuantity() {
        String quantity = "";
        short expected = 1;
        this.instance.update(this.product, quantity);
        for (ShoppingCartItem scItem : this.instance.getItems()) {
            if (scItem.getProduct().getId().equals(this.product.getId())) {
                short actual = scItem.getQuantity();
                assertEquals(expected, actual);
            }
        }
    }
    
    @Test
    public void whenUpdateWithNonNumber_shouldNotChangeTheShoppingCartItemQuantity() {
        String quantity = "a";
        short expected = 1;
        this.instance.update(this.product, quantity);
        for (ShoppingCartItem scItem : this.instance.getItems()) {
            if (scItem.getProduct().getId().equals(this.product.getId())) {
                short actual = scItem.getQuantity();
                assertEquals(expected, actual);
            }
        }
    }
    
    @Test
    public void whenUpdateWithNullProduct_shouldThrowNullPointerException() {
        String quantity = "2";
        this.product = null;
        assertThrows(NullPointerException.class, () -> {
            this.instance.update(this.product, quantity);
        });
    }

    @Test
    public void whenGetNumberOfItemsWithOneItem_shouldReturnTheSumOfEachItemQuantity() {
        int expected = 1;
        int actual = this.instance.getNumberOfItems();
        assertEquals(expected, actual);
    }

    @Test
    public void whenGetNumberOfItemsWithMoreThanOneItem_shouldReturnTheSumOfEachItemQuantity() {
        int expected = 3;
        this.instance.addItem(new Product(2, "productName", new BigDecimal(1.23), new Date(0)));
        this.instance.update(this.product, "2");
        int actual = this.instance.getNumberOfItems();
        assertEquals(expected, actual);
    }

    @Test
    public void whenGetSubtotalOfOneItem_shouldReturnTheSumOfItemQuantityTimesProductValue() {
        double expected = this.product.getPrice().doubleValue();
        double actual = this.instance.getSubtotal();
        assertEquals(expected, actual, 0.0000001);
    }

    @Test
    public void whenGetSubtotalOfMoreThanOneItem_shouldReturnTheSumOfItemQuantityTimesProductValue() {
        Product product1 = new Product(2, "productName", new BigDecimal(5.33), new Date(0));
        Product product2 = new Product(3, "productName", new BigDecimal(7.99), new Date(0));

        this.instance.addItem(product1);
        this.instance.addItem(product1);
        this.instance.addItem(product2);
        double expected = this.product.getPrice().add(product1.getPrice()).
                                                  add(product1.getPrice()).
                                                  add(product2.getPrice()).doubleValue();
        double actual = this.instance.getSubtotal();
        assertEquals(expected, actual, 0.0000001);
    }
    

    @Test
    public void whenCalculateTotalParamIsPosistive_shouldSetTotalToSubtotalPlusSurcharge() {
        String surcharge = "5.49";
        double subtotal = this.instance.getSubtotal();
        this.instance.calculateTotal(surcharge);
        double expected = subtotal + Double.parseDouble(surcharge);
        double actual = this.instance.getTotal();
        assertEquals(expected, actual);
    }

    @Test
    public void whenCalculateTotalParamIsNegative_shouldThrowIllegalArgumentException(){
        String surcharge = "-1.90";
        assertThrows(IllegalArgumentException.class, () -> {
            this.instance.calculateTotal(surcharge);
        });
    }
    
    @Test
    public void whenCalculateTotalParaIsEmpty_shouldThrowIllegalArgumentException() {
        String surcharge = "";
        assertThrows(IllegalArgumentException.class, () -> {
            this.instance.calculateTotal(surcharge);
        });
    }
    
    @Test
    public void whenCalculateTotalParaIsNotANumber_shouldThrowIllegalArgumentException() {
        String surcharge = "asd";
        assertThrows(IllegalArgumentException.class, () -> {
            this.instance.calculateTotal(surcharge);
        });
    }
    
    @Test
    public void whenClear_shouldClearItems() {
        this.instance.clear();
        boolean actual = this.instance.getItems().isEmpty();
        assertTrue(actual);
    }
    
    @Test
    public void whenClear_shouldSetTotalToZero() {
        this.instance.clear();
        double expected = 0.0;
        double actual = this.instance.getTotal();
        assertEquals(expected, actual);
    }
    
    @Test
    public void whenClear_shouldSetNumberOfItemsToZero() {
        this.instance.clear();
        int expected = 0;
        int actual = this.instance.getNumberOfItems();
        assertEquals(expected, actual);
    }
    
}
