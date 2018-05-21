package algraph.windows;

import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import window.Window;
import window.Method;

/**
 * Window used to create a new graph(randomized).
 */
public class NewWindow extends Window {
  public static final int MAXNODES = 10;
  public static final int MINWEIGHT = -64;
  public static final int MAXWEIGHT = 64;

  // GUI elements
  private TextField _txtNodes;
  private TextField _txtEdges;
  private TextField _txtMinWeight;
  private TextField _txtMaxWeight;

  // Attributes
  private int _nodes;
  private int _edges;
  private int _minWeight;
  private int _maxWeight;

  // Close button event
  private EventHandler<ActionEvent> btnOkAction() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        boolean ok = true;

        // Parse data
        int nodes = 0,
          edges = 0,
          minW = 0, 
          maxW = 0;
        try {
          nodes = Integer.parseInt(_txtNodes.getText());
          edges = Integer.parseInt(_txtEdges.getText());
          minW = Integer.parseInt(_txtMinWeight.getText());
          maxW = Integer.parseInt(_txtMaxWeight.getText());
        }
        catch (Exception e) {
          showError("Error", "Insert integers in the text fields.");
          ok = false;
        }

        // If parsed correctly
        if (ok)
          if (nodes > MAXNODES || minW < MINWEIGHT || maxW < MINWEIGHT || minW > MAXWEIGHT || maxW > MAXWEIGHT) {
            showError("Error", "One or more of the given integers are out of bound");
            ok = false;
          }
        if (ok)
          if (minW > maxW) {
            showError("Error", "Minimum weight grater than maximum");
            ok = false;
          }
        if (ok) {
          _nodes = nodes;
          _edges = edges;
          _minWeight = minW;
          _maxWeight = maxW;
          close();
        }
      }
    };
  }

  @Override
  public void start() {
    // Window creation
    _stage = new Stage();
    _stage.initModality(Modality.APPLICATION_MODAL);
    _stage.setTitle("New graph");

    // Scene setup
    VBox root = new VBox();
    root.setSpacing(5);
    root.setPadding(new Insets(10, 0, 0, 10));

    Scene scene = new Scene(root, 350, 250);
    _stage.setScene(scene);
    _stage.show();

    // GUI
    Text lblNodes = new Text("Insert number of nodes(max 10):");
    _txtNodes = new TextField("10");
    _txtNodes.setMaxWidth(100);

    Text lblEdges = new Text("Insert number of edges(max nodes * (nodes - 1)):");
    _txtEdges = new TextField("15");
    _txtEdges.setMaxWidth(100);

    Text lblMinWeight = new Text("Minimum weight[-64, +64]:");
    _txtMinWeight = new TextField("-64");
    _txtMinWeight.setMaxWidth(100);

    Text lblMaxWeight = new Text("Maximum weight[-64, +64]:");
    _txtMaxWeight = new TextField("64");
    _txtMaxWeight.setMaxWidth(100);

    Button btnOk = new Button("Ok");
    btnOk.setOnAction(btnOkAction());

    root.getChildren().addAll(lblNodes, _txtNodes, lblEdges, _txtEdges, 
      lblMinWeight, _txtMinWeight, lblMaxWeight, _txtMaxWeight, btnOk);

  }

  public void setOnOk(Method ok) {
    _onOk = ok;
  }

  // Get attributes
  public int getNodes() {
    return _nodes;
  }

  public int getEdges() {
    return _edges;
  }

  public int getMinWeight() {
    return _minWeight;
  }

  public int getMaxWeight() {
    return _maxWeight;
  }
}