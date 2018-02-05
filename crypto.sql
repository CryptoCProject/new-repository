-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: crypto
-- ------------------------------------------------------
-- Server version	5.7.15-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `auction`
--

DROP TABLE IF EXISTS `auction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auction` (
  `auction_id` int(11) NOT NULL AUTO_INCREMENT,
  `auctioneer_id` char(44) DEFAULT NULL,
  `auction_status` int(11) DEFAULT NULL,
  `auction_type` varchar(100) DEFAULT NULL,
  `initial_price` double DEFAULT NULL,
  `object_name` char(100) DEFAULT NULL,
  `bidder` char(44) DEFAULT NULL,
  `current_price` double DEFAULT NULL,
  PRIMARY KEY (`auction_id`),
  KEY `auctioneer_id_idx` (`auctioneer_id`),
  CONSTRAINT `auctioneer_id` FOREIGN KEY (`auctioneer_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auction`
--

LOCK TABLES `auction` WRITE;
/*!40000 ALTER TABLE `auction` DISABLE KEYS */;
INSERT INTO `auction` VALUES (118,'Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',1,'english',7.77,'jcjfjf',NULL,7.77),(119,'Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',0,'english',456.77,'hxdh',NULL,456.77),(120,'Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',2,'english',46.77,'jchd',NULL,46.77),(121,'Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',2,'english',0.75,'jfjjf',NULL,0.75),(122,'Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',2,'english',7.77,'jxhdjdsn',NULL,7.77),(123,'Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',2,'english',0.22,'hfjd','Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',0.2662),(124,'Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',2,'english',2.22,'hxdhd','Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',2.442),(125,'Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',2,'english',3.45,'bxbdbd','Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',3.7950000000000004),(126,'Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',2,'english',3.45,'hfd','Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',4.1745);
/*!40000 ALTER TABLE `auction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `miner`
--

DROP TABLE IF EXISTS `miner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `miner` (
  `miner_id` int(11) NOT NULL AUTO_INCREMENT,
  `public_key` blob,
  `balance` double DEFAULT '0',
  PRIMARY KEY (`miner_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `miner`
--

LOCK TABLES `miner` WRITE;
/*!40000 ALTER TABLE `miner` DISABLE KEYS */;
INSERT INTO `miner` VALUES (1,'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvYU3LLkFr1We4NU3S7hAmQDR5gP5SxQc1bOx0SHNq07+cKNp30njVVC0eC4yAoAd25iXjrxBqOpJFrqSfnPY5ubu8TzbEh+CxpO7tvZ/yhKVdNq/+TkVUPWWFzXvsk6hkjKHxYK4y3OWGhHPKsHOAfjF3jOP//7qVjmGw4ybpLiKJcyQ3q4czQI01RG1dxaFZH4jAmPsgt7Sitw4zzeCMaB0jdORbIbp4feox6qthzJe7w8Zx/O+Bh1bpHT1I7l8mMwxHmONoDgBAlpwq7AOQrnKaEI5izJay/e8e3kFtUKMyV8BEEBW5/Efa7G7o1s6d5JfzAe9JUP0iUt3ZCA4kwIDAQAB',0),(2,'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiHZvkBiSZpa6wahNA+Uw9Ddmtq0lxGapIICicX7PNglT3cjWThvziiDsfrwZz6aL/vf5Pim00ZMlvGVXdtnMRck1UD5c2bs/NXIlBL6JovUPLBH/4XCTNnDdhK9sktBU6BcpEpNigW530+23uXv0jLu0GSYTGh9WSEpOgTvWQGcfjs2zacNfagiQ2tv7yHJdOIZiQVCCzc+A6M0oMF/SWVw6QAUxi0zf7LCHEFwYv9EWb9Ko88BuFXBNVa8zxCzw/gSugRMNhEqodEZzGdVjwB2MtB5Ik8mJbo77twFtgqsvq3WKjAVRTDOqaTEIQ+JWnZ4TtIc3GcR+g12bpIfCrQIDAQAB',0),(3,'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApwTQClXjSLTVzHbepVOKqNL0TIxY4y23q9/mYPWNltby4rCUO/Ab8lCOJ/tsUZBGqT1ptflIToGwWxFz34iiuscJZRuIr7bcMF/xGMqZb8GeyhHH6phpz0b64eZlUWbJQMLULCsJHp0t4v/Bs7KUBoONqofoNUFpQu4o6ubLijbQJCdS4FkhmHEqKJvxzqHdRtd8s2dHFqV2peggjN3iOFCuJT/u6Oh6MuHFflkT+LFD9Dv40xNjyHij0zfEkQ1+UmvtNEENrJ3idPUBVzpn9lyHwGATKZG7V2BzY3d7A7aWLB8BFm4dKYjVEEoYc2MkV5udbdvnayZal5DWx4MJCwIDAQAB',0),(4,'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj27m5s15zIoFhaYbwJ40BGNBxQhz4jkQo5/2VNbTp2+f3WKruyIQxILU4PNgGVHOPdRXbCZeOJmpiPtjLXL3PGSoHyS4yk4Vuu4xgULDUjwbOxJI9Ld6Gd2yQynlDvQNf8lULoC0ZnTBt7FActxQxDxuQIvW4o0y99XQmtbhVFuaitlAOqiF55ZaW6c+Dm0cbztQ1+rG7dCFZjPsM9qEBEQVK1YRA0zvMkhmEhyaE0S1FbhhF68+cSmhDpRLS7zzi1FO9llI2AmP3Po+dwztHrocsSp2hCSvMb3e1ZuJoQ1REnvDg76BIXvFeTMplJ1WP+erZu2o4DCglRXRXkrm2QIDAQAB',0),(5,'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArJezD92ZkFgjIYHkWuhP2n+shK9qz+NifuF2bCOd+UWjNlfpveutyJJyrbV0XkoD96Lo9FfJxa1gIUhwnsfWOWeMlNp/lgQSHjT2N9buedoERPCSHPUoCV55GBZHbkp54Lc7lwy0nJhSik7q2vEXCMJu8bV0LboLyzbpceemzEcOiV2BCgAlx+abrZJPO3PdSxGXsybn7pajQ3uqbx0DNTiQCA7kBozlAM0HvMpGRgFPRktIZlx+r0nDScJ3XN7PRPbQI4z9wCd7pd4oIfuOhym/9HwHtf1cHWKxFQf83F26vjijuUnVD6rMmVvD148LcgiNfh8ljTf0UTVLn/pemwIDAQAB',0);
/*!40000 ALTER TABLE `miner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participant`
--

DROP TABLE IF EXISTS `participant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participant` (
  `participant_id` char(44) NOT NULL,
  `auction_id` int(11) NOT NULL,
  `outcome` bit(1) DEFAULT NULL,
  PRIMARY KEY (`participant_id`,`auction_id`),
  KEY `auction_id_idx` (`auction_id`),
  CONSTRAINT `auction_id` FOREIGN KEY (`auction_id`) REFERENCES `auction` (`auction_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `participant_id` FOREIGN KEY (`participant_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participant`
--

LOCK TABLES `participant` WRITE;
/*!40000 ALTER TABLE `participant` DISABLE KEYS */;
INSERT INTO `participant` VALUES ('Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',120,'\0'),('Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',121,'\0'),('Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',122,'\0'),('Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',123,'\0'),('Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',124,'\0'),('Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',125,'\0'),('Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=',126,'\0');
/*!40000 ALTER TABLE `participant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test`
--

DROP TABLE IF EXISTS `test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test`
--

LOCK TABLES `test` WRITE;
/*!40000 ALTER TABLE `test` DISABLE KEYS */;
/*!40000 ALTER TABLE `test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transaction` (
  `transaction_id` int(11) NOT NULL AUTO_INCREMENT,
  `auction_id` int(11) DEFAULT NULL,
  `transaction_value` double DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `auction_id_idx` (`auction_id`),
  CONSTRAINT `auction_id_c2` FOREIGN KEY (`auction_id`) REFERENCES `auction` (`auction_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` char(44) NOT NULL,
  `pwd` char(100) DEFAULT NULL,
  `public_key` blob,
  `balance` double DEFAULT '0',
  `last_login` mediumtext,
  `otp` char(80) DEFAULT NULL,
  `email` char(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('Yb5VqOL2tOFyM4vd8YTW2+4pyYhT4KBIXs7n8nua8LQ=','kL1nUOUoh5PvwCZ7xDtQ0x2qXep2VT/dMmV70uwxxN0=','MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4+y/3k/sTZAAs2E/p/vQbYb2ywOG8eODvwtrDZNvB3pOOU8r+k4QEp1nlNiMj5O1xlbrpUrNHqG76Vk+eY3HWodofpabV/zqHueF5+WYSdDC6Tb5HvMikj1ZPVsT85YdkSMtH+AMatS8oTNCPFhrmXIAuSEayvZYQ7XDrKor0WjdkhK1KXYY0vpM7SUjY3GaaY7E/IbH+pV08CaorWOwZunVBW3cFGQdEATz10kfa7t5QuK5UyNNXuUz6hpT5T08ju6+0+dzmMuTdBFIgcvZkRKRJfT5YaLKdQVlsD9ZdbfhFG7Y2JfCjKklvyxmuMNBQ/6qP/osJ1PXdkLc72sdEwIDAQAB',17.302760280788522,'1517762506395','B+E638UG6CLmONXxU/TGgYBl09llCFf+wY6y4SohPKc=','zafiratosv@yahoo.gr');
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

-- Dump completed on 2018-02-05 14:06:02
