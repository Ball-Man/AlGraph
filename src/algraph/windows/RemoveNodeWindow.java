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

public class RemoveNodeWindow extends Window {
  // GUI elements
  private TextField _txtNode;

  // Attributes
  private int _node;

  // Close button event
  private EventHandler<ActionEvent> btnOkAction() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        // Parse data
        int node = 0;
        try {
          node = Integer.parseInt(_txtNode.getText());
        }
        catch (Exception e) {
          showError("Error", "Insert an integer in the text field.");
          return;
        }

        _node = node;
        close();
      }
    };
  }

  @Override
  public void start() {
    // Window creation
    _stage = new Stage();
    _stage.initModality(Modality.APPLICATION_MODAL);
    _stage.setTitle("Remove node");

    // Scene setup
    VBox root = new VBox();
    root.setSpacing(5);
    root.setPadding(new Insets(10, 0, 0, 10));

    Scene scene = new Scene(root, 350, 100);
    _stage.setScene(scene);
    _stage.show();

    // GUI
    Text lblNode = new Text("ID of the node:");
    _txtNode = new TextField("0");
    _txtNode.setMaxWidth(100);

    Button btnOk = new Button("Remove");
    btnOk.setOnAction(btnOkAction());

    root.getChildren().addAll(lblNode, _txtNode, btnOk);

  }

  public void setOnOk(Method ok) {
    _onOk = ok;
  }

  // Get attributes
  public int getNode() {
    return _node;
  }
}