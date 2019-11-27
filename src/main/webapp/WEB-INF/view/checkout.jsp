
<c:set var="view" value="/checkout" scope="session"/>

            <div id="singleColumn">
                
                <h2><fmt:message key='checkout'/></h2>
                
                <p><fmt:message key='In order to purchase the items in your shopping cart, please provide us with the following information'/>:</p>
               
                <form id="checkoutForm" action="<c:url value='purchase'/>" method="post">
                    <table id="checkoutTable">
                        <c:if test="${!empty validationErrorFlag}">
                            <tr>
                                <td colspan="2" style="text-align: left;">
                                    <span class="error smalText"><fmt:message key='Please provide valid entries for the following field(s)'/>:
                                        <c:if test="${!empty nameError}">
                                            <br><span class="ident"><strong><fmt:message key='name'/></strong></span>
                                        </c:if>
                                        <c:if test="${!empty emailError}">
                                            <br><span class="ident"><strong>email</strong></span>
                                        </c:if>
                                        <c:if test="${!empty phoneError}">
                                            <br><span class="ident"><strong><fmt:message key='phone'/></strong></span>
                                        </c:if>
                                        <c:if test="${!empty addressError}">
                                                <br><span class="ident"><strong><fmt:message key='address'/></strong></span>
                                        </c:if>
                                        <c:if test="${!empty ccNumberError}">
                                                    <br><span class="ident"><strong><fmt:message key='credit card'/></strong></span>
                                        </c:if>
                                    </span>
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td><label for="name"><fmt:message key='name'/>:</label></td>
                            <td class="inputField">
                                <input type="text"
                                        size="31"
                                        maxlength="45"
                                        id="name"
                                        name="name"
                                        value="${param.name}">
                            </td>
                        </tr>
                        <tr>
                            <td><label for="email">email:</label></td>
                            <td class="inputField">
                                <input type="text"
                                        size="31"
                                        maxlength="45"
                                        id="email"
                                        name="email"
                                        value="${param.email}">
                            </td>
                        </tr>
                        <tr>
                            <td><label for="phone"><fmt:message key='phone'/>:</label></td>
                            <td class="inputField"> 
                                <input type="text"
                                        size="31"
                                        maxlength="16"
                                        id="phone"
                                        name="phone"
                                        value="${param.phone}">
                            </td>
                        </tr>
                        <tr>
                            <td><label for="address"><fmt:message key='address'/>:</address></label></td>
                            <td class="inputField">
                                <input type="text"
                                        size="31"
                                        maxlength="45"
                                        id="address"
                                        name="address"
                                        value="${param.address}">
                            </td>
                        </tr>
                        <tr>
                            <td><label for="creditcard"><fmt:message key='credit card number'/>:</label></td>
                            <td class="inputField">
                                <input type="text"
                                        size="31"
                                        maxlength="19"
                                        id="creditcard"
                                        name="creditcard"
                                        value="${param.creditcard}">
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <input type="submit" value="<fmt:message key='submit purchase'/>">
                            </td>
                        </tr>
                    </table>
                </form>
                <div id="infoBox">
                    <ul>
                        <li><fmt:message key='A'/> <fmt:formatNumber type="currency" 
                                                                     currencySymbol="&dollar;" 
                                                                     value="${initParam.deliverySurcharge}"/> 
                            <fmt:message key='delivery surcharge is applied to all purchase orders'/>.
                        </li>
                    </ul>

                    <table id="priceBox">
                        <tr>
                            <td>subtotal:</td>
                            <td class="checkoutPriceColumn">
                                <fmt:formatNumber type="currency" 
                                                  currencySymbol="&dollar;"
                                                  value="${cart.subtotal}"/>
                            </td>
                        </tr>
                        <tr>
                            <td><fmt:message key='delivery surcharge'/>:</td>
                            <td class="checkoutPriceColumn">
                                <fmt:formatNumber type="currency" 
                                                  currencySymbol="&dollar;"
                                                  value="${initParam.deliverySurcharge}"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="total">total:</td>
                            <td class="total checkoutPriceColumn">
                                <fmt:formatNumber type="currency" 
                                                  currencySymbol="&dollar;" 
                                                  value="${cart.total}"/>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>