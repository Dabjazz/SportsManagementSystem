package TennisBallGames;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddScoreController implements Initializable {
    @FXML
    private TextField hScoreText;
    @FXML
    private TextField vScoreText;
    @FXML
    private ComboBox matchesBar;

    private MatchesAdapter matchesAdapter;
    private TeamsAdapter teamsAdapter;

    //The data variable is used to populate the ComboBox
    final private ObservableList<String> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        matchesBar.setItems(data);
    }

    public void save(ActionEvent actionEvent) {
        int hScore = Integer.parseInt(hScoreText.getText());
        int vScore = Integer.parseInt(vScoreText.getText());
        int matchNum = getMatchNum();
        String hTeam = getTeam(true);
        String vTeam = getTeam(false);

        try {
            matchesAdapter.setTeamsScore(matchNum, hScore, vScore);
            teamsAdapter.setStatus(hTeam, vTeam, hScore, vScore);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) matchesBar.getScene().getWindow();
        stage.close();
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) matchesBar.getScene().getWindow();
        stage.close();
    }

    public void setModel(MatchesAdapter match, TeamsAdapter team) {
        matchesAdapter = match;
        teamsAdapter = team;
        buildComboBoxData();
    }

    private void buildComboBoxData() {
        try {
            data.addAll(matchesAdapter.getMatchesNamesList());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private int getMatchNum(){
        String numStr = matchesBar.getValue().toString();
        int endIndex = numStr.indexOf(')');
        numStr = numStr.substring(0, endIndex);
        int num = Integer.parseInt(numStr);
        return num;
    }

    private String getTeam(boolean home){
        String team = matchesBar.getValue().toString();
        int index = team.indexOf('-');

        if(home){
            //Home Team
            int startIndex = team.indexOf(')');
            team = team.substring(startIndex+2, index-1);
        }
        else{
            //Visitor Team
            team = team.substring(index+2);
        }

        return team;
    }
}
