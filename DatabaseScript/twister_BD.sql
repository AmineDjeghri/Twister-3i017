-- phpMyAdmin SQL Dump
-- version 4.4.15.7
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Mar 26 Février 2019 à 00:13
-- Version du serveur :  5.6.37
-- Version de PHP :  5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `twister_BD`
--

-- --------------------------------------------------------

--
-- Structure de la table `friendship`
--

CREATE TABLE IF NOT EXISTS `friendship` (
  `id_user1` int(11) NOT NULL,
  `id_user2` int(11) NOT NULL,
  `date_connexion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `friendship`
--

INSERT INTO `friendship` (`id_user1`, `id_user2`, `date_connexion`) VALUES
(30, 30, '2019-02-23 17:59:57'),
(30, 32, '2019-02-23 17:59:50'),
(33, 30, '2019-02-25 00:19:57'),
(33, 32, '2019-02-26 00:10:36');

-- --------------------------------------------------------

--
-- Structure de la table `Session`
--

CREATE TABLE `Session` (
  `id_user` int(11) NOT NULL,
  `key_session` varchar(64) NOT NULL,
  `date_session` timestamp  NULL ,
  `date_fin` timestamp  NULL ,
  `root_session` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `Session`
--

INSERT INTO `Session` (`id_user`, `key_session`, `date_session`, `date_fin`, `root_session`) VALUES
(34, 'PKYBt7N1sxzeAfezhN9xiQlct7sB7jQq', '2019-02-25 23:52:58', '2019-02-26 00:22:58', NULL),
(33, '8GQO3zAUPgofan9oyl5JtQEklxp0Z238', '2019-02-26 00:09:50', '2019-02-26 00:40:55', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id_user` int(11) NOT NULL,
  `login_user` varchar(32) DEFAULT NULL,
  `first_name_user` varchar(32) DEFAULT NULL,
  `family_name_user` varchar(32) DEFAULT NULL,
  `password_user` blob,
  `mail_user` varchar(64) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `user`
--

INSERT INTO `user` (`id_user`, `login_user`, `first_name_user`, `family_name_user`, `password_user`, `mail_user`) VALUES
(30, 'Adonis', 'adonis', '1234', 0x48616f75696c69, 'ahmed_haouili@yahoo.fr'),
(31, 'Amine', 'amine', '1234', 0x446a6567687269, 'amine_djeghri@yahoo.fr'),
(32, 'user0', 'user0', 'user0', 0x7573657230, 'user0@user0.com'),
(33, 'user1', 'user1', 'user1', 0x7573657231, 'user1_user1@yahoo.fr'),
(34, 'user2', 'user2', 'user2', 0x7573657232, 'user2_user2@yahoo.fr');

--
-- Index pour les tables exportées
--

--
-- Index pour la table `friendship`
--
ALTER TABLE `friendship`
  ADD PRIMARY KEY (`id_user1`,`id_user2`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `login_user` (`login_user`),
  ADD UNIQUE KEY `mail_user` (`mail_user`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=35;
/*!40101 SETa CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
