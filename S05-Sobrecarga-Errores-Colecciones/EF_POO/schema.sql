IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'LibreriaBazarDB')
    CREATE DATABASE LibreriaBazarDB;
GO

USE LibreriaBazarDB;
GO

IF OBJECT_ID('DetalleVenta', 'U') IS NOT NULL DROP TABLE DetalleVenta;
IF OBJECT_ID('Venta',        'U') IS NOT NULL DROP TABLE Venta;
IF OBJECT_ID('Producto',     'U') IS NOT NULL DROP TABLE Producto;
IF OBJECT_ID('Cliente',      'U') IS NOT NULL DROP TABLE Cliente;
GO

CREATE TABLE Cliente (
    codigo  INT            NOT NULL PRIMARY KEY,
    nombre  NVARCHAR(150)  NOT NULL,
    email   NVARCHAR(150)      NULL
);
GO

-- tipo: 'UtilEscolar' | 'Papeleria' | 'Bazar'
-- en_campania_escolar: solo aplica a UtilEscolar
-- venta_por_mayor:     solo aplica a Papeleria
-- categoria / temporada_alta: solo aplican a Bazar
CREATE TABLE Producto (
    codigo               INT            NOT NULL PRIMARY KEY,
    nombre               NVARCHAR(150)  NOT NULL,
    precio_base          DECIMAL(10,2)  NOT NULL,
    tipo                 NVARCHAR(20)   NOT NULL,
    en_campania_escolar  BIT                NULL,
    venta_por_mayor      BIT                NULL,
    categoria            NVARCHAR(30)       NULL,
    temporada_alta       BIT                NULL,
    CONSTRAINT CK_Producto_Precio CHECK (precio_base > 0)
);
GO

CREATE TABLE Venta (
    codigo         INT             NOT NULL PRIMARY KEY,
    codigo_cliente INT             NOT NULL,
    fecha          NVARCHAR(20)    NOT NULL,
    total          DECIMAL(10,2)   NOT NULL DEFAULT 0,
    CONSTRAINT FK_Venta_Cliente
        FOREIGN KEY (codigo_cliente) REFERENCES Cliente(codigo)
        ON DELETE CASCADE
);
GO

CREATE TABLE DetalleVenta (
    id              INT             IDENTITY(1,1) PRIMARY KEY,
    codigo_venta    INT             NOT NULL,
    codigo_producto INT             NOT NULL,
    cantidad        INT             NOT NULL,
    subtotal        DECIMAL(10,2)   NOT NULL,
    CONSTRAINT FK_Detalle_Venta
        FOREIGN KEY (codigo_venta) REFERENCES Venta(codigo)
        ON DELETE CASCADE,
    CONSTRAINT FK_Detalle_Producto
        FOREIGN KEY (codigo_producto) REFERENCES Producto(codigo),
    CONSTRAINT CK_Detalle_Cantidad CHECK (cantidad > 0)
);
GO

-- ==========================================================
-- Datos de prueba
-- ==========================================================

INSERT INTO Cliente (codigo, nombre, email) VALUES
(101, 'Ana Torres',   'ana@correo.com'),
(102, 'Luis Rojas',   NULL),
(103, 'Maria Lopez',  'maria@gmail.com'),
(104, 'Carlos Diaz',  'carlos@correo.com'),
(105, 'Rosa Mendoza', NULL);
GO

-- Precio final = precio_base * regla del subtipo:
--  201 Cuaderno:      8.50 * 0.90 (campania escolar)      = 7.65
--  202 Colores:      12.00 * 0.90 (campania escolar)      = 10.80
--  203 Papel Bond:   25.00 * 0.88 (venta por mayor)       = 22.00
--  204 Folder:       15.00 * 1.00 (venta al detalle)      = 15.00
--  205 Mochila:      65.00 * 1.00 (temporada alta)        = 65.00
--  206 Peluche:      22.00 * 0.80 (fuera de temporada alta) = 17.60
INSERT INTO Producto (codigo, nombre, precio_base, tipo, en_campania_escolar, venta_por_mayor, categoria, temporada_alta) VALUES
(201, 'Cuaderno Universitario 100 hojas', 8.50,  'UtilEscolar', 1,    NULL, NULL,        NULL),
(202, 'Set de Colores x12',              12.00,  'UtilEscolar', 1,    NULL, NULL,        NULL),
(203, 'Papel Bond A4 (millar)',          25.00,  'Papeleria',  NULL,  1,   NULL,        NULL),
(204, 'Folder Manila x50',               15.00,  'Papeleria',  NULL,  0,   NULL,        NULL),
(205, 'Mochila Escolar Reforzada',       65.00,  'Bazar',      NULL,  NULL,'Mochilas',   1),
(206, 'Peluche de Regalo',               22.00,  'Bazar',      NULL,  NULL,'Regalos',    0);
GO

INSERT INTO Venta (codigo, codigo_cliente, fecha, total) VALUES
(1, 101, '2026-06-01', 44.95),
(2, 102, '2026-06-03', 86.60),
(3, 103, '2026-06-05', 80.20),
(4, 101, '2026-06-10', 17.60);
GO

-- codigo_venta / codigo_producto / cantidad / subtotal (precio_final * cantidad)
INSERT INTO DetalleVenta (codigo_venta, codigo_producto, cantidad, subtotal) VALUES
(1, 201, 3, 22.95),  -- 7.65  * 3
(1, 203, 1, 22.00),  -- 22.00 * 1
(2, 205, 1, 65.00),  -- 65.00 * 1
(2, 202, 2, 21.60),  -- 10.80 * 2
(3, 206, 2, 35.20),  -- 17.60 * 2
(3, 204, 3, 45.00),  -- 15.00 * 3
(4, 206, 1, 17.60);  -- 17.60 * 1
GO

PRINT 'Schema y datos de prueba creados correctamente en LibreriaBazarDB.';
GO
