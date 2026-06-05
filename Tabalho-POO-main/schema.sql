-- Script para criar o banco de dados e tabela de pacientes

-- Criar banco de dados
CREATE DATABASE IF NOT EXISTS clinica;
USE clinica;

-- Criar tabela de pacientes
CREATE TABLE IF NOT EXISTS paciente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    telefone VARCHAR(11),
    email VARCHAR(100),
    endereco VARCHAR(255),
    data_nascimento DATE,
    peso DECIMAL(5, 2),
    altura DECIMAL(3, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_nome (nome),
    INDEX idx_cpf (cpf)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de exames médicos
CREATE TABLE IF NOT EXISTS exame (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo             VARCHAR(100)  NOT NULL,           -- ex: Sangue, Raio-X, Ultrassom
    data_realizacao  DATE          NOT NULL,
    resultado        VARCHAR(255)  NOT NULL,
    observacao       VARCHAR(255),
    nome_paciente    VARCHAR(100)  NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tipo          (tipo),
    INDEX idx_nome_paciente (nome_paciente)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de consultas médicas
CREATE TABLE IF NOT EXISTS consulta (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_consulta   DATE          NOT NULL,
    nome_paciente   VARCHAR(100)  NOT NULL,
    nome_medico     VARCHAR(100)  NOT NULL,
    diagnostico     VARCHAR(255),
    status          VARCHAR(50)   NOT NULL,            -- ex: Agendada, Realizada, Cancelada
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_nome_paciente (nome_paciente),
    INDEX idx_status        (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;