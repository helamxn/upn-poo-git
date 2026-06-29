




USE SistemaRegistroDB;
GO


INSERT INTO Cliente (codigo, nombre, email, activo) VALUES
(101, 'Ana Torres',    'ana@correo.com',   1),
(102, 'Luis Rojas',    NULL,               1),
(103, 'Maria Lopez',   'maria@gmail.com',  1),
(104, 'Carlos Diaz',   'carlos@correo.com',1),
(105, 'Rosa Mendoza',  NULL,               1);
GO


INSERT INTO Producto (codigo, nombre, precio_base, tipo, tiene_garantia, es_perecible, talla, en_temporada) VALUES
(201, 'Audifonos Bluetooth', 80.00,  'Electronico', 1,    NULL, NULL, NULL),
(202, 'Laptop HP',           2500.00,'Electronico', 1,    NULL, NULL, NULL),
(203, 'Galletas Oreo',         2.50, 'Alimento',   NULL,  1,   NULL, NULL),
(204, 'Leche Gloria',          3.80, 'Alimento',   NULL,  1,   NULL, NULL),
(205, 'Polo Deportivo',       25.00, 'Ropa',        NULL, NULL, 'M',  1),
(206, 'Casaca Invierno',      89.00, 'Ropa',        NULL, NULL, 'L',  0);
GO

PRINT 'Datos de prueba insertados correctamente.';
GO
