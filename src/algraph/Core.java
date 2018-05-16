package algraph;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
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
        
      }
    };
  }

  private EventHandler<ActionEvent> loadAction() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        
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
    MenuItem fileLoad = new MenuItem("Load");
    fileLoad.setOnAction(loadAction());
    MenuItem fileInfo = new MenuItem("Info");
    fileInfo.setOnAction(infoAction());
    MenuItem fileQuit = new MenuItem("Quit");
    fileQuit.setOnAction(quitAction());
    file.getItems().addAll(fileNew, fileSave, fileLoad, fileInfo, fileQuit);

    // Edit menu's items
    MenuItem editAddNode = new MenuItem("Add node");
    editAddNode.setOnAction(addNodeAction());
    MenuItem editRemoveNode = new MenuItem("Remove node");
    editRemoveNode.setOnAction(removeNodeAction());
    MenuItem editAddEdge = new MenuItem("Add edge");
    MenuItem editRemoveEdge = new MenuItem("Remove edge");
    MenuItem editEditEdge = new MenuItem("Edit edge");
    edit.getItems().addAll(editAddNode, editRemoveNode, editAddEdge, editRemoveEdge, editEditEdge);

    menu.getMenus().addAll(file, edit, run); 
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