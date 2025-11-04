\## Database setup (Create the Database via CMD)

1\. Open CMD (Windows + R, type cmd, then Enter).

2\. Type **mysql -u root** to login to your MySQL.

3\. Type **CREATE DATABASE db\_LibraryMS;** to create the database

4\. Type **USE db\_LibraryMS;** to use the new database

5\. Then type **exit** and press Enter.





\## Import the MySQL dump to recreate the tables in your database  

6\. In CMD, type **mysql -u root db\_LibraryMS < "C:\\path\\to\\LibManSys\\db\\db\_LibraryMS\_dump.sql"**

&nbsp;   Replace the C:\\path\\to\\LibManSys with your actual folder path



\## Update the database configuration file.

7\. Update the local database configuration file inside the project folder (DAO) DBConnection.java with your DB credentials:

&nbsp;  db.url=jdbc:mysql://localhost:3306/db\_LibraryMS

&nbsp;  db.user=root

&nbsp;  db.password=yourpassword

