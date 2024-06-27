-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 06 Sty 2019, 15:04
-- Wersja serwera: 10.1.37-MariaDB
-- Wersja PHP: 7.3.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Baza danych: `library`
--

--
-- Zrzut danych tabeli `book`
--

INSERT INTO `book` (`id`, `publication_year`, `author`, `isbn`, `publisher`, `title`, `category_id`) VALUES
(1,  '2011','Jo Nesbo', '978-83-271-5653-2', 'Dolnoślaske', 'Człowiek Nietoperz', 1),
(2,  '2013','Jo Nesbo', '978-83-245-9025-4', 'Dolnośląskie', 'Karaluchy', 1),
(3,  '2014','Jo Nesbo', '978-83-245-8991-3', 'Dolnoślaskie', 'Czerwone gardło', 1),
(4, '2015','Jo Nesbo', '978-83-245-8993-7', 'Dolnoślaskie', 'Trzeci klucz', 1),
(5, '2016','Jo Nesbo', '978-83-245-8994-4', 'Dolnośląskie', 'Pentagram', 1),
(6, '2015','Jo Nesbo', '978-83-245-8992-0', 'Dolnośląskie', 'Wybawiciel', 1),
(7, '2010','Jo Nesbo', '978-83-271-5731-7', 'Dolnośląskie', 'Pierwszy śnieg', 1),
(8, '2009','Jo Nesbo', '978-83-245-8995-1', 'Dolnoślaskie', 'Pancerne serce', 1),
(9, '2010','Stephen King', '978-83-7885-963-9', 'Albatros', 'Pan mercedes', 1),
(10, '2012','Stephen King', '978-83-7885-599-6', 'Albatros', 'Znalezione nie kradzione', 1),
(11, '2014','Stephen King', '978-83-7985-899-6', 'Albatros', 'Koniec warty', 1),
(12, '2016','Stephen King', '978-83-8123-266-1', 'Albatros', 'Outsider', 1),
(13, '2005','Lewis C.S', '978-83-7278-180-2', 'Media rodzina', 'Opowieści z narni - lew, czarownica i stara szafa', 2),
(14, '2006','Lewis C.S', '978-837278-181-9', 'Media rodzina', 'Opowieści z narni - Ksiąze kaspian', 2),
(15, '2008','Lewis C.S', '83-7278-182-6', 'Media rodzina', 'Opowieści z narni - podróż wędrowca do świtu', 2),
(16, '2010','Lewis C.S', '83-7278-183-4', 'Media rodzina', 'Opowieści z narni - srebrne krzesło', 2),
(17, '2012','Lewis C.S', '83-7278-184-2', 'Media rodzina', 'Opowieści z narni - Koń i jego chłopiec', 2),
(18, '2013','Lewis C.S', '83-7278-185-0', 'Media rodzina', 'Opowieści z narni - Siostrzeniec czarodzieja', 2),
(19, '2015','Lewis C.S', '83-7278-186-9', 'Media rodzina', 'Opowieść z narni - Ostatnia bitwa', 2),
(20, '2019','Martel Yann', '978-83-7659-928-1', 'Albatros', 'Życie Pi', 2),
(21, '1999','Turing Alan', '978-83-7885-845-4', 'Albatros', 'Enigma', 3),
(22, '2004','Strelau Jan', '83-87-957-04-6', 'GWP', 'Psychologia - podstawy psychologii', 4),
(23, '2011','Strelau Jan', '83-87957-06-2', 'GWP', 'Psychologia - jednoskta w społeczeństwie i elementy psychologi stosowanej', 4),
(24, '2015','Zimbardo Philip', '978-83-01-1955-3-3', 'PWN', 'Psychologia - kluczowe koncepcje, podstawy psychologii', 4),
(25, '2016','Zimbardo Philip', '978-83-0119554-0', 'PWN', 'Psychologia - kluczowe koncepcje, motywacja i uczenie się', 4),
(26, '2008','Zimbardo Philip', '978-83-01-19556-4', 'PWN', 'Psychologia - kluczowe koncepcje,psychologia osobowości', 4),
(27, '2009','Elsberg Marc', '978-83-280-1472-5', 'Wydawnictwo B', 'Blackout', 5),
(28, '2004','Elsberg Marc', '978-83-280-2165-5', 'Wydawnictwo B', 'Zero', 5),
(29, '2005','Fallony James', '978-83-7489-682-5', 'GWP', 'Mózg psychopaty', 4),
(30, '2008','Martin George R.R', '978-83-7506-830-6', 'Zysk i spółka', 'Gra o tron', 5);

--
-- Zrzut danych tabeli `category`
--

INSERT INTO `category` (`id`, `days_of_rent`, `name`) VALUES
(1, 30, 'Kryminał'),
(2, 30, 'Przygodowa'),
(3, 30, 'Biografia'),
(4, 30, 'Naukowe'),
(5, 30, 'Sci-fi');

--
-- Zrzut danych tabeli `hibernate_sequences`
--

--
-- Zrzut danych tabeli `role`
--

INSERT INTO `role` (`id`, `description`, `name`) VALUES
(1, 'Pracownik', 'ROLE_EMPLOYEE'),
(2, 'Użytkownik', 'ROLE_USER'),
(3, 'Menadżer', 'ROLE_MANAGER');

--
-- Zrzut danych tabeli `user`
--

INSERT INTO `user` (`id`, `address_line1`, `day_of_birth`, `email`, `last_name`, `name`, `password`, `phone`, `postal_code`, `town`, `role_id`) VALUES
(1, 'Biblioteczna', '1996-01-19', 'admin@biblioteka.com', 'admin', 'admin', '$2a$10$iKI3DPoazZkjq7l5YPokjenhoAsvX9PlcBK2iN8zD5y9VTIZaHVb.', '512155211', '41-800', 'Zabrze', 1),
(2, 'Cisowa 8B/8', '1998-12-20', 'rudziak555@gmail.com', 'Podstada', 'Ewa', '$2a$10$UQe56epSWBCDmeAWNcxjieO0KuIVmq2Z3PWyfc0NMoUmVHxGSi.sq', '', '41-800', 'Zabrze', 2),
(3, 'Jagodowa', '1983-02-18', 'jan@biblioteka.com', 'Kowalski', 'Jan', '$2a$10$Z3U.a50g.wA1IYbKo7m5GOXOVsDavXd7VyF0q8rJvDh5ff32h1.fS', '721507857', '44-100', 'Gliwice', 1),
(4, 'Porzeczkowa 2/5', '1977-04-12', 'andrzej@biblioteka.com', 'Cuber', 'Andrzej', '$2a$10$1Gvy5KDklSTjd18LnKUR/utvnRqwF46TysvWtXDQJOPyxu9aD8RZW', '721573821', '41-800', 'Zabrze', 1),
(5, 'Malinowa 8/5', '1995-06-20', 'paulina172@onet.pl', 'Stefańczyk', 'Paulina', '$2a$10$OGNZ7e8ruX7qVXtJHmLkseLqkWdlOK9YU9qGW3vQBZGnoYZ.ndjJq', '', '41-800', 'Zabrze', 2),
(6, 'Wolnośc 323/7', '1973-08-15', 'stefan73@gmail.com', 'Zatylny', 'Stefan', '$2a$10$c/hvu6hRtvC9UZJ.5QjIvuiiRj/119hUs1Dqopx/chKWqehORvoKW', '', '41-800', 'Zabrze', 2),
(7, 'Brzoskwiniowa 14/3', '1999-09-18', 'artur55555@o2.pl', 'Kopek', 'Artur', '$2a$10$/Pv59S7BKcpIWsZ8df0eYOL2bJVggZ1NuwUoydkIvx6BCyS9O3M2e', '', '41-800', 'Zabrze', 2),
(8, 'Arbuzowa 15/9', '1949-01-19', 'porzeczka721@wp.pl', 'Czerwony', 'Celina', '$2a$10$nybX7pIMvWE1Muong.92WusNTnnrc8Y3GDWWx844sHeDh/Fok8F7S', '', '41-800', 'Zabrze', 2),
(9, 'Biblioteczna', '1996-01-19', 'manager@biblioteka.com', 'manager', 'manager', '$2a$10$iKI3DPoazZkjq7l5YPokjenhoAsvX9PlcBK2iN8zD5y9VTIZaHVb.', '512155211', '41-800', 'Zabrze', 3);

INSERT INTO `department` (`department_id`, `name`,`address_line1`, `town`, `postal_code`) VALUES
(1,'Filia 1','Wolności 405','Zabrze','41-806'),
(2,'Filia 2','Plac teatralny 1','Zabrze','41-806');

UPDATE user
SET department_department_id = 1
WHERE id = 1;

UPDATE user
SET department_department_id = 2
WHERE id = 3;

UPDATE user
SET department_department_id = 1
WHERE id = 4;

INSERT INTO `book_entry` (`book_entry_id`, `inventory_number`,`signature`, `physical_state`, `id`,`department_department_id`) VALUES
(1,'KC/01/2019','F1 SKAND./S','Nowa',1,1),
(2,'KC/02/2019','F1 SKAND./S','Bardzo dobry',1,2),
(3,'KC/03/2019','F1 SKAND./S','Nowa',1,1),
(4,'KC/04/2019','F1 SKAND./S','Dobry',1,2),
(5,'KC/05/2019','F1 SKAND./S','Nowa',1,1),
(6,'KC/06/2019','F1 SKAND./S','Nowa',1,2);

COMMIT;
