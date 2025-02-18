import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Driver extends Application{
    private Button encryptButton;
    private Button decryptButton;
    
    public static void main(String[] args) {
        try {
           launch(args);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Encryption");
        encryptButton = new Button("Encrypt");
        decryptButton = new Button("Decrypt");
        VBox root = new VBox();
        Scene scene = new Scene(root, 500, 500);
        Button optButton = new Button("One Time Pad");
        Button realFenceButton = new Button("Rail Fence");
        root.getChildren().addAll(optButton, realFenceButton);
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        primaryStage.setScene(scene);
        
        optButton.setOnAction(e -> {
            OneTimePad otp = new OneTimePad();
            primaryStage.hide();
            Button fileButton = new Button("Encrypt File");
            Button textButton = new Button("Encrypt Text");
            Button back = new Button("Back");
            root.getChildren().clear();
            root.getChildren().addAll(fileButton, textButton, back);
            fileButton.setOnAction(e1 -> {
                Button back2 = new Button("Back");
                FileChooser fileChooser = new FileChooser();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                fileChooser.setTitle("Open Resource File");
                root.getChildren().clear();
                root.getChildren().addAll(encryptButton, decryptButton, back2);
                
                encryptButton.setOnAction(e3 -> {
                    File file = fileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                        try (FileInputStream in = new FileInputStream(file)) {
                            byte[] fileData = in.readAllBytes();
                            in.close();
                            byte[] key = otp.generateKey(fileData.length);
                            otp.setKey(key);
                            FileOutputStream out = new FileOutputStream(file);
                            out.write(otp.oneTimePadEncryptBinary(key, fileData, fileData.length));
                            out.close();
                            alert.setHeaderText("Success");
                            alert.setContentText("File encrypted successfully");
                            alert.showAndWait();
                        } catch (IOException e2) {
                            alert.setAlertType(Alert.AlertType.ERROR);
                            alert.setHeaderText("An error occurred");
                            alert.setContentText(e2.getMessage());
                            alert.showAndWait();
                        }
                    }
                });

                decryptButton.setOnAction(e4 -> {
                    if (otp.getKey() == null) {
                        showError("No key found");
                        return;                        
                    }
                    
                    File file = fileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                        try (FileInputStream in = new FileInputStream(file)) {
                            byte[] fileData = in.readAllBytes();
                            in.close();
                            FileOutputStream out = new FileOutputStream(file);
                            out.write(otp.oneTimePadDecryptBinary(otp.getKey(), fileData, fileData.length));
                            out.close();
                            alert.setHeaderText("Success");
                            alert.setContentText("File decrypted successfully");
                            alert.showAndWait();
                        } catch (IOException e2) {
                            alert.setAlertType(Alert.AlertType.ERROR);
                            alert.setHeaderText("An error occurred");
                            alert.setContentText(e2.getMessage());
                            alert.showAndWait();
                        }
                    }
                });

                back2.setOnAction(e2 -> {
                    primaryStage.hide();
                    root.getChildren().clear();
                    root.getChildren().addAll(fileButton, textButton, back);
                    primaryStage.show();
                });
               
            });

            textButton.setOnAction(e1 -> {
                TextArea cipherTextArea = new TextArea();
                Button back2 = new Button("Back");
                primaryStage.hide();
                root.getChildren().clear();
                root.getChildren().addAll(encryptButton, decryptButton, back);

                encryptButton.setOnAction(e2 -> {
                    primaryStage.hide();
                    Button encrypt = new Button("Encrypt");
                    Label keyLabel = new Label("Key length");
                    TextField keyField = new TextField();
                    HBox keyBox = new HBox(10, keyLabel, keyField);
                    keyField.setMaxWidth(200);
                    keyField.setFocusTraversable(false);
                    TextArea plainTextArea = new TextArea();
                    plainTextArea.setFocusTraversable(false);
                    cipherTextArea.setFocusTraversable(false);
                    cipherTextArea.setEditable(false);
                    keyField.setPromptText("Enter key length");
                    plainTextArea.setPromptText("Enter plain text");
                    TextArea keyArea = new TextArea();
                    keyArea.setEditable(false);
                    keyArea.setFocusTraversable(false);
                    root.getChildren().clear();
                    root.setPrefWidth(500);
                    root.setPrefHeight(600);
                    root.getChildren().addAll(keyBox, new Label("Plain text"), plainTextArea, 
                    new Label("Cipher text"), cipherTextArea, new Label("Key"), keyArea, encrypt, back2);
                    encrypt.setOnAction(e3 -> {
                        String keyLength = keyField.getText();
                        String plainText = plainTextArea.getText();
                        if (!plainText.isEmpty()) {
                            if (keyLength.isEmpty()) {
                                byte[] key = otp.generateKey(plainText.length()*2);
                                otp.setKey(key);
                                otp.setCipherText(otp.oneTimePadEncryptBinary(key, plainText.getBytes(), plainText.length()));
                                String cipherText = new String(otp.getCipherText());
                                cipherTextArea.setText(cipherText);
                            } else {
                                try {
                                    byte[] key = otp.generateKey(Integer.parseInt(keyLength));
                                    otp.setKey(key);
                                    otp.setCipherText(otp.oneTimePadEncryptBinary(key, plainText.getBytes(), Integer.parseInt(keyLength)));
                                    String cipherText = new String(otp.getCipherText());
                                    cipherTextArea.setText(cipherText);
                                } catch (Exception e4) {
                                    showError(e4.getMessage());
                                }
                            }
                            keyArea.setText("Key: " + new String(otp.getKey()));
                        }
                    });
                    back2.setOnAction(e4 -> {
                        primaryStage.hide();
                        root.getChildren().clear();
                        root.getChildren().addAll(encryptButton, decryptButton, back);
                        primaryStage.show();
                    });
                    primaryStage.show();
                });
                
                decryptButton.setOnAction(e2 -> {
                    primaryStage.hide();
                    cipherTextArea.setFocusTraversable(false);
                    TextArea plainTextArea = new TextArea();
                    Button decrypt = new Button("Decrypt");
                    plainTextArea.setFocusTraversable(false);
                    plainTextArea.setEditable(false);
                    cipherTextArea.setPromptText("Enter cipher text");
                    cipherTextArea.setEditable(true);
                    root.getChildren().clear();
                    root.setPrefWidth(500);
                    root.setPrefHeight(600);
                    root.getChildren().addAll(new Label("Cipher text"), cipherTextArea,
                    new Label("Plain text"), plainTextArea, decrypt, back);
                    decrypt.setOnAction(e3 -> {
                        byte[] key = otp.getKey();
                        String cipherText = cipherTextArea.getText();
                        if (!cipherText.isEmpty()) {
                            if (key == null) {
                                showError("No key found");
                            } else {
                                try {
                                    String plainText = new String(otp.oneTimePadDecrypt(key, otp.getCipherText(), otp.getCipherText().length));
                                    plainTextArea.setText(plainText);
                                } catch (Exception e4) {
                                    showError(e4.getMessage());
                                }
                            }
                        }
                    });
                    back2.setOnAction(e3 -> {
                        primaryStage.hide();
                        root.getChildren().clear();
                        root.getChildren().addAll(encryptButton, decryptButton, back);
                        primaryStage.show();
                    });
                    primaryStage.show();
                });
                primaryStage.show();
            });
            
            back.setOnAction(e1 -> {
                primaryStage.hide();
                root.getChildren().clear();
                root.getChildren().addAll(optButton, realFenceButton);
                primaryStage.show();
            });
            primaryStage.show();
        });

        realFenceButton.setOnAction(e -> {
            RailFence rf = new RailFence();
            Button encrypt = new Button("Encrypt");
            Button decrypt = new Button("Decrypt");
            Button back = new Button("Back");
            TextArea cipherTextArea = new TextArea();
            Label cipherLabel = new Label("Cipher text");
            primaryStage.hide();
            root.getChildren().clear();
            root.getChildren().addAll(encrypt, decrypt, back);

            encrypt.setOnAction(e1 ->{
                primaryStage.hide();
                Button encryptButton = new Button("Encrypt");
                Label keyLabel = new Label("Key");
                TextField keyField = new TextField();
                Label plainLabel = new Label("Plain text");
                TextArea plainTextArea = new TextArea();
                Button showMatrix = new Button("Show Matrix");
                Button back2 = new Button("Back");
                root.getChildren().clear();
                root.getChildren().addAll(keyLabel, keyField, plainLabel, plainTextArea, cipherLabel, cipherTextArea, encryptButton, showMatrix, back2);
                encryptButton.setOnAction(e2 -> {
                    String key = keyField.getText();
                    String plainText = plainTextArea.getText();
                    if (!plainText.isEmpty() && !key.isEmpty()) {
                        try {
                            String cipherText = rf.railFenceEncrypt(plainText, Integer.parseInt(key));
                            cipherTextArea.setText(cipherText);
                        } catch (Exception e3) {
                            showError(e3.getMessage());
                        }
                    }
                });
                showMatrix.setOnAction(e2 -> {
                    Label matrixLabel = new Label("Matrix");
                    TextArea matrixArea = new TextArea();
                    Button back3 = new Button("Back");
                    root.getChildren().clear();
                    root.getChildren().addAll(matrixLabel, matrixArea, back3);
                    if (rf.getRailFence() == null) {
                        showError("Encrypt the text first");
                        return;
                    }
                    for (int i = 0; i < rf.getRailFence().length; i++) {
                        for (int j = 0; j < rf.getRailFence()[i].getRail().length; j++) {
                            if (rf.getRailFence()[i].getRail()[j] == null) {
                                matrixArea.appendText("  ");
                            } else {
                                matrixArea.appendText(rf.getRailFence()[i].getRail()[j]);
                            }
                        }
                        matrixArea.appendText("\n");
                    }
                    back3.setOnAction(e3 -> {
                        primaryStage.hide();
                        root.getChildren().clear();
                        root.getChildren().addAll(keyLabel, keyField, plainLabel, plainTextArea, cipherLabel, cipherTextArea, encryptButton, showMatrix, back2);
                        primaryStage.show();
                    });
                    primaryStage.show();
                });
                back2.setOnAction(e2 -> {
                    primaryStage.hide();
                    root.getChildren().clear();
                    root.getChildren().addAll(encrypt, decrypt, back);
                    primaryStage.show();
                });
                primaryStage.show();
            });
            
            decrypt.setOnAction(e1 -> {
                primaryStage.hide();
                Button decryptButton = new Button("Decrypt");
                Label plainLabel = new Label("Plain text");
                TextArea plainTextArea = new TextArea();
                Button back2 = new Button("Back");
                root.getChildren().clear();
                root.getChildren().addAll(cipherLabel, cipherTextArea, plainLabel, plainTextArea, decryptButton, back2);
                decryptButton.setOnAction(e2 -> {
                    String cipherText = cipherTextArea.getText();
                    if (rf.getPunctuation() == null) {
                        showError("Encrypt the text first");
                        return;
                    }
                    if (!cipherText.isEmpty()) {
                        try {
                            String plainText = rf.railFenceDecrypt(cipherText);
                            plainTextArea.setText(plainText);
                        } catch (Exception e3) {
                            showError(e3.getMessage());
                        }
                    }
                });
                back2.setOnAction(e2 -> {
                    primaryStage.hide();
                    root.getChildren().clear();
                    root.getChildren().addAll(encrypt, decrypt, back);
                    primaryStage.show();
                });
                primaryStage.show();
            });
            back.setOnAction(e1 -> {
                primaryStage.hide();
                root.getChildren().clear();
                root.getChildren().addAll(optButton, realFenceButton);
                primaryStage.show();
            });    
            primaryStage.show();
        });
        primaryStage.show();
    }

    // method to show error message
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}
