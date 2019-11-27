package session;

import entity.Customer;
import entity.CustomerOrder;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class CustomerOrderFacade extends AbstractFacade<CustomerOrder> {

    @PersistenceContext(unitName = "ecommercePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerOrderFacade() {
        super(CustomerOrder.class);
    }

    @Override
    public CustomerOrder find(Object id) {
        CustomerOrder order = em.find(CustomerOrder.class, id);
        em.refresh(order);
        return order;
    }

//    @RolesAllowed("eCommerceAdmin")
    public CustomerOrder findByCustomer(Customer customer) {
        return (CustomerOrder) this.em.createNamedQuery(
                "CustomerOrder.findByCustomer").
                setParameter("customer", customer).
                getSingleResult();
    }

}
