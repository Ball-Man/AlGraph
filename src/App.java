import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import vector.Vector;
import algraph.Graph;
import algraph.Core;

public class App extends Application {
  private Core _core; 

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

    // Menu creation
    MenuBar menu = new MenuBar();

    // Scene setup
    root.setTop(menu);
    root.setCenter(graph);

    Scene scene = new Scene(root, 700, 450); 
    primaryStage.setScene(scene);
    primaryStage.show();

    // Core
    // Set layouts
    Core.setEdgesLayout(below);
    Core.setNodesLayout(above);

    // Core creation
    _core = new Core(menu);
  }
}

