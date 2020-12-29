package appServer;

public class Iterator<T> {
    T[] elements;
    int j;

    public Iterator (T[] initialElements)
    {
        this.elements = initialElements;
    }

    public T next()
    {
        T element =  elements[j];
        j++;
        return element;
    }

    public boolean hasNext()
    {
        return j < elements.length && elements[j] != null;
    }
}
