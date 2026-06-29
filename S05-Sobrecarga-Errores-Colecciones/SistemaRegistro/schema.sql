





IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'SistemaRegistroDB')
    CREATE DATABASE SistemaRegistroDB;
GO

USE SistemaRegistroDB;
GO


IF OBJECT_ID('DetalleVenta', 'U') IS NOT NULL DROP TABLE DetalleVenta;
IF OBJECT_ID('Venta',        'U') IS NOT NULL DROP TABLE Venta;
IF OBJECT_ID('Producto',     'U') IS NOT NULL DROP TABLE Producto;
IF OBJECT_ID('Cliente',      'U') IS NOT NULL DROP TABLE Cliente;
GO


CREATE TABLE Cliente (
    codigo  INT            NOT NULL PRIMARY KEY,
    nombre  NVARCHAR(150)  NOT NULL,
    email   NVARCHAR(150)      NULL,
    activo  BIT            NOT NULL DEFAULT 1
);
GO



CREATE TABLE Producto (
    codigo         INT            NOT NULL PRIMARY KEY,
    nombre         NVARCHAR(150)  NOT NULL,
    precio_base    FLOAT          NOT NULL,
    tipo           NVARCHAR(20)   NOT NULL,
    tiene_garantia BIT                NULL,
    es_perecible   BIT                NULL,
    talla          NVARCHAR(10)       NULL,
    en_temporada   BIT                NULL
);
GO


CREATE TABLE Venta (
    codigo         INT            NOT NULL PRIMARY KEY,
    codigo_cliente INT            NOT NULL,
    fecha          NVARCHAR(20)   NOT NULL,
    total          FLOAT          NOT NULL DEFAULT 0,
    CONSTRAINT FK_Venta_Cliente
        FOREIGN KEY (codigo_cliente) REFERENCES Cliente(codigo)
        ON DELETE CASCADE
);
GO


CREATE TABLE DetalleVenta (
    id              INT   IDENTITY(1,1) PRIMARY KEY,
    codigo_venta    INT   NOT NULL,
    codigo_producto INT   NOT NULL,
    cantidad        INT   NOT NULL,
    subtotal        FLOAT NOT NULL,
    CONSTRAINT FK_Detalle_Venta
        FOREIGN KEY (codigo_venta) REFERENCES Venta(codigo)
        ON DELETE CASCADE,
    CONSTRAINT FK_Detalle_Producto
        FOREIGN KEY (codigo_producto) REFERENCES Producto(codigo)
);
GO

PRINT 'Schema creado correctamente en SistemaRegistroDB.';
GO
