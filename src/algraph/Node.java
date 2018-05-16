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

class Node implements Listable {
  // Listable
  private Edge _next;
  
  // Attributes
  private Integer _id;
  private Integer _distance;

  // Parent graph
  private Graph _parent;

  // UI elements

  // Drag and drop
  // Temporary mouse data
  private double _mouseX;
  private double _mouseY;

  private int _x;   // Coordinates of the node
  private int _y;

  static private Pane _canvas;
  private VBox _node;

  private StackPane _stackpane;
  private Circle _circle;
  private Text _distanceText;
  private Text _idText;

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
          setPosition(_x + (int)deltaX, _y + (int)deltaY);    // Update current position
                                                              // and starting edges
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
    // Node
    _node = new VBox();
    
    // Stackpane for circle and text
    _stackpane = new StackPane();

    // Actual node(circle)
    _circle = new Circle(30, Color.WHITE);
    _circle.setStroke(Color.BLACK);
    _circle.setOnMousePressed(dragMousePressed());
    _circle.setOnMouseDragged(dragMouseDragged());
    
    // Distance text(used by Bellman-Ford algorithm)
    _distanceText = new Text();

    // Text inside the circle(showing the node's ID)
    _idText = new Text(_id.toString());
    _idText.setOnMousePressed(dragMousePressed());
    _idText.setOnMouseDragged(dragMouseDragged());

    // Add everything to canvas
    _stackpane.getChildren().addAll(_circle, _idText);
    _node.getChildren().addAll(_distanceText, _stackpane);

    _canvas.getChildren().add(_node);

    // Relocate node to the center of the screen
    _x = (int)_canvas.getBoundsInParent().getWidth() / 2;
    _y = (int)_canvas.getBoundsInParent().getHeight() / 2;
    int newX = (int)(_x - _node.getBoundsInParent().getWidth() / 2);
    int newY = (int)(_y - _node.getBoundsInParent().getHeight() + _circle.getBoundsInParent().getHeight() / 2 - _distanceText.getBoundsInParent().getHeight());

    _node.relocate(newX, newY);

  }


  // Get / Set the node id
  public void setID(int id) {
    _id = id;

    // Update layout
    _idText.setText(_id.toString());
  }

  public int getID() {
    return _id;
  }

  // Get / Set the distance(used in the Bellman Ford algorithm)
  public void setDistance(Integer distance) {
    _distance = distance;

    // Update layout
    _distanceText.setText(_distance.toString());
  }

  public int getDistance() {
    return _distance;
  }

  // Get center coordinates
  public int getX() {
    return _x;
  }

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
  public void setPosition(int x, int y) {
    _x = x;
    _y = y;
    int newX = (int)(_x - _node.getBoundsInParent().getWidth() / 2);
    int newY = (int)(_y - _node.getBoundsInParent().getHeight() + _circle.getBoundsInParent().getHeight() / 2);

    // Check for nodes not to go above the menubar
    if (newX < 0) {
      newX = 0;
      _x = (int)_node.getBoundsInParent().getWidth() / 2;
    }

    if (newY < 0) {
      newY = 0;
      _y = (int)(_node.getBoundsInParent().getHeight() - _circle.getBoundsInParent().getHeight() / 2);
    }

    _node.relocate(newX, newY);
    updateEdges();
  }

  // Update the edges' positioning according to the
  // node's center
  public void updateEdges() {
    Listable[] items = getList();
    for(int i = 0; i < items.length; i++)
      ((Edge)items[i]).setLineStart(_x, _y);
  }

  public static Pane getLayout() {
    return _canvas;
  }

  public static void setLayout(Pane layout) {
    _canvas = layout;
  }

  public void remove() {
    _canvas.getChildren().remove(_node);

    // Remove all edges coming from the removed node
    Listable head = this;
    while (!head.getFinished()) {
      ((Edge)head.getNext()).remove();
      head = head.getNext();
    }
  }
}
