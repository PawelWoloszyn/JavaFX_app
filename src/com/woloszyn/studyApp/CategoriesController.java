package com.woloszyn.studyApp;

import com.woloszyn.studyApp.dataModel.Category;
import com.woloszyn.studyApp.dataModel.EventData;
import com.woloszyn.studyApp.dataModel.ImportantEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;


public class CategoriesController {

    @FXML
    private ComboBox categoryPicker;

    @FXML
    private TextField categoryName;

    @FXML
    private Button removeButton, addButton;

    @FXML
    CheckBox removeCheckBox;

    public void initialize() {
//        categoryPicker.setItems(EventData.getInstance().getCategories());
        categoryPicker.getItems().addAll(EventData.getInstance().getCategories());
    }

    @FXML
    public void handleClicked(ActionEvent e) {
        if (e.getSource().equals(addButton)) {
            String input;
            if ((input = validateInput()) != null) {
                EventData.getInstance().getCategories().add(new Category(input));
                categoryPicker.getItems().clear();
                categoryPicker.getItems().addAll(EventData.getInstance().getCategories());
                System.out.println("Category " + input + " was added");
            }
        } else if (e.getSource().equals(removeButton)) {
            try {
                Category selectedCategory = (Category) categoryPicker.getSelectionModel().getSelectedItem();
                if (removeCheckBox.isSelected() && !displayCertaintyAlert("Warning",
                        "Are you sure about this?\nIf you click OK all events associated with this category will be removed")) {
                    return;
                } else {
                    for (ImportantEvent checkedEvent : EventData.getInstance().getImportantEvents()) {
                        if (checkedEvent.getCategory().equals(selectedCategory)) {
                            EventData.getInstance().deleteImportantEvent(checkedEvent);
                        }
                    }
                }
                EventData.getInstance().getCategories().remove(selectedCategory);
                categoryPicker.getItems().clear();
                categoryPicker.getItems().addAll(EventData.getInstance().getCategories());
                System.out.println("Remove button was clicked");
            } catch (NullPointerException f) {
                System.out.println(f.toString());
            }
        }
    }


    private String validateInput() {
        if (!categoryName.getText().trim().isEmpty()) {
            String input = categoryName.getText().trim();
            if (input.length() < 50) {
                return input;
            }
            //note throw a popup window here
            Controller.displayAlert("Too long", "Category must be <50 characters");
            return null;
        }
        return null;
    }

    private static boolean displayCertaintyAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        Label label = new Label(message);
        label.setWrapText(true);
        alert.getDialogPane().setContent(label);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

}
