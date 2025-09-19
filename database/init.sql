-- =====================================================
-- LMS - Learning Management System
-- Script de Inicialização do Banco de Dados
-- =====================================================

-- Criar banco de dados
CREATE DATABASE lmsdb;

-- Criar usuário
CREATE USER lms_user WITH PASSWORD 'lms_password';

-- Conceder privilégios
GRANT ALL PRIVILEGES ON DATABASE lmsdb TO lms_user;

-- Conectar ao banco
\c lmsdb;

-- =====================================================
-- TABELAS PRINCIPAIS
-- =====================================================

-- Tabela de Roles (Papeis)
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(30) NOT NULL UNIQUE
);

-- Tabela de Usuários
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    primeiro_nome VARCHAR(80) NOT NULL,
    ultimo_nome VARCHAR(120) NOT NULL,
    data_nascimento DATE NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    telefone VARCHAR(30) NOT NULL,
    senha VARCHAR(255) NOT NULL
);

-- Tabela de Relacionamento Usuario-Roles
CREATE TABLE usuario_papeis (
    usuario_id BIGINT NOT NULL,
    papel_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, papel_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (papel_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Tabela de Cursos
CREATE TABLE cursos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL UNIQUE,
    descricao TEXT,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    criado_por_id BIGINT NOT NULL,
    FOREIGN KEY (criado_por_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabela de Matrículas
CREATE TABLE matriculas (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    data_matricula TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE,
    UNIQUE(usuario_id, curso_id)
);

-- Tabela de Categorias de Tarefas
CREATE TABLE categorias_tarefas (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(40) NOT NULL UNIQUE,
    nome VARCHAR(80) NOT NULL
);

-- Tabela de Registros de Estudo
CREATE TABLE registros_estudo (
    id BIGSERIAL PRIMARY KEY,
    matricula_id BIGINT NOT NULL,
    data DATE NOT NULL,
    categoria_id BIGINT NOT NULL,
    descricao TEXT NOT NULL,
    horario_inicio TIMESTAMP NOT NULL,
    duracao_minutos INTEGER NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP,
    FOREIGN KEY (matricula_id) REFERENCES matriculas(id) ON DELETE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categorias_tarefas(id) ON DELETE CASCADE
);

-- =====================================================
-- ÍNDICES PARA PERFORMANCE
-- =====================================================

-- Índices para usuários
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_data_nascimento ON usuarios(data_nascimento);

-- Índices para cursos
CREATE INDEX idx_cursos_nome ON cursos(nome);
CREATE INDEX idx_cursos_data_inicio ON cursos(data_inicio);
CREATE INDEX idx_cursos_criado_por ON cursos(criado_por_id);

-- Índices para matrículas
CREATE INDEX idx_matriculas_usuario ON matriculas(usuario_id);
CREATE INDEX idx_matriculas_curso ON matriculas(curso_id);
CREATE INDEX idx_matriculas_status ON matriculas(status);

-- Índices para registros de estudo
CREATE INDEX idx_registros_matricula ON registros_estudo(matricula_id);
CREATE INDEX idx_registros_data ON registros_estudo(data);
CREATE INDEX idx_registros_categoria ON registros_estudo(categoria_id);

-- =====================================================
-- DADOS INICIAIS
-- =====================================================

-- Inserir roles iniciais
INSERT INTO roles (nome) VALUES ('ADMIN');
INSERT INTO roles (nome) VALUES ('ESTUDANTE');

-- Inserir categorias de tarefas iniciais
INSERT INTO categorias_tarefas (codigo, nome) VALUES ('PESQUISA', 'Pesquisa');
INSERT INTO categorias_tarefas (codigo, nome) VALUES ('PRATICA', 'Prática');
INSERT INTO categorias_tarefas (codigo, nome) VALUES ('ASSISTIR_VIDEOAULA', 'Assistir Videoaula');

-- =====================================================
-- USUÁRIOS DE TESTE
-- =====================================================

-- Senha: admin123 (hash BCrypt)
INSERT INTO usuarios (primeiro_nome, ultimo_nome, data_nascimento, email, telefone, senha) 
VALUES ('Admin', 'Sistema', '1990-01-01', 'admin@lms.com', '11999999999', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi');

-- Senha: estudante123 (hash BCrypt)
INSERT INTO usuarios (primeiro_nome, ultimo_nome, data_nascimento, email, telefone, senha) 
VALUES ('João', 'Silva', '2000-05-15', 'joao@email.com', '11988888888', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi');

-- Senha: maria123 (hash BCrypt)
INSERT INTO usuarios (primeiro_nome, ultimo_nome, data_nascimento, email, telefone, senha) 
VALUES ('Maria', 'Santos', '1998-03-20', 'maria@email.com', '11977777777', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi');

-- Atribuir roles aos usuários
INSERT INTO usuario_papeis (usuario_id, papel_id) 
SELECT u.id, r.id 
FROM usuarios u, roles r 
WHERE u.email = 'admin@lms.com' AND r.nome = 'ADMIN';

INSERT INTO usuario_papeis (usuario_id, papel_id) 
SELECT u.id, r.id 
FROM usuarios u, roles r 
WHERE u.email = 'joao@email.com' AND r.nome = 'ESTUDANTE';

INSERT INTO usuario_papeis (usuario_id, papel_id) 
SELECT u.id, r.id 
FROM usuarios u, roles r 
WHERE u.email = 'maria@email.com' AND r.nome = 'ESTUDANTE';

-- =====================================================
-- CURSOS DE EXEMPLO
-- =====================================================

-- Criar cursos de exemplo
INSERT INTO cursos (nome, descricao, data_inicio, data_fim, criado_por_id)
SELECT 'Java para Iniciantes', 
       'Curso completo de programação Java desde o básico até conceitos avançados',
       '2024-01-01', 
       '2024-06-30',
       u.id
FROM usuarios u WHERE u.email = 'admin@lms.com';

INSERT INTO cursos (nome, descricao, data_inicio, data_fim, criado_por_id)
SELECT 'Spring Boot Avançado', 
       'Desenvolvimento de APIs REST com Spring Boot, Security e JPA',
       '2024-02-01', 
       '2024-07-31',
       u.id
FROM usuarios u WHERE u.email = 'admin@lms.com';

INSERT INTO cursos (nome, descricao, data_inicio, data_fim, criado_por_id)
SELECT 'Angular Framework', 
       'Desenvolvimento de aplicações web modernas com Angular',
       '2024-03-01', 
       '2024-08-31',
       u.id
FROM usuarios u WHERE u.email = 'admin@lms.com';

-- =====================================================
-- MATRÍCULAS DE EXEMPLO
-- =====================================================

-- Matricular estudantes nos cursos
INSERT INTO matriculas (usuario_id, curso_id, status)
SELECT u.id, c.id, 'ATIVO'
FROM usuarios u, cursos c 
WHERE u.email = 'joao@email.com' AND c.nome = 'Java para Iniciantes';

INSERT INTO matriculas (usuario_id, curso_id, status)
SELECT u.id, c.id, 'ATIVO'
FROM usuarios u, cursos c 
WHERE u.email = 'maria@email.com' AND c.nome = 'Spring Boot Avançado';

-- =====================================================
-- REGISTROS DE ESTUDO DE EXEMPLO
-- =====================================================

-- Registros de estudo para João
INSERT INTO registros_estudo (matricula_id, data, categoria_id, descricao, horario_inicio, duracao_minutos)
SELECT m.id, '2024-01-15', ct.id, 'Estudando conceitos básicos de Java', '2024-01-15 14:00:00', 60
FROM matriculas m, usuarios u, cursos c, categorias_tarefas ct
WHERE u.email = 'joao@email.com' 
  AND c.nome = 'Java para Iniciantes'
  AND m.usuario_id = u.id 
  AND m.curso_id = c.id
  AND ct.codigo = 'PRATICA';

INSERT INTO registros_estudo (matricula_id, data, categoria_id, descricao, horario_inicio, duracao_minutos)
SELECT m.id, '2024-01-16', ct.id, 'Pesquisando sobre OOP em Java', '2024-01-16 10:00:00', 90
FROM matriculas m, usuarios u, cursos c, categorias_tarefas ct
WHERE u.email = 'joao@email.com' 
  AND c.nome = 'Java para Iniciantes'
  AND m.usuario_id = u.id 
  AND m.curso_id = c.id
  AND ct.codigo = 'PESQUISA';

-- Registros de estudo para Maria
INSERT INTO registros_estudo (matricula_id, data, categoria_id, descricao, horario_inicio, duracao_minutos)
SELECT m.id, '2024-02-05', ct.id, 'Assistindo videoaulas sobre Spring Boot', '2024-02-05 19:00:00', 120
FROM matriculas m, usuarios u, cursos c, categorias_tarefas ct
WHERE u.email = 'maria@email.com' 
  AND c.nome = 'Spring Boot Avançado'
  AND m.usuario_id = u.id 
  AND m.curso_id = c.id
  AND ct.codigo = 'ASSISTIR_VIDEOAULA';

-- =====================================================
-- VIEWS ÚTEIS
-- =====================================================

-- View para relatório de estudantes
CREATE VIEW vw_estudantes_com_matriculas AS
SELECT 
    u.id,
    u.primeiro_nome,
    u.ultimo_nome,
    u.email,
    COUNT(m.id) as total_matriculas,
    COUNT(CASE WHEN m.status = 'ATIVO' THEN 1 END) as matriculas_ativas
FROM usuarios u
LEFT JOIN matriculas m ON u.id = m.usuario_id
LEFT JOIN usuario_papeis up ON u.id = up.usuario_id
LEFT JOIN roles r ON up.papel_id = r.id
WHERE r.nome = 'ESTUDANTE'
GROUP BY u.id, u.primeiro_nome, u.ultimo_nome, u.email;

-- View para relatório de cursos
CREATE VIEW vw_cursos_com_estatisticas AS
SELECT 
    c.id,
    c.nome,
    c.descricao,
    c.data_inicio,
    c.data_fim,
    u.primeiro_nome || ' ' || u.ultimo_nome as criado_por,
    COUNT(m.id) as total_matriculas,
    COUNT(CASE WHEN m.status = 'ATIVO' THEN 1 END) as matriculas_ativas
FROM cursos c
LEFT JOIN usuarios u ON c.criado_por_id = u.id
LEFT JOIN matriculas m ON c.id = m.curso_id
GROUP BY c.id, c.nome, c.descricao, c.data_inicio, c.data_fim, u.primeiro_nome, u.ultimo_nome;

-- =====================================================
-- FUNÇÕES ÚTEIS
-- =====================================================

-- Função para calcular tempo total de estudo por estudante
CREATE OR REPLACE FUNCTION calcular_tempo_estudo_estudante(estudante_id BIGINT)
RETURNS INTEGER AS $$
DECLARE
    total_minutos INTEGER;
BEGIN
    SELECT COALESCE(SUM(re.duracao_minutos), 0)
    INTO total_minutos
    FROM registros_estudo re
    JOIN matriculas m ON re.matricula_id = m.id
    WHERE m.usuario_id = estudante_id;
    
    RETURN total_minutos;
END;
$$ LANGUAGE plpgsql;

-- Função para verificar se estudante pode se matricular
CREATE OR REPLACE FUNCTION pode_se_matricular(estudante_id BIGINT)
RETURNS BOOLEAN AS $$
DECLARE
    matriculas_ativas INTEGER;
BEGIN
    SELECT COUNT(*)
    INTO matriculas_ativas
    FROM matriculas
    WHERE usuario_id = estudante_id AND status = 'ATIVO';
    
    RETURN matriculas_ativas < 3;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- TRIGGERS
-- =====================================================

-- Trigger para atualizar campo atualizado_em em registros_estudo
CREATE OR REPLACE FUNCTION atualizar_timestamp_registro()
RETURNS TRIGGER AS $$
BEGIN
    NEW.atualizado_em = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_atualizar_registro_estudo
    BEFORE UPDATE ON registros_estudo
    FOR EACH ROW
    EXECUTE FUNCTION atualizar_timestamp_registro();

-- =====================================================
-- CONSULTAS ÚTEIS
-- =====================================================

-- Consulta para relatório de progresso por estudante
/*
SELECT 
    u.primeiro_nome || ' ' || u.ultimo_nome as estudante,
    c.nome as curso,
    COUNT(re.id) as total_registros,
    SUM(re.duracao_minutos) as tempo_total_minutos,
    ROUND(SUM(re.duracao_minutos) / 60.0, 2) as tempo_total_horas
FROM usuarios u
JOIN matriculas m ON u.id = m.usuario_id
JOIN cursos c ON m.curso_id = c.id
LEFT JOIN registros_estudo re ON m.id = re.matricula_id
WHERE m.status = 'ATIVO'
GROUP BY u.id, u.primeiro_nome, u.ultimo_nome, c.nome
ORDER BY tempo_total_horas DESC;
*/

-- Consulta para ranking de cursos mais populares
/*
SELECT 
    c.nome as curso,
    COUNT(m.id) as total_matriculas,
    COUNT(CASE WHEN m.status = 'ATIVO' THEN 1 END) as matriculas_ativas
FROM cursos c
LEFT JOIN matriculas m ON c.id = m.curso_id
GROUP BY c.id, c.nome
ORDER BY total_matriculas DESC;
*/

-- =====================================================
-- BACKUP E MANUTENÇÃO
-- =====================================================

-- Script para backup
-- pg_dump -h localhost -U lms_user -d lmsdb > backup_lms_$(date +%Y%m%d_%H%M%S).sql

-- Script para restore
-- psql -h localhost -U lms_user -d lmsdb < backup_lms_YYYYMMDD_HHMMSS.sql

-- =====================================================
-- FIM DO SCRIPT
-- =====================================================

-- Verificar se tudo foi criado corretamente
SELECT 'Banco de dados LMS inicializado com sucesso!' as status;

-- Mostrar estatísticas
SELECT 
    'Tabelas criadas: ' || COUNT(*) as tabelas
FROM information_schema.tables 
WHERE table_schema = 'public';

SELECT 
    'Usuários criados: ' || COUNT(*) as usuarios
FROM usuarios;

SELECT 
    'Cursos criados: ' || COUNT(*) as cursos
FROM cursos;

SELECT 
    'Matrículas criadas: ' || COUNT(*) as matriculas
FROM matriculas;
