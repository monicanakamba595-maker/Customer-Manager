
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CustomerManagerApp extends Application {

    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        Label title = new Label("Customer Manager");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("e.g., Mary Banda");
        Label nameLabel = new Label("Customer name");
        nameLabel.setLabelFor(nameField);

        ComboBox<String> provinceBox = new ComboBox<>();
        provinceBox.getItems().addAll("Central", "Copperbelt", "Eastern", "Luapula", "Lusaka",
                "Muchinga", "Northern", "North-Western", "Southern", "Western");
        provinceBox.setPromptText("Choose a province");
        Label provinceLabel = new Label("Province");
        provinceLabel.setLabelFor(provinceBox);

        Button saveButton = new Button("Save customer");
        saveButton.setDefaultButton(true);
        Button deleteButton = new Button("Delete selected");
        Label status = new Label();

        TableView<Customer> table = new TableView<>();
        table.setItems(customers);

        TableColumn<Customer, String> nameCol = new TableColumn<>("Customer name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Customer, String> provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));
        table.getColumns().add(nameCol);
        table.getColumns().add(provinceCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        saveButton.setOnAction(event -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                status.setText("Enter the customer name.");
                nameField.requestFocus();
                return;
            }
            String province = provinceBox.getValue();
            if (province == null) {
                status.setText("Choose a province.");
                provinceBox.requestFocus();
                return;
            }
            customers.add(new Customer(name, province));
            status.setText("Customer saved.");
            nameField.clear();
            provinceBox.setValue(null);
            nameField.requestFocus();
        });

        deleteButton.setOnAction(event -> {
            Customer selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                status.setText("Select a customer first.");
                return;
            }
            ButtonType delete = new ButtonType("Delete");
            Alert ask = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete the selected customer?", delete, ButtonType.CANCEL);
            ask.setHeaderText("Confirm deletion");
            if (ask.showAndWait().orElse(ButtonType.CANCEL) == delete) {
                customers.remove(selected);
                status.setText("Customer deleted.");
            } else {
                status.setText("Deletion cancelled.");
            }
        });

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.addRow(0, nameLabel, nameField);
        form.addRow(1, provinceLabel, provinceBox);
        HBox buttons = new HBox(10, saveButton, deleteButton);

        VBox root = new VBox(12, title, form, buttons, status, table);
        root.setPadding(new Insets(16));
        VBox.setVgrow(table, Priority.ALWAYS);

        stage.setTitle("Customer Manager");
        stage.setScene(new Scene(root, 480, 520));
        stage.show();
        nameField.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}