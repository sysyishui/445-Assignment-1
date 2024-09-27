import java.util.Arrays;

public class ArrayDS<T extends Comparable<? super T>> implements SequenceInterface<T>, ReorderInterface {

    private T[] data;
    private int size;
    private static final int INITIAL_CAPACITY = 10;

    // Default constructor
    @SuppressWarnings("unchecked")
    public ArrayDS() {
        data = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    // Copy constructor
    @SuppressWarnings("unchecked")
    public ArrayDS(ArrayDS<T> other) {
        this.data = (T[]) new Comparable[other.data.length];
        this.size = other.size;
        System.arraycopy(other.data, 0, this.data, 0, other.size);
    }

    // Helper method to resize the internal array if needed
    private void ensureCapacity() {
        if (size == data.length) {
            data = Arrays.copyOf(data, data.length * 2);
        }
    }

    @Override
    public void append(T item) {
        ensureCapacity();
        data[size] = item;
        size++;
    }

    @Override
    public void prefix(T item) {
        ensureCapacity();
        System.arraycopy(data, 0, data, 1, size);  // Shift elements to the right
        data[0] = item;  // Insert the new item at the front
        size++;
    }

    @Override
    public T deleteTail() throws EmptySequenceException {
        if (isEmpty()) {
            throw new EmptySequenceException("The sequence is empty.");
        }
        T tailItem = data[size - 1];
        data[size - 1] = null;  // Help garbage collector
        size--;
        return tailItem;
    }

    @Override
    public T deleteHead() throws EmptySequenceException {
        if (isEmpty()) {
            throw new EmptySequenceException("The sequence is empty.");
        }
        T headItem = data[0];
        System.arraycopy(data, 1, data, 0, size - 1);  // Shift elements to the left
        data[size - 1] = null;
        size--;
        return headItem;
    }

    @Override
    public boolean trim(int numItems) {
        if (numItems > size) {
            return false;
        }
        for (int i = 0; i < numItems; i++) {
            data[size - 1] = null;
            size--;
        }
        return true;
    }

    @Override
    public boolean cut(int start, int numItems) {
        if (start + numItems > size) {
            return false;
        }
        for (int i = start; i < start + numItems; i++) {
            data[i] = null;
        }
        System.arraycopy(data, start + numItems, data, start, size - (start + numItems));
        size -= numItems;
        return true;
    }

    @Override
    public void reverse() {
        for (int i = 0; i < size / 2; i++) {
            T temp = data[i];
            data[i] = data[size - 1 - i];
            data[size - 1 - i] = temp;
        }
    }

    @Override
    public void rotateLeft() {
        if (size <= 1) return;
        T first = data[0];
        System.arraycopy(data, 1, data, 0, size - 1);
        data[size - 1] = first;
    }

    @Override
    public void rotateRight() {
        if (size <= 1) return;
        T last = data[size - 1];
        System.arraycopy(data, 0, data, 1, size - 1);
        data[0] = last;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(data, null);
        size = 0;
    }

    // New Methods Implemented:

    @Override
    public int getFrequencyOf(T item) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (data[i].equals(item)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void insert(T item, int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        ensureCapacity();
        System.arraycopy(data, index, data, index + 1, size - index); // Shift elements to the right
        data[index] = item;
        size++;
    }

    @Override
    public T itemAt(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return data[index];
    }

    @Override
@Override
public void shuffle(int[] sourcePositions, int[] targetPositions) {
    if (sourcePositions.length != targetPositions.length) {
        throw new IllegalArgumentException("Source and target arrays must be of the same length.");
    }

    // Check for out-of-bounds and duplicate target positions
    boolean[] positionCheck = new boolean[size];
    for (int i = 0; i < sourcePositions.length; i++) {
        if (sourcePositions[i] < 0 || sourcePositions[i] >= size || targetPositions[i] < 0 || targetPositions[i] >= size) {
            throw new IndexOutOfBoundsException("Source or target index out of bounds.");
        }
        if (positionCheck[targetPositions[i]]) {
            throw new IllegalArgumentException("Duplicate target index detected.");
        }
        positionCheck[targetPositions[i]] = true;
    }

    // Shuffle logic
    T[] temp = Arrays.copyOf(data, size);
    for (int i = 0; i < sourcePositions.length; i++) {
        data[targetPositions[i]] = temp[sourcePositions[i]];
    }
}
    @Override
    public T predecessor(T item) {
        for (int i = 1; i < size; i++) {
            if (data[i].equals(item)) {
                return data[i - 1];
            }
        }
        return null; // No predecessor found
    }

 @Override
    public T first() {
    return isEmpty() ? null : data[0];
    }

@Override
    public T last() {
    return isEmpty() ? null : data[size - 1];
    }


    @Override
    public int lastOccurrenceOf(T item) {
        for (int i = size - 1; i >= 0; i--) {
            if (data[i] != null && data[i].equals(item)) {
                return i;
            }
        }
        return -1; // Item not found
    }
    
    @Override
    public SequenceInterface<T> slice(T item) {
        ArrayDS<T> result = new ArrayDS<>();
        for (int i = 0; i < size; i++) {
            if (data[i].compareTo(item) <= 0) {
                result.append(data[i]);
            }
        }
        return result;
    }

    @Override
    public SequenceInterface<T> slice(int start, int numItems) {
        if (start + numItems > size) {
            return null;
        }
        ArrayDS<T> result = new ArrayDS<>();
        for (int i = start; i < start + numItems; i++) {
            result.append(data[i]);
        }
        return result;
    }

    @Override
    public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < size; i++) {
        sb.append(data[i]);  // Append elements directly without adding extra formatting
    }
    return sb.toString();  // Return the plain string without additional brackets
    }

    // CompareTo method with wildcard to allow comparison between different types of ArrayDS
    public int compareTo(ArrayDS<? extends T> other) {
        int minSize = Math.min(this.size, other.size);
        for (int i = 0; i < minSize; i++) {
            int comparison = this.data[i].compareTo(other.data[i]);
            if (comparison != 0) {
                return comparison;
            }
        }
        return Integer.compare(this.size, other.size);
    }

    // Compare method for test cases
    public static <T extends Comparable<? super T>> void compare(ArrayDS<T> seq1, ArrayDS<? extends T> seq2) {
        int result = seq1.compareTo(seq2);
        if (result < 0) {
            System.out.println(seq1 + " is less than " + seq2);
        } else if (result > 0) {
            System.out.println(seq1 + " is greater than " + seq2);
        } else {
            System.out.println(seq1 + " is equal to " + seq2);
        }
    }
}
