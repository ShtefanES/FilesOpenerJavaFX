package ru.neoanon.filesopener.ui;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class Main extends Application {
	@Override
	public void start(Stage stage) throws IOException {

		FXMLLoader loader = new FXMLLoader();
		ResourceBundle bundle = ResourceBundle.getBundle("ru.neoanon.filesopener.bundles.Bundle", new Locale("en"));
		loader.setLocation(getClass().getResource("/ru/neoanon/filesopener/layouts/RootLayout.fxml"));
		loader.setResources(bundle);

		StackPane root = (StackPane) loader.load();

		RootController rootController = loader.getController();

		Scene scene = new Scene(root, 500, 260);

		stage.setTitle("FilesOpener");
		stage.setScene(scene);
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				rootController.getFilesTabController().getMultithreadedFileReader().stopAll();
			}
		});
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
