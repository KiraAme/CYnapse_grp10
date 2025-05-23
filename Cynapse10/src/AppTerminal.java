import java.util.Scanner;
import java.io.*;

/**
 * AppTerminal.java
 * 
 * Classe principale pour le générateur de labyrinthes en mode terminal.
 * Permet de créer, modifier, sauvegarder et résoudre un labyrinthe.
 * @version 1.0
 * @author Groupe 10
 */
public class AppTerminal {
    /**
     * Classe principale pour le générateur de labyrinthes en mode terminal.
     * Permet de créer, modifier, sauvegarder et résoudre un labyrinthe.
     * @param args Arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        // Initialisation du générateur de labyrinthe
        // Utilisation de Scanner pour la saisie utilisateur
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Générateur de Labyrinthe (Terminal) ===");

        int longueur = 0, largeur = 0;
        long seed = 0;

        // Saisie sécurisée de la longueur
        while (true) {
            System.out.print("Longueur (max 30) : ");
            if (scanner.hasNextInt()) {
                longueur = scanner.nextInt();
                if (longueur >= 1 && longueur <= 30) break;
            } else {
                scanner.next(); // Consomme l'entrée invalide
            }
            System.out.println("Veuillez entrer un entier entre 1 et 30.");
        }

        // Saisie sécurisée de la largeur
        while (true) {
            System.out.print("Largeur (max 30) : ");
            if (scanner.hasNextInt()) {
                largeur = scanner.nextInt();
                if (largeur >= 1 && largeur <= 50) break;
            } else {
                scanner.next();
            }
            System.out.println("Veuillez entrer un entier entre 1 et 30.");
        }

        // Saisie sécurisée du seed
        while (true) {
            System.out.print("Seed (0 pour aléatoire) : ");
            if (scanner.hasNextLong()) {
                seed = scanner.nextLong();
                break;
            } else {
                scanner.next();
            }
            System.out.println("Veuillez entrer un nombre valide.");
        }

        if (seed == 0) seed = System.currentTimeMillis();

        Labyrinthe lab = new Labyrinthe("Terminal", longueur, largeur, seed);
        // Demande labyrinthe parfait ou imparfait
        boolean parfait = true;
        while (true) {
            System.out.print("Labyrinthe parfait (sans boucle) ? (o/n) : ");
            String rep = scanner.next().trim().toLowerCase();
            if (rep.equals("o")) {
                parfait = true;
                lab.genererLabyrinthe();
                break;
            } else if (rep.equals("n")) {
                parfait = false;
                lab.genererImparfait();
                break;
            } else {
                System.out.println("Veuillez répondre par 'o' ou 'n'.");
            }
        }

        System.out.println("\nLabyrinthe généré :\n");
        System.out.println(lab);

        scanner.nextLine(); // consomme le retour à la ligne

        // === MENU PRINCIPAL ===
        boolean quitter = false;
        while (!quitter) {

            System.out.println("\nQue voulez-vous faire ?");
            System.out.println("1. Modifier une case");
            System.out.println("2. Sauvegarder");
            System.out.println("3. Restaurer");
            System.out.println("4. Résoudre");
            System.out.println("5. Quitter");
            System.out.print("Votre choix : ");
            String choixMenu = scanner.nextLine().trim();
            lab.reset();
            switch (choixMenu) {
                case "1": // Modifier une case
                    String modif = "o";
                    while (modif.equalsIgnoreCase("o")) {
                        int x = -1, y = -1;
                        // Saisie sécurisée de X
                        while (true) {
                            System.out.print("Coordonnée X de la case (0 à " + (lab.getLargeur()-1) + ") : ");
                            if (scanner.hasNextInt()) {
                                x = scanner.nextInt();
                                if (x >= 0 && x < lab.getLargeur()) break;
                            } else {
                                scanner.next();
                            }
                            System.out.println("Veuillez entrer un entier valide.");
                        }
                        // Saisie sécurisée de Y
                        while (true) {
                            System.out.print("Coordonnée Y de la case (0 à " + (lab.getLongueur()-1) + ") : ");
                            if (scanner.hasNextInt()) {
                                y = scanner.nextInt();
                                if (y >= 0 && y < lab.getLongueur()) break;
                            } else {
                                scanner.next();
                            }
                            System.out.println("Veuillez entrer un entier valide.");
                        }
                        scanner.nextLine(); // consomme le retour à la ligne

                        // Saisie sécurisée de la direction
                        String direction = "";
                        while (true) {
                            System.out.print("Mur à modifier (n/s/e/o) : ");
                            direction = scanner.nextLine().trim().toLowerCase();
                            if (direction.equals("n") || direction.equals("s") || direction.equals("e") || direction.equals("o")) break;
                            System.out.println("Veuillez entrer 'n', 's', 'e' ou 'o'.");
                        }
                        if (direction.equals("n")) direction = "nord";
                        if (direction.equals("s")) direction = "sud";
                        if (direction.equals("e")) direction = "est";
                        if (direction.equals("o")) direction = "ouest";

                        lab.modifierLabyrinthe(lab.getCarte()[x][y], direction);
                        System.out.println("Labyrinthe modifié :");
                        System.out.println(lab);
                        System.out.println("Historique des modifications :");
                        for (String ligne : lab.getHistorique()) {
                            System.out.println(ligne);
                        }
                        System.out.print("Modifier une autre case ? (o/n) : ");
                        modif = scanner.nextLine();
                    }
                    break;

                case "2": // Sauvegarder
                    try (PrintWriter writer = new PrintWriter(new FileWriter("labyrinthe_save.txt"))) {
                        writer.println(lab.getLongueur());
                        writer.println(lab.getLargeur());
                        writer.println(lab.getSeed());
                        for (String ligne : lab.getHistorique()) {
                            writer.println(ligne);
                        }
                        System.out.println("Labyrinthe sauvegardé !");
                    } catch (IOException e) {
                        System.out.println("Erreur lors de la sauvegarde.");
                    }
                    break;

                case "3": // Restaurer
                    try (BufferedReader reader = new BufferedReader(new FileReader("labyrinthe_save.txt"))) {
                        int longueurR = Integer.parseInt(reader.readLine());
                        int largeurR = Integer.parseInt(reader.readLine());
                        long seedR = Long.parseLong(reader.readLine());
                        lab = new Labyrinthe("Restauré", longueurR, largeurR, seedR);
                        lab.genererLabyrinthe();
                        String ligne;
                        while ((ligne = reader.readLine()) != null) {
                            if (ligne.startsWith("Case")) {
                                try {
                                    String[] parts = ligne.split("[(),:]");
                                    int x = Integer.parseInt(parts[1].trim());
                                    int y = Integer.parseInt(parts[2].trim());
                                    String[] murParts = ligne.split("mur ");
                                    String direction = murParts[1].split(" ")[0].trim().toLowerCase();
                                    direction = direction.replaceAll("[éèêë]", "e");
                                    direction = direction.replaceAll("[àâä]", "a");
                                    direction = direction.replaceAll("[ûùü]", "u");
                                    direction = direction.replaceAll("[ôö]", "o");
                                    direction = direction.replaceAll("[îï]", "i");
                                    if (direction.equals("nord") || direction.equals("sud") || direction.equals("est") || direction.equals("ouest")) {
                                        lab.modifierLabyrinthe(lab.getCarte()[x][y], direction);
                                    }
                                } catch (Exception ex) {
                                    // Ignore la ligne si erreur de parsing
                                }
                            }
                        }
                        System.out.println("Labyrinthe restauré :\n");
                        System.out.println(lab);
                    } catch (Exception e) {
                        System.out.println("Erreur lors de la restauration.");
                    }
                    break;

                case "4": // Résoudre
                    int choix = 0;
                    while (true) {
                        System.out.println("Choisissez un algorithme de résolution :");
                        System.out.println("1. Trémaux");
                        System.out.println("2. DeadEnd");
                        System.out.println("3. Dijkstra");
                        System.out.print("Votre choix : ");
                        if (scanner.hasNextInt()) {
                            choix = scanner.nextInt();
                            if (choix >= 1 && choix <= 3) break;
                        } else {
                            scanner.next();
                        }
                        System.out.println("Veuillez entrer 1, 2 ou 3.");
                    }
                    switch (choix) {
                        case 1:
                            new Tremaux().algoDirect(lab, null, null);
                            break;
                        case 2:
                            new DeadEnd().algoDirect(lab, null, null);
                            break;
                        case 3:
                            new Dijkstra().algoDirect(lab, null, null);
                            break;
                    }
                    System.out.println("\nLabyrinthe après résolution :\n");
                    System.out.println(lab);
                    scanner.nextLine(); // consomme le retour à la ligne
                    break;

                case "5": // Quitter
                    quitter = true;
                    break;

                default:
                    System.out.println("Choix invalide.");
            }
        }

        System.out.println("Fin.");
        scanner.close();
    }
}