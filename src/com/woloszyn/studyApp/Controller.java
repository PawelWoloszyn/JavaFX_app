package com.woloszyn.studyApp;

// TODO: 27.12.18 Add possibility to sort events by Category or time
// TODO: 29.12.18 Do some actual validation on loading fxml file
// TODO: 29.12.18 When right clicking on event, Context menu pops up: add query, delete

import com.woloszyn.studyApp.dataModel.EventData;
import com.woloszyn.studyApp.dataModel.ImportantEvent;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.util.Optional;


public class Controller {


    @FXML
    private ListView<ImportantEvent> listOfEvents;

    @FXML
    private Label categoryDisplay;

    @FXML
    private TextArea textArea;

    @FXML
    private BorderPane mainBorderPaneWindow;

    @FXML
    ContextMenu listContextMenu;

    @FXML
    private Label queriesAmount;

    public void initialize() {


//        listContextMenu = new ContextMenu();
//        MenuItem deleteMenuItem = new MenuItem("Delete");
//        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                ImportantEvent item = listOfEvents.getSelectionModel().getSelectedItem();
//                deleteEvent(item);
//            }
//        });
//
//        listContextMenu.getItems().addAll(deleteMenuItem);
//


        textArea.setEditable(false);
        listOfEvents.setItems(EventData.getInstance().getImportantEvents());
        listOfEvents.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        listOfEvents.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ImportantEvent>() {
            @Override
            public void changed(ObservableValue<? extends ImportantEvent> observable, ImportantEvent oldValue, ImportantEvent newValue) {
                if (newValue != null) {
                    System.out.println("Listener was called");
                    ImportantEvent event = listOfEvents.getSelectionModel().getSelectedItem();
                    textArea.setText(event.getDescription());
                    categoryDisplay.setText(event.getCategory().toString());
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(event.getQueries().size());
                    String x = "" + event.getQueries().size();
                    queriesAmount.setText(x);
                }
            }
        });
    }

    @FXML
    private void categoriesEventDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPaneWindow.getScene().getWindow());
        dialog.setTitle("Manage categories");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("manageCategories.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog for some reason");
            e.printStackTrace();
            return;
        }
        ButtonType okButton = ButtonType.OK;
        ButtonType cancelButton = ButtonType.CANCEL;
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
        dialog.showAndWait();
    }

    @FXML
    public void showAddEventDialog() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPaneWindow.getScene().getWindow());
        dialog.setTitle("Add new event");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addEventDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog for some reason");
            e.printStackTrace();
            return;
        }

        ButtonType okButton = ButtonType.OK;
        ButtonType cancelButton = ButtonType.CANCEL;

        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        Button btnOK = (Button) dialog.getDialogPane().lookupButton(okButton);
        AddEventController controller = fxmlLoader.getController();
        btnOK.addEventFilter(ActionEvent.ACTION, event -> {
            if (!controller.validateEnteredData()) {
                System.out.println("Data is incomplete, please enter data into empty fields");
                displayAlert("Wrong data", "All fields mustn't be empty");
                event.consume();
            } else if (EventData.getInstance().checkIfExists(controller.processEnteredData())) {
                System.out.println("This event already exists");
                displayAlert("Wrong data", "An event with this name already exists in selected category");
                event.consume();
            } else {
                ImportantEvent newItem = controller.processEnteredData();
                EventData.getInstance().addImportantEvent(newItem);
                if (newItem != null) {
                    listOfEvents.getSelectionModel().select(newItem);
                }
            }
        });
        dialog.showAndWait();
    }

    @FXML
    private void showEditEventDialog() {
        ImportantEvent selectedEvent = listOfEvents.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            EditEventController.setSelectedEvent(selectedEvent);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainBorderPaneWindow.getScene().getWindow());
            dialog.setTitle("Edit event");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("editEventDialog.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch (IOException e) {
                System.out.println("Couldn't load the dialog for some reason");
                e.printStackTrace();
                return;
            }

            ButtonType updateButton = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = ButtonType.CANCEL;

            dialog.getDialogPane().getButtonTypes().addAll(cancelButton, updateButton);

            Button btnUpdate = (Button) dialog.getDialogPane().lookupButton(updateButton);
            EditEventController controller2 = fxmlLoader.getController();
            btnUpdate.addEventFilter(ActionEvent.ACTION, event -> {
                if (!controller2.validateEnteredData()) {
                    System.out.println("Data is incomplete, please enter data into empty fields");
                    displayAlert("Wrong data", "All fields mustn't be empty");
                    event.consume();
                } else {
                    ImportantEvent newItem = controller2.processEnteredData();
                    if (newItem != null) {
                        EventData.getInstance().updateImportantEvent(selectedEvent, newItem);
                        listOfEvents.getSelectionModel().clearSelection();
                        listOfEvents.getSelectionModel().select(newItem);
                        EditEventController.setSelectedEvent(newItem);
                    }
                    // NOTE ask me if I'm sure about saving changes, and check if I have actually changed something
                }
            });
            dialog.showAndWait();
        }
    }

    private void deleteEvent(ImportantEvent importantEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete event");
        alert.setHeaderText("Delete: " + importantEvent);
        alert.setContentText("Are you sure you want to delete this event?\nPress OK to confirm");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            EventData.getInstance().deleteImportantEvent(importantEvent);
        }
    }


    //note make an event handler to handle those two in one function
    @FXML
    private void handleDeleteButton() {
        ImportantEvent selectedEvent = listOfEvents.getSelectionModel().getSelectedItem();
        if (selectedEvent != null)
            deleteEvent(selectedEvent);
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.DELETE)) {
            handleDeleteButton();
        }
    }


    @FXML
    private void handleAbout() {
        // FIXME: 29.12.18 enable going to link with clicking on it or at least allow to copy it
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setContentText("This app is a part of repository:\nhttps://github.com/PawelWoloszyn\nThanks" +
                " for checking it out :)");
        Optional<ButtonType> result = alert.showAndWait();
    }


    public static void displayAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        Label label = new Label(message);
        label.setWrapText(true);
        alert.getDialogPane().setContent(label);
        alert.showAndWait();
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }

}
