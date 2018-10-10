package algraph;

import list.Listable;

import javafx.scene.shape.Line;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * A graph's edge and its graphical elements. 
 */
class Edge implements Listable, java.io.Serializable {
  // Listable
  private Edge _next;

  // Attributes
  private Integer _weight;
  private int _from;
  private int _to;

  // UI elements
  static private Pane _canvas;

  private transient Arrow _line;
  private transient Text _text;

  public Edge(int from, int to, int w) {
    _from = from;
    _to = to;
    _weight = w;

    // Layout
    generateGUI();
  }

  /**
   * Set the edge's weight.
   */
  public void setWeight(int w) { 
    _weight = w;

    // Layout
    _text.setText(_weight.toString());
  }

  /**
   * Get the edge's weight.
   */
  public int getWeight() {
    return _weight;
  }

  /**
   * Set the edge's starting node.
   */
  public void setFrom(int node) {
    _from = node;
  }

  /**
   * Get the edge's starting node.
   */
  public int getFrom() {
    return _from;
  }

  /**
   * Set the edge's ending node.
   */
  public void setTo(int node) {
    _to = node;
  }

  /**
   * Get the edge's ending node.
   */
  public int getTo() {
    return _to;
  }

  /**
   * Get the edge's length.
   */
  public int getLength() {
    return 0;
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
  /**
   * Set the line starting and ending points.
   */
  public void setLine(int x1, int y1, int x2, int y2) {
    this.setLineStart(x1, y1);
    this.setLineEnd(x2, y2);

    _text.relocate((x1 + 2 * x2) / 3, (y1 + 2 * y2) / 3);
  }

  /**
   * Set the line starting point.
   */
  public void setLineStart(int x, int y) {
    _line.setLineStart(x, y);

    _text.relocate((x + 2 * _line.getEndX()) / 3, (y + 2 * _line.getEndY()) / 3);
  }

  /**
   * Set the line ending point.
   */
  public void setLineEnd(int x, int y) {
    _line.setLineEnd(x, y);

    _text.relocate((_line.getStartX() + 2 * x) / 3, (_line.getStartY() + 2 * y) / 3);
  }

  /**
   * Remove graphical elements from the parent node.
   */
  public void remove() {
    _canvas.getChildren().removeAll(_line, _text);
  }

  /**
   * Generate GUI from the internal data.
   */
  public void generateGUI() {
    _line = new Arrow();
    _text = new Text(0, 0, _weight.toString());
    _canvas.getChildren().addAll(_line, _text);
  }

  /**
   * Get parent node.
   */
  public static Pane getLayout() {
    return _canvas;
  }

  /**
   * Set parent node.
   */
  public static void setLayout(Pane layout) {
    _canvas = layout;
  }
}