package ru.neoanon.filesopener.services;

import java.util.List;

import ru.neoanon.filesopener.model.Configuration;

public interface IConfigurations {

	Configuration getConfiguration(String nameSetting);

	List<String> getSettingNamesList();

	void setSettingNamesList();

	boolean isContainSettingName(String settingName);

	void addConfiguration(Configuration configuration);

}
