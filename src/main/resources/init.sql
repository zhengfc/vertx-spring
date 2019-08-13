CREATE TABLE IF NOT EXISTS `user` (
  `username` varchar(32) NOT NULL,
  `email` varchar(32) DEFAULT NULL,
  `mobile` varchar(11) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `dep_id` smallint(6) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
