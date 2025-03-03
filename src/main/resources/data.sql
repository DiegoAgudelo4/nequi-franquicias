-- Desactivar temporalmente el autoincremento
SET SESSION sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

-- Insertar franquicias con ID manual
INSERT IGNORE INTO `franquicia` (`idFranquicia`, `nombre`) VALUES
(1, 'Franquicia A'),
(2, 'Franquicia B'),
(3, 'Franquicia C');

-- Insertar sucursales con ID manual
INSERT IGNORE INTO `sucursal` (`idSucursal`, `nombre`, `idFranquicia`) VALUES
(1, 'Sucursal 1A', 1),
(2, 'Sucursal 2A', 1),
(3, 'Sucursal 1B', 2),
(4, 'Sucursal 2B', 2),
(5, 'Sucursal 1C', 3);

-- Insertar productos con ID manual
INSERT IGNORE INTO `producto` (`idProducto`, `nombre`, `stock`, `idSucursal`) VALUES
(1, 'Producto X', 100, 1),
(2, 'Producto Y', 50, 1),
(3, 'Producto Z', 200, 2),
(4, 'Producto W', 30, 3),
(5, 'Producto V', 80, 4),
(6, 'Producto U', 120, 5);

-- Reactivar el autoincremento
SET SESSION sql_mode = '';
