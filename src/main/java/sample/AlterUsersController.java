package sample;

import database.DataBaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import struc.User;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ResourceBundle;

public class AlterUsersController {
    private final DataBaseHandler dbConnection = new DataBaseHandler();
    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<Integer> roles = FXCollections.observableArrayList();
    private final String selectQueryUsers = "SELECT * FROM ST58211.UZIVATELE";

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField tfPassword;

    @FXML
    private TextField tfLogin;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfSurname;

    @FXML
    private ComboBox<Integer> cbRole;

    @FXML
    private TableColumn<User, String> tableCol1;

    @FXML
    private TableColumn<User, String> tableCol3;

    @FXML
    private TableColumn<User, Integer> tableCol2;

    @FXML
    private TableColumn<User, String> tableCol5;

    @FXML
    private TableColumn<User, String> tableCol4;

    @FXML
    private TableColumn<User, String> tableCol7;

    @FXML
    private TableColumn<User, String> tableCol6;

    @FXML
    private TableColumn<User, Integer> tableCol8;


    @FXML
    void addUser(ActionEvent event) {
        User newUser = new User(tfName.getText().trim(), tfSurname.getText().trim(), tfLogin.getText().trim(),
                tfPassword.getText().trim(), cbRole.getValue());
        if (checkLogin(tfLogin.getText().trim())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Please enter different login!");
            alert.showAndWait();
        } else {
            String login = newUser.getLogin();
            String password = newUser.getPassword();
            String name = newUser.getName();
            String surname = newUser.getSurname();
            int roleID = newUser.getRoleId();
            String insertQueryUsers = "insert into ST58211.UZIVATELE (ST58211.UZIVATELE.LOGIN, ST58211.UZIVATELE.HESLO," +
                    " ST58211.UZIVATELE.JMENO, ST58211.UZIVATELE.PRIJMENI, ST58211.UZIVATELE.ROLE_ROLE_ID)" +
                    " values (?, ?, ?, ?, ?)";
            try {
                PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(insertQueryUsers);
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, name);
                preparedStatement.setString(4, surname);
                preparedStatement.setInt(5, roleID);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            users.clear();
            fillUsers(selectQueryUsers);
            tableView.setItems(users);
        }

    }

    @FXML
    void deleteUser(ActionEvent event) {
        int userID = tableView.getSelectionModel().getSelectedItem().getUserID();
        String deleteQuery = "DELETE FROM ST58211.UZIVATELE WHERE ST58211.UZIVATELE.UZIVATEL_ID = ?";
        try {
            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(deleteQuery);
            preparedStatement.setInt(1, userID);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        users.remove(tableView.getSelectionModel().getSelectedItem());
        tableView.setItems(users);
    }

    @FXML
    void initialize() {
        roles.addAll(1, 2, 3, 4);
        cbRole.setItems(roles);
        cbRole.getSelectionModel().selectFirst();
        fillUsers(selectQueryUsers);
        tableView.setEditable(true);

        tableView.setItems(users);

        tableCol1.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableCol1.setCellFactory(TextFieldTableCell.forTableColumn());

        tableCol2.setCellValueFactory(new PropertyValueFactory<>("userID"));

        tableCol3.setCellValueFactory(new PropertyValueFactory<>("surname"));
        tableCol3.setCellFactory(TextFieldTableCell.forTableColumn());

        tableCol4.setCellValueFactory(new PropertyValueFactory<>("login"));
        tableCol4.setCellFactory(TextFieldTableCell.forTableColumn());

        tableCol5.setCellValueFactory(new PropertyValueFactory<>("password"));
        tableCol5.setCellFactory(TextFieldTableCell.forTableColumn());

        tableCol6.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableCol6.setCellFactory(TextFieldTableCell.forTableColumn());

        tableCol7.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        tableCol7.setCellFactory(TextFieldTableCell.forTableColumn());

        tableCol8.setCellValueFactory(new PropertyValueFactory<>("roleId"));
        tableCol8.setCellFactory(ComboBoxTableCell.forTableColumn(1, 2, 3, 4));
    }

    private void fillUsers(String selectQuery) {
        try {
            final PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(selectQuery);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final int userID = resultSet.getInt("UZIVATEL_ID");
                final String name = resultSet.getString("JMENO");
                final String surname = resultSet.getString("PRIJMENI");
                final String password = resultSet.getString("HESLO");
                final String email = resultSet.getString("EMAIL");
                final String telephone = resultSet.getString("PHONE");
                final String login = resultSet.getString("LOGIN");
                final int role = resultSet.getInt("ROLE_ROLE_ID");
                final User newUs = new User(userID, name, surname, login, password, email, telephone, role);
                users.add(newUs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean checkLogin(String login) {
        Iterator<User> it = users.iterator();
        while (it.hasNext()) {
            String curr = it.next().getLogin();
            if (curr.equals(login)) {
                return true;
            }
        }
        return false;
    }

    public void changeJmenoCellEvent(TableColumn.CellEditEvent edditedCell) {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        String edited = edditedCell.getNewValue().toString();
        if (selectedUser.getUserID()==Controller.logedInUser.getUserID()){
            Controller.logedInUser.setName(edited);
        }
        selectedUser.setName(edited);
        String alterName = "UPDATE ST58211.UZIVATELE SET ST58211.UZIVATELE.JMENO = ?" +
                "WHERE ST58211.UZIVATELE.UZIVATEL_ID = ?";
        try {
            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(alterName);
            preparedStatement.setString(1, selectedUser.getName());
            preparedStatement.setInt(2, selectedUser.getUserID());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changePrijmeniCellEvent(TableColumn.CellEditEvent edditedCell) {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        String edited = edditedCell.getNewValue().toString();
        if (selectedUser.getUserID()==Controller.logedInUser.getUserID()){
            Controller.logedInUser.setSurname(edited);
        }
        selectedUser.setSurname(edited);
        String alterSurname = "UPDATE ST58211.UZIVATELE SET ST58211.UZIVATELE.PRIJMENI = ?" +
                "WHERE ST58211.UZIVATELE.UZIVATEL_ID = ?";
        try {
            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(alterSurname);
            preparedStatement.setString(1, selectedUser.getSurname());
            preparedStatement.setInt(2, selectedUser.getUserID());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeLoginCellEvent(TableColumn.CellEditEvent edditedCell) {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        String temp = edditedCell.getNewValue().toString();
        if (checkLogin(temp)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("User with the same LOGIN already exists!");
            alert.showAndWait();
        } else {
            selectedUser.setLogin(temp);
            String alterLogin = "UPDATE ST58211.UZIVATELE SET ST58211.UZIVATELE.LOGIN = ?" +
                    "WHERE ST58211.UZIVATELE.UZIVATEL_ID = ?";
            try {
                PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(alterLogin);
                preparedStatement.setString(1, selectedUser.getLogin());
                preparedStatement.setInt(2, selectedUser.getUserID());
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeHesloCellEvent(TableColumn.CellEditEvent edditedCell) {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        selectedUser.setPassword(edditedCell.getNewValue().toString());
        String alterHeslo = "UPDATE ST58211.UZIVATELE SET ST58211.UZIVATELE.HESLO = ?" +
                "WHERE ST58211.UZIVATELE.UZIVATEL_ID = ?";
        try {
            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(alterHeslo);
            preparedStatement.setString(1, selectedUser.getPassword());
            preparedStatement.setInt(2, selectedUser.getUserID());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeEmailCellEvent(TableColumn.CellEditEvent edditedCell) {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        String edited = edditedCell.getNewValue().toString();
        if (selectedUser.getUserID()==Controller.logedInUser.getUserID()){
            Controller.logedInUser.setEmail(edited);
        }
        selectedUser.setEmail(edited);
        String alterEmail = "UPDATE ST58211.UZIVATELE SET ST58211.UZIVATELE.EMAIL = ?" +
                "WHERE ST58211.UZIVATELE.UZIVATEL_ID = ?";
        try {
            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(alterEmail);
            preparedStatement.setString(1, selectedUser.getEmail());
            preparedStatement.setInt(2, selectedUser.getUserID());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changePhoneCellEvent(TableColumn.CellEditEvent edditedCell) {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        String edited = edditedCell.getNewValue().toString();
        if (selectedUser.getUserID()==Controller.logedInUser.getUserID()){
            Controller.logedInUser.setTelephone(edited);
        }
        selectedUser.setTelephone(edited);
        String alterPhone = "UPDATE ST58211.UZIVATELE SET ST58211.UZIVATELE.PHONE = ?" +
                "WHERE ST58211.UZIVATELE.UZIVATEL_ID = ?";
        try {
            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(alterPhone);
            preparedStatement.setString(1, selectedUser.getTelephone());
            preparedStatement.setInt(2, selectedUser.getUserID());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeRoleCellEvent(TableColumn.CellEditEvent<User, Integer> edditedCell) {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        selectedUser.setRoleId(edditedCell.getNewValue());
        String alterRole = "UPDATE ST58211.UZIVATELE SET ST58211.UZIVATELE.ROLE_ROLE_ID = ?" +
                "WHERE ST58211.UZIVATELE.UZIVATEL_ID = ?";
        try {
            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(alterRole);
            preparedStatement.setInt(1, selectedUser.getRoleId());
            preparedStatement.setInt(2, selectedUser.getUserID());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
