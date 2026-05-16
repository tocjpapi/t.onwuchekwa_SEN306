package Week_8;

public interface QueueADT {
    void enqueue(int element);
    int dequeue();
    boolean isEmpty();
    int size();
}