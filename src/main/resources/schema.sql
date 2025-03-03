CREATE TABLE IF NOT EXISTS `franquicia` (
  `idFranquicia` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `active` tinyint NOT NULL DEFAULT '1',
  PRIMARY KEY (`idFranquicia`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `sucursal` (
  `idSucursal` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL DEFAULT 'Name_example',
  `active` tinyint NOT NULL DEFAULT (1),
  `idFranquicia` int NOT NULL,
  PRIMARY KEY (`idSucursal`),
  KEY `FK_sucursal_franquicia` (`idFranquicia`),
  CONSTRAINT `FK_sucursal_franquicia` FOREIGN KEY (`idFranquicia`) REFERENCES `franquicia` (`idFranquicia`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `producto` (
  `idProducto` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL DEFAULT 'nombre_example',
  `stock` int NOT NULL,
  `active` tinyint NOT NULL DEFAULT (1),
  `idSucursal` int NOT NULL,
  PRIMARY KEY (`idProducto`),
  KEY `FK_producto_sucursal` (`idSucursal`),
  CONSTRAINT `FK_producto_sucursal` FOREIGN KEY (`idSucursal`) REFERENCES `sucursal` (`idSucursal`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


