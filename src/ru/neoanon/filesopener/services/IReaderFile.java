package ru.neoanon.filesopener.services;

import java.io.BufferedReader;
import java.nio.file.Path;

public interface IReaderFile {

	BufferedReader getFile(Path path);

}
