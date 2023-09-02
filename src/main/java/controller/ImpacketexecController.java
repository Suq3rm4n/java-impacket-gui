package controller;

import cn.hutool.core.util.StrUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import utils.Kinds_Coder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static controller.MainController.scriptPath;
import static utils.ImpacketPath.*;

public class ImpacketexecController {

    private final Kinds_Coder coder =new Kinds_Coder();


    @FXML
    private TextField textField_password_IP;

    @FXML
    private TextField textField_pasword_username;

    @FXML
    private TextField textField_Password;

    @FXML
    private TextField textField_password_command;

    @FXML
    private ChoiceBox<String> choiceBox_coder;

    @FXML
    private Button Button_PassAtt;

    @FXML
    private TextArea textArea_Passwordpassing;

    @FXML
    private Button Button_PassConnect;

    @FXML
    private Button Button_PassDisconnect;

    @FXML
    private TextField textField_Hash_IP;

    @FXML
    private TextField textField_Hash_username;

    @FXML
    private TextField textField_Hash;

    @FXML
    private TextField textField_Hash_command;

    @FXML
    private ChoiceBox<String> choiceBox2_coder;

    @FXML
    private Button Button_HashAtt;

    @FXML
    private TextArea textArea_Hashpassing;

    @FXML
    private Button Button_HashConnect;

    @FXML
    private Button Button_HashDisconnect;

    public static Process shellProcess;
    public static PrintWriter shellWriter;
    public static ExecutorService executorService;

    String Tool = "";
    public String getTool_name(String scriptPath){
        if (scriptPath.equals("Wmiexec")){
            Tool = Wmixexec;
        } else if (scriptPath.equals("Psexec")){
            Tool = Psexec;
        } else if (scriptPath.equals("ATexec")){
            Tool = Atexec;
        } else if (scriptPath.equals("SMBexec")){
            Tool = SMBexec;
        } else if (scriptPath.equals("DCOMexec")){
            Tool = DCOMexec;
        }
        return Tool;
    }



    private void connectToShell_Password() {
        String encoder1 = choiceBox_coder.getValue();
        String Tool_name = getTool_name(scriptPath);
        String impacketCommand = "python" + " " + Tool_name + " " + textField_pasword_username.getText() + ":" + textField_Password.getText() + "@" + textField_password_IP.getText();
        try {
            shellProcess = Runtime.getRuntime().exec(impacketCommand); // 替换为你要执行的 Shell 命令
            shellWriter = new PrintWriter(shellProcess.getOutputStream());

                executorService.submit(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(shellProcess.getInputStream(),encoder1))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String finalLine = line;
                        Platform.runLater(() -> textArea_Passwordpassing.appendText(finalLine + "\n"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnectFromShell_Paasword() {
        if (shellProcess != null) {
            shellProcess.destroy();
            shellProcess = null;
            shellWriter = null;
            Platform.runLater(() -> {
                textArea_Passwordpassing.appendText("已退出！");
            });
        }
    }
    private void sendCommandToShell_Password() {
        if (shellWriter != null) {
            String command = textField_password_command.getText();
            Platform.runLater(() -> textArea_Passwordpassing.appendText("命令： " + command + "\n"));
            shellWriter.println(command);
            shellWriter.flush();
            textField_password_command.clear();
        }
    }

    @FXML
    void PassAtt_clickedConnect(MouseEvent event) {
        connectToShell_Password();
    }

    @FXML
    void PassAtt_clicked(MouseEvent event) {
        sendCommandToShell_Password();
    }

    @FXML
    void PassAtt_clickedDisconnect(MouseEvent event) {
        disconnectFromShell_Paasword();
    }



    private void connectToShell_Hash() {
        String encoder2 = choiceBox2_coder.getValue();
        String Tool_name = getTool_name(scriptPath);
        //python3 wmiexec.py -hashes be3b434bf59824a7d996a36bc2aa9057:93969368ddf7e1fbfa5f42513ae3c387 administrator@192.168.1.62
        String impacketCommand = "python " + Tool_name + " -hashes " + " " +textField_Hash.getText() + " " + textField_Hash_username.getText() + "@" + textField_Hash_IP.getText();
        try {
            shellProcess = Runtime.getRuntime().exec(impacketCommand); // 替换为你要执行的 Shell 命令
            shellWriter = new PrintWriter(shellProcess.getOutputStream());

            executorService.submit(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(shellProcess.getInputStream(),encoder2))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String finalLine = line;
                        Platform.runLater(() -> textArea_Hashpassing.appendText(finalLine + "\n"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnectFromShell_Hash() {
        if (shellProcess != null) {
            shellProcess.destroy();
            shellProcess = null;
            shellWriter = null;
            Platform.runLater(() -> {
                textArea_Hashpassing.appendText("已退出！");
            });
        }
    }


    private void sendCommandToShell_Hash() {
        if (shellWriter != null) {
            String command = textField_Hash_command.getText();
            shellWriter.println(command);
            shellWriter.flush();
            textField_Hash_command.clear();
        }
    }


    @FXML
    void HashAtt_clickedConnect(MouseEvent event) {
        connectToShell_Hash();
    }

    @FXML
    void HashAtt_clicked(MouseEvent event) {
        sendCommandToShell_Hash();
    }

    @FXML
    void HashAtt_clickedDisConnect(MouseEvent event) {
        disconnectFromShell_Hash();
    }












    @FXML
    public void initialize() {
        textArea_Passwordpassing.appendText(
                "\n 本工具用于内网渗透，使用交互式shell，使用先点击connect，再输入命令点击send，结束记得DisConnect\n");

        executorService = Executors.newSingleThreadExecutor();

        //设置自动换行
        textArea_Passwordpassing.setWrapText(true);
        textArea_Passwordpassing.setWrapText(true);

        ObservableList<String> items = FXCollections.observableArrayList(coder.getKindList());
        choiceBox_coder.setItems(items);
        choiceBox_coder.setValue(items.get(1));

        ObservableList<String> items2 = FXCollections.observableArrayList(coder.getKindList());
        choiceBox2_coder.setItems(items2);
        choiceBox2_coder.setValue(items2.get(1));


        //适配屏幕
        System.setProperty("prism.allowhidpi", "true");

//        //渲染Tab1 左上 下拉选
//        //设置初始化数据
//        choiceBox_coder.setItems(exp.getFXKindList());
//        //设置默认选项
//        choiceBox_kinds.setValue(exp.getKindList().get(0));
//        //选项绑定监听器，当左上 下拉选 数据发生改变，更新列表数据，同时更新exp下拉选数据
//        choiceBox_kinds.getSelectionModel().selectedItemProperty()
//                .addListener((observable, oldValue, newValue) -> buildChoiceBoxListener(newValue));
//
//        // 第一次渲染该页面时渲染数据
//        if (!initialized) {
//            //更新列表数据
//            buildChoiceBoxListener(exp.getKindList().get(0));
//            initialized = true;
//        }

    }




}
