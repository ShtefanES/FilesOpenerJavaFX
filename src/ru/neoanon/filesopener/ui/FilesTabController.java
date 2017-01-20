package ru.neoanon.filesopener.ui;

import java.io.File;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import ru.neoanon.filesopener.services.MultithreadedFileReader;
import ru.neoanon.filesopener.services.OnEndFile;

public class FilesTabController implements OnEndFile {

	private ObservableList<String> observableListFiles;
	private Color colorEvenLine = Color.GREEN;
	private Color colorUnevenLine = Color.BLUE;

	private MultithreadedFileReader multithreadedFileReader;

	@FXML
	private Label chooseFileLabel;

	@FXML
	private ListView<String> listViewFiles;

	@FXML
	private Button openButton;

	@FXML
	private void onFileOpen() {

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("TXT Files", "*.txt"));

		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			if (!multithreadedFileReader.isFileBeingRead(file.getPath())) {

				multithreadedFileReader.add(file.getPath());

				observableListFiles.add(file.getPath());
			}

		}
	}

	public void initialize() {

		multithreadedFileReader = MultithreadedFileReader.getInstance();
		multithreadedFileReader.setWatcher(this);

		observableListFiles = FXCollections.observableArrayList();
		listViewFiles.setItems(observableListFiles);

		listViewFiles.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

			@Override
			public ListCell<String> call(ListView<String> param) {
				return new ListCell<String>() {
					@Override
					protected void updateItem(String s, boolean bln) {
						super.updateItem(s, bln);

						if (s != null || !bln) {

							HBox hbox = null;
							FXMLLoader loader = new FXMLLoader();
							loader.setLocation(
									getClass().getResource("/ru/neoanon/filesopener/layouts/StopButton.fxml"));
							try {
								hbox = loader.load();
							} catch (IOException e) {
								e.printStackTrace();
							}

							Text text = (Text) hbox.getChildren().get(0);
							text.setText(s);

							int index = getIndex();
							index++;
							if (index % 2 == 0) {
								text.setFill(colorEvenLine);
							} else {
								text.setFill(colorUnevenLine);
							}

							Button stopButton = (Button) hbox.getChildren().get(1);
							stopButton.setOnAction(new EventHandler<ActionEvent>() {

								@Override
								public void handle(ActionEvent event) {
									listViewFiles.getItems().remove(getIndex());
									multithreadedFileReader.stop(s);
								}
							});

							setGraphic(hbox);
						} else {
							setGraphic(null);
						}
					}
				};
			}
		});
	}

	public MultithreadedFileReader getMultithreadedFileReader() {
		return multithreadedFileReader;
	}

	public void setColorEvenLine(Color colorEvenLine) {
		this.colorEvenLine = colorEvenLine;
	}

	public void setColorUnevenLine(Color colorUnevenLine) {
		this.colorUnevenLine = colorUnevenLine;
	}

	public void updateColor() {
		listViewFiles.setItems(null);
		listViewFiles.setItems(observableListFiles);
	}

	public void initWatcherOnWrite(RootController rootController) {
		multithreadedFileReader.setWatcher(rootController);
	}

	@Override
	public void onEndFile(String fullFilePath) {

		for (int i = 0; i < observableListFiles.size(); i++) {
			if (observableListFiles.get(i).equals(fullFilePath)) {
				listViewFiles.getItems().remove(i);
				return;
			}
		}
	}

	public Label getChooseFileLabel() {
		return chooseFileLabel;
	}
}
