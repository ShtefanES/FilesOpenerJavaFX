# FilesOpenerJavaFX
FilesOpener упрощенное приложение, позволяющее параллельно выводить информацию из нескольких файлов на экран(интерфейс
программы имеет интернационализацию).

Основное окно приложения состоит из 3х вкладок. Первая непосредственно отображает выводимую из файлов информацию. 
Заполнение информацией текстового элемента происходит параллельно из нескольких файлов несколькими потоками со скоростью 
согласно настроек программы.

Панель управления потоками находится на второй вкладке. Существует возможность создать новыйпоток(указав имя файла для 
считывания), остановить работающий поток. Действующие потоки представлены в виде списка, четные и нечетные строки 
раскрашены в разные цвета. При завершении чтения файла поток остановливается и удаляется из списка.

На третей вкладке располагаются настройки программы: максимальный размер тектового элемента(200 строк по умолчанию), 
скорость считывания(1 строка в 5 сек по умолчанию), выбор расцветки четных и нечетных строк списка активных потоков, 
выбор языка прогаммы(русский и английский). Присутствует  возможность прочитать настройки программы из БД, а так же 
сохранить в БД.

Для работы с postgresql:
--------------------
Загрузить JDBC driver для pstgresql  https://jdbc.postgresql.org/download/postgresql-9.4.1212.jre6.jar

CREATE DATABASE setting_base;

CREATE USER setting_user WITH password 'Rambler333';

GRANT ALL privileges ON DATABASE setting_base TO setting_user;

CREATE TABLE Setting (
  name VARCHAR(20) NOT NULL,
  maxSize INTEGER NOT NULL,
	speedReading INTEGER NOT NULL,
	colorEven varchar(20) NOT NULL,
	colorUneven VARCHAR(20) NOT NULL,
	language VARCHAR(20) NOT NULL,
	PRIMARY KEY (name)
);

INSERT INTO Setting VALUES(
'default', 200, 5, 'GREEN', 'BLUE', 'en'
);