package ru.neoanon.filesopener.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.scene.paint.Color;
import ru.neoanon.filesopener.model.Configuration;

public class Configurations implements IConfigurations {

	public static final String DB_URL = "jdbc:postgresql://localhost/setting_base";
	public static final String DB_USER = "setting_user";
	public static final String DB_PASSWORD = "Rambler333";
	public static final String DRIVER_NAME = "org.postgresql.Driver";

	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private List<String> settingNamesList;

	private final static Configurations instance = new Configurations();

	public static Configurations getInstance() {
		return instance;
	}

	private Configurations() {

		setSettingNamesList();
	}

	@Override
	public Configuration getConfiguration(String nameSetting) {

		Configuration configuration = null;

		connection = openConnection();

		try {
			preparedStatement = connection.prepareStatement("SELECT * FROM Setting WHERE name = ?");

			preparedStatement.setString(1, nameSetting);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				configuration = new Configuration();

				configuration.setCurentLanguage(new Locale(resultSet.getString("language")));
				configuration.setMaxSizeText(resultSet.getInt("maxSize"));
				configuration.setReadingSpeedText(resultSet.getInt("speedReading"));
				configuration.setEvenColorLine(Color.valueOf(resultSet.getString("colorEven")));
				configuration.setUnevenColorLine(Color.valueOf(resultSet.getString("colorUneven")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection);
			closePreparedStatement(preparedStatement);
			closeResultSet(resultSet);
		}
		return configuration;
	}

	@Override
	public void addConfiguration(Configuration configuration) {

		connection = openConnection();

		try {
			preparedStatement = connection.prepareStatement("INSERT INTO Setting  VALUES (?, ?, ?, ?, ?, ?)");

			preparedStatement.setString(1, configuration.getNameSetting());
			preparedStatement.setInt(2, configuration.getMaxSizeText());
			preparedStatement.setInt(3, configuration.getReadingSpeedText());
			preparedStatement.setString(4, configuration.getEvenColorLine().toString());
			preparedStatement.setString(5, configuration.getUnevenColorLine().toString());
			preparedStatement.setString(6, configuration.getCurentLanguage().getLanguage());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection);
			closePreparedStatement(preparedStatement);
		}

	}

	@Override
	public List<String> getSettingNamesList() {
		return settingNamesList;
	}

	@Override
	public void setSettingNamesList() {

		settingNamesList = new ArrayList<>();

		connection = openConnection();

		try {
			preparedStatement = connection.prepareStatement("SELECT name FROM Setting");

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				settingNamesList.add(resultSet.getString("name"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection);
			closePreparedStatement(preparedStatement);
			closeResultSet(resultSet);
		}
	}

	private Connection openConnection() {

		Connection connection = null;
		try {
			Class.forName(DRIVER_NAME);
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	private void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void closePreparedStatement(PreparedStatement preparedStatement) {
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isContainSettingName(String settingName) {

		for (String str : settingNamesList) {
			if (str.equals(settingName)) {
				return true;
			}
		}
		return false;
	}
}
