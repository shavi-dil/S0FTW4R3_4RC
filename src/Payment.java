/**
 * Simulated payment validation for checkout.
 */
public class Payment {
    public static void validate(String cardHolder, String cardNumber) {
        if (cardHolder == null || cardHolder.trim().isEmpty()) {
            throw new IllegalArgumentException("Name on card cannot be blank.");
        }
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number cannot be blank.");
        }
        String digits = cardNumber.replaceAll("\\D", "");
        if (digits.length() != 16) {
            throw new IllegalArgumentException("Card number must be 16 digits.");
        }
    }

    public static boolean process(String cardHolder, String cardNumber) {
        validate(cardHolder, cardNumber);
        return true;
    }
}
