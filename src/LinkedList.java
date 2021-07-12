public class LinkedList<T> implements List<T> {

    private Node<T> head;
    private int size;

    public LinkedList() {
        head = null;
        size = 0;
    }

    @Override
    public void add(T item) {
        if(head == null) {
            head = new Node<T>(item);
        } else {
            Node<T> node = head;
            for(int i = 0; i < size-1; i++) {
                node = node.next;
            }
            node.next = new Node<T>(item);
        }
        size++;
    }

    @Override
    public void add(int pos, T item) throws Exception {
        if(pos < 0 || pos > size) {
            throw new Exception("List index out of bounds");
        }
        if(pos == 0) {
            Node<T> node = new Node<T>(item);
            node.next = head;
            head = node;
        } else {
            Node<T> prev = head;
            for(int i = 0; i < pos-1; i++) {
                prev = prev.next;
            }
            Node<T> newNode = new Node<T>(item);
            newNode.next = prev.next;
            prev.next = newNode;
        }
        size++;
    }

    @Override
    public T get(int pos) throws Exception {
        if (pos < 0 || pos >= size) {
            throw new Exception("List index out of bounds");
        }
        Node<T> curr = head;
        for (int i = 0; i < pos; i++) {
            curr = curr.next;
        }
        return curr.data;
    }

    @Override
    public T remove(int pos) throws Exception {
        if(pos < 0 || pos > size) {
            throw new Exception("List index out of bounds");
        }
        if(pos == 0) {
            Node<T> node = head;
            head = head.next;
            size--;
            return node.data;
        }
        Node<T> prev = head;
        for(int i = 0; i < pos-1; i++) {
            prev = prev.next;
        }
        Node<T> node = prev.next;
        prev.next = node.next;
        size --;
        return node.data;
    }

    public Node<T> getHead() {
        return head;
    }

    @Override
    public int size() {
        return size;
    }

}

