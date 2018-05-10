package list;

public interface Listable {
  // Get / Set next
  public Listable getNext();
  public void setNext(Listable next);

  // Get if finished
  public boolean getFinished();

  // Insert / Remove
  public Listable removeNext();

  // Get an array containing the list of next items
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