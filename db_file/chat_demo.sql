-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 23, 2021 at 07:45 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `chat_demo`
--

-- --------------------------------------------------------

--
-- Table structure for table `firebase_token`
--

CREATE TABLE `firebase_token` (
  `id` int(20) NOT NULL,
  `user_id` int(20) NOT NULL,
  `token` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `message`
--

CREATE TABLE `message` (
  `message_id` int(20) NOT NULL,
  `content_type` varchar(200) NOT NULL,
  `content` text NOT NULL,
  `title` text NOT NULL,
  `sender_id` int(20) NOT NULL,
  `receiver_id` int(20) NOT NULL,
  `time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `thread_id` int(20) NOT NULL,
  `isunread` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `message`
--

INSERT INTO `message` (`message_id`, `content_type`, `content`, `title`, `sender_id`, `receiver_id`, `time`, `thread_id`, `isunread`) VALUES
(1, 'message', 'hey there', '', 1, 2, '2021-01-23 06:38:57', 1, 1),
(5, 'message', 'hello', '', 1, 2, '2021-01-23 06:44:30', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `message_thread_list`
--

CREATE TABLE `message_thread_list` (
  `id` int(20) NOT NULL,
  `sender_id` int(20) NOT NULL,
  `reciever_id` int(20) NOT NULL,
  `last_message` int(20) NOT NULL,
  `last_message_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `isUnread` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `message_thread_list`
--

INSERT INTO `message_thread_list` (`id`, `sender_id`, `reciever_id`, `last_message`, `last_message_time`, `isUnread`) VALUES
(1, 1, 2, 0, '2021-01-23 06:44:30', 1);

-- --------------------------------------------------------

--
-- Table structure for table `user_info`
--

CREATE TABLE `user_info` (
  `uid` int(20) NOT NULL,
  `user_name` varchar(200) NOT NULL,
  `user_full_name` varchar(500) NOT NULL,
  `profile_picture` varchar(500) NOT NULL,
  `position` varchar(200) NOT NULL,
  `password` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user_info`
--

INSERT INTO `user_info` (`uid`, `user_name`, `user_full_name`, `profile_picture`, `position`, `password`) VALUES
(1, 'starlut', 'Kamrul Hasan', '', 'xxx', '12345'),
(2, 'starlut2', 'kamrul 2', '', '1123', '12345');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `firebase_token`
--
ALTER TABLE `firebase_token`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`message_id`);

--
-- Indexes for table `message_thread_list`
--
ALTER TABLE `message_thread_list`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_info`
--
ALTER TABLE `user_info`
  ADD PRIMARY KEY (`uid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `firebase_token`
--
ALTER TABLE `firebase_token`
  MODIFY `id` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `message`
--
ALTER TABLE `message`
  MODIFY `message_id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `message_thread_list`
--
ALTER TABLE `message_thread_list`
  MODIFY `id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `user_info`
--
ALTER TABLE `user_info`
  MODIFY `uid` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
