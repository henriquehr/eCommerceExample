
package session;

import entity.OrderedProduct;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class OrderedProductFacade extends AbstractFacade<OrderedProduct> {

    @PersistenceContext(unitName = "ecommercePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrderedProductFacade() {
        super(OrderedProduct.class);
    }

    public List<OrderedProduct> findByOrderId(Object id) {
        return this.em.createNamedQuery("OrderedProduct.findByCustomerOrderId").setParameter("customerOrderId", id).getResultList();
    }

}
