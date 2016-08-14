-- SQL statements which are executed at application startup if hibernate.hbm2ddl.auto is 'create' or 'create-drop'
INSERT INTO Usuario(nome,senha, email, enabled, ehTaxista, dataCadastro, celular, cpf, cidade, bairro, logradouro, cnh) VALUES("Marcos Maia", "12345678","maia.marcos@gmail.com", true, true, '2011-11-21 22:17:32', "61 8408-5302", "011.700.836-34", "Brasilia", "Asa Sul", "SQS 208 Bl A", "342.323.234");
INSERT INTO Usuario(nome,senha, email, enabled, ehTaxista, dataCadastro, celular, cpf, cidade, bairro, logradouro) VALUES("Jonh Doe", "12345678","jhondoe@gmail.com", true, false, '2011-12-24 20:00:12', "61 8443-4323", "043.723.123-56", "Brasilia", "Asa Norte", "SQN 304 Bl F");
INSERT INTO Usuario(nome,senha, email, enabled, ehTaxista, dataCadastro, celular, cpf, cidade, bairro, logradouro, cnh) VALUES("Herivelto Gabriel", "12345678","heriveltogabriel@gmail.com", true, false, '2011-12-24 20:00:12', "61 8445-4323", "043.756.123-56", "Brasilia", "Asa Norte", "SQN 304 Bl F", "543.345.543");


-- inserts de perfis de usuários na tabela de roles.
INSERT INTO Role(nome,descricao) VALUES("ADMIN","Role para Usuários de Administração do software");
INSERT INTO Role(nome,descricao) VALUES("CLIENTE","Role para Clientes do software, todos tem esta role assim que o cadastro é feito no sistema");
INSERT INTO Role(nome,descricao) VALUES("TAXISTA","Role para Taxistas do software");

-- Associa usuários a roles
INSERT INTO Usuario_Role(usuario_id,role_id) VALUES(1,1);
INSERT INTO Usuario_Role(usuario_id,role_id) VALUES(1,2);
INSERT INTO Usuario_Role(usuario_id,role_id) VALUES(1,3); 

INSERT INTO Solicitacao(dataHora, destino, informacoesAdicionais, numeroPassageiros, origem, status, passageiro_id, taxista_id) VALUES('2012-05-2 22:17:32', "Aer Int Pres Juscelino Kubitschek, Brasilia, Brazil", "3 malas médias", 2, "Taguatinga, Brasilia - Distrito Federal, Brazil",	0, 2, 1);