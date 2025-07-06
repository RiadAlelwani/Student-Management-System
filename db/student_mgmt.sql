-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 04, 2025 at 10:14 PM
-- Server version: 5.5.24-log
-- PHP Version: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `student_mgmt`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE IF NOT EXISTS `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `username`, `password`, `name`, `email`, `gender`, `age`) VALUES
(2, 'admin', 'admin', 'riad', 'riadalelwani@gmail.com', 'Male', 45),
(4, 'user', '76c234178cc423cb71c46809291e0a78025f0d28102aeabbaf51b0bfb1e2eed6', 'user', 'user@gmail.com', 'Female', 35),
(5, 'admin1', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'admin', 'amin@gmail.com', 'Male', 33);

-- --------------------------------------------------------

--
-- Table structure for table `course`
--

CREATE TABLE IF NOT EXISTS `course` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text,
  `credits` int(11) DEFAULT NULL,
  `teacher_id` int(11) DEFAULT NULL,
  `department_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `teacher_id` (`teacher_id`),
  KEY `fk_course_department` (`department_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `course`
--

INSERT INTO `course` (`id`, `name`, `description`, `credits`, `teacher_id`, `department_id`) VALUES
(1, 'Java Basics', 'Introduction to Java programming', 3, 8, 1),
(2, 'Data Structure ', 'programming', 3, 1, 1),
(3, 'c++', 'Introduction to c++ programming', 3, 1, 1),
(4, 'MATH -4', 'Basic of Math', 4, 7, 1),
(5, 'c#', 'Introduction to c# programming', 3, 2, 1),
(6, 'MATH - 2', 'Basic of Math', 3, 3, 1),
(7, 'MATH - 3', 'Advance of Math', 3, 4, 1),
(8, 'image processing', 'image processing', 3, 7, 1),
(9, 'solving problem', 'solving problem', 3, 7, 1),
(10, 'MATH -1', 'Basic of Math', 4, 3, 1);

-- --------------------------------------------------------

--
-- Table structure for table `department`
--

CREATE TABLE IF NOT EXISTS `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `department`
--

INSERT INTO `department` (`id`, `name`) VALUES
(1, 'Computer Science'),
(2, 'Mathematics'),
(3, 'Physics'),
(4, 'Biology'),
(5, 'Chemistry'),
(6, 'English'),
(7, 'History'),
(8, 'Economics');

-- --------------------------------------------------------

--
-- Table structure for table `enrollment`
--

CREATE TABLE IF NOT EXISTS `enrollment` (
  `student_id` int(11) NOT NULL DEFAULT '0',
  `course_id` int(11) NOT NULL DEFAULT '0',
  `semester_id` int(11) NOT NULL,
  `grade` double DEFAULT NULL,
  `teacher_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`student_id`,`course_id`),
  KEY `course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `enrollment`
--

INSERT INTO `enrollment` (`student_id`, `course_id`, `semester_id`, `grade`, `teacher_id`) VALUES
(1, 1, 1, 95, 1),
(1, 2, 1, 90, 2),
(1, 3, 1, 90, 3),
(1, 8, 1, 85, 4),
(2, 1, 1, 85, 5),
(2, 2, 1, 85, 6),
(2, 3, 1, 85, 1),
(4, 2, 1, 85, 8),
(5, 5, 5, 90, 3),
(8, 1, 1, 88, 3);

-- --------------------------------------------------------

--
-- Table structure for table `semester`
--

CREATE TABLE IF NOT EXISTS `semester` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `is_open` tinyint(1) NOT NULL,
  `season` varchar(20) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `semester`
--

INSERT INTO `semester` (`id`, `is_open`, `season`, `year`) VALUES
(1, 1, 'Fall', 2025),
(2, 1, 'Spring', 2024),
(3, 1, 'Summer', 2023),
(4, 1, 'Fall', 2024),
(5, 1, 'Spring', 2022),
(6, 0, 'Summer', 2021);

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE IF NOT EXISTS `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `major` varchar(100) DEFAULT NULL,
  `gpa` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`id`, `name`, `email`, `gender`, `age`, `major`, `gpa`) VALUES
(1, 'Riad Muftah', 'riadalelwani@gmail.com', 'Male', 30, 'Computer', 0),
(2, 'Rabe M B Abdelkareem', 'Rabe_Abdelkareem@Gmail.com', 'Male', 23, 'Computer', 0),
(3, 'Ali Salem', 'Ali_Salem@gmail.com', 'Male', 25, 'Electrical', 4),
(4, 'Eman Ahmed', 'Eman_Ahmed@gmail.com', 'Female', 33, 'Cilvil', 0),
(5, 'Mariam Ahmed', 'Mariam_Ahmed@gmail.com', 'Female', 30, 'Cilvil', 3.5),
(6, 'walid adam', 'walid_adam@gmail.com', 'Male', 35, 'computer', 3),
(7, 'manam Ali', 'manam_Ali@gmail.com', 'Male', 22, 'computer', 0),
(8, 'Mariam Ali', 'Mariam_Ali@gmail.com', 'Female', 28, 'Cilvil', 3.2),
(9, 'Ebrahim Riad', 'Ebrahim_Riad@gmail.com', 'Male', 22, 'computer', 4),
(10, 'mohammed salah', 'mohammed_salah@yahoo.com', 'Male', 25, 'computer', 4),
(11, 'Ahmed Ali', 'Ahmed_Ali@gmail.com', 'Male', 22, 'Computer', 0);

-- --------------------------------------------------------

--
-- Table structure for table `teacher`
--

CREATE TABLE IF NOT EXISTS `teacher` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `salary` double DEFAULT NULL,
  `department_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_teacher_department` (`department_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `teacher`
--

INSERT INTO `teacher` (`id`, `name`, `email`, `gender`, `age`, `salary`, `department_id`) VALUES
(1, 'Ali saleh Ali', 'Alisaleh@gmail.com', 'Male', 35, 3000, 1),
(2, 'Abdelrahman Ahmed', 'AbdelrahmanAhmed@gmail.com', 'Male', 40, 5000, 2),
(3, 'walid adam', 'walid_adam@gmail.com', 'Male', 55, 6000, 3),
(4, 'akram jalal', 'akram_jalal@gmail.com', 'Male', 35, 4500, 4),
(5, 'adam walid ', 'adam_walid@gmail.com', 'Male', 40, 3000, 5),
(6, 'Hassan salem', 'Hassan_salem@gmail.com', 'Male', 35, 4500, 6),
(7, 'Tahani Elmabrouk', 'Tahani_Elmabrouk@gmail.com', 'Female', 40, 7000, 1),
(8, 'Mohammed saad', 'Mohammed_saad@gmail.com', 'Male', 42, 5000, 1),
(9, 'Riad Muftah', 'Riad_Muftah@gmail.com', 'Male', 30, 3000, 1),
(10, 'saleh Ali', 'salehAli@gmail.com', 'Male', 33, 3000, 2);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `course`
--
ALTER TABLE `course`
  ADD CONSTRAINT `course_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`),
  ADD CONSTRAINT `fk_course_department` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`);

--
-- Constraints for table `enrollment`
--
ALTER TABLE `enrollment`
  ADD CONSTRAINT `enrollment_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `enrollment_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `teacher`
--
ALTER TABLE `teacher`
  ADD CONSTRAINT `fk_teacher_department` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
  ADD CONSTRAINT `teacher_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
