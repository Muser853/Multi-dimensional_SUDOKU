import java.util.Objects;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<T> implements Iterable<T>, Queue<T>, Stack<T> {
    private int size;
    private Node<T> head;
    private Node<T> tail;

    @Override
    public Iterator<T> iterator() {
        return new LLIterator(this.head);
    }

    private class LLIterator implements Iterator<T> {
        private Node<T> next;

        public LLIterator(Node<T> head) {
            this.next = head;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = next.getData();
            next = next.getNext();
            return data;
        }
    }

    private static class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }

        public T getData() {
            return data;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }

    public LinkedList() {
        this.size = 0;
        head = null;
        tail = null;
    }


    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    public T remove() {
        if (isEmpty()) {
            return null; //Should return null, not throw exception for Queue interface
        }
        T item = head.getData();
        head = head.getNext();
        if (head == null) {
            tail = null;
        }
        size--;
        return item;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            return remove();
        } else if (index == size - 1) {
            return removeLast();
        } else {
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }
            T item = current.getNext().getData();
            current.setNext(current.getNext().getNext());
            size--;
            return item;
        }
    }
    public void push(T item) {
        addFirst(item);
    }

    public T pop() {
        return remove();
    }
    

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedList<?> other = (LinkedList<?>) o;
        if (size != other.size) return false;
        Node<T> current = head;
        Node<?> otherCurrent = other.head;
        while (current != null) {
            if (!Objects.equals(current.getData(), otherCurrent.getData())) return false;
            current = current.getNext();
            otherCurrent = otherCurrent.getNext();
        }
        return true;
    }

    public void add(int index, T item) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            addFirst(item);
        } else if (index == size) {
            addLast(item);
        } else {
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }
            current.setNext(new Node<>(item, current.getNext()));
            size++;
        }
    }

    public void addLast(T item) {
        Node<T> newNode = new Node<>(item, null);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
    }

    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T value = tail.getData();
        if (head == tail) {
            head = tail = null;
        } else {
            Node<T> current = head;
            while (current.getNext() != tail) {
                current = current.getNext();
            }
            tail = current;
            tail.setNext(null);
        }
        size--;
        return value;
    }

    public T getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail.data;
    }


    public boolean contains(T item) {
        Node<T> current = head;
        while (current != null) {
            if (Objects.equals(item, current.data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.getData());
            if (current.getNext() != null) {
                sb.append(", ");
            }
            current = current.getNext();
        }
        sb.append("]");
        return sb.toString();
    }

    //Corrected add method
    public void add(T item) {
        addFirst(item);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Node<T> getHead() {
        return head;
    }

    public void setHead(Node<T> head) {
        this.head = head;
    }

    public void addFirst(T item) {
        head = new Node<>(item, head);
        if (tail == null) {
            tail = head;
        }
        size++;
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return head.data;
    }

    public T poll() {
        return remove();
    }

    public void offer(T item) {
        addLast(item);
    }
}
