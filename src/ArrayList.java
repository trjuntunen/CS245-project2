
public class ArrayList<T> implements List<T> {

    T[] arr;
    int size;

    @SuppressWarnings("unchecked")
	public ArrayList() {
        size = 0;
        arr = (T[]) new Object[10];
    }

    private void growArray() {
        @SuppressWarnings("unchecked")
		T[] newArr = (T[]) new Object[arr.length * 2];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        arr = newArr;
    }

    @Override
    public void add(T item) throws Exception {
        if(size == arr.length) {
            growArray();
        }
        arr[size++] = item;
    }

    @Override
    public void add(int pos, T item) throws Exception {
        if(size == arr.length) {
            growArray();
        }
        if (size - pos >= 0) System.arraycopy(arr, pos, arr, pos + 1, size - pos);
        arr[pos] = item;
        size++;
    }

    @Override
    public T get(int pos) throws Exception {
        if(pos < 0 || pos > size-1) {
            throw new Exception("List index out of bounds");
        }
        return arr[pos];
    }

    @Override
    public T remove(int pos) throws Exception {
        return null;
    }

    @Override
    public int size() {
        return size;
    }
}
