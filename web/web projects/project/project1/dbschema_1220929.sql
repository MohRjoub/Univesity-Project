-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jan 16, 2025 at 12:26 PM
-- Server version: 8.0.40
-- PHP Version: 8.3.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `web1220929_task_allocator`
--

-- --------------------------------------------------------

--
-- Table structure for table `projects`
--

CREATE TABLE `projects` (
  `project_id` varchar(10) COLLATE utf8mb4_general_ci NOT NULL,
  `title` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `description` text COLLATE utf8mb4_general_ci,
  `customer_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `total_budget` decimal(10,2) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `team_leader_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `projects`
--

INSERT INTO `projects` (`project_id`, `title`, `description`, `customer_name`, `total_budget`, `start_date`, `end_date`, `team_leader_id`) VALUES
('ABCD-11111', 'Laptop repair', 'repair a laptop for a customer', 'Ahmad Mousa', 500.00, '2025-01-13', '2025-01-31', 1000000009),
('ABCD-11112', 'Medical Application', 'Application for Medical Services', 'Rjoub Hospital', 10000.00, '2025-01-13', '2025-07-21', 1000000009),
('ABCD-11113', 'JDECO App', 'Application for Jerusalim Destrict Company', 'JDECO Manager', 12000.00, '2025-01-21', '2025-05-21', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `project_documents`
--

CREATE TABLE `project_documents` (
  `id` int NOT NULL,
  `project_id` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `file_path` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `project_documents`
--

INSERT INTO `project_documents` (`id`, `project_id`, `title`, `file_path`) VALUES
(7, 'ABCD-11111', 'lap picutre', 'documents/ABCD-111110.jpg'),
(8, 'ABCD-11112', 'Documatation', 'documents/ABCD-111120.pdf'),
(9, 'ABCD-11113', 'App Dashboard', 'documents/ABCD-111130.png');

-- --------------------------------------------------------

--
-- Table structure for table `tasks`
--

CREATE TABLE `tasks` (
  `task_id` int NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `description` text COLLATE utf8mb4_general_ci,
  `project_id` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `effort` int DEFAULT NULL,
  `priority` enum('Low','Medium','High') COLLATE utf8mb4_general_ci DEFAULT 'Medium',
  `status` enum('Pending','In Progress','Completed','Active') COLLATE utf8mb4_general_ci DEFAULT 'Pending',
  `completion_percentage` int NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tasks`
--

INSERT INTO `tasks` (`task_id`, `name`, `description`, `project_id`, `start_date`, `end_date`, `effort`, `priority`, `status`, `completion_percentage`) VALUES
(16, 'remove case', 'remove back case for the laptop', 'ABCD-11111', '2025-01-13', '2025-01-14', 1, 'High', 'In Progress', 25),
(17, 'remove motherboard', 'remove laptop motherboard', 'ABCD-11111', '2025-01-15', '2025-01-16', 1, 'High', 'Active', 0),
(18, 'replace cpu', 'replace laptop cpu with new one', 'ABCD-11111', '2025-01-16', '2025-01-17', 1, 'Low', 'Pending', 0),
(19, 'Test lsptop', 'test the laptop after repair', 'ABCD-11111', '2025-01-19', '2025-01-20', 1, 'Medium', 'Pending', 0),
(20, 'Design Dashboard', 'Design Dashboard for the app', 'ABCD-11112', '2025-01-13', '2025-01-20', 5, 'Medium', 'Pending', 0);

-- --------------------------------------------------------

--
-- Table structure for table `team_assignments`
--

CREATE TABLE `team_assignments` (
  `assignment_id` int NOT NULL,
  `task_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `role` enum('Developer','Designer','Tester','Analyst','Support') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `contribution_percentage` int DEFAULT NULL,
  `start_date` date NOT NULL,
  `end_date` date DEFAULT NULL,
  `accepted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `team_assignments`
--

INSERT INTO `team_assignments` (`assignment_id`, `task_id`, `user_id`, `role`, `contribution_percentage`, `start_date`, `end_date`, `accepted`) VALUES
(61, 16, 1000000010, 'Support', 100, '2025-01-13', NULL, 1),
(62, 18, 1000000011, 'Support', 100, '2025-01-13', NULL, 0),
(63, 17, 1000000012, 'Support', 100, '2025-01-13', NULL, 1),
(64, 19, 1000000010, 'Tester', 100, '2025-01-13', NULL, 0),
(65, 20, 1000000010, 'Designer', 50, '2025-01-13', NULL, 0),
(66, 20, 1000000011, 'Designer', 50, '2025-01-13', NULL, 0);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int NOT NULL,
  `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `role` enum('Manager','ProjectLeader','TeamMember') COLLATE utf8mb4_general_ci NOT NULL,
  `full_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `phone` varchar(15) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `qualification` text COLLATE utf8mb4_general_ci,
  `skills` text COLLATE utf8mb4_general_ci,
  `house_no` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `street` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `city` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `country` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `date_of_birth` date NOT NULL,
  `id_number` varchar(50) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `role`, `full_name`, `email`, `phone`, `qualification`, `skills`, `house_no`, `street`, `city`, `country`, `date_of_birth`, `id_number`) VALUES
(1000000008, 'MoRjoub', '123456789', 'Manager', 'Mohammad Ryad Mohammad Rojoob', 'mohammadrojoob@gmail.com', '0595410777', 'CS BA', 'skill1 skill2 skill3', '10', 'Main street', 'Dura -AL kom', 'Palestine', '2004-07-21', '409183076'),
(1000000009, 'AhmadRjoub', '123456789', 'ProjectLeader', 'Ahmad Ryad Mohammad Rjoub', 'ahmadrojoob@gmail.com', '0595410777', 'CS BA', 'skill1 skill2 skill3', '10', 'Main street', 'Dura -AL kom', 'Palestine', '2004-12-05', '123456789'),
(1000000010, 'ibRjoub', '123456789asd', 'TeamMember', 'Ibraheem Ryad Ahmad Rjoub', 'ibraheemrojoob@gmail.com', '0595410777', 'CS BA', 'skill1 skill2 skill3', '10', 'Main street', 'Dura -AL kom', 'Palestine', '1996-01-01', '40129865'),
(1000000011, 'GhHajAli', '123456789', 'TeamMember', 'Ghaith Haj Ali', 'ghaithhajali@gmail.com', '0599547895', 'CS BA', 'skill1 skill2 skill3', '4', 'Main street', 'Nablus-Jamaeen', 'Palestine', '2004-01-01', '402154897'),
(1000000012, 'zaRajab', '123456789abc', 'TeamMember', 'Zaid Mahmood Rajab', 'zaidrajab@gmail.com', '0595410777', 'CS BA', 'skill1 skill2 skill3', '5', 'Main street', 'Dura -AL kom', 'Palestine', '2002-11-30', '40129865');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `projects`
--
ALTER TABLE `projects`
  ADD PRIMARY KEY (`project_id`),
  ADD KEY `projectLeader` (`team_leader_id`);

--
-- Indexes for table `project_documents`
--
ALTER TABLE `project_documents`
  ADD PRIMARY KEY (`id`),
  ADD KEY `project_id` (`project_id`);

--
-- Indexes for table `tasks`
--
ALTER TABLE `tasks`
  ADD PRIMARY KEY (`task_id`),
  ADD KEY `project_id` (`project_id`);

--
-- Indexes for table `team_assignments`
--
ALTER TABLE `team_assignments`
  ADD PRIMARY KEY (`assignment_id`),
  ADD KEY `task_id` (`task_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `project_documents`
--
ALTER TABLE `project_documents`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `tasks`
--
ALTER TABLE `tasks`
  MODIFY `task_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `team_assignments`
--
ALTER TABLE `team_assignments`
  MODIFY `assignment_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=67;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1000000013;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `projects`
--
ALTER TABLE `projects`
  ADD CONSTRAINT `projectLeader` FOREIGN KEY (`team_leader_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `project_documents`
--
ALTER TABLE `project_documents`
  ADD CONSTRAINT `project_documents_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`);

--
-- Constraints for table `tasks`
--
ALTER TABLE `tasks`
  ADD CONSTRAINT `tasks_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`);

--
-- Constraints for table `team_assignments`
--
ALTER TABLE `team_assignments`
  ADD CONSTRAINT `team_assignments_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`),
  ADD CONSTRAINT `team_assignments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
