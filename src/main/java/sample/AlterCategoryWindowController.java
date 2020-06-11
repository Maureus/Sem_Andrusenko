package sample;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import database.DataBaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.jetbrains.annotations.NotNull;
import struc.Category;


public class AlterCategoryWindowController {
    private ObservableList<Category> categories = FXCollections.observableArrayList();
    private DataBaseHandler dataBaseHandler = new DataBaseHandler();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Category, Integer> col_id;

    @FXML
    private TextField nazevCategry;

    @FXML
    private TableColumn<Category, String> col_popis;

    @FXML
    private TextArea popisCategory;

    @FXML
    private TableColumn<Category, String> col_nazev;

    @FXML
    private TableView<Category> tvCategory;

    @FXML
    void addCategory(ActionEvent event) {
        if (!nazevCategry.getText().isEmpty()) {
            Category prepCat = new Category(nazevCategry.getText().trim(), popisCategory.getText().trim());
            String insertQueryUsers = "insert into KATEGORIE (NAZEV, POPIS) values (?, ?)";
            try {
                PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(insertQueryUsers);
                preparedStatement.setString(1, prepCat.getNazev());
                preparedStatement.setString(2, prepCat.getPopis());
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            fillCategorie();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Please enter name of category!");
            alert.showAndWait();
        }
    }

    @FXML
    void deleteSelected(ActionEvent event) {
        if (tvCategory.getSelectionModel().getSelectedItem()!=null){
            int catID = tvCategory.getSelectionModel().getSelectedItem().getCategoryID();
            String deleteQuery = "DELETE FROM ST58211.KATEGORIE WHERE ST58211.KATEGORIE.KATEGORIE_ID = ?";
            try {
                PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(deleteQuery);
                preparedStatement.setInt(1, catID);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            categories.remove(tvCategory.getSelectionModel().getSelectedItem());
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Please select category from the list!");
            alert.showAndWait();
        }

    }

    @FXML
    void initialize() {
        fillCategorie();
        tvCategory.setEditable(true);
        tvCategory.setItems(categories);

        col_nazev.setCellValueFactory(new PropertyValueFactory<>("nazev"));
        col_nazev.setCellFactory(TextFieldTableCell.forTableColumn());

        col_id.setCellValueFactory(new PropertyValueFactory<>("categoryID"));

        col_popis.setCellValueFactory(new PropertyValueFactory<>("popis"));
        col_popis.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    private void fillCategorie() {
        categories.clear();
        String selectQuery = "select * from KATEGORIE";

        try {
            final PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectQuery);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int catID = resultSet.getInt("KATEGORIE_ID");
                final String nazevCat = resultSet.getString("NAZEV");
                final String popisCat = resultSet.getString("POPIS");

                final Category newCat = new Category(nazevCat, popisCat, catID);
                categories.add(newCat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeNazevCellEvent(TableColumn.@NotNull CellEditEvent edditedCell) {
        Category selectedCat = tvCategory.getSelectionModel().getSelectedItem();
        selectedCat.setNazev(edditedCell.getNewValue().toString());
        String alterName = "UPDATE ST58211.KATEGORIE SET ST58211.KATEGORIE.NAZEV = ?" +
                "WHERE ST58211.KATEGORIE.KATEGORIE_ID = ?";
        try {
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(alterName);
            preparedStatement.setString(1, selectedCat.getNazev());
            preparedStatement.setInt(2, selectedCat.getCategoryID());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changePopisCellEvent(TableColumn.@NotNull CellEditEvent edditedCell) {
        Category selectedCat = tvCategory.getSelectionModel().getSelectedItem();
        selectedCat.setPopis(edditedCell.getNewValue().toString());
        String alterName = "UPDATE ST58211.KATEGORIE SET ST58211.KATEGORIE.POPIS = ?" +
                "WHERE ST58211.KATEGORIE.KATEGORIE_ID = ?";
        try {
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(alterName);
            preparedStatement.setString(1, selectedCat.getPopis());
            preparedStatement.setInt(2, selectedCat.getCategoryID());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
