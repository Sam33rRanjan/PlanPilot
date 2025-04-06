01. Plan Pilot:  A smart task management system

02. Prerequisites - Java 21, MySQL 8, Eclipse Enterprise Edition IDE, Git

03. How to run the application:
      Step 1- Install Maven(Java EE) Integration for Eclipse from Eclipse Marketplace.
      
      Step 2- Install Spring Tools (aka Spring Tool Suite) from Eclipse Marketplace.
      
      Step 3- Install Lombok on your device and configure it for Eclipse IDE.
      
      Step 4- Create a database named 'planpilot' in MySQL database.
      
      Step 5- Install git on your device.
      
      Step 6- Create a directory named 'PlanPilot' in your device.
      
      Step 7- Navigate to the 'PlanPilot' directory in the terminal and run the command "git clone https://github.com/Sam33rRanjan/PlanPilot.git"
      
      Step 8- Launch Eclipse IDE and select 'import' from the 'file' section in the top left corner.
      
      Step 9- Search projects from folder and archive and select it.
      Step 10- Select the 'PlanPilot' directory for the 'import source directory' at top of popup and click 'finish'.
      
      Step 11- Wait a little and let it download the required dependencies.
      
      Step 12- Navigate to the src/main/resources and open the 'application.properties' file.
      
      Step 13- Edit the fields on line 3 & 4 and set it to your MySQL username and password. Make sure you have created a database named 'planpilot' in MySQL server and it should be running on port no. 3306.
      
      Step 14- Edit the fields on line 17 & 18 and set it to your gmail id and app password. Make sure it is a GMAIL account. 
      (How to generate an app password: Go to 'https://myaccount.google.com/' and search for 'App passwords' in the search bar. Select 'app passwords' and create a new app password and paste the 16-digit app password without any spaces in line 18)
      
      Step 15- If you run the program for the first time it will create an account with username 'Head Admin', email 'admin@gmail.com' and password 'admin' with admin privileges. If you want to change any of the above, navigate to               src/main/java/incture/planPilot/service/auth in the 'PlanPilot' directory and open the 'AuthServiceImplementation' file and edit the fields in line 31,32 and 33.
      
      Step 16- Run the applications as a Spring Boot Application. After you run the application the console will show the port number on which Tomcat server is running which is by default 8080.
