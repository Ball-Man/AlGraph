package list;

/**
 * Interface used to simulate a simple not homogeneous linked list. 
 */
public interface Listable {
  /**
   * Get the next linked element.
   */
  public Listable getNext();

  /**
   * Set the next linked element.
   */
  public void setNext(Listable next);

  /**
   * Returns true if the next linked element is null.
   */
  public boolean getFinished();

  /**
   * Remove the next linked element and restore the list.
   */
  public Listable removeNext();

  /**
   * Insert an element as the next one and restore the list.
   */
  public Listable insertNext(Listable next);

  /**
   * Create an array containing all the next linked elements.
   */
  public default Listable[] getList() {
    // Count items
    int count = 0;
    Listable head = this;
    for (; !head.getFinished(); count++)
      head = head.getNext();

    // Save actual items
    head = this;
    Listable[] items = new Listable[count];
    for (int i = 0; i < count; i++) {
      head = head.getNext();
      items[i] = head;
    }

    return items;
  }
}