package Week_8;

public class Main {
    public static void main(String[] args) {

        // Exercise 1
        System.out.println("=== LinkedQueue ===");
        QueueADT queue = new LinkedQueue();
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        System.out.println(queue.dequeue()); 
        System.out.println(queue.dequeue()); 
        System.out.println(queue.size());   
        System.out.println(queue.isEmpty());

        // Exercise 2
        System.out.println("\n=== OverdraftAccount ===");
        OverdraftAccount acc = new OverdraftAccount();
        acc.deposit(100);
        acc.withdraw(50);
        acc.withdraw(400); 
        acc.withdraw(200); 
        System.out.println("Balance: " + acc.getBalance());
    }
}