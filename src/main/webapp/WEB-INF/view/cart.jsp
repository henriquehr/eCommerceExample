            <c:set var="view" value="/cart" scope="session"/>

            <div id="singleColumn">
                
                <c:choose>
                    <c:when test="${cart.numberOfItems > 1}">
                        <p><fmt:message key='your shopping cart contains'/> ${cart.numberOfItems} <fmt:message key='items'/>.</p>
                    </c:when>
                    <c:when test="${cart.numberOfItems == 1}">
                        <p><fmt:message key='your shopping cart contains'/> ${cart.numberOfItems} item.</p>
                    </c:when>
                    <c:otherwise>
                        <p><fmt:message key='your shopping cart is empty'/>.</p>
                    </c:otherwise>
                </c:choose>
                
                <div id="actionBar">
                    <c:if test="${!empty cart && cart.numberOfItems != 0}">
                        <c:url var="url" value="viewCart">
                            <c:param name="clear" value="true"/>
                        </c:url>
                        <a href="${url}" class="bubble hMargin"><fmt:message key='clear cart'/></a>
                    </c:if>

                    <c:set var="value">
                        <c:choose>
                            <c:when test="${!empty selectedCategory}">
                                category
                            </c:when>
                            <c:otherwise>
                                index.jsp
                            </c:otherwise>
                        </c:choose>
                    </c:set>

                    <c:url var="url" value="${value}"/>
                    <a href="${url}" class="bubble hMargin"><fmt:message key='continue shopping'/></a>

                    <c:if test="${!empty cart && cart.numberOfItems != 0}">
                        <a href="<c:url value='checkout'/>" class="bubble hMargin"><fmt:message key='proceed to checkout'/> &#x279f;</a>
                    </c:if>
                </div>

                <c:if test="${!empty cart && cart.numberOfItems != 0}">

                    <h4 id="subtotal">subtotal: <fmt:formatNumber type="currency" 
                                                                  currencySymbol="&dollar;" 
                                                                  value="${cart.subtotal}"/>
                    </h4>
                    
                    <table id="cartTable">
                        <tr class="header">
                            <th><fmt:message key='product'/></th>
                            <th><fmt:message key='name'/></th>
                            <th><fmt:message key='price'/></th>
                            <th><fmt:message key='quantity'/></th>
                        </tr>

                        <c:forEach var="cartItem" items="${cart.items}" varStatus="iter">
                            
                            <c:set var="product" value="${cartItem.product}"/>
                            
                            <tr class="${((iter.index % 2) == 0) ? 'lightBlue' : 'white'}">
                                <td>
                                    <img src="${initParam.productImagePath}${product.name}.png" alt="${product.name}">
                                </td>
                                <td>${product.name}</td>
                                <td>
                                    <fmt:formatNumber type="currency" 
                                                      currencySymbol="&dollar;" 
                                                      value="${cartItem.total}"/>
                                    <br>
                                    <span class="smallText">( <fmt:formatNumber type="currency" 
                                                                        currencySymbol="&dollar;" 
                                                                        value="${product.price}"/> / <fmt:message key='unit'/> )
                                    </span>
                                </td>
                                <td>
                                    <form action="<c:url value='updateCart'/>" method="post">
                                        <input type="hidden"
                                                name="productId"
                                                value=${product.id}>
                                        <input type="text"
                                                maxlength="2"
                                                size="2"
                                                value="${cartItem.quantity}"
                                                name="quantity"
                                                style="margin:5px">
                                        <input type="submit"
                                                name="submit"
                                                value="<fmt:message key='update'/>">
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:if>
            </div>