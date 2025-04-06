# Plan Pilot: Smart Task Management System

Plan Pilot is a robust and efficient task management system designed to streamline your workflow and enhance productivity.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Initial Account Setup](#initial-account-setup)

## Features

- **Task Creation and Management:** Easily create, edit, and delete tasks.
- **User Authentication:** Secure user authentication and authorization.
- **Email Notifications:** Receive email notifications for task updates and reminders.
- **Admin Privileges:** Manage all tasks.
- **Database Integration:** Utilizes MySQL for persistent data storage.
- **Spring Boot Application:** Built with Spring Boot for rapid development and deployment.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21:** [Download Java 21](https://www.oracle.com/java/technologies/javase-jdk21-archive-downloads.html)
- **MySQL 8:** [Download MySQL 8](https://dev.mysql.com/downloads/mysql/)
- **Eclipse Enterprise Edition IDE:** [Download Eclipse EE](https://www.eclipse.org/downloads/packages/release/2023-12/r/eclipse-ide-enterprise-java-and-web-developers)
- **Git:** [Download Git](https://git-scm.com/downloads)
- **Maven Integration for Eclipse:** Install from Eclipse Marketplace.
- **Spring Tools (Spring Tool Suite):** Install from Eclipse Marketplace.
- **Lombok:** [Download Lombok](https://projectlombok.org/download) and configure it for Eclipse.

## Installation

1.  **Clone the Repository:**
    ```terminal/git bash
    git clone [https://github.com/Sam33rRanjan/PlanPilot.git](https://github.com/Sam33rRanjan/PlanPilot.git)
    ```

2.  **Import the Project into Eclipse:**
    -   Open Eclipse IDE.
    -   Go to `File` > `Import...`.
    -   Select `Existing Maven Projects` or `Projects from Folder and Archive`.
    -   Choose the `PlanPilot` directory as the import source.
    -   Click `Finish`.

3.  **Wait for Dependency Download:**
    -   Eclipse will automatically download the required Maven dependencies.

## Configuration

1.  **MySQL Database Setup:**
    -   Create a database named `planpilot` in your MySQL server.

2.  **Configure `application.properties`:**
    -   Navigate to `src/main/resources/application.properties`.
    -   Update the MySQL username and password:
        ```properties
        spring.datasource.username=your_mysql_username
        spring.datasource.password=your_mysql_password
        ```
    -   Ensure your MySQL server is running on port 3306.
    -   Update the Gmail credentials for email notifications:
        ```properties
        spring.mail.username=your_gmail_address
        spring.mail.password=your_gmail_app_password
        ```
    -   **Generate an App Password (for Gmail):**
        -   Go to [Google Account Security](https://myaccount.google.com/apppasswords).
        -   Create an app password and paste the 16-digit password in the `spring.mail.password` field.

## Running the Application

1.  **Run as Spring Boot Application:**
    -   In Eclipse, right-click on the `PlanPilot` project.
    -   Select `Run As` > `Spring Boot App`.

2.  **Access the Application:**
    -   The application will run on the Tomcat server, typically on port 8080.
    -   Open your web browser and navigate to `http://localhost:8080`.

## Initial Account Setup

-   The first time you run the application, it will create an admin account:
    -   **Username:** `Head Admin`
    -   **Email:** `admin@gmail.com`
    -   **Password:** `admin`

-   **Modify Initial Account Details:**
    -   To change these details, navigate to `src/main/java/incture/planPilot/service/auth/AuthServiceImplementation.java`.
    -   Edit the values on lines 31, 32, and 33.
    -   You can't create an admin account, so you have to edit these before running for first time or edit your details using /api/user/updateUser endpoint.
