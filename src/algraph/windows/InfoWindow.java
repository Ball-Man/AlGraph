package algraph.windows;

import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.control.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import window.Window;
import window.Method;

/**
 * Window used to show info about the algorithm.
 */
public class InfoWindow extends Window {
  // Close button event
  private EventHandler<ActionEvent> btnOkAction() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        close();
      }
    };
  }

  @Override
  public void start() {
    // Window creation
    _stage = new Stage();
    _stage.initModality(Modality.APPLICATION_MODAL);
    _stage.setTitle("The Bellman-Ford algorithm");

    // Scene setup
    VBox root = new VBox();
    root.setSpacing(5);
    root.setPadding(new Insets(10, 10, 0, 10));

    Scene scene = new Scene(root, 450, 580);
    _stage.setScene(scene);
    _stage.show();

    // GUI
    Label lblExplanation = new Label("The Bellman-Ford algorithm is used to find shortest paths inside a graph. It's " +
      "actually a 'One to All' algorithm, which means it calculates all the shortest paths between a chosen node(called root) " +
      "and all the others. In this project it's used as 'One to One', which means that only the shortest path between the root " +
      "and the chosen destination node will be showed, even if all the other paths are still computed by the algorithm.\n" +
      "It works by a mechanism called Edge relaxing; When an edge is relaxed, its value converges to a new value which is nearer " +
      "the node's shortest distance to the root. The algorithm works by cycling over this simple mechanism.\n" +
      "If well implemented, this algorithm is also able to check for negative cycles.\n" +
      "Here's the algorithm pseudocode:");
    lblExplanation.setWrapText(true);

    // Algorithm pseudocode
    Label lblCode = new Label(
      "// Assuming: V number of nodes in the graph\n" +
      "// Assuming: 'in' vector of false of V elements\n" +
      "// Assuming: 'd' vector of integers all set to +infinity\n" +
      "// Assuming: 'path' vector of integers\n" +
      "bfm(Graph graph, int root, bool in[], int d[], int path[])\n" +
      "Queue q = new Queue();\n" +
      "q.enqueue(root);\n" +
      "d[root] = 0;\n" +
      "while (not q.isEmpty())\n" +
      "  int u = q.dequeue();\n" +
      "  in[u] = false;\n" +
      "  for (v in graph.adj(u))\n" +
      "    if (d[u] + graph.weight(u, v) < d[v])\n" +
      "      path[v] = u;\n" +
      "      d[v] = d[u] + graph.weight(u, v);\n" +
      "      if (not in[v])\n" +
      "        q.enqueue(v);\n" +
      "        in[q] = true;\n\n\n"
      );
    lblCode.setFont(Font.font("Monospace", 12));
    lblCode.setTextFill(Color.GRAY);

    // Empty action
    _onOk = new Method() {
      @Override
      public void invoke() {

      }
    };

    Button btnOk = new Button("Ok");
    btnOk.setOnAction(btnOkAction());

    root.getChildren().addAll(lblExplanation, lblCode, btnOk);

  }
}