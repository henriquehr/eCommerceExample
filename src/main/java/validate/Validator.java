
package validate;

import javax.servlet.http.HttpServletRequest;


public class Validator {

    public boolean validateQuantity(String productId, String quantity) {
        boolean errorFlag = false;
        if (!productId.isEmpty() && !quantity.isEmpty()) {
            int i = -1;
            try {
                i = Integer.parseInt(quantity);
            } catch (NumberFormatException ex) {
                System.out.println("User did not enter a number in the quantity field.");
            }
            if (i < 0 || i > 99) {
                errorFlag = true;
            }
        }
        return errorFlag;
    }
    
    public boolean validateForm(String name,
                                String email,
                                String phone,
                                String address,
                                String ccNumber,
                                HttpServletRequest request) {

        boolean errorFlag = false;
        if (name == null || name.equals("") || name.length() > 45){
            errorFlag = true;
            boolean nameError = true;
            request.setAttribute("nameError", nameError);
        }
        if (email == null || email.equals("") || !email.contains("@")) {
            errorFlag = true;
            boolean emailError = true;
            request.setAttribute("emailError", emailError);
        }
        if (phone == null || phone.equals("") || phone.length() < 9) {
            errorFlag = true;
            boolean phoneError = true;
            request.setAttribute("phoneError", phoneError);
        }
        if (address == null || address.equals("") || address.length() > 45) {
            errorFlag = true;
            boolean addressError = true;
            request.setAttribute("addressError", addressError);
        }
        if (ccNumber == null || ccNumber.equals("") || ccNumber.length() > 19) {
            errorFlag = true;
            boolean ccNumberError = true;
            request.setAttribute("ccNumberError", ccNumberError);
        }
        return errorFlag;
    }
    
}
