package list;

public interface Listable<T> {
  public Listable<T> getNext();
  public boolean getFinished();
}