-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: db_LibraryMS
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tbook`
--

DROP TABLE IF EXISTS `tbook`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbook` (
  `book_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(150) NOT NULL,
  `author` varchar(100) NOT NULL,
  `publisher` varchar(100) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `year_published` year(4) DEFAULT NULL,
  `quantity` smallint(5) unsigned DEFAULT NULL,
  `price` decimal(10,4) DEFAULT NULL,
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbook`
--

LOCK TABLES `tbook` WRITE;
/*!40000 ALTER TABLE `tbook` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbook` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbtr`
--

DROP TABLE IF EXISTS `tbtr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbtr` (
  `btr_id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `borrow_date` date DEFAULT NULL,
  `due_date` date DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`btr_id`),
  KEY `student_id` (`student_id`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `tbtr_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `tstudent` (`student_id`),
  CONSTRAINT `tbtr_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `tbook` (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbtr`
--

LOCK TABLES `tbtr` WRITE;
/*!40000 ALTER TABLE `tbtr` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbtr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `treturn`
--

DROP TABLE IF EXISTS `treturn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `treturn` (
  `return_id` int(11) NOT NULL AUTO_INCREMENT,
  `btr_id` int(11) NOT NULL,
  `return_date` date NOT NULL,
  `condition_on_return` varchar(100) NOT NULL,
  `days_overdue` int(11) DEFAULT 0,
  `penalty` decimal(10,4) DEFAULT 0.0000,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`return_id`),
  KEY `btr_id` (`btr_id`),
  CONSTRAINT `treturn_ibfk_1` FOREIGN KEY (`btr_id`) REFERENCES `tbtr` (`btr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `treturn`
--

LOCK TABLES `treturn` WRITE;
/*!40000 ALTER TABLE `treturn` DISABLE KEYS */;
/*!40000 ALTER TABLE `treturn` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tstudent`
--

DROP TABLE IF EXISTS `tstudent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tstudent` (
  `student_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `course` varchar(100) DEFAULT NULL,
  `contact_no` varchar(15) DEFAULT NULL,
  `email_address` varchar(100) DEFAULT NULL,
  `date_registered` date DEFAULT NULL,
  PRIMARY KEY (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tstudent`
--

LOCK TABLES `tstudent` WRITE;
/*!40000 ALTER TABLE `tstudent` DISABLE KEYS */;
/*!40000 ALTER TABLE `tstudent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tusers`
--

DROP TABLE IF EXISTS `tusers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tusers` (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `full_name` varchar(150) NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `last_login` date DEFAULT NULL,
  `security_question` varchar(255) NOT NULL,
  `security_answer` varchar(50) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tusers`
--

LOCK TABLES `tusers` WRITE;
/*!40000 ALTER TABLE `tusers` DISABLE KEYS */;
INSERT INTO `tusers` VALUES ('jorelle123','Jorelle','8888','2025-11-04','What was the name of your first pet?','counter');
/*!40000 ALTER TABLE `tusers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'db_LibraryMS'
--
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ZERO_IN_DATE,NO_ZERO_DATE,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_loginUser` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_loginUser`(IN `p_username` VARCHAR(50), IN `p_password` VARCHAR(255))
BEGIN
    DECLARE user_count INT;

    -- Count matching user
    SELECT COUNT(*) INTO user_count
    FROM tusers
    WHERE username = p_username
      AND password = p_password;

    -- If user exists
    IF user_count = 1 THEN
        -- Return user info
        SELECT full_name
        FROM tusers
        WHERE username = p_username
          AND password = p_password;

        -- Update last login
        UPDATE tusers
        SET last_login = CURRENT_TIMESTAMP
        WHERE username = p_username
          AND password = p_password;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-04 23:33:26
