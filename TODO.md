# TODO

# classe transaction: 
- Ajout des cas particulier pour la "Genesis"
- Ajout d'accesseurs pour tout les champs

Info du sujet : 
> On l’appelle bloc «génésis». Le hash de son prédécesseur est zéro, sa nonce reste à
> zéro, il a une unique transaction réduite à la chaîne « Genesis » 1 . Par contre il a bien un hash. Il n’est pas
> nécessaire de le miner.

# classe BlockChain
- methode d'ajout de blocs (avec toutes les verifications que ça implique)


# classe Block
- Minage du bloc

# Test de validité 1 et 2
- présence du Génésis, les ash des blocks sont cohérents, le chaînage, aussi, les
racines de Merkle aussi


# Programme principal (À mettre dans bnb/Main.java)
- Contient le main() :
1. Création du genesis par Creator avec envoi de 50 Bnb à Creator par coinbase.
2. Phase "Helicopter money" où coinbase envoie 50 Bnb à tous les utilisateurs user1 à userN, (pas pour Creator).
3. Phase de marché où les transactions sont entre deux utilisateurs choisis au hasard. On Utilise une file
(FIFO) globale de transactions où le mineur vient se servir.
4. Un mineur est choisi au hasard (là Creator peut jouer). Il prend un certain nombre de transactions
dans la liste globale, et crée le nouveau bloc avec les transactions sous forme de chaînes de caractères "Usern1 envoie X Bnb à Usern2"
et ajoute une transaction où coinbase lui envoie le montant correspondant s’il est supérieur à zéro.
5. On continue la phase de marché avec éventuellement un minage basé uniquement sur les frais de
transaction si la phase d’inflation est terminée.
6. On arrête le programme soit sur une interruption, soit sur un nombre de blocs ou cycle prédéterminé.

Notes : ATTENTION, le montant des transactions est en satoBNB, donc les tx précisé aus dessus sont de 50x10⁸ SatoBNB


# Sauvegarde en JSON
- lecture/ecriture basique
- ?

# Interface de gestion
-  génération de blockchain en choisissant le nombre de blocs, la difficulté, le nombre maximal de transactions par
bloc
- sauver ou lire dans un fichier au format JSON
- vérifier l’intégrité de la blockchain
- sortir du programme
- afficher la blockchain ou un bloc d’un numéro donné


