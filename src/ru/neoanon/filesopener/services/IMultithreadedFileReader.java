package ru.neoanon.filesopener.services;

import java.util.List;

public interface IMultithreadedFileReader {

	void add(String fullFilePath);

	void stop(String fullFilePath);

	void stopAll();

	void setReadTimeInterval(int seconds);
	
	void setWatcher(OnGotString watcher);
	
	void setWatcher(OnEndFile watcher);

	List<String> filesBeingRead();
	
	boolean isFileBeingRead(String fullFilePath);

}
