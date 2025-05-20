import java.util.Scanner;

public class AppTerminal {
    public static void main(String[] args) {
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
            System.out.println("Veuillez entrer un entier entre 1 et 50.");
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
            System.out.println("Veuillez entrer un entier entre 1 et 50.");
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

        // Ajout : modification du labyrinthe
        scanner.nextLine(); // consomme le retour à la ligne
        System.out.print("Voulez-vous modifier une case ? (o/n) : ");
        String modif = scanner.nextLine();
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
                System.out.print("Mur à modifier (nord/sud/est/ouest) : ");
                direction = scanner.nextLine().trim().toLowerCase();
                if (direction.equals("n") || direction.equals("s") || direction.equals("e") || direction.equals("o")) break;
                System.out.println("Veuillez entrer 'n', 's', 'e' ou 'o'.");
            }

            lab.modifierLabyrinthe(lab.getCarte()[x][y], direction);
            System.out.println("Labyrinthe modifié :");
            System.out.println(lab);

            System.out.print("Modifier une autre case ? (o/n) : ");
            modif = scanner.nextLine();
        }

        // Saisie sécurisée du choix d'algo
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

        

        System.out.println("Fin.");
        scanner.close();
    }
}