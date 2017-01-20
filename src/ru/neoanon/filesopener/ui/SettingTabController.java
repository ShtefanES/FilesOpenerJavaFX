package ru.neoanon.filesopener.ui;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import ru.neoanon.filesopener.model.Configuration;
import ru.neoanon.filesopener.services.Configurations;
import javafx.util.StringConverter;

public class SettingTabController {

	private RootController rootController;
	private FilesTabController filesTabController;
	private Configuration configuration;
	private Configurations configurations;

	@FXML
	private Label maxSizeLabel;
	@FXML
	private TextField maxSizeTextField;
	@FXML
	private Label readingSpeedLabel;
	@FXML
	private TextField readingSpeedTextField;
	@FXML
	private Label colorEvenLabel;
	@FXML
	private ColorPicker evenColorPicker;
	@FXML
	private Label colorUnevenLabel;
	@FXML
	private ColorPicker unevenColorPicker;
	@FXML
	private Label languageLabel;
	@FXML
	private ComboBox<Locale> languagesChoice;
	@FXML
	private Button downloadButton;
	@FXML
	private Button saveButton;
	@FXML
	private Button applyButton;

	public void initialize() {
		configuration = new Configuration();

		configurations = Configurations.getInstance();

		languagesChoice.getItems().addAll(configuration.getLanuages());
		languagesChoice.setConverter(new StringConverter<Locale>() {
			@Override
			public String toString(Locale l) {
				return l.getLanguage().toUpperCase();
			}

			@Override
			public Locale fromString(String s) {
				return Locale.forLanguageTag(s);
			}
		});

		initUISetting();
	}

	@FXML
	private void onSave() throws IOException {

		if (isCorrectEnteredIntegers()) {

			TextInputDialog inputName = (TextInputDialog) getLoader(
					"/ru/neoanon/filesopener/layouts/InputSaveName.fxml").load();

			Optional<String> result = inputName.showAndWait();
			if (result.isPresent()) {

				if (result.get().equals("")) {

					Alert alertEmptySettingName = (Alert) getLoader(
							"/ru/neoanon/filesopener/layouts/AlertEmptySettingName.fxml").load();
					alertEmptySettingName.showAndWait();

				} else if (configurations.isContainSettingName(result.get())) {

					Alert alertDuplicateSettingName = (Alert) getLoader(
							"/ru/neoanon/filesopener/layouts/AlertDuplicateSettingName.fxml").load();
					alertDuplicateSettingName.showAndWait();

				} else {

					if (result.get().length() > 20) {
						configuration.setNameSetting(result.get().substring(0, 20));
					} else {
						configuration.setNameSetting(result.get());
					}

					configuration.setMaxSizeText(Integer.parseInt(maxSizeTextField.getText()));
					configuration.setReadingSpeedText(Integer.parseInt(readingSpeedTextField.getText()));
					configuration.setEvenColorLine(evenColorPicker.getValue());
					configuration.setUnevenColorLine(unevenColorPicker.getValue());
					configuration.setCurentLanguage(languagesChoice.getValue());

					configurations.getSettingNamesList().add(result.get());
					configurations.addConfiguration(configuration);
				}
			}
		}
	}

	@FXML
	private void onDownload() throws IOException {

		ChoiceDialog<String> download = (ChoiceDialog) getLoader("/ru/neoanon/filesopener/layouts/DownloadChoice.fxml")
				.load();
		download.getItems().addAll(configurations.getSettingNamesList());
		download.setSelectedItem(configurations.getSettingNamesList().get(0));

		Optional<String> result = download.showAndWait();
		if (result.isPresent()) {

			configuration = configurations.getConfiguration(result.get());

			initUISetting();

			applySetting();

		}
	}

	@FXML
	private void onApply() throws IOException {

		if (isCorrectEnteredIntegers()) {

			applySetting();

			configuration.setCurentLanguage(languagesChoice.getValue());
		}

	}

	public void setRoorController(RootController rootController) {
		this.rootController = rootController;
	}

	public void setFilesTabController(FilesTabController filesTabController) {
		this.filesTabController = filesTabController;
	}

	private void changeLanguage(Locale locale) {
		maxSizeLabel.setText(ResourceBundle.getBundle("ru.neoanon.filesopener.bundles.Bundle", locale)
				.getString("SettingTabController.lbl.maxSizeLabel"));
		readingSpeedLabel.setText(ResourceBundle.getBundle("ru.neoanon.filesopener.bundles.Bundle", locale)
				.getString("SettingTabController.lbl.readingSpeedLabel"));
		colorEvenLabel.setText(ResourceBundle.getBundle("ru.neoanon.filesopener.bundles.Bundle", locale)
				.getString("SettingTabController.lbl.colorEvenLabel"));
		colorUnevenLabel.setText(ResourceBundle.getBundle("ru.neoanon.filesopener.bundles.Bundle", locale)
				.getString("SettingTabController.lbl.colorUnevenLabel"));
		languageLabel.setText(ResourceBundle.getBundle("ru.neoanon.filesopener.bundles.Bundle", locale)
				.getString("SettingTabController.lbl.languageLabel"));

		filesTabController.getChooseFileLabel()
				.setText(ResourceBundle.getBundle("ru.neoanon.filesopener.bundles.Bundle", locale)
						.getString("FilesTabController.lbl.chooseFileLabel"));

		rootController.getOutputTab().setText(ResourceBundle.getBundle("ru.neoanon.filesopener.bundles.Bundle", locale)
				.getString("RootController.tab.outputTab"));
		rootController.getFilesTab().setText(ResourceBundle.getBundle("ru.neoanon.filesopener.bundles.Bundle", locale)
				.getString("RootController.tab.filesTab"));
		rootController.getSettingTab().setText(ResourceBundle.getBundle("ru.neoanon.filesopener.bundles.Bundle", locale)
				.getString("RootController.tab.settingTab"));
	}

	private void applySetting() {
		rootController.setMaxSizeTextArea(Integer.parseInt(maxSizeTextField.getText()));
		filesTabController.getMultithreadedFileReader()
				.setReadTimeInterval(Integer.parseInt(readingSpeedTextField.getText()));
		filesTabController.setColorEvenLine(evenColorPicker.getValue());
		filesTabController.setColorUnevenLine(unevenColorPicker.getValue());
		filesTabController.updateColor();
		changeLanguage(languagesChoice.getValue());
	}

	private void initUISetting() {
		maxSizeTextField.setText(String.valueOf(configuration.getMaxSizeText()));
		readingSpeedTextField.setText(String.valueOf(configuration.getReadingSpeedText()));
		evenColorPicker.setValue(configuration.getEvenColorLine());
		unevenColorPicker.setValue(configuration.getUnevenColorLine());
		languagesChoice.setValue(configuration.getCurentLanguage());
	}

	private boolean isCorrectEnteredIntegers() throws IOException {

		try {
			int maxSizeTextArea = Integer.parseInt(maxSizeTextField.getText());
			int readingSpeedText = Integer.parseInt(readingSpeedTextField.getText());
			if (maxSizeTextArea <= 0 || readingSpeedText <= 0) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {

			Alert alertIncorrectSymbol = (Alert) getLoader("/ru/neoanon/filesopener/layouts/AlertIncorrectSymbol.fxml")
					.load();
			alertIncorrectSymbol.showAndWait();
			return false;
		}
		return true;
	}

	private FXMLLoader getLoader(String resourcePath) {

		FXMLLoader loader = new FXMLLoader();

		ResourceBundle bundle = ResourceBundle.getBundle("ru.neoanon.filesopener.bundles.Bundle",
				configuration.getCurentLanguage());
		loader.setLocation(getClass().getResource(resourcePath));
		loader.setResources(bundle);

		return loader;
	}

}
