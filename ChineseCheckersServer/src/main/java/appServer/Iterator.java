package appServer;

/**
 * Class goes through elements in an array
 * @param <T> any object
 */
public class Iterator<T> {
    T[] elements;
    int j;

    public Iterator (T[] initialElements)
    {
        this.elements = initialElements;
    }

    /**
     * Methods that returns element in array and increments index, so next time it returns next element
     * and so on
     * @return Next element in the array
     */
    public T next() {
        T element = elements[j];
        j++;
        return element;
    }

    /**
     * Method checks if the array has next element
     * @return Whether in the array exist an element with index greater than index of the current
     * one
     */
    public boolean hasNext()
    {
        return j < elements.length && elements[j] != null;
    }
}
