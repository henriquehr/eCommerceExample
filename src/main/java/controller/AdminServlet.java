
package controller;

import entity.Customer;
import entity.CustomerOrder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import session.CustomerFacade;
import session.CustomerOrderFacade;
import session.OrderManager;

@WebServlet(name="AdminServlet", 
            urlPatterns={"/admin/",
                         "/admin/viewOrders",
                         "/admin/viewCustomers",
                         "/admin/customerRecord",
                         "/admin/orderRecord",
                         "/admin/logout"})
@ServletSecurity(@HttpConstraint(
//    transportGuarantee = ServletSecurity.TransportGuarantee.CONFIDENTIAL,
    rolesAllowed = {"eCommerceAdmin"}))
public class AdminServlet extends HttpServlet {
   
    private Customer customer;
    private CustomerOrder order;
    private List customerList = new ArrayList();
    private List orderList = new ArrayList();

    @EJB
    private OrderManager orderManager;
    @EJB
    private CustomerFacade customerFacade;
    @EJB
    private CustomerOrderFacade customerOrderFacade;
    
    private void processViewCustomers(HttpServletRequest request) {
        this.customerList = this.customerFacade.findAll();
        request.setAttribute("customerList", this.customerList);
    }    
    
    private void processViewOrders(HttpServletRequest request) {
        this.orderList = this.customerOrderFacade.findAll();
        request.setAttribute("orderList", this.orderList);
    }
    
    private void processCustomerRecord(HttpServletRequest request) {
        String customerId = request.getQueryString();
        this.customer = this.customerFacade.find(Integer.parseInt(customerId));
        request.setAttribute("customerRecord", this.customer);
        this.order = this.customerOrderFacade.findByCustomer(customer);
        request.setAttribute("order", this.order);
    }
    
    private void processOrderRecord(HttpServletRequest request) {
        String orderId = request.getQueryString();
        Map<String, Object> orderMap = this.orderManager.getOrderDetails(Integer.parseInt(orderId));
        request.setAttribute("customer", orderMap.get("customer"));
        request.setAttribute("products", orderMap.get("products"));
        request.setAttribute("orderRecord", orderMap.get("orderRecord"));
        request.setAttribute("orderedProducts", orderMap.get("orderedProducts"));
    }
    
    private void processLogout(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        session = request.getSession();
        session.invalidate();
        response.sendRedirect("/eCommerce/admin/");
    }
    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        String userPath = request.getServletPath();
        
        if (userPath.equals("/admin/viewCustomers")) {
            this.processViewCustomers(request);
        }
        
        if (userPath.equals("/admin/viewOrders")) {
            this.processViewOrders(request);
        }
        
        if (userPath.equals("/admin/customerRecord")) {
            this.processCustomerRecord(request);
        }
        
        if (userPath.equals("/admin/orderRecord")) {
            this.processOrderRecord(request);
        }
        
        if (userPath.equals("/admin/logout")) {
            this.processLogout(session, request, response);
            return;
        }
        
        userPath = "/admin/index.jsp";
        try {
            request.getRequestDispatcher(userPath).forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    } 

  
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

}
