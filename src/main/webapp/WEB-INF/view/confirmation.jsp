            <div id="singleColumn">
                <p id="confirmationText">
                    <strong>Your order has been successfully processed and will be delivered soon.</strong>
                    <br><br>
                    Please keep a note of you confirmation number:
                    <strong>${orderRecord.confirmationNumber}</strong>
                    <br>
                    If you have a query concerning your order, feel free to <a href="#">contact us</a>.
                    <br><br>
                    Thank you for choosing us.
                </p>

                <div class="summaryColumn">
                    <table id="orderSummaryTable" class="detailsTable">
                        <tr class="header">
                            <th colspan="3">order summary</th>
                        </tr>

                        <tr class="tableHeading">
                            <td>product</td>
                            <td>quantity</td>
                            <td>price</td>
                        </tr>

                        <c:forEach var="orderedProduct" items="${orderedProducts}" varStatus="iter">
                            <tr class="${((iter.index % 2) != 0) ? 'lightBlue' : 'white'}">
                                <td>${products[iter.index].name}</td>
                                <td class="quantityColumn">
                                    ${orderedProduct.quantity}
                                </td>
                                <td class="confirmationPriceColumn">
                                    &dollar; ${products[iter.index].price * orderedProduct.quantity}
                                </td>
                            </tr>
                        </c:forEach>

                        <tr class="lightBlue"><td colspan="3" style="padding: 0 20px"><hr></td></tr>

                        <tr class="lightBlue">
                            <td colspan="2" id="deliverySurchargeCellLeft"><strong>delivery surcharge:</strong></td>
                            <td id="deliverySurchargeCellRight">&dollar; ${initParam.deliverySurcharge}</td>
                        </tr>
                        
                        <tr class="lightBlue">
                            <td colspan="2" id="totalCellLeft"><strong>total:</strong></td>
                            <td id="totalCellRight">&dollar; ${orderRecord.amount}</td>
                        </tr>

                        <tr class="lightBlue"><td colspan="3" style="padding: 0 20px"><hr></td></tr>

                        <tr class="lightBlue">
                            <td colspan="3" id="dateProcessRow"><strong>date processed:</strong>
                                ${orderRecord.dateCreated}
                            </td>
                        </tr>
                    </table>
                </div>

                <div class="summaryColumn">
                    <table id="deliveryAddressTable" class="detailsTable">
                        <tr class=header>
                            <th colspan="3">delivery address</th>
                        </tr>
                        <tr>
                            <td colspan="3" class="lightBlue">
                                ${customer.name}
                                <br>
                                ${customer.address}
                                <br>
                                <hr>
                                <strong>email:</strong> ${customer.email}
                                <br>
                                <strong>phone:</strong> ${customer.phone}
                            </td>
                        </tr>
                    </table>

                </div>
            </div>