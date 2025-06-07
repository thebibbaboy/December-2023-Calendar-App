/*
-Nicholas Ortiz
-GUI Programming- Final Project
-December 2023 Calendar Application  
 */

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DecemberCalendarApp extends Application {

    int DAYS_IN_MONTH = 31;

    private final TextArea[] dayTextAreas = new TextArea[DAYS_IN_MONTH];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage mainStage) {
        mainStage.setTitle("December 2023 Calendar");
        mainStage.setResizable(true);

        BorderPane root = new BorderPane();

        // Set the application icon
        Image icon = new Image("icons/snowflake.png");
        mainStage.getIcons().add(icon);

        // Set the background image
        Image backgroundImage = new Image("icons/winter.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(
                BackgroundSize.AUTO,
                BackgroundSize.AUTO,
                true,
                true,
                true,
                false
            );
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        root.setBackground(new Background(background));

        GridPane calendarGrid = createCalendarGrid();
        VBox box = new VBox(calendarGrid);

        Scene mainScene = new Scene(root);
        mainStage.setScene(mainScene);

        root.setCenter(box);

        // Add snowflake images
        ImageView startSnowflakeImage = new ImageView(new Image("icons/snowflake.png"));
        startSnowflakeImage.setFitHeight(50);
        startSnowflakeImage.setFitWidth(50);

        Label startMonthLabel = new Label("December 2023", startSnowflakeImage);
        startMonthLabel.setStyle("-fx-font-size: 64; -fx-font-weight: bold;");  // Set bold font weight

        // Center the titles and images using HBox and StackPane
        HBox titleBox = new HBox(startMonthLabel);
        titleBox.setAlignment(Pos.CENTER);
        StackPane titlePane = new StackPane(titleBox);
        root.setTop(titlePane);

        //Add buttons
        Button quitButton = new Button("Quit");
        box.getChildren().addAll(quitButton); 
        VBox.setVgrow(calendarGrid, Priority.ALWAYS);

        Button aboutButton = new Button("About");
        box.getChildren().addAll(aboutButton);
        VBox.setVgrow(calendarGrid, Priority.ALWAYS);

        aboutButton.setOnAction((event) -> showAboutAlert());
        quitButton.setOnAction((event) -> mainStage.close()); 

        Button saveButton = new Button("Save as TXT");
        box.getChildren().add(saveButton);
        VBox.setVgrow(calendarGrid, Priority.ALWAYS);

        saveButton.setOnAction((event) -> saveCalendarAsText(mainStage));

        mainStage.show();
        mainStage.sizeToScene();
    }

    private GridPane createCalendarGrid() {
        GridPane calendarGrid = new GridPane();
        calendarGrid.setAlignment(Pos.CENTER);
        calendarGrid.setHgap(10);  // Adjust the horizontal gap
        calendarGrid.setVgap(10);  // Adjust the vertical gap

        int gridRow = 1;
        for (int zeroDay = 0; zeroDay < DAYS_IN_MONTH; zeroDay++) {
            TextArea dayTextArea = new TextArea();
            dayTextArea.setPrefRowCount(5);  // Adjust the row count
            dayTextArea.setPrefColumnCount(15);  // Adjust the column count
            dayTextArea.setMaxSize(400, 300);  // Set maximum size
            dayTextArea.setEditable(true);  
            dayTextArea.setWrapText(false);

            // Add blue border color to rows and columns
            dayTextArea.setBorder(new Border(new BorderStroke(Color.DEEPSKYBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));

            dayTextAreas[zeroDay] = dayTextArea;

            String dayOfWeek = Zellercongruence(zeroDay + 1, 12, 2023);
            String dayString = dayOfWeek + " the " + addSuffix(zeroDay + 1);
            dayTextArea.setText(dayString);

            int zellerInt = ZellercongruenceAsInt(zeroDay + 1, 12, 2023);
            int gridColumn = ConvertZellerDayToGridColumn(zellerInt);
            if (gridColumn == 0) {
                gridRow++;
            }
            calendarGrid.add(dayTextArea, gridColumn, gridRow);
        }

        return calendarGrid;
    }

    // Print the day for a date
    static String Zellercongruence(int day, int month, int year) {
        if (month == 1 || month == 2) {
            month += 12;
            year--;
        }

        int q = day;
        int m = month;
        int k = year % 100;
        int j = year / 100;

        int h = (q + 13 * (m + 1) / 5 + k + k / 4 + j / 4 + 5 * j) % 7;

        if (h == 0) {
            return "Saturday";
        } else if (h == 1) {
            return "Sunday";
        } else if (h == 2) {
            return "Monday";
        } else if (h == 3) {
            return "Tuesday";
        } else if (h == 4) {
            return "Wednesday";
        } else if (h == 5) {
            return "Thursday";
        } else {
            return "Friday";
        }
    }

    static int ZellercongruenceAsInt(int day, int month, int year) {
        if (month == 1) {
            month = 13;
            year--;
        }
        if (month == 2) {
            month = 14;
            year--;
        }
        int q = day;
        int m = month;
        int k = year % 100;
        int j = year / 100;
        int h = q + 13 * (m + 1) / 5 + k + k / 4 + j / 4 + 5 * j;

        return h % 7;
    }

    static int ConvertZellerDayToGridColumn(int ZellerDay) {
        int column = -1;

        if (ZellerDay >= 1 && ZellerDay <= 6) {
            column = ZellerDay - 1;
        } else if (ZellerDay == 0) {
            column = 6;
        }

        return column;
    }

    private void showAboutAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About December Calendar App");
        alert.setHeaderText(null);
        alert.setContentText("December Calendar App\n\n"
            + "This calendar application displays the calendar for \n December 2023! "
            + "You can save the calendar events as a text \n file using the 'Save as TXT' button below the about button.");

        // Set the alert icon (you can customize it)
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image("icons/snowflake.png"));  // Use the correct icon path

        alert.showAndWait();
    }

    private void saveCalendarAsText(Stage mainStage) {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Text files", "*.txt");
        chooser.getExtensionFilters().add(filter);

        File file = chooser.showSaveDialog(mainStage);

        if (file == null) {
            return; // If the user cancels the file chooser
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Iterate through all text areas and write data to the text file
            for (int dayZero = 0; dayZero < dayTextAreas.length; dayZero++) {
                int zellerInt = ZellercongruenceAsInt(dayZero + 1, 12, 2023);
                int gridColumn = ConvertZellerDayToGridColumn(zellerInt);
                if (gridColumn == 0) {
                    writer.newLine();
                }
                writer.write(dayTextAreas[dayZero].getText() + "\n");  // Add newline character
            }
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    private String addSuffix(int dayNumber) {
        if (dayNumber >= 11 && dayNumber <= 13) {
            return dayNumber + "th: ";
        } else {
            switch (dayNumber % 10) {
                case 1:
                    return dayNumber + "st: ";
                case 2:
                    return dayNumber + "nd: ";
                case 3:
                    return dayNumber + "rd: ";
                default:
                    return dayNumber + "th: ";
            }
        }
    }
}
