package window;

import javafx.stage.Stage;
import javafx.scene.control.Alert;

/**
 * Abstract class defining a new window for the application.
 */
public abstract class Window {
  protected Stage _stage;
  protected Method _onOk;

  public abstract void start();

  public void close() {
    _stage.close();
    _onOk.invoke();
  }

  public static void showError(String title, String body) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setContentText(body);
    alert.showAndWait();
  }
}