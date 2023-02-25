drop table if exists `feed_recv`;
CREATE TABLE `feed_recv` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `uid` varchar(16) NOT NULL,
  `feed_owner_id` varchar(16) NOT NULL,
  `feed_create_time` datetime NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uid_feed_id` (`uid`,`feed_id`),
  KEY `idx_user` (`uid`,`feed_owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists `feed_source`;
CREATE TABLE `feed_source` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `feed_type` int(11) NOT NULL,
  `source_owner_id` varchar(13) NOT NULL,
  `source_id` bigint(20) NOT NULL,
  `source_type` int(11) NOT NULL,
  `source_owner_name` varchar(45) NOT NULL DEFAULT '',
  `source_owner_icon` varchar(150) NOT NULL DEFAULT '',
  `rose_count` int(11) NOT NULL DEFAULT '0',
  `title` varchar(1024) DEFAULT NULL,
  `feed_create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `content` varchar(8192) DEFAULT '""',
  `state` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gender` varchar(10) NOT NULL DEFAULT '',
  `topic_id` int(11) NOT NULL DEFAULT '0',
  `tags` varchar(30) NOT NULL DEFAULT '' COMMENT '标签',
  `at_infos` varchar(4096) NOT NULL DEFAULT '' COMMENT '被@人的信息',
  `privilege` int(11) NOT NULL DEFAULT '2' COMMENT '公开',
  `scene_type` int(11) DEFAULT '0' COMMENT '来源场景',
  `scene_param` varchar(1000) DEFAULT '' COMMENT '场景参数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_feed_id` (`feed_id`),
  KEY `idx_source` (`source_owner_id`,`source_id`,`source_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists `feed_send`;
CREATE TABLE `feed_send` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `uid` varchar(16) NOT NULL,
  `source_id` bigint(20) NOT NULL,
  `source_type` int(11) NOT NULL,
  `feed_create_time` datetime NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `privilege` int(11) NOT NULL DEFAULT '2' COMMENT '公开',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_feed_id` (`feed_id`),
  UNIQUE KEY `idx_source` (`uid`,`source_id`,`source_type`),
  KEY `idx_uid_feed_id` (`uid`,`feed_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


