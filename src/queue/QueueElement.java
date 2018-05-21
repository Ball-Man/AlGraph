package queue;

/**
 * The actual queue node. 
 */
public class QueueElement<T> {
  private T _value;
  private QueueElement<T> _next;

  public QueueElement(T value) {
    _value = value;
  }


  /**
   * Get the next element.
   */
  public QueueElement<T> getNext() {
    return _next;
  }

  /**
   * Set the next element and return it.
   */
  public QueueElement<T> setNext(QueueElement<T> next) {
    _next = next;
    return _next;
  }

  /**
   * Get inner value of the node.
   */
  public T getValue() {
    return _value;
  }
}