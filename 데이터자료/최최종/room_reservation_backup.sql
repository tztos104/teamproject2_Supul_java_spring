-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: room
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `reservation_backup`
--

DROP TABLE IF EXISTS `reservation_backup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation_backup` (
  `rv_id` int NOT NULL AUTO_INCREMENT,
  `branch_name` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `cancle` tinyint(1) NOT NULL DEFAULT '0',
  `date` date DEFAULT NULL,
  `imp_uidexe` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `imp_uid` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `merchant_uid` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `no_show` tinyint(1) NOT NULL DEFAULT '0',
  `paid` tinyint(1) NOT NULL DEFAULT '0',
  `pay_cancle` tinyint(1) NOT NULL DEFAULT '0',
  `pay_id` int NOT NULL,
  `price` int NOT NULL,
  `reviewyn` tinyint(1) NOT NULL DEFAULT '0',
  `rv_date` datetime(6) DEFAULT NULL,
  `rv_num` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `rv_people` int DEFAULT NULL,
  `rv_price` int DEFAULT NULL,
  `rvpay_id` int NOT NULL,
  `thema_name` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `thema_id` int NOT NULL,
  `time` time(6) DEFAULT NULL,
  `user_name` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `user_id` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`rv_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation_backup`
--

LOCK TABLES `reservation_backup` WRITE;
/*!40000 ALTER TABLE `reservation_backup` DISABLE KEYS */;
/*!40000 ALTER TABLE `reservation_backup` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-02  8:00:54
