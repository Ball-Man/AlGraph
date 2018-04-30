package list;

public interface Listable {
  // Get / Set next
  public Listable getNext();
  public void setNext(Listable next);

  // Get if finished
  public boolean getFinished();

  // Insert / Remove
  public Listable removeNext();
}