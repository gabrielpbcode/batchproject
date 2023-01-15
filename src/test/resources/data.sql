CREATE TABLE IF NOT EXISTS BOOKS(
  id BIGINT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  author VARCHAR(100) NOT NULL,
  language VARCHAR(50),
  year VARCHAR(4),
  rented boolean DEFAULT FALSE
);

INSERT INTO BOOKS (id, name, author, year, language)
VALUES ('1', 'Energy and Civilization: A History', 'Vaclav Smil', '2018', 'English'),
       ('2', 'Criando Microsserviços', 'Newman', '2020', 'Portuguese'),
       ('3', 'Devops nativo de nuvem com kubernets', 'Arundel & Domingus', '2017', 'Portuguese');