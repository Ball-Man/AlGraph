import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import algraph.Edge;

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
    Pane graph = new Pane();
    graph.setStyle("-fx-background-color: #FF0000;");

    // Menu
    Menu file = new Menu("File");
    MenuBar menu = new MenuBar(file);

    root.setTop(menu);
    root.setCenter(graph);

    Scene scene = new Scene(root, 700, 450); 
    primaryStage.setScene(scene);

    primaryStage.show();

    Edge.setLayout(graph);
    Edge edge = new Edge(1, 1, 20);
    edge.setPointFrom(20, 100);
    edge.setPointTo(300, 300);
  }
}

