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
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(999) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `modi_date` datetime(6) DEFAULT NULL,
  `reg_date` datetime(6) DEFAULT NULL,
  `writer` varchar(255) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `branch_id` int DEFAULT NULL,
  `qna_id` int NOT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `FKo36mlos9ed2n6lna198ldqkgf` (`branch_id`),
  KEY `FKo9thuwuxb9axlg5yw5shudpfu` (`qna_id`),
  CONSTRAINT `FKo36mlos9ed2n6lna198ldqkgf` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`branch_id`),
  CONSTRAINT `FKo9thuwuxb9axlg5yw5shudpfu` FOREIGN KEY (`qna_id`) REFERENCES `board_qna` (`qna_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (3,'안녕하세요 \'SUPUL [대구점]\' 입니다. \r\n\r\n방탈출 카페에서 주최하는 이벤트 중 하나로 \"가입 이벤트\"가 있습니다. \r\n\r\n가입 이벤트는 신규 회원들을 환영하고 기존 고객에게 감사의 인사를 전하는 기회로 자주 활용되며, 다양한 혜택을 통해 방탈출 카페의 게임을 더 즐길 수 있도록 도와줍니다. \r\n이외에 다른 이벤트는 공지사항 게시판에서 확인하실 수 있습니다. \r\n\r\n감사합니다.',NULL,'2023-10-02 05:42:49.052463','dea444',5,20),(5,'안녕하세요, \'SUPUL [강남점]\'입니다.\r\n\r\n저희 방탈출 카페는 고객 여러분에게 다양한 시설과 장비를 제공하고 있습니다. 아래는 저희 카페 내부의 시설과 장비에 대한 정보입니다:\r\n\r\n- 다양한 테마 룸: 저희 카페에는 다양한 테마의 방탈출 게임 룸이 준비되어 있습니다. 고객은 자신의 취향과 난이도에 맞는 테마를 선택할 수 있습니다.\r\n\r\n- 고급 가구 및 장식품: 각 룸은 고급스러운 가구와 특별한 장식품으로 꾸며져 있어, 고객들이 현실에서 벗어나 모험을 느낄 수 있도록 합니다.\r\n\r\n- 퍼즐과 게임: 방탈출 게임은 다양한 퍼즐과 미션으로 구성되어 있습니다. 이를 해결하고 퀘스트를 완수하는 과정에서 논리력과 협력력을 발휘할 수 있습니다.\r\n\r\n- 고급 사운드 시스템: 저희 룸은 최신 사운드 시스템을 사용하여 게임의 몰입감을 높입니다. 효과음과 음악은 게임 경험을 풍부하게 만들어줍니다.\r\n\r\n- 힌트 및 안전 시스템: 게임을 즐기다 어려움을 겪는 경우, 힌트 시스템을 통해 도움을 받을 수 있습니다. 또한, 안전을 위해 모든 룸은 비상 출구를 갖추고 있습니다.\r\n\r\n- 커뮤니티 공간: 방탈출 게임 후에는 고객들이 휴식하고 이야기를 나눌 수 있는 커뮤니티 공간이 마련되어 있습니다.\r\n\r\n우리의 목표는 고객들에게 흥미진진한 체험과 추억을 제공하는 것입니다.\r\n\r\n\r\n더 많은 정보나 예약 관련 문의사항이 있으시다면 언제든지 저희에게 문의해 주세요. 감사합니다.',NULL,'2023-10-02 05:46:23.956468','gang111',4,19),(6,'안녕하세요, \'SUPUL [수원점]\'입니다.\r\n\r\n\'SUPUL\'은 미션과 퍼즐을 통해 고객들에게 색다른 경험을 제공하고자 했습니다. 그 첫 시작은 작은 공간에서의 아이디어로부터 출발했으며, 고객들이 현실에서 벗어나 모험을 느낄 수 있는 장소를 만들고자 힘썼습니다.\r\n\r\n고객들에게 재미와 도전을 주는 게임을 개발하고, 특별한 효과음과 사운드를 구현하기 위해 최선을 다하였습니다. 이를 통해 방탈출 게임은 점차 성공적으로 성장하고, 많은 이들에게 사랑받는 방탈출 카페로 자리잡게 되었습니다.\r\n\r\n저희는 항상 고객들의 피드백을 듣고, 더 나은 게임과 경험을 제공하기 위해 노력하고 있습니다. 방탈출 카페의 역사는 고객들의 참여와 성원 덕분에 오늘날까지 이어져왔습니다.\r\n\r\n더 많은 이야기와 정보를 원하시면 언제든지 저희에게 문의해 주세요. 감사합니다.',NULL,'2023-10-02 05:48:37.946376','su5555',3,18),(7,'안녕하세요, \'SUPUL [수원점]\'입니다.\r\n\r\n방탈출 카페 방문 시 특별한 요청사항이 있다면 언제든지 문의해 주세요. 저희는 최선을 다해 요청사항을 수용해 드리려 노력하고 있습니다. 그러나 아쉽게도 밥을 제공하는 서비스는 제공하지 못하고 있습니다.\r\n\r\n그 외의 요청사항이나 궁금한 사항이 있다면 언제든지 문의 주시면, 최선을 다해 도움 드리겠습니다.\r\n\r\n감사합니다.',NULL,'2023-10-02 05:50:14.479498','su5555',3,13),(8,'답변 감사합니다',NULL,'2023-10-02 06:59:21.646270','rrr888',NULL,19);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-02  8:00:55
