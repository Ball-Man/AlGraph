import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import vector.Vector;
import algraph.Graph;

public class App extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    // Window setup
    primaryStage.setTitle("The best AlGraph you'll ever see!");
    primaryStage.setX(0);
    primaryStage.setY(0);
    primaryStage.setMaximized(true);

    // Layout setup
    BorderPane root = new BorderPane();

    // Central work area
    StackPane graph = new StackPane();
    Pane above = new Pane();
    Pane below = new Pane();
    graph.getChildren().addAll(below, above);

    // Menu
    Menu file = new Menu("File");
    MenuBar menu = new MenuBar(file);

    root.setTop(menu);
    root.setCenter(graph);

    Scene scene = new Scene(root, 700, 450); 
    primaryStage.setScene(scene);

    primaryStage.show();

    Graph.setEdgesLayout(below);
    Graph.setNodesLayout(above);
    Graph g = new Graph();

    g.addNode();
    g.addNode();
    g.addNode();

    g.addEdge(0, 1, 10);
    g.addEdge(1, 2, 20);
    g.addEdge(2, 0, 30);

    g.updateEdges();

  }
}

