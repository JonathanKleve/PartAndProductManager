# Part and Product Manager

## Project Overview

The Part and Product Manager is a desktop application designed to streamline the management of inventory for parts and finished products. It provides a robust system for performing essential CRUD (Create, Read, Update, Delete) operations on both individual parts and composite products, ensuring efficient data organization and retrieval.

This project serves as a demonstration of core software development principles, including object-oriented programming, database integration, and application design.

## Features
### Part Management:

* Add new parts with unique identifiers, names, and inventory levels.
* View details of existing parts.
* Update part information (e.g., quantity, name).
* Delete parts from the inventory.

### Product Management:

* Create new products, associating them with existing parts (e.g., a "Computer" product composed of "CPU," "RAM," and "Storage" parts).
* View comprehensive details of products, including their constituent parts.
* Modify product information and their associated parts.
* Delete products.

* Data Persistence: All part and product data is securely stored and retrieved from a relational database.

* Search Functionality: Search for parts and products by name or ID.

* Reporting: Generate a few pre-programmed reports for inventory analysis.

## Technologies Used
* Programming Languages: Java 17

* Database: MySQL 8.0

* Development Environment: IntelliJ IDEA CE

* Version Control: Git

* Database Management Tool: MySQL Workbench 8.0

* Database Connector: MySQL Connector/J 9.1.0

## Getting Started
Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
* Before you begin, ensure you have the following installed:
  * Git: For cloning the repository.
  * [Download Git](https://git-scm.com/downloads)
* Java Development Kit (JDK): 17
  * [Download JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.java.net/install/)
* MySQL Server: MySQL 8.0
* MySQL Client/Workbench (Optional but Recommended): For managing your database.
  * [MySQL Workbench](https://dev.mysql.com/downloads/workbench/)

### Installation
* Clone the repository:
  * ``` git clone https://github.com/JonathanKleve/PartAndProductManager.git
  cd PartAndProductManager
  ```

* Database Setup:

* Start your MySQL server.

* Open MySQL Workbench (or your preferred MySQL client).

* Manually create a new database for the project (e.g., part_product_db).

* Manually create the necessary tables within this database. Refer to your project's technical documentation for the required table structure (e.g., Part table, Product table, Product_Part association table).

* Configure Database Connection:

* Locate the database connection configuration in the project (e.g., src/main/java/kleve/PartAndProductManager/DAO/JDBC.java).

* Update the database connection details (username, password, database name, host) to match your local MySQL setup.

* Build the Project:

* For Java (IntelliJ IDEA):

  * Open IntelliJ IDEA.

  * Select File > Open and navigate to the PartAndProductManager directory.

  * IntelliJ should automatically detect the project and configure it.

  * Build the project: Build > Build Project.

## Usage
* To add a new part: 1. Launch the application. 2. Navigate to the 'Parts' section. 3. Click 'Add New Part' and fill in the details. 4. Save

* To run the Java application:

* From IntelliJ IDEA, right-click on your main class (e.g., Main.java) and select Run 'Main.main()'.

* From the command line (after building): java -jar [your-java-jar-file-name].jar (e.g., java -jar PartAndProductManager.jar)

* For more user instructions, see the [Design, Testing and User Documentation write-up](docs/Design%2C%20Testing%2C%20and%20User%20Documentation.docx)

## Project Documentation
This project is accompanied by comprehensive documentation detailing its design, testing, and usage. These documents provide deeper insights into the project's architecture and development process.

### Diagrams:

* [Link to UI Diagram](docs/UI%20Diagram.png)

* [Link to UML Diagram](docs/UML%20Diagram.png)

### Project Write-ups:

* Project Plan and Requirements: This document outlines the business problem, customer, business case, SDLC methodology, deliverables, implementation strategy, validation, environments, costs, and human resource requirements.
[Link to Project Plan and Requirements](docs/Project%20Plan%20and%20Requirements.docx)

* Design, Testing, and User Documentation: This comprehensive document includes the Class Design, UI Design, Unit Test Plan and Results, Developer & Maintenance Guide, and User Manual.
[Link to Project Plan and Requirements](docs/Design%2C%20Testing%2C%20and%20User%20Documentation.docx)

## Contributing
As a personal portfolio project, direct contributions are not actively sought. However, if you find any issues or have suggestions, feel free to open an issue on this repository.

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Contact
Jonathan Kleve
Email: 194426067+JonathanKleve@users.noreply.github.com
