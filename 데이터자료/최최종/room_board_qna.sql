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
-- Table structure for table `board_qna`
--

DROP TABLE IF EXISTS `board_qna`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_qna` (
  `qna_id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(999) COLLATE utf8mb3_unicode_ci NOT NULL,
  `modi_date` datetime(6) DEFAULT NULL,
  `reg_date` datetime(6) DEFAULT NULL,
  `title` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL,
  `type` varchar(250) COLLATE utf8mb3_unicode_ci NOT NULL,
  `branch_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`qna_id`),
  KEY `FK2w6int9w26f8gfaoqucq4hmcn` (`branch_id`),
  KEY `FKscnmkg0fyjyhrm8nkyy74n308` (`user_id`),
  CONSTRAINT `FK2w6int9w26f8gfaoqucq4hmcn` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`branch_id`),
  CONSTRAINT `FKscnmkg0fyjyhrm8nkyy74n308` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_qna`
--

LOCK TABLES `board_qna` WRITE;
/*!40000 ALTER TABLE `board_qna` DISABLE KEYS */;
INSERT INTO `board_qna` VALUES (1,'방탈출 카페에서 어떤 테마를 선택해야 할지 고민 중입니다. 추천해 주실 수 있나요?',NULL,'2023-09-01 11:01:42.000000','테마 선택 문의','기타문의',1,1),(2,'특정 날짜에 방탈출 카페에서 예약 가능한 시간을 확인하려고 합니다. 도움을 부탁드립니다.',NULL,'2023-09-02 12:12:42.000000','예약 가능한 날짜 확인','예약문의',2,2),(3,'방탈출 카페의 가격과 가능한 할인 혜택에 대해 자세한 정보를 얻고 싶습니다.',NULL,'2023-09-03 13:23:42.000000','가격 및 할인 문의','기타문의',3,11),(4,'방탈출 카페의 정확한 위치와 주변 주요 랜드마크에 대한 정보를 알려주세요.',NULL,'2023-09-04 14:34:42.000000','방탈출 카페 위치','기타문의',4,12),(5,'방탈출 카페 내의 알림판에서 게임에 관한 정보를 확인했는데, 몇 가지 질문이 있습니다.',NULL,'2023-09-05 15:45:42.000000','알림판 문의','기타문의',5,21),(6,'가족 단위로 방탈출 카페를 예약하려고 합니다. 인원 및 요금 관련 정보를 알려주세요.',NULL,'2023-09-06 16:56:42.000000','가족 단위 예약 문의','취소문의',1,22),(7,'방탈출 카페의 영업 시간을 확인하고 싶습니다. 주말에도 운영되나요?',NULL,'2023-09-07 17:21:42.000000','카페 운영 시간 문의','예약문의',2,31),(8,'어린이와 함께 방탈출 카페를 방문하려고 합니다. 어린이용 게임이 있는지 알려주세요.',NULL,'2023-09-08 18:23:42.000000','어린이와 함께하는 게임','기타문의',3,32),(9,'게임 중에 힌트가 필요할 때 어떻게 제공되나요? 힌트 시스템에 대한 정보를 알려주세요.',NULL,'2023-09-09 19:45:42.000000','힌트 제공 방법','기타문의',4,41),(10,'방탈출 카페 주변에 주차 시설이 제공되나요? 주차 관련 정보를 알려주세요.',NULL,'2023-09-10 20:26:42.000000','주차 시설 문의','기타문의',5,42),(11,'회사 팀 빌딩 이벤트를 위해 방탈출 카페를 예약하려고 합니다. 가능한 일정과 특별한 패키지에 대한 정보를 알려주세요.',NULL,'2023-09-11 13:01:42.000000','팀 빌딩 이벤트 문의','예약문의',1,43),(12,'친구의 생일을 위해 방탈출 카페에서 생일 파티를 계획 중입니다. 예약 가능한 날짜와 서비스에 대한 정보를 알려주세요.',NULL,'2023-09-12 14:02:42.000000','생일 파티 예약 문의','기타문의',2,44),(13,'방탈출 카페 방문 시 특별한 요청사항이 있는데, 가능한지 확인하고 싶습니다. 예를 들어, 식사 제공 여부 등에 대한 문의입니다.',NULL,'2023-09-13 15:15:42.000000','특별한 요청사항 문의','취소문의',3,45),(14,'방탈출 카페의 안전 절차에 대해서 알려주세요.',NULL,'2023-09-14 16:16:42.000000','안전 절차 문의','기타문의',4,14),(15,'예약 취소 정책과 수수료에 대한 정보를 알려주세요.',NULL,'2023-09-15 17:17:42.000000','예약 취소 정책 문의','취소문의',5,3),(16,'방탈출 카페에서 사용되는 테마에 대한 소개를 해주세요.',NULL,'2023-09-16 18:18:42.000000','테마 소개 문의','기타문의',1,5),(17,'카페에서 사용하는 게임 기술에 대해 궁금합니다.',NULL,'2023-09-17 19:19:42.000000','게임 기술 문의','예약문의',2,7),(18,'방탈출 카페의 역사와 창업 이야기가 궁금합니다.',NULL,'2023-09-18 20:20:42.000000','카페 역사 문의','예약문의',3,8),(19,'카페 내부의 시설 및 장비에 대한 정보를 알려주세요.',NULL,'2023-09-19 21:21:42.000000','시설 정보 문의','기타문의',4,18),(20,'방탈출 카페에서 주최하는 이벤트에 대한 정보를 얻고 싶습니다.',NULL,'2023-09-20 22:22:42.000000','이벤트 정보 문의','기타문의',5,24),(21,'카페 내에서 사용되는 테크놀로지에 대한 정보를 알려주세요.',NULL,'2023-09-21 23:23:42.000000','테크놀로지 정보 문의','예약문의',1,46),(22,'dsfdfsfdf','2023-10-02 07:40:42.077547',NULL,'sfsafda','예약문의',4,3);
/*!40000 ALTER TABLE `board_qna` ENABLE KEYS */;
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
