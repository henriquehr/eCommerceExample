
package cart;

import entity.Product;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ShoppingCartItemTest {
    
    private ShoppingCartItem instance;
    private Product product;
    
    public ShoppingCartItemTest() {
    }
        
    @BeforeEach
    public void setUp() {
        this.product = new Product(1, "productName", new BigDecimal(1.23), new Date(0));
        this.instance = new ShoppingCartItem(this.product);
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void whenConstructorReceiveNullParam_shouldThrowNullPointerException() {
        this.product = null;
        assertThrows(NullPointerException.class, () -> {
            this.instance = new ShoppingCartItem(this.product);
        });
    }

    @Test
    public void whenSetQuantityIsPositive_shouldGetQuantityEqualsToQuantityParam() {
        short quantity = 10;
        this.instance.setQuantity(quantity);
        short expected = quantity;
        short actual = this.instance.getQuantity();
        assertEquals(expected, actual);
    }
    
    @Test
    public void whenSetQuantityIsZero_shouldThrowIllegalArgumentExpeption() {
        short quantity = 0;
        assertThrows(IllegalArgumentException.class, () -> {
            this.instance.setQuantity(quantity);
        });
    }

    @Test
    public void whenSetQuantityIsNegative_ThrowIllegalArgumentExpeption() {
        short quantity = -1;
        assertThrows(IllegalArgumentException.class, () -> {
            this.instance.setQuantity(quantity);
        });
    }
    
    @Test
    public void whenIncrementQuantity_shouldGetOldQuantityPlusOne() {
        short oldQuantity = this.instance.getQuantity();
        this.instance.incrementQuantity();
        short expected = (short) (oldQuantity + 1);
        short actual = this.instance.getQuantity();
        assertEquals(expected, actual);
    }
    
    @Test
    public void whenDecrementQuantity_shouldGetOldQuantityMinusOne() {
        short oldQuantity = this.instance.getQuantity();
        this.instance.decrementQuantity();
        short expected = (short) (oldQuantity - 1);
        short actual = this.instance.getQuantity();
        assertEquals(expected, actual);
    }
    
    @Test
    public void whenGetTotal_shouldGetProductPriceTimesQuantity() {
        short quantity = this.instance.getQuantity();
        BigDecimal price = this.product.getPrice();
        double expected = price.doubleValue() * quantity;
        double actual = instance.getTotal();
        assertEquals(expected, actual, 0.000000001);
    }
    
}
