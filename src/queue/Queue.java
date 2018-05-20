package queue;

public class Queue<T> {
  private QueueElement<T> _head;
  private QueueElement<T> _tail;

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

  public T dequeue() {
    T value = _head.getValue();

    // If last element in queue
    if (_head == _tail)
      _head = _tail = null;
    else
      _head = _head.getNext();

    return value;
  }

  public boolean getEmpty() {
    return _head == null;
  }
}