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
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `noshowcount` int DEFAULT NULL,
  `birth` varchar(255) COLLATE utf8mb3_unicode_ci NOT NULL,
  `blacklist` tinyint(1) DEFAULT '0',
  `email` varchar(255) COLLATE utf8mb3_unicode_ci NOT NULL,
  `phone` varchar(255) COLLATE utf8mb3_unicode_ci NOT NULL,
  `reg_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `user_name` varchar(255) COLLATE utf8mb3_unicode_ci NOT NULL,
  `user_pw` varchar(255) COLLATE utf8mb3_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_a3imlf41l37utmxiquukk8ajc` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,3,'1998-01-01',1,'rena5170@gmail.com','010-0000-1111','2023-08-31 15:00:00','aaa111','유저일','asdf1234!'),(2,3,'1999-02-02',1,'zerochips123@gmail.com','010-1111-2222','2023-09-01 15:00:00','bbb222','유저이','asdf1234!'),(3,3,'1995-03-03',1,'dbtpgus111@gmail.com','010-2222-3333','2023-09-02 15:00:00','ccc333','유저삼','asdf1234!'),(4,3,'1990-04-04',1,'kangjh1994@gmail.com','010-3333-4444','2023-09-03 15:00:00','ddd444','유저사','asdf1234!'),(5,2,'1993-05-05',0,'tztos104@gmail.com','010-4444-5555','2023-09-04 15:00:00','eee555','유저오','asdf1234!'),(6,3,'1985-06-06',1,'snrn1996@gmail.com','010-5555-6666','2023-09-05 15:00:00','fff666','유저육','asdf1234!'),(7,1,'1989-07-07',0,'alswjd4968@gmail.com','010-6666-7777','2023-09-06 15:00:00','ggg777','유저칠','asdf1234!'),(8,3,'1988-08-08',1,'sarahkim@gmail.com','010-7777-8888','2023-09-07 15:00:00','hhh888','유저팔','asdf1234!'),(9,2,'1991-09-09',0,'dbtpgus111@gmail.com','010-8888-9999','2023-09-08 15:00:00','iii999','유저구','asdf1234!'),(10,3,'1986-10-10',1,'lisawu@gmail.com','010-9999-0000','2023-09-09 15:00:00','jjj000','유저십','asdf1234!'),(11,3,'1992-11-11',1,'tomwilson@gmail.com','010-1234-5678','2023-09-10 15:00:00','kkk111','유저십일','asdf1234!'),(12,3,'1997-12-12',1,'gracepark@gmail.com','010-2345-6789','2023-09-11 15:00:00','lll222','유저십이','asdf1234!'),(13,3,'1993-05-13',1,'chriskim@gmail.com','010-3456-7890','2023-09-12 15:00:00','mmm333','유저십삼','asdf1234!'),(14,2,'1988-04-14',0,'nataliejones@gmail.com','010-4567-8901','2023-09-13 15:00:00','nnn444','유저십사','asdf1234!'),(15,3,'1990-03-15',1,'williamchen@gmail.com','010-5678-9012','2023-09-14 15:00:00','ooo555','유저십오','asdf1234!'),(16,2,'1987-02-16',0,'olivialee@gmail.com','010-6789-0123','2023-09-15 15:00:00','ppp666','유저십육','asdf1234!'),(17,0,'1986-01-17',0,'samjackson@gmail.com','010-7890-1234','2023-09-16 15:00:00','qqq777','유저십칠','asdf1234!'),(18,0,'1995-08-18',0,'elizabethkim@gmail.com','010-8901-2345','2023-09-17 15:00:00','rrr888','유저십팔','asdf1234!'),(19,0,'1992-07-19',0,'danielbrown@gmail.com','010-9012-3456','2023-09-18 15:00:00','sss999','유저십구','asdf1234!'),(20,2,'1998-06-20',0,'sofialee@gmail.com','010-0123-4567','2023-09-19 15:00:00','ttt000','유저이십','asdf1234!'),(21,3,'1989-03-21',1,'jameskim@gmail.com','010-0000-0001','2023-09-20 15:00:00','uuu111','유저이십일','asdf1234!'),(22,2,'1991-12-22',0,'gracewang@gmail.com','010-0000-0002','2023-09-21 15:00:00','vvv222','유저이십이','asdf1234!'),(23,1,'1996-11-23',0,'davidchen@gmail.com','010-0000-0003','2023-09-22 15:00:00','www333','유저이십삼','asdf1234!'),(24,3,'1993-09-24',1,'sarahbrown@gmail.com','010-0000-0004','2023-09-23 15:00:00','xxx444','유저이십사','asdf1234!'),(25,2,'1987-08-25',0,'andrewlee@gmail.com','010-0000-0005','2023-09-24 15:00:00','yyy555','유저이십오','asdf1234!'),(26,0,'1990-07-26',0,'michellekim@gmail.com','010-0000-0006','2023-09-25 15:00:00','zzz666','유저이십육','asdf1234!'),(27,3,'1985-06-27',1,'robertjones@gmail.com','010-0000-0007','2023-09-26 15:00:00','aaa777','유저이십칠','asdf1234!'),(28,2,'1992-05-28',0,'emilywilson@gmail.com','010-0000-0008','2023-09-27 15:00:00','bbb888','유저이십팔','asdf1234!'),(29,0,'1988-04-29',0,'johnpark@gmail.com','010-0000-0009','2023-09-28 15:00:00','ccc999','유저이십구','asdf1234!'),(30,0,'1994-03-30',0,'sophiakim@gmail.com','010-0000-0010','2023-09-29 15:00:00','ddd000','유저삼십','asdf1234!'),(31,3,'1989-02-01',1,'williamlee@gmail.com','010-0000-0011','2023-09-30 15:00:00','eee111','유저삼십일','asdf1234!'),(32,3,'1997-01-02',1,'elizabethlee@gmail.com','010-0000-0012','2023-10-01 15:00:00','fff222','유저삼십이','asdf1234!'),(33,1,'1995-12-03',0,'michaelkim@gmail.com','010-0000-0013','2023-10-02 15:00:00','ggg333','유저삼십삼','asdf1234!'),(34,2,'1993-11-04',0,'nataliewang@gmail.com','010-0000-0014','2023-10-03 15:00:00','hhh444','유저삼십사','asdf1234!'),(35,3,'1988-10-05',1,'samlee@gmail.com','010-0000-0015','2023-10-04 15:00:00','iii555','유저삼십오','asdf1234!'),(36,3,'1991-09-06',1,'gracejones@gmail.com','010-0000-0016','2023-10-05 15:00:00','jjj666','유저삼십육','asdf1234!'),(37,1,'1987-08-07',0,'davidbrown@gmail.com','010-0000-0017','2023-10-06 15:00:00','kkk777','유저삼십칠','asdf1234!'),(38,0,'1994-07-08',0,'lisachen@gmail.com','010-0000-0018','2023-10-07 15:00:00','lll888','유저일삼십팔','asdf1234!'),(39,1,'1986-06-09',0,'oliviawilson@gmail.com','010-0000-0019','2023-10-08 15:00:00','mmm999','유저삼십구','asdf1234!'),(40,2,'1998-05-10',0,'johnchen@gmail.com','010-0000-0020','2023-10-09 15:00:00','nnn000','유저사십','asdf1234!'),(41,0,'1990-04-11',0,'emilyjones@gmail.com','010-0000-0021','2023-10-10 15:00:00','ooo111','유저사십일','asdf1234!'),(42,3,'1996-03-12',1,'williampark@gmail.com','010-0000-0022','2023-10-11 15:00:00','ppp222','유저사십이','asdf1234!'),(43,0,'1993-02-13',0,'sarahwang@gmail.com','010-0000-0023','2023-10-12 15:00:00','qqq333','유저사십삼','asdf1234!'),(44,2,'1988-01-14',0,'jameslee@gmail.com','010-0000-0024','2023-10-13 15:00:00','rrr444','유저사십사','asdf1234!'),(45,2,'1992-12-15',0,'elizabethkim@gmail.com','010-0000-0025','2023-10-14 15:00:00','sss555','유저사십오','asdf1234!'),(46,0,'1991-11-16',0,'danielwilson@gmail.com','010-0000-0026','2023-10-15 15:00:00','ttt666','유저사십육','asdf1234!'),(47,3,'1997-10-17',1,'nataliechen@gmail.com','010-0000-0027','2023-10-16 15:00:00','uuu777','유저사십칠','asdf1234!'),(48,1,'1986-09-18',0,'johndoe@gmail.com','010-0000-0028','2023-10-17 15:00:00','vvv888','유저사십팔','asdf1234!'),(49,2,'1989-08-19',0,'gracelim@gmail.com','010-0000-0029','2023-10-18 15:00:00','www999','유저사십구','asdf1234!'),(50,1,'1994-07-20',0,'michaeljones@gmail.com','010-0000-0030','2023-10-19 15:00:00','xxx000','유저오십','asdf1234!'),(52,0,'1992-10-06',0,'kasean@naver.com','01012345678','2023-10-01 22:11:48','user111','일유저','user111~@@');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-02  8:00:53
