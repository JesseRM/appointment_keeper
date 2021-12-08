# Appointment Keeper
Appointment Keeper is a simple appointment management system.  It allows a user to make and manage appointments. It is written in Java and uses JavaFX for the GUI. 

The major objectives of this project were:  
  1. Allow a user to see appointment times relative to the time zone they are in. Meaning that if a user in Los Angeles created an appointment for 8:00 AM a user in
     in New York would see the appointment with a start time of 11:00 AM.  
  2. Allow a user to create and modify an appointment with information such as a title, description, location and start and end times.
  3. Allow a user to create and modify a customer profile with information such as name, address and phone number.
  4. Have the program present province options based on the country the user selects.
  5. Use a relational database to persist data.

<img src="https://raw.githubusercontent.com/JesseRM/appointment_keeper/master/src/main/resources/screenshot/appointment_keeper.JPG" width="600">
<img src="https://raw.githubusercontent.com/JesseRM/appointment_keeper/master/src/main/resources/screenshot/appointment_keeper2.JPG" width="500">

# Usage
The simplest way to try this application is to clone this repository with an IDE that can run Maven projects such as NetBeans or IntelliJ IDEA.

Instructions for cloning git repositories on thes IDEs can be found a the following links:  
&nbsp;&nbsp;[NetBeans](https://netbeans.apache.org/kb/docs/ide/git.html)  
&nbsp;&nbsp;[IntelliJ IDEA](https://blog.jetbrains.com/idea/2020/10/clone-a-project-from-github/)
  
Once the program is running the user is presented with a login in screen.  

Login credentials are:  
&nbsp;&nbsp;**Username:** user  
&nbsp;&nbsp;**Password:** password

Once logged in the user will be presented with a home screen where the user can create and manage appointments and customers.

# Technologies
  1. Java 11
  2. JavaFX 17
  3. PostgreSQL
  4. Maven
