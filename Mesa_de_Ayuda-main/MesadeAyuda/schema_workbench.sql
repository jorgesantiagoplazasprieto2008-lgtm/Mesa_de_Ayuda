-- =====================================================================
-- SCRIPT DE BASE DE DATOS PARA MYSQL WORKBENCH - MESA DE AYUDA SENA CIMM
-- =====================================================================

CREATE DATABASE IF NOT EXISTS `mesadeayuda` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `mesadeayuda`;

-- 1. TABLA USUARIOS
DROP TABLE IF EXISTS `notificaciones`;
DROP TABLE IF EXISTS `comentarios`;
DROP TABLE IF EXISTS `tickets`;
DROP TABLE IF EXISTS `categorias`;
DROP TABLE IF EXISTS `usuarios`;

CREATE TABLE `usuarios` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `nombre` VARCHAR(100) NOT NULL,
    `correo` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    `rol` VARCHAR(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. TABLA CATEGORIAS
CREATE TABLE `categorias` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `nombre` VARCHAR(100) NOT NULL,
    `descripcion` TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. TABLA TICKETS
CREATE TABLE `tickets` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `titulo` VARCHAR(200) NOT NULL,
    `descripcion` TEXT,
    `estado` VARCHAR(30) NOT NULL DEFAULT 'NUEVO',
    `prioridad` VARCHAR(20) NOT NULL DEFAULT 'MEDIA',
    `fecha_creacion` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `fecha_limite_sla` DATETIME,
    `solicitante_id` BIGINT NOT NULL,
    `agente_id` BIGINT,
    `categoria_id` BIGINT,
    FOREIGN KEY (`solicitante_id`) REFERENCES `usuarios`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`agente_id`) REFERENCES `usuarios`(`id`) ON DELETE SET NULL,
    FOREIGN KEY (`categoria_id`) REFERENCES `categorias`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. TABLA COMENTARIOS (GLOBAL)
CREATE TABLE `comentarios` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `ticket_id` BIGINT NOT NULL,
    `usuario_id` BIGINT NOT NULL,
    `texto` TEXT NOT NULL,
    `fecha` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`ticket_id`) REFERENCES `tickets`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`usuario_id`) REFERENCES `usuarios`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. TABLA NOTIFICACIONES EN-APP
CREATE TABLE `notificaciones` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `usuario_id` BIGINT NOT NULL,
    `ticket_id` BIGINT NOT NULL,
    `mensaje` TEXT NOT NULL,
    `tipo_canal` VARCHAR(20) DEFAULT 'APP',
    `leido` TINYINT(1) DEFAULT 0,
    `fecha` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`usuario_id`) REFERENCES `usuarios`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`ticket_id`) REFERENCES `tickets`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================================
-- INSERCIÓN DE DATOS INICIALES (CREDANCIALES Y CATEGORÍAS)
-- =====================================================================

-- Insertar Usuarios por Defecto (Solicitantes, Agentes y Administrador)
INSERT INTO `usuarios` (`id`, `nombre`, `correo`, `password`, `rol`) VALUES
(1, 'Juan Pérez (Solicitante)', 'solicitante@cimm.edu', '12345', 'SOLICITANTE'),
(2, 'Carlos Rodríguez (Agente)', 'agente@cimm.edu', '12345', 'AGENTE'),
(3, 'Ana Martínez (Agente Red)', 'ana@cimm.edu', '12345', 'AGENTE'),
(4, 'Luis Gómez (Agente Software)', 'luis@cimm.edu', '12345', 'AGENTE'),
(5, 'Administrador SENA', 'admin@cimm.edu', '12345', 'ADMIN');

-- Insertar Categorías del Sistema
INSERT INTO `categorias` (`id`, `nombre`, `descripcion`) VALUES
(1, 'Hardware', 'Fallas en equipos físicos, monitores, teclados o impresoras'),
(2, 'Software', 'Inconvenientes con aplicaciones, sistemas operativos o programas'),
(3, 'Redes y Conectividad', 'Problemas de acceso a WiFi, red LAN o servidor de archivos'),
(4, 'Cuentas y Accesos', 'Reinicio de contraseñas, permisos de usuario o correos');

-- Insertar Tickets de Ejemplo Iniciales
INSERT INTO `tickets` (`id`, `titulo`, `descripcion`, `estado`, `prioridad`, `fecha_creacion`, `fecha_limite_sla`, `solicitante_id`, `agente_id`, `categoria_id`) VALUES
(1, 'Falla en teclado de la sala de sistemas 302', 'El teclado de la estación #5 presenta teclas bloqueadas y no permite escribir.', 'EN_PROCESO', 'ALTA', NOW(), DATE_ADD(NOW(), INTERVAL 4 HOUR), 1, 2, 1),
(2, 'Servidor de base de datos caído en ambiente local', 'Error de conexión general al servidor MySQL durante la práctica.', 'NUEVO', 'CRITICA', NOW(), DATE_ADD(NOW(), INTERVAL 2 HOUR), 1, 3, 3);

-- Insertar Comentario de Ejemplo
INSERT INTO `comentarios` (`ticket_id`, `usuario_id`, `texto`, `fecha`) VALUES
(1, 2, 'Iniciando diagnóstico técnico en el equipo #5.', NOW());

-- Confirmar transacción
COMMIT;
