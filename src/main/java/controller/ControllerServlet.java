
package controller;

import cart.ShoppingCart;
import entity.Category;
import entity.Product;
import java.io.IOException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import session.CategoryFacade;
import session.OrderManager;
import session.ProductFacade;
import validate.Validator;

@WebServlet(name = "Controller", 
            loadOnStartup = 1, 
            urlPatterns = {"/category", 
                            "/viewCart", 
                            "/checkout",
                            "/chooseLanguage",
                            "/addToCart", 
                            "/updateCart", 
                            "/purchase"})
public class ControllerServlet extends HttpServlet {
    
    private String surcharge;
    
    @EJB
    private CategoryFacade categoryFacade;
    @EJB
    private ProductFacade  productFacade;
    @EJB
    private OrderManager orderManager;

    
    private void processCategory(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer categoryId;
        try {
            categoryId = Integer.parseInt(request.getQueryString());
        } catch (NumberFormatException e) {
            categoryId = null;
        }
        if (categoryId != null) {
            Category selectedCategory = this.categoryFacade.find(categoryId);
            if (selectedCategory == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            session.setAttribute("selectedCategory", selectedCategory);
            Collection<Product> categoryProducts = selectedCategory.getProductCollection();
            session.setAttribute("categoryProducts", categoryProducts);
        }
    }
    
    private String processViewCart(HttpSession session, HttpServletRequest request) {
        String userPath = "/cart";
        String clear = request.getParameter("clear");
        if ((clear != null) && clear.equals("true")) {
            ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
            cart.clear();
        }
        return userPath;
    }
    
    private void processCheckout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart.getItems().isEmpty()) {
            this.goToUrl("/index.jsp", request, response);
            return;
        }
        cart.calculateTotal(this.surcharge);
    }
    
    private String processChooseLanguage(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        String userPath = null;
        String language = request.getParameter("language");
        String userView = (String) session.getAttribute("view");
        request.setAttribute("language", language);
        if ((userView != null) && (!userView.equals("/index"))) {
            userPath = userView;
        } else {
            this.goToUrl("/index.jsp", request, response);
        }
        return userPath;
    }
        
    private String processAddToCart(ShoppingCart cart, HttpSession session, HttpServletRequest request) {
        String userPath = "/category";
        String productId = request.getParameter("productId");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }
        if (!productId.isEmpty()) {
            Product product = this.productFacade.find(Integer.parseInt(productId));
            cart.addItem(product);
        }
        return userPath;
    }
    
    private String processUpdateCart(ShoppingCart cart, Validator validator, HttpServletRequest request) {
        String userPath = "/cart";
        String productId = request.getParameter("productId");
        String quantity = request.getParameter("quantity");
        boolean invalidEntry = validator.validateQuantity(productId, quantity);
        if (!invalidEntry) {
            Product product = this.productFacade.find(Integer.parseInt(productId));
            cart.update(product, quantity);
        }
        return userPath;
    }
    
    private String processPurchase(ShoppingCart cart, Validator validator, HttpSession session, HttpServletRequest request) {
        String userPath = "";
        String language = "";
        if (cart != null) {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String ccNumber = request.getParameter("creditcard");
            
            boolean validationErrorFlag = validator.validateForm(name, email, phone, address, ccNumber, request);
            if (validationErrorFlag) {
                request.setAttribute("validationErrorFlag", validationErrorFlag);
                userPath = "/checkout";
            } else {                        
                int orderId = this.orderManager.placeOrder(name, email, phone, address, ccNumber, cart);
                if (orderId != 0) {
                    Locale locale = (Locale) session.getAttribute("javax.servlet.jsp.jstl.fmt.locale.session");
                    
                    if (locale != null) {
                        language = (String) locale.getLanguage();
                    }
                    cart = null;
                    session.invalidate();
                    if (!language.isEmpty()) {
                        request.setAttribute("language", language);
                    }
                    Map<String, Object> orderMap = this.orderManager.getOrderDetails(orderId);
                    request.setAttribute("customer", orderMap.get("customer"));
                    request.setAttribute("products", orderMap.get("products"));
                    request.setAttribute("orderRecord", orderMap.get("orderRecord"));
                    request.setAttribute("orderedProducts", orderMap.get("orderedProducts"));
                    userPath = "/confirmation";
                } else {
                    userPath = "/checkout";
                    request.setAttribute("orderFailureFlag", true);
                }
            }
        }
        return userPath;
    }
    
    private void goToUrl(String url, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        this.surcharge = servletConfig.getServletContext().getInitParameter("deliverySurcharge");
        getServletContext().setAttribute("categories", categoryFacade.findAll());
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userPath = request.getServletPath();
        HttpSession session = request.getSession();
        
        switch (userPath) {
            case "/category":
                this.processCategory(session, request, response);
                break;
            case "/viewCart":
                userPath = this.processViewCart(session, request);
                break;
            case "/checkout":
                this.processCheckout(session, request, response);
                break;
            case "/chooseLanguage":
                userPath = this.processChooseLanguage(session, request, response);
                if (userPath == null) {
                    return;
                }
                break;
        }

        String url = "WEB-INF/view" + userPath + ".jsp";

        this.goToUrl(url, request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String userPath = request.getServletPath();
        HttpSession session = request.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        Validator validator = new Validator();

        switch (userPath) {
            case "/addToCart":
                userPath = this.processAddToCart(cart, session, request);
                break;
            case "/updateCart":
                userPath = this.processUpdateCart(cart, validator, request);
                break;
            case "/purchase":
                userPath = this.processPurchase(cart, validator, session, request);
                break;
            default:
                break;
        }

        String url = "WEB-INF/view" + userPath + ".jsp";

        this.goToUrl(url, request, response);
    }
}
