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
OPTION #1  
To try this application one can downlaod the latest release, unzip the file and execute the JAR file contained within.  
**Required:** To run the JAR file the machine running the JAR file must have the Java VM installed.  It can be downloaled from the [OFFICIAL JAVA WEBSITE](https://www.java.com/en/).

A PostgreSQL database is required with the following schemas:

Users

<table>
    <tr>
        <th>Column</th>
        <th>Type</th>
    </tr>
    <tr>
        <td>user_id (PK)</td>
        <td>Integer</td>
    </tr>
    <tr>
        <td>name</td>
        <td>String</td>
    </tr>
    <tr>
        <td>password_hash</td>
        <td>String</td>
    </tr>
    <tr>
        <td>salt</td>
        <td>String</td>
    </tr>
</table>

Appointments

<table>
    <tr>
        <th>Column</th>
        <th>Type</th>
    </tr>
    <tr>
        <td>appointment_id (PK)</td>
        <td>Integer</td>
    </tr>
    <tr>
        <td>user_id (FK)</td>
        <td>Integer</td>
    </tr>
    <tr>
        <td>customer_id (FK)</td>
        <td>Integer</td>
    </tr>
    <tr>
        <td>title</td>
        <td>String</td>
    </tr>
    <tr>
        <td>description</td>
        <td>String</td>
    </tr>
    <tr>
        <td>type</td>
        <td>String</td>
    </tr>
    <tr>
        <td>location</td>
        <td>String</td>
    </tr>
    <tr>
        <td>status</td>
        <td>String</td>
    </tr>
    <tr>
        <td>start_time</td>
        <td>Timestamp with time zone</td>
    </tr>
    <tr>
        <td>end_time</td>
        <td>Timestamp with time zone/td>
    </tr>
</table>

Customers

<table>
    <tr>
        <th>Column</th>
        <th>Type</th>
    </tr>
    <tr>
        <td>customer_id (PK)</td>
        <td>Integer</td>
    </tr>
    <tr>
        <td>name</td>
        <td>String</td>
    </tr>
    <tr>
        <td>address</td>
        <td>String</td>
    </tr>
    <tr>
        <td>phone_number</td>
        <td>String</td>
    </tr>
    <tr>
        <td>postal_code</td>
        <td>String</td>
    </tr>
    <tr>
        <td>province_id (FK)</td>
        <td>Integer</td>
    </tr>
</table>

Countries

<table>
    <tr>
        <th>Column</th>
        <th>Type</th>
    </tr>
    <tr>
        <td>country_id (PK)</td>
        <td>Integer</td>
    </tr>
    <tr>
        <td>name</td>
        <td>Integer</td>
    </tr>
</table>

Provinces

<table>
    <tr>
        <th>Column</th>
        <th>Type</th>
    </tr>
    <tr>
        <td>province_id (PK)</td>
        <td>Integer</td>
    </tr>
    <tr>
        <td>name</td>
        <td>Integer</td>
    </tr>
    <tr>
        <td>country_id (FK)</td>
        <td>Integer</td>
    </tr>
</table>

OPTION #2  
Another option is to clone this repository with an IDE that can run Maven projects such as NetBeans or IntelliJ IDEA.  

Instructions for cloning git repositories on these IDEs can be found a the following links:  
&nbsp;&nbsp;[NetBeans](https://netbeans.apache.org/kb/docs/ide/git.html)  
&nbsp;&nbsp;[IntelliJ IDEA](https://blog.jetbrains.com/idea/2020/10/clone-a-project-from-github/)

# Technologies
  1. Java 17
  2. JavaFX 17
  3. FXML
  4. PostgreSQL
  5. Maven
