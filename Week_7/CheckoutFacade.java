package Week_7;

import java.time.LocalDateTime;



class Inventory {
    boolean checkStock(String productId) { return true; }
    void reserve(String productId) { System.out.println("Reserved " + productId); }
    void release(String productId) { System.out.println("Released " + productId); }
}

class Payment {
    boolean charge(String userId, double amount) { return true; }
    void refund(String userId, double amount) { System.out.println("Refunded " + amount); }
}

class Shipping {
    String createLabel(String address) { return "TRK" + System.currentTimeMillis(); }
    void schedulePickup(String label) { System.out.println("Pickup scheduled for " + label); }
    boolean isAvailable() { return true; }
}

class Email {
    void send(String to, String subject, String body) {
        System.out.println("\n[Email to " + to + "]\nSubject: " + subject + "\n" + body + "\n");
    }
}

// --- New Extension Subsystems from Exercise 2 ---

class TaxCalculator {
    public double computeTax(String state, double amount) {
        // Hardcoded: "CA" = 8%, else 0%
        if ("CA".equalsIgnoreCase(state)) {
            return amount * 0.08;
        }
        return 0.0;
    }
}

class Logger {
    public void log(String userId, boolean success) {
        System.out.println("[LOG] " + LocalDateTime.now() + " | User: " + userId + " | Success: " + success);
    }
}

// --- Result Class (from Screenshot 2026-05-02 at 9.35.34 PM.jpg) ---

class OrderResult {
    private final boolean success;
    private final String trackingNumber;
    private final String message;

    public OrderResult(boolean success, String trackingNumber, String message) {
        this.success = success;
        this.trackingNumber = trackingNumber;
        this.message = message;
    }

    @Override
    public String toString() {
        return "OrderResult [Success=" + success + ", Tracking=" + trackingNumber + ", Message=" + message + "]";
    }
}



public class CheckoutFacade {
    private final Inventory inventory;
    private final Payment payment;
    private final Shipping shipping;
    private final Email email;
    private final TaxCalculator taxCalculator;
    private final Logger logger;              

    public CheckoutFacade() {
        this.inventory = new Inventory();
        this.payment = new Payment();
        this.shipping = new Shipping();
        this.email = new Email();
        this.taxCalculator = new TaxCalculator();
        this.logger = new Logger();
    }

    public OrderResult checkout(String userId, String productId, double price, String address, String state) {
        boolean overallSuccess = false;
        String tracking = null;
        String msg;

        try {

            if (!inventory.checkStock(productId)) {
                msg = "Out of stock";
            } else {
                inventory.reserve(productId);


                if (!payment.charge(userId, price)) {
                    inventory.release(productId);
                    msg = "Payment failed";
                } else if (!shipping.isAvailable()) {

                    payment.refund(userId, price);
                    inventory.release(productId);
                    msg = "Shipping unavailable";
                } else {

                    tracking = shipping.createLabel(address);
                    shipping.schedulePickup(tracking);
                    

                    double tax = taxCalculator.computeTax(state, price);
                    double total = price + tax;


                    String body = String.format("Item: %s\nBase Price: $%.2f\nTax: $%.2f\nTotal: $%.2f", 
                                                productId, price, tax, total);
                    email.send(userId, "Order Receipt", body);
                    
                    overallSuccess = true;
                    msg = "Order successful";
                }
            }
        } catch (Exception e) {
            msg = "System error";
        }


        logger.log(userId, overallSuccess);

        return new OrderResult(overallSuccess, tracking, msg);
    }

    public static void main(String[] args) {
        CheckoutFacade store = new CheckoutFacade();
        
        // Test Case: User in CA (should have tax)
        System.out.println("--- Test 1: California User ---");
        store.checkout("student@pau.edu.ng", "laptop_01", 1000.00, "123 Lekki", "CA");

        // Test Case: User in NY (should have 0% tax)
        System.out.println("--- Test 2: Other State ---");
        store.checkout("user_02", "mouse_02", 50.00, "456 Victoria Island", "NY");
    }
}