package ru.neoanon.filesopener.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;

public class MultithreadedFileReader implements IMultithreadedFileReader {

	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private Map<String, ScheduledFuture<?>> futures = new ConcurrentHashMap<>();
	private OnGotString watcher;
	private OnEndFile watcherEndFile;

	private int interval = 5;

	private final static MultithreadedFileReader instance = new MultithreadedFileReader();

	public static MultithreadedFileReader getInstance() {
		return instance;
	}

	private MultithreadedFileReader() {

	}

	@Override
	public void add(final String fullFilePath) {

		if (futures.containsKey(fullFilePath)) {

			return;
		}

		ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(new Runnable() {

			private BufferedReader reader;

			@Override
			public void run() {
				if (Thread.currentThread().isInterrupted()) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
				}
				if (reader == null) {
					File file = new File(fullFilePath);
					try {
						reader = Files.newBufferedReader(file.toPath());
					} catch (IOException e) {
						stop(fullFilePath);
						return;
					}
				}

				try {
					String line = reader.readLine();

					if (line == null) {
						stop(fullFilePath);
						reader.close();

						if (watcherEndFile != null) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									watcherEndFile.onEndFile(fullFilePath);
								}
							});
						}
					} else {

						if (watcher != null) {

							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									watcher.onGotString(line + "\n");
								}
							});
						}

					}

				} catch (IOException e) {
					stop(fullFilePath);
					return;
				}

			}

		}, 0, interval, TimeUnit.SECONDS);

		futures.put(fullFilePath, future);

	}

	@Override
	public void stop(String fullFilePath) {
		if (futures.get(fullFilePath) != null) {
			futures.get(fullFilePath).cancel(true);
			futures.remove(fullFilePath);
		}
	}

	@Override
	public void stopAll() {
		scheduler.shutdownNow();
		futures.clear();

	}

	@Override
	public void setReadTimeInterval(int seconds) {
		interval = seconds;
	}

	@Override
	public void setWatcher(OnGotString watcher) {
		this.watcher = watcher;
	}

	@Override
	public List<String> filesBeingRead() {
		return new ArrayList<>(futures.keySet());
	}

	@Override
	public boolean isFileBeingRead(String fullFilePath) {
		if (futures.containsKey(fullFilePath)) {
			return true;
		}
		return false;
	}

	@Override
	public void setWatcher(OnEndFile watcher) {
		watcherEndFile = watcher;
	}

}
