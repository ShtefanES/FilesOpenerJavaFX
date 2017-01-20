package ru.neoanon.filesopener.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import ru.neoanon.filesopener.services.OnGotString;

public class RootController implements OnGotString {

	@FXML
	private TextArea textArea;

	@FXML
	private GridPane gridPane;

	@FXML
	private FilesTabController gridPaneController;

	@FXML
	private BorderPane borderPane;

	@FXML
	private Tab outputTab;
	@FXML
	private Tab filesTab;
	@FXML
	private Tab settingTab;

	@FXML
	private SettingTabController borderPaneController;

	public void initialize() {

		textArea.setPrefRowCount(200);
		gridPaneController.initWatcherOnWrite(this);

		borderPaneController.setFilesTabController(gridPaneController);
		borderPaneController.setRoorController(this);

	}

	public FilesTabController getFilesTabController() {
		return gridPaneController;
	}

	public TextArea getTextArea() {
		return textArea;
	}

	public Tab getOutputTab() {
		return outputTab;
	}

	public Tab getFilesTab() {
		return filesTab;
	}

	public Tab getSettingTab() {
		return settingTab;
	}

	public void setMaxSizeTextArea(int maxSizeTextArea) {
		textArea.setPrefRowCount(maxSizeTextArea);

	}

	@Override
	public void onGotString(String string) {
		textArea.appendText(string);

	}
}
