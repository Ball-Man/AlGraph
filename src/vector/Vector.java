package vector;

import java.lang.Math;
import java.lang.Iterable;
import java.util.Iterator;
import java.util.Arrays;

/**
 * Dynamic generic vector class.
 * Also iterable.
 */
public class Vector<T> implements Iterable<T>, java.io.Serializable {
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

  /**
   * Get the vector's length.
   */
  public Integer getLength() {
    return _length;
  }

  /**
   * Push a new element into the array.
   * The new element is placed as the last one.
   */
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

  /**
   * Remove and the items at the given index and resize the array.
   */
  public void remove(int index) {
    _length--;
    for (int i = index; i < _length; i++)
      _array[i] = _array[i + 1];
  }

  /**
   * Get the array element at the given position.
   */
  public T at(int i) {
    return _array[i];
  }

  /**
   * Set the array element at the given posiition to the given value.
   */
  public void setAt(int i, T value) {
    _array[i] = value;
  }

  public T[] getArray() {
    return _array;
  }

  // Iterable interface
  public Iterator<T> iterator() {
    return Arrays.asList(_array).subList(0, _length).iterator();
  }
}