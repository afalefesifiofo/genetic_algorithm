package Sac_A_Dos;

import Algo_Genetiques.Individu_SAD;
import Algo_Genetiques.Population;
import Util.Lecture;
import java.io.InputStream;
import java.util.Random;

public class Client_Sac_A_Dos {

    /**
     * lit une liste de poids dans un fichier
     * @param nomFichier nom du fichier texte contenant les poids
     * @param nbr_objets nombre d'objets possibles
     * @return tableau de poids
     */
    public static double[] charge_poids(String nomFichier, int nbr_objets) {
        double[] poids = new double[nbr_objets];
        InputStream IS = Lecture.ouvrir(nomFichier);
        if (IS == null) {
            System.err.println("Impossible d'ouvrir le fichier \"" + nomFichier + "\" (n'existe pas ? pas au bon endroit ?)");
        }
        int i = 0;
        double somme = 0;
        while (!Lecture.finFichier(IS) && i < nbr_objets) {
            poids[i] = Lecture.lireDouble(IS);
            somme += poids[i];
            i++;
        }
        System.out.println("charge_poids (" + nomFichier + ") : poids total des objets = " + somme);
        Lecture.fermer(IS);
        return poids;
    }

    public static void main(String[] args) {
        /* Paramètres */
        int nbr_indiv = 100; // Nombre d'individus dans la population
        double prob_mut = 0.1; // Probabilité de mutation
        int nbr_generations = 100; // Nombre de générations
        int nbr_objets = 28; // Nombre d'objets
        int capacite = 1581; // Capacité maximale du sac

        //  **Lecture des poids et valeurs depuis le fichier**
        double[] poids = charge_poids("./data_sad/nbrobj" + nbr_objets + "_capacite" + capacite + ".txt", nbr_objets);
        int[] poidsInt = new int[nbr_objets];
        int[] valeurs = new int[nbr_objets];
        Random rand = new Random();

        // Initialisation aléatoire des valeurs (ici, valeurs = poids pour simplifier)
        for (int i = 0; i < nbr_objets; i++) {
            poidsInt[i] = (int) poids[i];
            valeurs[i] = (int) poids[i];
        }

        // **Création des individus initiaux**
        Individu_SAD[] individus = new Individu_SAD[nbr_indiv];
        for (int i = 0; i < nbr_indiv; i++) {
            individus[i] = new Individu_SAD(poidsInt, valeurs, capacite);
        }

        //  **Création de la population initiale**
        Population<Individu_SAD> population = new Population<>(individus);

        //  **Générations successives**
        for (int generation = 1; generation <= nbr_generations; generation++) {
            System.out.println("⚙️ Génération " + generation);
            population.reproduction(prob_mut); // Croisement et mutation
            population.afficherStatistiques(); // Adaptation moyenne et maximale

            // Condition d'arrêt : si l'adaptation maximale atteint la capacité maximale
            if (population.adaptation_maximale() >= capacite) {
                System.out.println(" Capacité maximale atteinte à la génération " + generation);
                break;
            }
        }

        //  **Affichage du meilleur individu final**
        Individu_SAD meilleur = population.individu_maximal();
        System.out.println("\n🏅 Meilleur individu final :");
        System.out.println(meilleur);
    }
}
