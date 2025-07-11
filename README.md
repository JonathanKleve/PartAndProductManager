Part and Product Manager
Project Overview
The Part and Product Manager is a desktop application designed to streamline the management of inventory for parts and finished products. It provides a robust system for performing essential CRUD (Create, Read, Update, Delete) operations on both individual parts and composite products, ensuring efficient data organization and retrieval.

This project serves as a demonstration of core software development principles, including object-oriented programming, database integration, and application design.

Features
Part Management:

Add new parts with unique identifiers, names, and inventory levels.

View details of existing parts.

Update part information (e.g., quantity, name).

Delete parts from the inventory.

Product Management:

Create new products, associating them with existing parts (e.g., a "Computer" product composed of "CPU," "RAM," and "Storage" parts).

View comprehensive details of products, including their constituent parts.

Modify product information and their associated parts.

Delete products.

Data Persistence: All part and product data is securely stored and retrieved from a relational database.

Search Parts and Products by name or id.

Generates a few pre-programmed reports.

Technologies Used
Programming Languages:

Java 17

Database:

MySQL 8.0

Development Environment:

IntelliJ IDEA CE

Version Control: Git

Database Management Tool: MySQL Workbench 8.0

MySQL Connector J 9.1.0

Getting Started
Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

Prerequisites
Before you begin, ensure you have the following installed:

Git: For cloning the repository.

Download Git

Java Development Kit (JDK): 17

Download JDK or OpenJDK

MySQL Server: MySQL 8.0

MySQL Client/Workbench (Optional but Recommended): For managing your database.

MySQL Workbench

Installation
Clone the repository:

git clone https://github.com/JonathanKleve/PartAndProductManager.git
cd PartAndProductManager

Database Setup:

Start your MySQL server.

Open your MySQL client (e.g., MySQL Workbench or command line).

Create a new database for the project. For example:

CREATE DATABASE part_product_db;
USE part_product_db;

Execute the SQL schema script to create the necessary tables. You will need to locate this script within the cloned repository. (e.g., src/main/resources/schema.sql or similar).

-- Example: Assuming your schema is in a file named schema.sql
-- SOURCE path/to/your/schema.sql;

Configure Database Connection:

Locate the database configuration file in the project (e.g., src/main/resources/application.properties, config.properties, or within a Java/C++ class).

Update the database connection details (username, password, database name, host) to match your local MySQL setup.

Build the Project:

For Java (IntelliJ IDEA):

Open IntelliJ IDEA.

Select File > Open and navigate to the PartAndProductManager directory.

IntelliJ should automatically detect the project and configure it.

Build the project: Build > Build Project.

For C++ (IntelliJ IDEA or other IDE/compiler):

Open the C++ project in your IDE (e.g., IntelliJ IDEA with C/C++ plugin, Visual Studio Code, Code::Blocks).

Configure your build system (e.g., CMake, Make, or your IDE's built-in build).

Build the project.

Usage

To run the Java application:

From IntelliJ IDEA, right-click on your main class (e.g., Main.java) and select Run 'Main.main()'.

From the command line (after building): java -jar [PartAndProductManager.exe].jar

To run the C++ application:

From your IDE, run the executable.

From the command line (after building): .\PartAndProductManager.exe (Windows) or ./PartAndProductManager.exe (Linux/macOS)

Project Documentation
This project is accompanied by comprehensive documentation detailing its design, testing, and usage. These documents provide deeper insights into the project's architecture and development process.

Diagrams:

[Link to UI Diagram (docs/UI%20Diagram.png)]

[Link to UML Diagram (docs/UML%20Diagram.png)]

Project Write-ups:

[Link to Project Plan and Requirements (docs/Project%20Plan%20and%20Requirements.docx)]

[Link to Project Plan and Requirements (docs/Design%2C%20Testing%2C%20and%20User%20Documentation.docx)]

Contributing
As a personal portfolio project, direct contributions are not actively sought. However, if you find any issues or have suggestions, feel free to open an issue on this repository.

License
This project is licensed under the MIT License - see the LICENSE file for details.

Contact
Jonathan Kleve

Email: 194426067+JonathanKleve@users.noreply.github.com
