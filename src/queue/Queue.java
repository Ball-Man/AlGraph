package queue;

/**
 * Generic dynamic queue. 
 */
public class Queue<T> {
  private QueueElement<T> _head;
  private QueueElement<T> _tail;

  /**
   * Push a new element into the queue.
   */
  public void enqueue(T element) {
    // If queue is empty
    if (_head == null && _tail == null)
    {
      _head = _tail = new QueueElement<T>(element);
      return;
    }
    // Else...
    _tail.setNext(new QueueElement<T>(element));
    _tail = _tail.getNext();
  }

  /**
   * Release an element from the queue and return it.
   */
  public T dequeue() {
    T value = _head.getValue();

    // If last element in queue
    if (_head == _tail)
      _head = _tail = null;
    else
      _head = _head.getNext();

    return value;
  }

  /**
   * Returns true if the queue is empty.
   */
  public boolean getEmpty() {
    return _head == null;
  }

  /**
   * Get the queue's head.
   * Used to cycle through the elements without affecting the queue's elements.
   */
  public QueueElement<T> getHead() {
    return _head;
  }
}