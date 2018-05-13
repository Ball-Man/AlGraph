package vector;

import java.lang.Math;
import java.lang.Iterable;
import java.util.Iterator;
import java.util.Arrays;

public class Vector<T> implements Iterable<T> {
  // Info
  private int _capacity;
  private int _length;

  // Actual items
  private T[] _array;

  public Vector() {
    this(0);
  }

  @SuppressWarnings("unchecked")
  public Vector(int n) {
    _capacity = (int)Math.pow(2, (int)Math.ceil(Math.log1p(n) / Math.log(2)));
    _length = n;

    // Initialize elements
    _array = (T[])new Object[_capacity];
  }

  // Get length
  public Integer getLength() {
    return _length;
  }

  // Push element
  @SuppressWarnings("unchecked")
  public void push(T element) {
    if (_length == _capacity) {
      // Expand
      _capacity = (int)Math.pow(2, (int)Math.ceil(Math.log1p(_capacity) / Math.log(2)));
      T[] _tmpArray = (T[])new Object[_capacity];
      for (int i = 0; i < _array.length; i++)
        _tmpArray[i] = _array[i];

      _array = _tmpArray;
    }

    // Actually push the new item
    _array[_length++] = element;
  }

  // Remove array element from the given index
  public void remove(int index) {
    _length--;
    for (int i = index; i < _length; i++)
      _array[i] = _array[i + 1];
  }

  // Get element at i position
  public T at(int i) {
    return _array[i];
  }

  public T[] getArray() {
    return _array;
  }

  // Iterable interface
  public Iterator<T> iterator() {
    return Arrays.asList(_array).subList(0, _length).iterator();
  }
}