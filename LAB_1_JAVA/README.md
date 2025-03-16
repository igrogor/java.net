## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).


как запускать:

компиляция: javac -cp ../lib/gson-2.12.1.jar *.java

запуск первого: java -cp .:../lib/gson-2.10.1.jar App 65432 65433

запуск второго: java -cp .:../lib/gson-2.10.1.jar App 65433 65432

65433 — порт, который будет использовать сервер второго экземпляра.

65432 — порт, на который второй экземпляр будет отправлять данные.