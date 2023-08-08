# Connect 4

A simple connect 4 board game with rotations.

## Description

This game allows you to play ranked or casual games with other users.  
Casual games are created by inviting players you have as friends.  
The score ranking is displayed and it is also possible to manage your account.

## Build With
* Java EE
* Javascript
* HTML
* CSS
* MySQL
* Maven
* Tomcat

## Getting Started

### Dependencies

* JDK 17.0.6 (Java 17.0.6)
* MySQL 8.0.33
* Maven 3.9.1
* Tomcat 8.5.88

### Installing

1. Clone the repository on your machine: ``` git clone https://github.com/mph256/Connect4.git ```
2. Run the sql script using the following command from your MySQL command prompt once you are logged in: ``` source PATH\database.sql ``` (replace PATH with the appropriate file path)
3. Create the .war file from the project source directory with: ``` mvn package ```
4. Move the generated file from the target directory to the webapps directory of your Tomcat server
5. Replace the context.xml file in the conf directory of your Tomcat server with the one available in the project

### Executing

1. Start the server using the following command from the bin directory of your Tomcat server: ``` startup.bat ```
2. The application is accessible at the following url: http://localhost:8080/Connect4/connection

## Authors

[mph256](https://github.com/mph256)

## Version History

* 0.1
    * Initial Release

## License

This project is licensed under the MIT License - see the LICENSE.md file for details