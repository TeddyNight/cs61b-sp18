public class LinkedListDeque<T> {
    private class Node<T> {
        private Node<T> prev;
        private T data;
        private Node<T> next;

        public Node(Node prev, T data, Node next) {
            this.prev = prev;
            this.data = data;
            this.next = next;
        }
    }
    private Node<T> sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node<T>(null,null,null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }
    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return sentinel.next==sentinel;
    }
    public void addFirst(T item) {
        Node<T> newnode = new Node<T>(sentinel,item,sentinel.next);
        sentinel.next.prev = newnode;
        sentinel.next = newnode;
        size++;
    }
    public void addLast(T item) {
        Node<T> newnode = new Node<T>(sentinel.prev,item,sentinel);
        sentinel.prev.next = newnode;
        sentinel.prev = newnode;
        size++;
    }
    public T removeFirst() {
        if(isEmpty()) {
            return null;
        }
        T data = sentinel.next.data;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size--;
        return data;
    }
    public T removeLast() {
        if(isEmpty()) {
            return null;
        }
        T data = sentinel.prev.data;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size--;
        return data;
    }
    public T get(int index){
        if(isEmpty()) {
            return null;
        }
        Node<T> ptr=sentinel.next;
        for(int i=0;i<index;i++){
            ptr = ptr.next;
        }
        return ptr.data;
    }
    public void printDeque() {
        Node<T> ptr = sentinel.next;
        while(ptr!=sentinel) {
            System.out.print(ptr.data+" ");
            ptr = ptr.next;
        }
        System.out.println("");
    }
    public T getRecursiveHelper(Node<T> ptr,int count){
        if(ptr==sentinel||ptr==null) {
            return null;
        }
        if(count==0){
            return ptr.data;
        }
        return getRecursiveHelper(ptr.next,--count);
    }
    public T getRecursive(int index){
        return getRecursiveHelper(sentinel.next,index);
    }
}
