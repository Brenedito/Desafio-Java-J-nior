-- ##############################################
-- ##      SCRIPT DE POPULAÇÃO DE DADOS        ##
-- ##############################################

-- Inserindo usuários com status
INSERT INTO users (name, email, cpf, registration_date, status) VALUES
('Ana Clara Borges', 'ana.borges@example.com', '111.222.333-44', '2025-08-10T10:00:00', 'ATIVO'),
('Bruno Medeiros', 'bruno.medeiros@example.com', '222.333.444-55', '2025-08-12T11:30:00', 'ATIVO'),
('Carla Vieira', 'carla.vieira@example.com', '333.444.555-66', '2025-08-15T14:00:00', 'INATIVO'),
('Daniel Andrade', 'daniel.andrade@example.com', '444.555.666-77', CURRENT_TIMESTAMP, 'ATIVO');

-- Inserindo livros com status
INSERT INTO book (title, author, isbn, published_date, total_quantity, available_quantity, status) VALUES
('Clean Code', 'Robert C. Martin', '978-0132350884', '2008-01-01T00:00:00', 5, 4, 'DISPONIVEL'),
('O Programador Pragmático', 'Andrew Hunt, David Thomas', '978-8578271131', '1999-01-01T00:00:00', 3, 3, 'DISPONIVEL'),
('O Hobbit', 'J.R.R. Tolkien', '978-8595084742', '1937-01-01T00:00:00', 8, 8, 'DISPONIVEL'),
('1984', 'George Orwell', '978-8535914849', '1949-01-01T00:00:00', 4, 3, 'DISPONIVEL'),
('Domain-Driven Design', 'Eric Evans', '978-0321125217', '2003-01-01T00:00:00', 2, 1, 'DISPONIVEL'),
('A Revolução dos Bichos', 'George Orwell', '978-8535909555', '1945-01-01T00:00:00', 3, 0, 'INDISPONIVEL');