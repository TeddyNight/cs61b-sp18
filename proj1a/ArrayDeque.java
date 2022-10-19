public class ArrayDeque<T> {
    private T[] items;
    private final float REFACTOR = 1.75f;
    /**
     * front 指向的是插入位置，rear指向最后一个数据，要确保这俩东西
     */
    int front;
    int rear;
    public ArrayDeque(){
        front = 0; // items[front + 1]
        rear = 0;
        items = (T[]) new Object[8];
    }
    public int size() {
        /** 数组下标从0到items.length-1
         *  我这里采用头指针前置的做法
         *  下标相减的时候需要+1算出数量，同时front指示插入位置，其真正的开头是front+1
         *  最后式子应该化简为rear-front+items.length
         *  加items.length是当rear-front出现负数的情况
         */
        return (items.length+(rear-(front+1))+1)%items.length;
    }
    public int length() {
        return items.length;
    }
    public boolean isEmpty() {
        return size()==0;
    }
    public boolean isFull() {
        return size()==items.length-1;
    }
    private void resize(int newSize) {
        int oldSize = size();
        T[] newitems = (T[]) new Object[newSize];
        for(int i=0;i<oldSize;i++) {
            newitems[i+1] = get(i);
        }
        items = newitems;
        front = 0;
        rear = oldSize;
    }
    public void addFirst(T item) {
        if(isFull()){
            resize((int)(items.length * REFACTOR));
        }
        items[front] = item;
        front = (items.length+front-1) % items.length;
    }
    public void addLast(T item) {
        if(isFull()){
            resize((int)(items.length * REFACTOR));
        }
        rear = (rear+1)%items.length;
        items[rear] = item;
    }
    public T removeFirst() {
        if(isEmpty()) {
            return null;
        }
        if((float)size() / (float)items.length < 0.25f){
            resize(items.length/2);
        }
        front = (front+1) % items.length;
        return items[front];
    }
    public T removeLast() {
        if(isEmpty()) {
            return null;
        }
        T data = items[rear];
        rear = (items.length+rear-1) % items.length;
        if((float)size() / (float)items.length < 0.25f){
            resize(items.length/2);
        }
        return data;
    }
    public T get(int index){
        if(isEmpty() || index > size()-1) {
            return null;
        }
        index = (front+1+index) % items.length;
        return items[index];
    }
    public void printDeque(){
        for(int i=0;i<size();i++) {
            System.out.print(get(i)+" ");
        }
        System.out.println("");
    }
}
