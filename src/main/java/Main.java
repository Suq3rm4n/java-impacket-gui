import cn.hutool.core.io.resource.ResourceUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.Http.HttpTools;
import utils.Http.Response;

import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class Main extends Application {
    public Main() {
    }

    public void start(Stage primaryStage) throws Exception {
//        // 密钥校验
//        boolean passwordCorrect = checkPassword();
//        if (!passwordCorrect) {
//            System.exit(0); // 密钥不正确，关闭应用程序
//        }

        Parent root = (Parent) FXMLLoader.load(ResourceUtil.getResource("fxml/Main.fxml"));
        primaryStage.setTitle("By Superman 20230902");
        Scene scene = new Scene(root, 1280.0, 910.0);
        scene.getStylesheets().add(((URL) Objects.requireNonNull(Main.class.getResource("/css/main.css"))).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        // 监听窗口关闭事件
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            System.exit(0); // 完全退出应用程序
        });
    }

    private boolean checkPassword() {
        boolean flag = false;
        HashMap<String, String> headersMap = new HashMap<>();
        headersMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        headersMap.put("Accept-Language", "zh-CN,zh;q=0.9");

        // 创建对话框
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("密钥校验");
        dialog.setHeaderText("请输入密钥");
        dialog.setContentText("密钥:");

        // 显示对话框并获取用户输入的密钥
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String key = result.get();

            // 发起密钥校验的请求
            String url = "http://124.222.32.173:8080/api/login?key=" + key;
            Response response = HttpTools.get(url, headersMap, "utf-8");
            System.out.println(response.getCode());

            if (response.getCode() == 500) {
                flag = true;
            } else {
                // 密钥不正确，显示警告对话框
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("密钥不正确");
                alert.setHeaderText("密钥校验失败");
                alert.setContentText("请输入正确的密钥！");
                alert.showAndWait();
            }
        }

        return flag;
    }

    public static void main(String[] args) {
        launch(args);
    }
}