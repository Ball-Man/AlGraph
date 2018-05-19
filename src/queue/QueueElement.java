package queue;

class QueueElement<T> {
  private T _value;
  private QueueElement<T> _next;

  public QueueElement(T value) {
    _value = value;
  }


  // Get / Set next element
  public QueueElement<T> getNext() {
    return _next;
  }

  public QueueElement<T> setNext(QueueElement<T> next) {
    _next = next;
    return _next;
  }

  // Get inner value
  public T getValue() {
    return _value;
  }
}