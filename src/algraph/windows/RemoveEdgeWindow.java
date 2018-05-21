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
 * Window used to remove an existing edge.
 */
public class RemoveEdgeWindow extends Window {
  // GUI elements
  private TextField _txtFrom;
  private TextField _txtTo;

  // Attributes
  private int _from;
  private int _to;

  // Close button event
  private EventHandler<ActionEvent> btnOkAction() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        // Parse data
        int from = 0,
          to = 0;
        try {
          from = Integer.parseInt(_txtFrom.getText());
          to = Integer.parseInt(_txtTo.getText());
        }
        catch (Exception e) {
          showError("Error", "Insert integers in the text fields.");
          return;
        }

        _from = from;
        _to = to;
        close();
      }
    };
  }

  @Override
  public void start() {
    // Window creation
    _stage = new Stage();
    _stage.initModality(Modality.APPLICATION_MODAL);
    _stage.setTitle("Remove edge");

    // Scene setup
    VBox root = new VBox();
    root.setSpacing(5);
    root.setPadding(new Insets(10, 0, 0, 10));

    Scene scene = new Scene(root, 350, 170);
    _stage.setScene(scene);
    _stage.show();

    // GUI
    Text lblFrom = new Text("Edge's starting node:");
    _txtFrom = new TextField("0");
    _txtFrom.setMaxWidth(100);

    Text lblTo = new Text("Edge's ending node:");
    _txtTo = new TextField("1");
    _txtTo.setMaxWidth(100);

    Button btnOk = new Button("Remove");
    btnOk.setOnAction(btnOkAction());

    root.getChildren().addAll(lblFrom, _txtFrom, lblTo, 
      _txtTo, btnOk);

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
}