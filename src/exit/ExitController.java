package exit;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * @author lenovo
 */
public class ExitController {
    public Button confirmExit;

    public void confirmExit(MouseEvent mouseEvent) {
        Platform.exit();
    }

    public void cancelExit(MouseEvent mouseEvent) {
        Stage stage = (Stage) confirmExit.getScene().getWindow();
        stage.close();
    }
}
