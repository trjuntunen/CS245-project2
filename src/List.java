public interface List<T> {
    public void add(T item);
    public void add(int pos, T item) throws Exception;
    public T get(int pos) throws Exception;
    public T remove(int pos) throws Exception;
    public int size();
}
