INSERT INTO `feed_send` (`id`, `feed_id`, `uid`, `source_id`, `source_type`, `feed_create_time`, `create_time`, `privilege`)
VALUES
	(1, 135113, '219672201', 78220, 1, '2021-10-11 19:27:57', '2021-10-11 19:27:57', 1),
	(2, 135121, '36050769', 277, 24, '2021-10-26 10:42:51', '2021-10-26 10:42:50', 1);

INSERT INTO `feed_source` (`id`, `feed_id`, `feed_type`, `source_owner_id`, `source_id`, `source_type`, `source_owner_name`, `source_owner_icon`, `rose_count`, `title`, `feed_create_time`, `content`, `state`, `create_time`, `gender`, `topic_id`, `tags`, `at_infos`, `privilege`, `scene_type`, `scene_param`)
VALUES
	(1, 135113, 1, '219672201', 78220, 1, '玩吧用户2201', 'https://qiniustatic.wodidashi.com/user/icon/3_3x.png?imageView/2/w/108/h/108', 0, '', '2021-10-11 19:27:57', '{\"giftStrategyId\":\"196\",\"albumImage\":\"{\\\"uid\\\":\\\"219672201\\\",\\\"iconImg\\\":\\\"https://qiniustatic.wodidashi.com/picture/jt99HlOCOFekx?imageView/2/w/200/h/200\\\",\\\"iconImgLarge\\\":\\\"https://qiniustatic.wodidashi.com/picture/jt99HlOCOFekx\\\",\\\"forwardState\\\":0}\",\"location\":\"{\\\"lo\\\":116.4367505529168,\\\"la\\\":39.940416873875456}\"}', 0, '2021-10-11 19:27:57', 'f', 0, '2526', '', 1, 2, '{\"roomId\":\"1336457\"}'),
	(2, 135121, 18, '36050769', 277, 24, '10889', 'https://qiniustatic.wodidashi.com/head/s9fsoi6bckkiH?imageView/2/w/108/h/108', 0, '', '2021-10-26 10:42:51', '{\"location\":\"{\\\"lo\\\":116.43644392247903,\\\"la\\\":39.9403008921132}\",\"video\":\"{\\\"uid\\\":\\\"36050769\\\",\\\"imageUrl\\\":\\\"https://qiniustatic.wodidashi.com/video/W8wrfQkPD2uQR?vframe/jpg/offset/0\\\",\\\"playUrl\\\":\\\"https://qiniustatic.wodidashi.com/video/W8wrfQkPD2uQR\\\",\\\"width\\\":1080,\\\"height\\\":1920,\\\"videoLength\\\":8}\"}', 3, '2021-10-26 10:42:50', 'f', 0, '', '', 1, 0, '');

INSERT INTO `feed_recv` (`id`, `feed_id`, `uid`, `feed_owner_id`, `feed_create_time`, `create_time`)
VALUES
	(1, 135113, '219672201', '219672201', '2021-10-11 19:27:57', '2021-10-11 19:27:57'),
	(2, 135121, '219672201', '36050769', '2021-10-26 10:42:51', '2021-10-11 19:27:57'),
	(3, 135121, '36050769', '36050769', '2021-10-26 10:42:51', '2021-10-11 19:27:57'),
	(4, 135113, '36050769', '219672201', '2021-10-11 19:27:57', '2021-10-11 19:27:57');

