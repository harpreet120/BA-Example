-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema db_planinator
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema db_planinator
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `db_planinator` DEFAULT CHARACTER SET utf8 ;
USE `db_planinator` ;

-- -----------------------------------------------------
-- Table `db_planinator`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_planinator`.`User` ;

CREATE TABLE IF NOT EXISTS `db_planinator`.`User` (
  `name` VARCHAR(45) NULL,
  `lastname` VARCHAR(45) NULL,
  `username` VARCHAR(45) NULL,
  `userRole` ENUM('ADMINISTRATOR', 'MANAGEMENT', 'OWNER') NULL,
  `password` VARCHAR(45) NULL,
  `employeeNumber` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`employeeNumber`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_planinator`.`Budget`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_planinator`.`Budget` ;

CREATE TABLE IF NOT EXISTS `db_planinator`.`Budget` (
  `budgetId` INT NOT NULL AUTO_INCREMENT,
  `expirationDate` DATE NULL,
  `plannedAmount` FLOAT NULL,
  `budgetDescription` VARCHAR(45) NULL,
  `employeeNumber` INT NOT NULL,
  `archivated` TINYINT(1) NULL,
  PRIMARY KEY (`budgetId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_planinator`.`History`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_planinator`.`History` ;

CREATE TABLE IF NOT EXISTS `db_planinator`.`History` (
  `costId` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NULL,
  `value` FLOAT NULL,
  `date` DATE NULL,
  `budgetId` INT NOT NULL,
  PRIMARY KEY (`costId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_planinator`.`Forecast`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_planinator`.`Forecast` ;

CREATE TABLE IF NOT EXISTS `db_planinator`.`Forecast` (
  `foreCastId` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NULL,
  `value` FLOAT NULL,
  `date` DATE NULL,
  `budgetId` INT NOT NULL,
  PRIMARY KEY (`forecastId`))
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
