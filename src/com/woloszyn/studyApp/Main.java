package com.woloszyn.studyApp;

import com.woloszyn.studyApp.dataModel.EventData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("Study App");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        try{
            EventData.getInstance().loadEvents();
        } catch(Exception e ){
            e.printStackTrace();
        }
        try {
            EventData.getInstance().loadCategories();
        } catch (Exception e) {
            e.printStackTrace();
        }
//NOTE remove this when not needed!
//        System.out.println(javafx.scene.text.Font.getFamilies());
        super.init();
    }

    @Override
    public void stop() throws Exception {
        try{
            EventData.getInstance().saveEvents();
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            EventData.getInstance().saveCategories();
        } catch (Exception e){
            e.printStackTrace();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


