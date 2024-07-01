// For week 3
// raup@itu.dk * 18/09/2021
package exercises03;

interface BoundedBufferInteface<T> {
    public T take() throws Exception;
    public void insert(T elem) throws Exception;
}
