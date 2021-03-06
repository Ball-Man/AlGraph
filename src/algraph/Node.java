package algraph;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import list.Listable;

/**
 * A graph's node.
 */
class Node implements Listable, java.io.Serializable {
  // The radius of the circle
  static final int RADIUS = 30;

  // Listable
  private Edge _next;
  
  // Attributes
  private Integer _id;

  // Parent graph
  private Graph _parent;

  // UI elements

  // Drag and drop
  // Temporary mouse data
  private double _mouseX;
  private double _mouseY;

  private int _x;   // Coordinates of the node
  private int _y;

  static private transient Pane _canvas;
  private transient VBox _node;

  private transient StackPane _stackpane;
  private transient Circle _circle;
  private transient Text _idText;

  // Drag&Drop
  // When clicked, saves the mouse coordinates
  private EventHandler<MouseEvent> dragMousePressed() {
    return new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
          _mouseX = event.getScreenX();
          _mouseY = event.getScreenY();
        }
      }
    };
  }

  // When dragged, uses the difference between the new coordinates and
  // the temporary one to move the node accordingly
  private EventHandler<MouseEvent> dragMouseDragged() {
    return new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
          double deltaX = event.getScreenX() - _mouseX;
          double deltaY = event.getScreenY() - _mouseY;
          setPosition(_x + (int)deltaX, _y + (int)deltaY);
          _parent.updateEdgesTo(_id, _x, _y);     // Update ending edges
          
          _mouseX = event.getScreenX();
          _mouseY = event.getScreenY();
        }
      }
    };
  }

  public Node(int id, Graph parent) {
    _id = id;
    _parent = parent;

    // Layout
    generateGUI();
  }


  /**
   * Set the node's id.
   */
  public void setID(int id) {
    _id = id;

    // Update layout
    _idText.setText(_id.toString());
  }

  /**
   * Get the node's id.
   */
  public int getID() {
    return _id;
  }

  /**
   * Get the node's center's x position.
   */
  public int getX() {
    return _x;
  }

  /**
   * Get the node's center's y position.
   */
  public int getY() {
    return _y;
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
  // (which is different from what setNext does)ble centerble center
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
   * Set the node's center's position.
   */
  public void setPosition(int x, int y) {
    _x = x;
    _y = y;
    int newX = (int)(_x - _node.getBoundsInParent().getWidth() / 2);
    int newY = (int)(_y - _node.getBoundsInParent().getHeight() / 2);

    // Check for nodes not to go above the menubar
    if (newX < 0) {
      newX = 0;
      _x = (int)(_node.getBoundsInParent().getWidth() / 2);
    }

    if (newY < 0) {
      newY = 0;
      _y = (int)(_node.getBoundsInParent().getHeight() / 2);
    }

    _node.relocate(newX, newY);
    updateEdges();
  }

  /**
   * Update line positioning for edges starting from this node.
   */
  public void updateEdges() {
    Listable[] items = getList();
    for(int i = 0; i < items.length; i++)
      ((Edge)items[i]).setLineStart(_x, _y);
  }

  /**
   * Remove GUI from the parent node.
   */
  public void remove() {
    _canvas.getChildren().remove(_node);

    // Remove all edges coming from the removed node
    Listable head = this;
    while (!head.getFinished()) {
      ((Edge)head.getNext()).remove();
      head = head.getNext();
    }
  }

  /**
   * Generate GUI from the internal data.
   */
  public void generateGUI() {
    // Node
    _node = new VBox();
    
    // Stackpane for circle and text
    _stackpane = new StackPane();

    // Actual node(circle)
    _circle = new Circle(this.RADIUS, Color.WHITE);
    _circle.setStroke(Color.BLACK);
    _circle.setOnMousePressed(dragMousePressed());
    _circle.setOnMouseDragged(dragMouseDragged());

    // Text inside the circle(showing the node's ID)
    _idText = new Text(_id.toString());
    _idText.setOnMousePressed(dragMousePressed());
    _idText.setOnMouseDragged(dragMouseDragged());

    // Add everything to canvas
    _stackpane.getChildren().addAll(_circle, _idText);
    _node.getChildren().add(_stackpane);

    _canvas.getChildren().add(_node);

    // Generate edges' GUI
    for (Listable edge: getList())
      ((Edge)edge).generateGUI();

    setPosition(_x, _y);
  }

  /**
   * Set fill color for the node(cirlce).
   */
  public void setColor(Color color) {
    _circle.setFill(color);
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
