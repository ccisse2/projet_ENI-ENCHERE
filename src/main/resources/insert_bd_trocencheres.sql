BEGIN TRANSACTION;

-- Script d'insertion dans la base de données ENCHERES_DB
--   type :      SQL Server 2022

-- Adresses Campus ENI Ecole Nantes
INSERT INTO ADRESSES (rue, code_postal, ville, adresse_eni) VALUES
    ('2B RUE BENJAMIN FRANKLIN', '44800', 'SAINT HERBLAIN', 1);
DECLARE @AddNantesFranklin AS INTEGER = SCOPE_IDENTITY();

INSERT INTO ADRESSES (rue, code_postal, ville, adresse_eni) VALUES
    ('3 RUE MICKAËL FARADAY', '44800', 'SAINT HERBLAIN', 1);
DECLARE @AddNantesFaraday AS INTEGER = SCOPE_IDENTITY();

-- Adresse Campus ENI Ecole Rennes
INSERT INTO ADRESSES (rue, code_postal, ville, adresse_eni) VALUES
    ('8 RUE LÉO LAGRANGE', '35131', 'CHARTRES DE BRETAGNE', 1);
DECLARE @AddRennes AS INTEGER = SCOPE_IDENTITY();

-- Adresse Campus ENI Ecole Quimper
INSERT INTO ADRESSES (rue, code_postal, ville, adresse_eni) VALUES
    ('2 RUE GEORGES PERROS', '29000', 'QUIMPER', 1);

-- Adresse Campus ENI Ecole Niort
INSERT INTO ADRESSES (rue, code_postal, ville, adresse_eni) VALUES
    ('19 AVENUE LÉO LAGRANGE', '79000', 'NIORT', 1);

-- Catégories
INSERT INTO CATEGORIES (libelle) VALUES
    ('Ameublement');
DECLARE @CatMeuble AS INTEGER = SCOPE_IDENTITY();

INSERT INTO CATEGORIES (libelle) VALUES
    ('Informatique');
DECLARE @CatInfo AS INTEGER = SCOPE_IDENTITY();

INSERT INTO CATEGORIES (libelle) VALUES
    ('Sport & Loisir');

INSERT INTO CATEGORIES (libelle) VALUES
    ('Vêtement');
DECLARE @CatVet AS INTEGER = SCOPE_IDENTITY();

-- Utilisateurs
-- Chiffrement des mots de passe avec {bcrypt} Mots de passe =  Pa$$w0rd
-- L'administrateur pour les tests des formateurs
DECLARE @CoachAdminId AS INTEGER;
INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, mot_de_passe, administrateur, no_adresse) VALUES
    ('coach_admin', 'COACH', 'Eni', 'coach@campus-eni.fr', '{bcrypt}$2a$10$tKtwPcqKLa6Hw8nnGr96LeN.hxzhjXGzQe1DuN6hjlnRGJgiTqcMu', 1, @AddNantesFranklin);
SET @CoachAdminId = SCOPE_IDENTITY();

-- Utilisateur toto pour les tests des formateurs
DECLARE @CoachTotoId AS INTEGER;
INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, mot_de_passe, administrateur, no_adresse) VALUES
    ('coach_toto', 'Toto', 'Eni', 'toto@campus-eni.fr', '{bcrypt}$2a$10$tKtwPcqKLa6Hw8nnGr96LeN.hxzhjXGzQe1DuN6hjlnRGJgiTqcMu', 0, @AddNantesFaraday);
SET @CoachTotoId = SCOPE_IDENTITY();

-- Utilisateur titi pour les tests des formateurs
DECLARE @CoachTitiId AS INTEGER;
INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, mot_de_passe, no_adresse) VALUES
    ('coach_titi', 'Titi', 'Eni', 'titi@campus-eni.fr', '{bcrypt}$2a$10$tKtwPcqKLa6Hw8nnGr96LeN.hxzhjXGzQe1DuN6hjlnRGJgiTqcMu', @AddRennes);
SET @CoachTitiId = SCOPE_IDENTITY();

-- Utilisateur tata pour les tests des formateurs
DECLARE @CoachTataId AS INTEGER;
INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, mot_de_passe, administrateur, no_adresse) VALUES
    ('coach_tata', 'Tata', 'Eni', 'tata@campus-eni.fr', '{bcrypt}$2a$10$tKtwPcqKLa6Hw8nnGr96LeN.hxzhjXGzQe1DuN6hjlnRGJgiTqcMu', 0, @AddNantesFaraday);
SET @CoachTataId = SCOPE_IDENTITY();

-- Articles à vendre
-- 4 articles de tests sans enchère
DECLARE @ArticleId1 AS INTEGER;
DECLARE @ArticleId2 AS INTEGER;
DECLARE @ArticleId3 AS INTEGER;
DECLARE @ArticleId4 AS INTEGER;

INSERT INTO ARTICLES_A_VENDRE (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, id_utilisateur, no_categorie, no_adresse_retrait) VALUES
    ('PC ENI', 'Un PC de salle de cours', DATEADD(DAY, 1, GETDATE()), DATEADD(DAY, 15, GETDATE()), 1, @CoachTotoId, @CatInfo, @AddNantesFaraday);
SET @ArticleId1 = SCOPE_IDENTITY();

INSERT INTO ARTICLES_A_VENDRE (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, id_utilisateur, no_categorie, no_adresse_retrait) VALUES
    ('Disque Dur', 'Disque dur externe', DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, 20, GETDATE()), 1, @CoachTotoId, @CatInfo, @AddNantesFaraday);
SET @ArticleId2 = SCOPE_IDENTITY();

INSERT INTO ARTICLES_A_VENDRE (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, id_utilisateur, no_categorie, no_adresse_retrait) VALUES
    ('PC Gamer pour travailler', 'Un PC de Gamer avec une RAM de 36Go', DATEADD(DAY, 1, GETDATE()), DATEADD(DAY, 15, GETDATE()), 2, @CoachTotoId, @CatInfo, @AddNantesFranklin);
SET @ArticleId3 = SCOPE_IDENTITY();

INSERT INTO ARTICLES_A_VENDRE (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, id_utilisateur, no_categorie, no_adresse_retrait) VALUES
    ('Baskets blanches (femme)', 'Chaussures pour le sport ou tous les jours', DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, 10, GETDATE()), 1, @CoachTitiId, @CatVet, @AddRennes);
SET @ArticleId4 = SCOPE_IDENTITY();

-- Enchère en cours
DECLARE @ArticleEnchereEnCours AS INTEGER = @ArticleId1;
DECLARE @Offre AS INTEGER = 2;

-- 2 enchères sur 1 même article
INSERT INTO ENCHERES (id_utilisateur, no_article, date_enchere, montant_enchere) VALUES
    (@CoachTitiId, @ArticleEnchereEnCours, DATEADD(DAY, -2, GETDATE()), @Offre);

-- Mise à jour article
UPDATE ARTICLES_A_VENDRE SET prix_vente = @Offre WHERE no_article = @ArticleEnchereEnCours;

-- Mise à jour des points pour l'acquéreur
UPDATE UTILISATEURS SET credit = credit - @Offre WHERE id = @CoachTitiId;

INSERT INTO ENCHERES (id_utilisateur, no_article, date_enchere, montant_enchere) VALUES
    (@CoachTataId, @ArticleEnchereEnCours, DATEADD(DAY, -1, GETDATE()), @Offre + 1);

-- Mise à jour article
UPDATE ARTICLES_A_VENDRE SET prix_vente = @Offre + 1 WHERE no_article = @ArticleEnchereEnCours;

-- Mise à jour des points pour l'acquéreur
UPDATE UTILISATEURS SET credit = credit - (@Offre + 1) WHERE id = @CoachTataId;

-- Article avec enchère clôturée
INSERT INTO ARTICLES_A_VENDRE (nom_article, description, date_debut_encheres, date_fin_encheres, statut_enchere, prix_initial, prix_vente, id_utilisateur, no_categorie, no_adresse_retrait) VALUES
    ('Chaise', 'Chaise de bureau', DATEADD(DAY, -3, GETDATE()), DATEADD(DAY, -1, GETDATE()), 2, 1, 2, @CoachTotoId, @CatMeuble, @AddNantesFranklin);
DECLARE @ArticleEnchereCloturee AS INTEGER = SCOPE_IDENTITY();

-- Enchère sur article clôturé
INSERT INTO ENCHERES (id_utilisateur, no_article, date_enchere, montant_enchere) VALUES
    (@CoachTitiId, @ArticleEnchereCloturee, DATEADD(DAY, -2, GETDATE()), @Offre);

-- Mise à jour des points pour l'acquéreur
UPDATE UTILISATEURS SET credit = credit - @Offre WHERE id = @CoachTitiId;

-- Mise à jour article
UPDATE ARTICLES_A_VENDRE SET prix_vente = @Offre WHERE no_article = @ArticleEnchereCloturee;

-- Mise à jour des points pour le vendeur
UPDATE UTILISATEURS SET credit = credit + @Offre WHERE id = @CoachTotoId;

-- Article avec enchère annulée
INSERT INTO ARTICLES_A_VENDRE (nom_article, description, date_debut_encheres, date_fin_encheres, statut_enchere, prix_initial, id_utilisateur, no_categorie, no_adresse_retrait) VALUES
    ('Bureau', 'Bureau d''ordinateur', DATEADD(DAY, 2, GETDATE()), DATEADD(DAY, 16, GETDATE()), 100, @CoachTitiId, @CatMeuble, @AddNantesFranklin);

COMMIT TRANSACTION;
