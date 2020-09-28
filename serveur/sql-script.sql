DROP TABLE Cine_PANIER;
DROP TABLE Cine_COMMANDE;
DROP TABLE Cine_Client;

CREATE TABLE Cine_Client(
    idClient VARCHAR2(50) NOT NULL,
    nom VARCHAR2(100),
    photo VARCHAR2(500),
    email VARCHAR2(100),
    tel VARCHAR2(15),
    adresse VARCHAR2(100),
    pointFidelites NUMBER(3),
    CONSTRAINT pk_Cine_client PRIMARY KEY (idClient)
);
CREATE TABLE Cine_COMMANDE(
    idCommande  NUMBER(5) NOT NULL,
    idClient  VARCHAR2(50),
    dateCommande TIMESTAMP, 
    adresseLivraison VARCHAR2(100),
    CONSTRAINT pk_Cine_commande PRIMARY KEY (idCommande),
    CONSTRAINT fk_Cine_commande_client FOREIGN KEY (idClient) REFERENCES Cine_Client(idClient)
);


CREATE TABLE Cine_PANIER(
    idCommande  NUMBER(5) NOT NULL,
    idProduit  NUMBER(10) NOT NULL,
    typeProduit VARCHAR2(4) CHECK(typeProduit IN('Plat','Film')),
    quantite NUMBER(3),
    CONSTRAINT pk_Cine_PANIER PRIMARY KEY (idCommande, idProduit,typeProduit),
    CONSTRAINT fk_Cine_PANIER_commande FOREIGN KEY ( idCommande ) REFERENCES Cine_COMMANDE(idCommande)
);




-- Jeu des données

insert into Cine_Client (idClient,nom,pointFidelites) values ('A123456', 'Thomas Gerard',56);
insert into Cine_Client (idClient,nom,pointFidelites) values ('A684975', 'MARTIN BERNARD',100);
insert into Cine_Client (idClient,nom,pointFidelites) values ('B647823', 'ROBERT DURAND',260);
insert into Cine_Client (idClient,nom,pointFidelites) values ('D025404', 'SIMON LEFEBVRE',5);
insert into Cine_Client (idClient,nom,pointFidelites) values ('R652357', 'Ali Loua',12);
insert into Cine_Client (idClient,nom,pointFidelites)values ('F111254', 'SARAH DURA',45);



insert into Cine_COMMANDE values (1, 'A123456', to_date('20200326', 'yyyymmdd'), '20 rue gabriel peri - 38000 Grenoble');
insert into Cine_COMMANDE values (2, 'A123456', to_date('20200221', 'yyyymmdd'), '12 rue du Rempart Matabiau - 31000 TOULOUSE');
insert into Cine_COMMANDE values (3, 'B647823', to_date('20200413', 'yyyymmdd'), '141 quai Pierre Scize - 69009 Lyon');
insert into Cine_COMMANDE values (4, 'D025404', to_date('20200326', 'yyyymmdd'), '60 Rue de Turbigo - 75003 Paris');
insert into Cine_COMMANDE values (5, 'R652357', to_date('20200221', 'yyyymmdd'), '60 Rue des esperances - 75003 Paris');
insert into Cine_COMMANDE values (6, 'F111254', to_date('20200413', 'yyyymmdd'), '23 Rue de Turcs - 69009 Lyon');



insert into Cine_PANIER values (1,500,'Plat',2);
insert into Cine_PANIER values (2,456,'Plat',4);
insert into Cine_PANIER values (3,123,'Film',1);
insert into Cine_PANIER values (4,200,'Plat',2);
insert into Cine_PANIER values (5,156,'Plat',1);
insert into Cine_PANIER values (6,208,'Film',4);


