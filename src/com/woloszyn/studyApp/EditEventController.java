package com.woloszyn.studyApp;

import com.woloszyn.studyApp.dataModel.Category;
import com.woloszyn.studyApp.dataModel.EventData;
import com.woloszyn.studyApp.dataModel.ImportantEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EditEventController {


    @FXML
    private ComboBox categoryPicker;

    @FXML
    private TextField newEventName;

    @FXML
    private TextArea newEventNotes;

    private static ImportantEvent selectedEvent;

    public void initialize() {
//        categoryPicker.setItems(EventData.getInstance().getCategories());
        categoryPicker.getItems().addAll(EventData.getInstance().getCategories());
        System.out.println("Initialize was called");
        newEventName.setText(selectedEvent.getName());
        newEventNotes.setText(selectedEvent.getDescription());
        categoryPicker.getSelectionModel().select(selectedEvent.getCategory());

    }


    public ImportantEvent processEnteredData() {
        try {
            String name = newEventName.getText().trim();
            String notes = newEventNotes.getText().trim();
            String categoryString = categoryPicker.getSelectionModel().getSelectedItem().toString();
            if (name.equals("") || categoryString.equals("") || notes.equals(""))
                throw new EditEventController.EmptyFieldException();
            Category category = new Category(categoryString);
            ImportantEvent newImportantEvent = new ImportantEvent(name, notes, category);
            // NOTE need to actually validate if element already exists!!! just like in add event
//            setSelectedEvent(newImportantEvent);
            return newImportantEvent;
        } catch (EditEventController.EmptyFieldException e) {
            System.out.println("EmptyFieldException was thrown");
            return null;
        }

    }

    public boolean validateEnteredData() {
        return !newEventName.getText().trim().isEmpty() && !newEventNotes.getText().trim().isEmpty() && !categoryPicker.getSelectionModel().isEmpty();
    }


    private class EmptyFieldException extends Exception {
        EmptyFieldException() {
        }

        EmptyFieldException(String message) {
            super(message);
        }
    }

    public static void setSelectedEvent(ImportantEvent passedEvent) {
        System.out.println("Static field was set");
        selectedEvent = passedEvent;
    }
}
