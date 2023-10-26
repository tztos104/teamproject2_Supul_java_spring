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
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `payment_id` int NOT NULL AUTO_INCREMENT,
  `access_token` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `amount` int NOT NULL,
  `apply_num` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `bank_code` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `bank_name` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `buyer_addr` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `buyer_email` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `buyer_name` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `buyer_postcode` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `buyer_tel` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `cancel_amount` int NOT NULL,
  `cancel_reason` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `cancelled_at` bigint NOT NULL,
  `card_code` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `card_name` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `card_number` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `card_quota` int NOT NULL,
  `card_type` int NOT NULL,
  `cash_receipt_issued` bit(1) NOT NULL,
  `channel` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `currency` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `custom_data` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `customer_uid` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `customer_uid_usage` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `emb_pg_provider` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `escrow` bit(1) NOT NULL,
  `expired_at` int DEFAULT NULL,
  `fail_reason` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `failed_at` bigint NOT NULL,
  `imp_uid` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `merchant_uid` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `now` int DEFAULT NULL,
  `paid_at` bigint NOT NULL,
  `pay_method` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `pg_id` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `pg_provider` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `pg_tid` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `receipt_url` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `started_at` bigint NOT NULL,
  `status` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `user_agent` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `vbank_code` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `vbank_date` bigint NOT NULL,
  `vbank_holder` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `vbank_issued_at` bigint NOT NULL,
  `vbank_name` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `vbank_num` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
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
