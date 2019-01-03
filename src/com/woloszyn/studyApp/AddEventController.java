package com.woloszyn.studyApp;

import com.woloszyn.studyApp.dataModel.Category;
import com.woloszyn.studyApp.dataModel.EventData;
import com.woloszyn.studyApp.dataModel.ImportantEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddEventController {
    @FXML
    private ComboBox categoryPicker;

    @FXML
    private TextField newEventName;

    @FXML
    private TextArea newEventNotes;


    public void initialize() {
        categoryPicker.getItems().addAll(EventData.getInstance().getCategories());
    }


    public ImportantEvent processEnteredData() {
        try {
            String name = newEventName.getText().trim();
            String notes = newEventNotes.getText().trim();
            String categoryString = categoryPicker.getSelectionModel().getSelectedItem().toString();
            if (name.equals("") || categoryString.equals("") || notes.equals(""))
                throw new EmptyFieldException();
            Category category = new Category(categoryString);
            ImportantEvent newImportantEvent = new ImportantEvent(name, notes, category);
            return newImportantEvent;
        } catch (EmptyFieldException e) {
            System.out.println("EmptyFieldException was thrown");
            return null;
        }

    }

    public boolean validateEnteredData(){
        if(newEventName.getText().trim().isEmpty() || newEventNotes.getText().trim().isEmpty() || categoryPicker.getSelectionModel().isEmpty()){
            return false;
        } else{
            return true;
        }
    }


   private class EmptyFieldException extends Exception {
        EmptyFieldException() {
        }

        EmptyFieldException(String message) {
            super(message);
        }
    }
}