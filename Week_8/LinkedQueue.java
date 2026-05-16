package Week_8;

public class LinkedQueue implements QueueADT {

    private class Node {
        int data;
        Node next;
        Node(int data) { this.data = data; }
    }

    private Node head;
    private Node tail;
    private int count;

    @Override
    public void enqueue(int element) {
        Node newNode = new Node(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        count++;
    }

    @Override
    public int dequeue() {
        if (isEmpty()) throw new RuntimeException("Queue is empty");
        int value = head.data;
        head = head.next;
        if (head == null) tail = null;
        count--;
        return value;
    }

    @Override
    public boolean isEmpty() { return count == 0; }

    @Override
    public int size() { return count; }
}