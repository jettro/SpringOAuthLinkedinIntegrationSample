CREATE TABLE `Connection` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `accountId` varchar(255) NOT NULL DEFAULT '',
  `providerId` varchar(255) NOT NULL DEFAULT '',
  `accessToken` varchar(255) NOT NULL DEFAULT '',
  `secret` varchar(255) DEFAULT NULL,
  `refreshToken` varchar(255) DEFAULT NULL,
  `providerAccountId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;;

