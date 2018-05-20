package algraph;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import queue.*;


class BFMGUI {
  // GUI Elements
  private static VBox _canvas;

  private HBox _distances;
  private HBox _queue;

  private void addToQueue(Integer integer) {
    StackPane node = new StackPane();
    Rectangle rectangle = new Rectangle(0, 0, 30, 30);
    rectangle.setFill(Color.WHITE);
    rectangle.setStroke(Color.BLACK);
    Text text = new Text(integer.toString());

    node.getChildren().addAll(rectangle, text);
    _queue.getChildren().add(node);
  }

  private void addToDistances(Integer integer) {
    StackPane node = new StackPane();
    Rectangle rectangle = new Rectangle(0, 0, 30, 30);
    rectangle.setFill(Color.CYAN);
    //rectangle.setStroke(Color.BLACK);
    Text text = new Text(integer != Integer.MAX_VALUE ? integer.toString() : "+∞");

    node.getChildren().addAll(rectangle, text);
    _distances.getChildren().add(node);
  }

  public BFMGUI() {
    Text distances = new Text("Distances from the root:");
    Text queue = new Text("Queue status:");

    _distances = new HBox();
    _distances.setSpacing(3);
    _distances.setPadding(new Insets(0, 0, 0, 10));
    _queue = new HBox();
    _queue.setSpacing(3);
    _queue.setPadding(new Insets(0, 0, 0, 10));

    _canvas.getChildren().addAll(distances, _distances, queue, _queue);
  }

  public void updateQueue(Queue<Integer> queue) {
    _queue.getChildren().clear();

    // Scan the internal queue withou dequeueing elements
    QueueElement<Integer> head = queue.getHead();
    while (head != null) {
      addToQueue(head.getValue());
      head = head.getNext();
    }
  }

  public void updateDistances(int[] distances) {
    _distances.getChildren().clear();

    for (int i : distances)
      addToDistances(i);
  }

  // Layout
  public void remove() {
    _canvas.getChildren().clear();
  }

  // Set Layout
  public static void setLayout(VBox canvas) {
    _canvas = canvas;
  }

}