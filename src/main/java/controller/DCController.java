package controller;

import cn.hutool.core.util.StrUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utils.Kinds_Coder;

import static utils.Zerologon.ZerologonExec;

public class DCController {
    private final Kinds_Coder coder = new Kinds_Coder();

    @FXML
    private TextField Ms17010_IP;

    @FXML
    private TextField Filed_Ms17010command;

    @FXML
    private ChoiceBox<String> choiceBox_coder1;

    @FXML
    private Button Button_17010attack;

    @FXML
    private TextArea textArea_Ms17010;

    @FXML
    private TextField Ms17010_IPS;

    @FXML
    private Button Button_Ms17010Scan;

    @FXML
    private TextField textField_DCiP;

    @FXML
    private TextField textField_Domain;

    @FXML
    private ChoiceBox<String> choiceBox_coder2;

    @FXML
    private Button Button_ZeroAtt;

    @FXML
    private TextArea textArea_ZeroResult;

    @FXML
    private TextField textField_DomainMachineName;

    @FXML
    void Ms17010Att_clicked(MouseEvent event) {

    }

    @FXML
    void Zerologon_clicked(MouseEvent event) {

        //获取IP
        String DC_IP;
        if (StrUtil.isBlank(textField_DCiP.getText())) {
            Platform.runLater(() -> {
                textField_DCiP.appendText("\n");
                textField_DCiP.appendText("请填写IP：");
            });
            return;
        }
        DC_IP = textField_DCiP.getText();

        //获取Domain
        String Domain;
        if (StrUtil.isBlank(textField_Domain.getText())) {
            Platform.runLater(() -> {
                textField_Domain.appendText("\n");
                textField_Domain.appendText("请填写Domain：");
            });
            return;
        }
        Domain = textField_Domain.getText();

        //获取机器用户名
        String DomainMachineName;
        if (StrUtil.isBlank(textField_DomainMachineName.getText())) {
            Platform.runLater(() -> {
                textField_DomainMachineName.appendText("\n");
                textField_DomainMachineName.appendText("请填写机器用户名：");
            });
            return;
        }
        DomainMachineName = textField_DomainMachineName.getText();

        String encoder2 = choiceBox_coder2.getValue();
        String ZeroResult = ZerologonExec(DC_IP, Domain, DomainMachineName, encoder2);
        Platform.runLater(() -> {
            textArea_ZeroResult.appendText("\n");
            if (ZeroResult.contains("OK")){
                textArea_ZeroResult.appendText(ZeroResult);
            } else {
                textArea_ZeroResult.appendText("漏洞攻击失败");
            }

        });

    }

    @FXML
    public void initialize() {
        ObservableList<String> items = FXCollections.observableArrayList(coder.getKindList());
        choiceBox_coder1.setItems(items);
        choiceBox_coder1.setValue(items.get(0));

        ObservableList<String> items2 = FXCollections.observableArrayList(coder.getKindList());
        choiceBox_coder2.setItems(items2);
        choiceBox_coder2.setValue(items2.get(0));

        Platform.runLater(() -> {
            textArea_Ms17010.appendText("\n");
            textArea_Ms17010.appendText("MS17010还没找到合适的脚本，找到了填充");
        });

        //适配屏幕
        System.setProperty("prism.allowhidpi", "true");
    }
}
