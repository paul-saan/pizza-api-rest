DROP TABLE pizza CASCADE;
DROP TABLE ingredients CASCADE;
DROP TABLE pizza_ingredient CASCADE;

CREATE TABLE pizza (
  id INT NOT NULL,
  nom VARCHAR(255) NOT NULL,
  pate VARCHAR(255) NOT NULL,
  prixBase INT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE ingredients (
  id INT NOT NULL,
  name VARCHAR(255) NOT NULL,
  prix INT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE pizza_ingredient (
  pizza_id INT NOT NULL,
  ingredient_id INT NOT NULL,
  PRIMARY KEY (pizza_id, ingredient_id),
  FOREIGN KEY (pizza_id) REFERENCES pizza(id) ON DELETE CASCADE,
  FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE
);

INSERT INTO ingredients (id, name, prix) VALUES
  (1, 'Tomates', 2),
  (2, 'Mozzarella', 3),
  (3, 'Jambon', 5),
  (4, 'Champignons', 2),
  (5, 'Olives', 1),
  (6, 'Poivron', 1),
  (7, 'Ananas', 2),
  (8, 'Saumon', 7),
  (9, 'Chèvre', 4),
  (10, 'Roquette', 1),
  (11, 'Chorizo', 4),
  (12, 'Épinards', 1),
  (13, 'Ricotta', 3),
  (14, 'Gorgonzola', 5),
  (15, 'Bleu', 2);

INSERT INTO pizza (id, nom, pate, prixBase) VALUES (1, 'Margherita', 'Fine', 8);
INSERT INTO pizza_ingredient (pizza_id, ingredient_id) VALUES (1, 1); -- Tomates
INSERT INTO pizza_ingredient (pizza_id, ingredient_id) VALUES (1, 2); -- Mozzarella

-- Pizza 2 : Regina
INSERT INTO pizza (id, nom, pate, prixBase) VALUES (2, 'Regina', 'Fine', 10);
INSERT INTO pizza_ingredient (pizza_id, ingredient_id) VALUES (2, 1); -- Tomates
INSERT INTO pizza_ingredient (pizza_id, ingredient_id) VALUES (2, 2); -- Mozzarella
INSERT INTO pizza_ingredient (pizza_id, ingredient_id) VALUES (2, 3); -- Jambon

-- Pizza 3 : Napolitaine
INSERT INTO pizza (id, nom, pate, prixBase) VALUES (3, 'Napolitaine', 'Fine', 9);
INSERT INTO pizza_ingredient (pizza_id, ingredient_id) VALUES (3, 1); -- Tomates
INSERT INTO pizza_ingredient (pizza_id, ingredient_id) VALUES (3, 2); -- Mozzarella
INSERT INTO pizza_ingredient (pizza_id, ingredient_id) VALUES (3, 4); -- Champignons
INSERT INTO pizza_ingredient (pizza_id, ingredient_id) VALUES (3, 5); -- Olives

-- Pizza 4 : Quatre saisons
INSERT INTO pizza (id, nom, pate, prixBase) VALUES (4, 'Quatre saisons', 'Fine', 12);
INSERT INTO pizza_ingredient (pizza_id, ingredient_id) VALUES (4, 1); -- Tomates
INSERT INTO pizza_ingredient (pizza_id, ingredient_id) VALUES (4, 2); -- Mozzarella
INSERT INTO pizza_ingredient (pizza_id, ingredient_id) VALUES (4, 3); -- Jambon
INSERT INTO pizza_ingredient (pizza_id, ingredient_id) VALUES (4, 4); -- Champignons
INSERT INTO pizza_ingredient (pizza_id, ingredient_id) VALUES (4, 6); -- Poivron

-- Pizza 5 : Dessert - Poire et Gorgonzola
INSERT INTO pizza (id, nom, pate, prixBase) VALUES (5, 'Poire et Gorgonzola', 'Fine', 12);
INSERT INTO pizza_ingredient (pizza_id, ingredient_id) VALUES (5, 15); -- Poire
INSERT INTO pizza_ingredient (pizza_id, ingredient_id) VALUES (5, 14); -- Gorgonzola

SELECT ingredients.name 
FROM ingredients,pizza_ingredient 
WHERE ingredients.id=pizza_ingredient.ingredient_id 
AND pizza_ingredient.pizza_id = ?;