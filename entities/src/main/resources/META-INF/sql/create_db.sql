-- MySQL Script generated by MySQL Workbench
-- Thu Jan  3 14:38:51 2019
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema MOVIEVERSE
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema MOVIEVERSE
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `MOVIEVERSE` ;
USE `MOVIEVERSE` ;

-- -----------------------------------------------------
-- Table `MOVIEVERSE`.`CATEGORY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `MOVIEVERSE`.`CATEGORY` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(45) NOT NULL,
  `TAGS` VARCHAR(80) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `SQL_CATEGORY_ID_INDEX` (`ID` ASC));


-- -----------------------------------------------------
-- Table `MOVIEVERSE`.`PERSON`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `MOVIEVERSE`.`PERSON` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `FIRSTNAME` VARCHAR(50) NOT NULL,
  `LASTNAME` VARCHAR(100) NOT NULL,
  `EMAIL` VARCHAR(45) NOT NULL,
  `ADDRESS` VARCHAR(45) NOT NULL,
  `CITY` VARCHAR(45) NOT NULL,
  `PASSWORD` VARCHAR(100) NULL DEFAULT NULL,
  `DTYPE` VARCHAR(31) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX (`EMAIL` ASC),
  UNIQUE INDEX `SQL_PERSON_EMAIL_INDEX` (`EMAIL` ASC),
  UNIQUE INDEX `SQL_PERSON_ID_INDEX` (`ID` ASC));


-- -----------------------------------------------------
-- Table `MOVIEVERSE`.`GROUPS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `MOVIEVERSE`.`GROUPS` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(50) NOT NULL,
  `DESCRIPTION` VARCHAR(300) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`));


-- -----------------------------------------------------
-- Table `MOVIEVERSE`.`PERSON_GROUPS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `MOVIEVERSE`.`PERSON_GROUPS` (
  `GROUPS_ID` INT NOT NULL,
  `EMAIL` VARCHAR(45) NOT NULL,
  INDEX `SQL_PERSONGROUPS_EMAIL_INDEX` (`EMAIL` ASC),
  INDEX `SQL_PERSONGROUPS_ID_INDEX` (`GROUPS_ID` ASC),
  CONSTRAINT `FK_PERSON_GROUPS_PERSON`
    FOREIGN KEY (`EMAIL`)
    REFERENCES `MOVIEVERSE`.`PERSON` (`EMAIL`),
  CONSTRAINT `FK_PERSON_GROUPS_GROUPS`
    FOREIGN KEY (`GROUPS_ID`)
    REFERENCES `MOVIEVERSE`.`GROUPS` (`ID`));


-- -----------------------------------------------------
-- Table `MOVIEVERSE`.`ORDER_STATUS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `MOVIEVERSE`.`ORDER_STATUS` (
  `ID` INT NOT NULL,
  `STATUS` VARCHAR(45) NOT NULL,
  `DESCRIPTION` VARCHAR(200),
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `SQL_ORDERSTATUS_ID_INDEX` (`ID` ASC));


-- -----------------------------------------------------
-- Table `MOVIEVERSE`.`CUSTOMER_ORDER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `MOVIEVERSE`.`CUSTOMER_ORDER` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `AMOUNT` FLOAT(52) NOT NULL,
  `DATE_CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CUSTOMER_ID` INT NOT NULL,
  `STATUS_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `SQL_ORDER_STATUS_ID_INDEX` (`STATUS_ID` ASC),
  INDEX `SQL_ORDER_CUSTOMER_ID_INDEX` (`CUSTOMER_ID` ASC),
  UNIQUE INDEX `SQL_ORDER_ID_INDEX` (`ID` ASC),
  CONSTRAINT `FK_CUSTOMER_ORDER_ORDER_STATUS1`
    FOREIGN KEY (`STATUS_ID`)
    REFERENCES `MOVIEVERSE`.`ORDER_STATUS` (`ID`),
  CONSTRAINT `FK_CUSTOMER_ORDER_CUSTOMER1`
    FOREIGN KEY (`CUSTOMER_ID`)
    REFERENCES `MOVIEVERSE`.`PERSON` (`ID`));


-- -----------------------------------------------------
-- Table `MOVIEVERSE`.`PRODUCT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `MOVIEVERSE`.`PRODUCT` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(45) NOT NULL,
  `PRICE` DECIMAL(10,2) NOT NULL,
  `DESCRIPTION` VARCHAR(2000) NOT NULL,
  `IMG` VARCHAR(45) NULL DEFAULT NULL,
  `CATEGORY_ID` INT NOT NULL,
  `IMG_SRC` BLOB(1073741823) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `SQL_PRODUCT_ID_INDEX` (`ID` ASC),
  CONSTRAINT `FK_PRODUCT_CATEGORY`
    FOREIGN KEY (`CATEGORY_ID`)
    REFERENCES `MOVIEVERSE`.`CATEGORY` (`ID`));


-- -----------------------------------------------------
-- Table `MOVIEVERSE`.`ORDER_DETAIL`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `MOVIEVERSE`.`ORDER_DETAIL` (
  `ORDER_ID` INT NOT NULL,
  `PRODUCT_ID` INT NOT NULL,
  `QTY` INT NOT NULL,
  PRIMARY KEY (`ORDER_ID`, `PRODUCT_ID`),
  UNIQUE INDEX `SQL_ORDER_DETAIL_INDEX` (`ORDER_ID` ASC, `PRODUCT_ID` ASC),
  INDEX `SQL_ORDER_PRODUCT_ID_INDEX` (`PRODUCT_ID` ASC),
  INDEX `SQL_ORDER_DETAIL_ID_INDEX` (`ORDER_ID` ASC),
  CONSTRAINT `FK_ORDER_DETAIL_PRODUCT`
    FOREIGN KEY (`PRODUCT_ID`)
    REFERENCES `MOVIEVERSE`.`PRODUCT` (`ID`),
  CONSTRAINT `FK_ORDER_DETAIL_ORDER`
    FOREIGN KEY (`ORDER_ID`)
    REFERENCES `MOVIEVERSE`.`CUSTOMER_ORDER` (`ID`));


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;