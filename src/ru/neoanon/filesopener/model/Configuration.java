package ru.neoanon.filesopener.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.scene.paint.Color;

public class Configuration {

	private Locale languageRu = new Locale("ru");
	private Locale languageEn = new Locale("en");

	private List<Locale> languages;

	private int maxSizeText = 200;
	private int readingSpeedText = 5;
	private Color evenColorLine = Color.GREEN;
	private Color unevenColorLine = Color.BLUE;
	private Locale curentLanguage = languageEn;
	private String nameSetting;

	public Configuration() {
		languages = new ArrayList<>();
		languages.add(languageRu);
		languages.add(languageEn);
	}

	public List<Locale> getLanuages() {
		return languages;
	}

	public void setCurentLanguage(Locale curentLanguage) {
		this.curentLanguage = curentLanguage;
	}

	public Locale getCurentLanguage() {
		return curentLanguage;
	}

	public void setMaxSizeText(int maxSizeText) {
		this.maxSizeText = maxSizeText;
	}

	public int getMaxSizeText() {
		return maxSizeText;
	}

	public void setReadingSpeedText(int readingSpeedText) {
		this.readingSpeedText = readingSpeedText;
	}

	public int getReadingSpeedText() {
		return readingSpeedText;
	}

	public void setEvenColorLine(Color evenColorLine) {
		this.evenColorLine = evenColorLine;
	}

	public Color getEvenColorLine() {
		return evenColorLine;
	}

	public void setUnevenColorLine(Color unevenColorLine) {
		this.unevenColorLine = unevenColorLine;
	}

	public Color getUnevenColorLine() {
		return unevenColorLine;
	}

	public void setNameSetting(String nameSetting) {
		this.nameSetting = nameSetting;
	}

	public String getNameSetting() {
		return nameSetting;
	}

}
