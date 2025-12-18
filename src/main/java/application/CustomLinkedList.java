package application;

/**
 *
 * @author Kouresh Tayabaly
 * @description Custom implementation of Linked List for Coursework.
 */
public class CustomLinkedList<T> {

// Inner class representing a node in the linked list
    private class Node {

        T data; // The data type "T" means it could be any type of data
        Node next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private int size;

    public CustomLinkedList() {
        this.head = null;
        this.size = 0;
    }

    // Add element to the end of the list
    public void add(T data) {
        Node newNode = new Node(data);

        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    // Get element at index
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    // Remove element at index
    public boolean remove(int index) {
        if (index < 0 || index >= size) {
            return false;
        }

        if (index == 0) {
            head = head.next;
        } else {
            Node current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next;
        }
        size--;
        return true;
    }

    // Get size of the list
    public int size() {
        return size;
    }

    // Check if list is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Clear all elements
    public void clear() {
        head = null;
        size = 0;
    }
}
