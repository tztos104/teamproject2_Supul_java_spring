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
-- Table structure for table `homepage_intro`
--

DROP TABLE IF EXISTS `homepage_intro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `homepage_intro` (
  `title1` varchar(255) COLLATE utf8mb3_unicode_ci NOT NULL,
  `content1` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `content2` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `content3` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `content4` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `content5` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `content6` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `subtitle1` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `title2` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `title3` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`title1`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `homepage_intro`
--

LOCK TABLES `homepage_intro` WRITE;
/*!40000 ALTER TABLE `homepage_intro` DISABLE KEYS */;
INSERT INTO `homepage_intro` VALUES ('SUPUL','\"SUPUL\"은 리얼 방 탈출을 넘어 새로운 세계를 추구하는 방 탈출 게임입니다.\n주어진 시간 동안 여러분이 경험해 보지 못한 또는 경험해보고 싶었던 새로운 세상이 펼쳐집니다.\n사실감 넘치는 인테리어와 신기하고 신선한 문제와 트릭 장치 그리고 전문작가의 흥미진진한 스토리까지!\n최고의 방 탈출 카페 \"SUPUL\"에서 당신이 꿈꿔왔던 주인공이 되는 최고의 경험을 만나보세요.','DESIGN(기술력) : 독보적인 기술로 차별화된 전자 자동화 시스템','SPECIALITY(전문성) : 방탈출 게임을 전문적으로 연구 및 개발하는 디자인 조직 구성','ORIGINAL(독창성) : 모든 테마 및 스토리는 고객의 생각과 의견을 적극 반영','UPGRADE(발전성) : 지속적인 기술 개발 및 연구를 통하여 새로운 재미를 제공','BUSSINESS (사업성) :  높은 인지도 및 인기를 가진 브랜드로서 본사의 전문적이고 조직적인 영업관리 및 지원',' Why we are Best?','새로운 세계로의 초대, \"SUPUL\" 이란?','WHAT CAN WE DO?');
/*!40000 ALTER TABLE `homepage_intro` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-02  8:00:56
