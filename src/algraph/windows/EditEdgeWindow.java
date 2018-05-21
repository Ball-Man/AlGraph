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
 * Window used to edit the weight of an existing edge.
 */
public class EditEdgeWindow extends Window {
  public final static int MAXWEIGHT = 64;
  public final static int MINWEIGHT = -64;

  // GUI elements
  private TextField _txtFrom;
  private TextField _txtTo;
  private TextField _txtWeight;

  // Attributes
  private int _from;
  private int _to;
  private int _weight;

  // Close button event
  private EventHandler<ActionEvent> btnOkAction() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        // Parse data
        int from = 0,
          to = 0,
          weight = 0;
        try {
          from = Integer.parseInt(_txtFrom.getText());
          to = Integer.parseInt(_txtTo.getText());
          weight = Integer.parseInt(_txtWeight.getText());
        }
        catch (Exception e) {
          showError("Error", "Insert integers in the text fields.");
          return;
        }

        if (weight > MAXWEIGHT || weight < MINWEIGHT) {
          showError("Error", "Weight out of bounds.");
          return;
        }

        _from = from;
        _to = to;
        _weight = weight;
        close();
      }
    };
  }

  @Override
  public void start() {
    // Window creation
    _stage = new Stage();
    _stage.initModality(Modality.APPLICATION_MODAL);
    _stage.setTitle("Edit edge");

    // Scene setup
    VBox root = new VBox();
    root.setSpacing(5);
    root.setPadding(new Insets(10, 0, 0, 10));

    Scene scene = new Scene(root, 350, 200);
    _stage.setScene(scene);
    _stage.show();

    // GUI
    Text lblFrom = new Text("Edge's starting node:");
    _txtFrom = new TextField("0");
    _txtFrom.setMaxWidth(100);

    Text lblTo = new Text("Edge's ending node:");
    _txtTo = new TextField("1");
    _txtTo.setMaxWidth(100);

    Text lblWeight = new Text("New weight[-64, +64]:");
    _txtWeight = new TextField("0");
    _txtWeight.setMaxWidth(100);

    Button btnOk = new Button("Edit");
    btnOk.setOnAction(btnOkAction());

    root.getChildren().addAll(lblFrom, _txtFrom, lblTo, 
      _txtTo, lblWeight, _txtWeight, btnOk);

  }

  public void setOnOk(Method ok) {
    _onOk = ok;
  }

  // Get attributes
  public int getFrom() {
    return _from;
  }

  public int getTo() {
    return _to;
  }

  public int getWeight() {
    return _weight;
  }
}