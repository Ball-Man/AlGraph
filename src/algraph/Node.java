package algraph;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import list.Listable;

public class Node implements Listable {
  // Listable
  private Edge _next;
  
  // Attributes
  private Integer _id;
  private Integer _distance;

  // UI elements
  static private Pane _canvas;
  private VBox _node;

  private StackPane _stackpane;
  private Circle _circle;
  private Text _distanceText;
  private Text _idText;

  public Node(int id) {
    _id = id;

    // Layout
    _node = new VBox();
    _stackpane = new StackPane();
    _circle = new Circle(30, Color.WHITE);
    _circle.setStroke(Color.BLACK);
    _distanceText = new Text();
    _idText = new Text(_id.toString());

    _stackpane.getChildren().addAll(_circle, _idText);
    _node.getChildren().addAll(_distanceText, _stackpane);

    _canvas.getChildren().add(_node);
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

  // Layout
  public void setPosition(int x, int y) {
    _node.relocate(x - ( _node.getBoundsInParent().getWidth() + _distanceText.getBoundsInParent().getWidth() ) / 2, y - ( _node.getBoundsInParent().getHeight() / 2 + _distanceText.getBoundsInParent().getHeight() ));
  }

  public static Pane getLayout() {
    return _canvas;
  }

  public static void setLayout(Pane layout) {
    _canvas = layout;
  }
}
