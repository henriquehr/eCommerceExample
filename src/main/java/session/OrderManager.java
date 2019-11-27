
package session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entity.Customer;
import entity.CustomerOrder;
import entity.OrderedProduct;
import entity.OrderedProductPK;
import entity.Product;
import cart.ShoppingCart;
import cart.ShoppingCartItem;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrderManager {

    @PersistenceContext(unitName = "ecommercePU")
    private EntityManager em;
    @Resource
    private SessionContext context;
    @EJB
    private ProductFacade productFacade;
    @EJB
    private CustomerOrderFacade customerOrderFacade;
    @EJB
    private OrderedProductFacade orderedProductFacade;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int placeOrder(String name, String email, String phone, String address, String ccNumber, ShoppingCart cart) {
        try {
            Customer customer = this.addCustomer(name, email, phone, address, ccNumber);
            CustomerOrder order = this.addOrder(customer, cart);
            this.addOrderedItems(order, cart);
            return order.getId();
        } catch (Exception ex) {
            ex.printStackTrace();
            this.context.setRollbackOnly();
            return 0;
        }
    }

    private Customer addCustomer(String name, String email, String phone, String address, String ccNumber) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setCcNumber(ccNumber);

        this.em.persist(customer);

        return customer;
    }

    private CustomerOrder addOrder(Customer customer, ShoppingCart cart) {
        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setAmount(BigDecimal.valueOf(cart.getTotal()));

        Random random = new Random();
        int i = random.nextInt(9999999);
        order.setConfirmationNumber(i);

        this.em.persist(order);

        return order;
    }

    private void addOrderedItems(CustomerOrder order, ShoppingCart cart) {
        this.em.flush();
        
        List<ShoppingCartItem> items = cart.getItems();

        for (ShoppingCartItem scItem : items) {
            int productId = scItem.getProduct().getId();
            OrderedProductPK orderedProductPK = new OrderedProductPK();
            orderedProductPK.setCustomerOrderId(order.getId());
            orderedProductPK.setProductId(productId);
            OrderedProduct orderedItem = new OrderedProduct(orderedProductPK);
            orderedItem.setQuantity(scItem.getQuantity());

            this.em.persist(orderedItem);
        }
    }

    public Map<String, Object> getOrderDetails(int orderId) {
        Map<String, Object> orderMap = new HashMap<>();
        CustomerOrder order = this.customerOrderFacade.find(orderId);
        Customer customer = order.getCustomer();
        List<OrderedProduct> orderedProducts = this.orderedProductFacade.findByOrderId(orderId);
        List<Product> products = new ArrayList<>();
        for (OrderedProduct op : orderedProducts) {
            Product p = (Product) this.productFacade.find(op.getOrderedProductPK().getProductId());
            products.add(p);
        }
        orderMap.put("orderRecord", order);
        orderMap.put("customer", customer);
        orderMap.put("orderedProducts", orderedProducts);
        orderMap.put("products", products);

        return orderMap;
    }

}
