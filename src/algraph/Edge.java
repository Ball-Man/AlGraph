package algraph;

import list.Listable;

import javafx.scene.shape.Line;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

class Edge implements Listable {
  // Listable
  private Edge _next;

  // Attributes
  private Integer _weight;
  private int _from;
  private int _to;

  // UI elements
  static private Pane _canvas;

  private Line _line;
  private Text _text;

  public Edge(int from, int to, int w) {
    _from = from;
    _to = to;
    _weight = w;

    // Layout
    _line = new Line();
    _text = new Text(0, 0, _weight.toString());
    _canvas.getChildren().addAll(_line, _text);
  }

  // Get / Set the edge weight
  public void setWeight(int w) { 
    _weight = w;

    // Layout
    _text.setText(_weight.toString());
  }

  public int getWeight() {
    return _weight;
  }

  // Get / Set from node
  public void setFrom(int node) {
    _from = node;
  }

  public int getFrom() {
    return _from;
  }

  // Get / Set to node
  public void setTo(int node) {
    _to = node;
  }

  public int getTo() {
    return _to;
  }

  // Listable methods
  public Listable getNext() {
    return _next;
  }

  // Sets the new list as a next, no matter if there's
  // already a 'next' element
  public void setNext(Listable next) {
    _next = ((Edge)next);
  }

  public boolean getFinished() {
    return _next == null;
  }

  // Removes the next element from the list, but restores the list
  // (which is different from what setNext does)
  public Listable removeNext() {
    if(getFinished())   // If next is null, can't remove it
      return this;

    // Else...
    _next = ((Edge)_next.getNext());  // The garbage collector will take care of
                                      // the removed item

    return this;
  }

  // Inserts a single node as next node in the list
  // and preserves the list's structure
  public Listable insertNext(Listable next) {
    next.setNext(_next);
    _next = (Edge)next;

    return _next;
  }

  // Layout
  // Line positioning
  public void setLine(int x1, int y1, int x2, int y2) {
    _line.setStartX(x1);
    _line.setStartY(y1);
    _line.setEndX(x2);
    _line.setEndY(y2);

    _text.relocate((x1 + x2) / 2, (y1 + y2) / 2);
  }

  public void setLineStart(int x, int y) {
    _line.setStartX(x);
    _line.setStartY(y);

    _text.relocate((x + _line.getEndX()) / 2, (y + _line.getEndY()) / 2);
  }

  public void setLineEnd(int x, int y) {
    _line.setEndX(x);
    _line.setEndY(y);

    _text.relocate((_line.getStartX() + x) / 2, (_line.getStartY() + y) / 2);
  }

  public void remove() {
    _canvas.getChildren().removeAll(_line, _text);
  }

  public static Pane getLayout() {
    return _canvas;
  }

  public static void setLayout(Pane layout) {
    _canvas = layout;
  }
}