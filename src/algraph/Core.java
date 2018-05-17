package algraph;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.io.*;
import javafx.stage.*;
import window.Window;
import algraph.windows.*;
import window.Method;

public class Core {
  // Internal graph
  private Graph _graph;

  // Internal menu bar
  private MenuBar _menu;

  // Layouts
  private static Pane _edgesCanvas;
  private static Pane _nodesCanvas;

  // Menu events
  private EventHandler<ActionEvent> newAction() {
    return new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        NewWindow window = new NewWindow();
        window.setOnOk(new Method(){
          @Override
          public void invoke() {
            if (!_graph.randomize(window.getNodes(), window.getEdges(),
              window.getMinWeight(), window.getMaxWeight()))
              Window.showError("Error", "Wrong number of nodes or edges.");
            else
              _graph.updateEdges();
          }
        });
        window.start();
      }
    };
  }

  private EventHandler<ActionEvent> saveAction() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        // Setup window for saving file
        FileChooser dialog = new FileChooser();
        dialog.setTitle("Save graph");
        dialog.getExtensionFilters().addAll(
          new FileChooser.ExtensionFilter("Serialized graph(.ser)", "*.ser"),
          new FileChooser.ExtensionFilter("All Files(.*)", "*.*"));
        File file = dialog.showSaveDialog(new Stage());
        
        // If no file has been selected
        if (file == null)
          return;

        if (!saveGraph(file))
          Window.showError("Error", "Invalid file.");
      }
    };
  }

  private EventHandler<ActionEvent> openAction() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        // Setup window for laoding file
        FileChooser dialog = new FileChooser();
        dialog.setTitle("Open graph");
        dialog.getExtensionFilters().addAll(
          new FileChooser.ExtensionFilter("Serialized graph(.ser)", "*.ser"),
          new FileChooser.ExtensionFilter("All Files(.*)", "*.*"));
        File file = dialog.showOpenDialog(new Stage());

        // If no file has been selected
        if (file == null)
          return;

        if (openGraph(file)) {
          _graph.generateGUI();
          _graph.updateEdges();
          return;
        }
        // Else..

        Window.showError("Error", "Invalid file.");
      }
    };
  }

  private EventHandler<ActionEvent> infoAction() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {

      }
    };
  }

  private EventHandler<ActionEvent> quitAction() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Platform.exit();
      }
    };
  }

  private EventHandler<ActionEvent> addNodeAction() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (_graph.getNodesLength() < NewWindow.MAXNODES)
          _graph.addNode();
        else
          Window.showError("Error", "Maximum number of nodes reached(10).");
      }
    };
  }

  private EventHandler<ActionEvent> removeNodeAction() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        RemoveNodeWindow window = new RemoveNodeWindow();
        window.setOnOk(new Method(){
          @Override
          public void invoke() {
            if (!_graph.removeNode(window.getNode()))
              Window.showError("Error", "The given node doesn't exist.");
          }
        });
        window.start();
      }
    };
  }

  private EventHandler<ActionEvent> addEdgeAction() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        AddEdgeWindow window = new AddEdgeWindow();
        window.setOnOk(new Method(){
          @Override
          public void invoke() {
            if (!_graph.addEdge(window.getFrom(), window.getTo(), window.getWeight()))
              Window.showError("Error", "Invalid or already existing edge.");
            else
              _graph.updateEdges();
          }
        });
        window.start();
      }
    };
  }

  private EventHandler<ActionEvent> removeEdgeAction() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        RemoveEdgeWindow window = new RemoveEdgeWindow();
        window.setOnOk(new Method(){
          @Override
          public void invoke() {
            if (!_graph.removeEdge(window.getFrom(), window.getTo()))
              Window.showError("Error", "Invalid or not existing edge.");
          }
        });
        window.start();
      }
    };
  }

  private EventHandler<ActionEvent> editEdgeAction() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        EditEdgeWindow window = new EditEdgeWindow();
        window.setOnOk(new Method(){
          @Override
          public void invoke() {
            if (!_graph.editEdge(window.getFrom(), window.getTo(), window.getWeight()))
              Window.showError("Error", "Invalid or not existing edge.");
            else
              _graph.updateEdges();
          }
        });
        window.start();
      }
    };
  }

  public Core(MenuBar menu) {
    // Graph creation
    _graph = new Graph((int)_nodesCanvas.getBoundsInParent().getWidth(), (int)_nodesCanvas.getBoundsInParent().getHeight());

    // Menu creation
    Menu file = new Menu("File");
    Menu edit = new Menu("Edit");
    Menu run = new Menu("Run");

    // File menu's items
    MenuItem fileNew = new MenuItem("New graph");
    fileNew.setOnAction(newAction());
    MenuItem fileSave = new MenuItem("Save");
    fileSave.setOnAction(saveAction());
    MenuItem fileOpen = new MenuItem("Open");
    fileOpen.setOnAction(openAction());
    MenuItem fileInfo = new MenuItem("Info");
    fileInfo.setOnAction(infoAction());
    MenuItem fileQuit = new MenuItem("Quit");
    fileQuit.setOnAction(quitAction());
    file.getItems().addAll(fileNew, fileSave, fileOpen, fileInfo, fileQuit);

    // Edit menu's items
    MenuItem editAddNode = new MenuItem("Add node");
    editAddNode.setOnAction(addNodeAction());
    MenuItem editRemoveNode = new MenuItem("Remove node");
    editRemoveNode.setOnAction(removeNodeAction());
    MenuItem editAddEdge = new MenuItem("Add edge");
    editAddEdge.setOnAction(addEdgeAction());
    MenuItem editRemoveEdge = new MenuItem("Remove edge");
    editRemoveEdge.setOnAction(removeEdgeAction());
    MenuItem editEditEdge = new MenuItem("Edit edge's weigth");
    editEditEdge.setOnAction(editEdgeAction());
    edit.getItems().addAll(editAddNode, editRemoveNode, editAddEdge, editRemoveEdge, editEditEdge);

    menu.getMenus().addAll(file, edit, run); 
  }

  public boolean saveGraph(File file) {
    try {
      FileOutputStream stream = new FileOutputStream(file);
      ObjectOutputStream serializer = new ObjectOutputStream(stream);
      
      // Serialize graph
      serializer.writeObject(_graph);

      serializer.close();
      stream.close();
    }
    catch (Exception e) {
      Window.showError("Error", e.getMessage());
      return false;
    }

    return true;
  }

  public boolean openGraph(File file) {
    Graph graph;
    
    try {
      FileInputStream stream = new FileInputStream(file);
      ObjectInputStream deserializer = new ObjectInputStream(stream);

      // Deserialize graph in a temporary variable
      graph = (Graph)deserializer.readObject();

      deserializer.close();
      stream.close();
    }
    catch (Exception e) {
      Window.showError("Error", e.getMessage());
      return false;
    }

    // If no exceptions occurred replace the old graph
    _graph.clear();
    _graph = graph;

    return true;
  }

  // Layout management
  public static void setEdgesLayout(Pane canvas) {
    _edgesCanvas = canvas;
    Edge.setLayout(canvas);
  }

  public static void setNodesLayout(Pane canvas) {
    _nodesCanvas = canvas;
    Node.setLayout(canvas);
  }
}