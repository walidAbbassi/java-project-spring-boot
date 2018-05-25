--DROP TABLE IF EXISTS `etudiant`;
--DROP TABLE IF EXISTS `role`;
--DROP TABLE IF EXISTS `user`;
--DROP TABLE IF EXISTS `user_role`;
INSERT INTO `role` (`role_id`, `role`) VALUES ('1', 'ADMIN');
INSERT INTO `role` (`role_id`, `role`) VALUES ('2', 'USER');