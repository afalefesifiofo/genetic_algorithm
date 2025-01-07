package Algo_Genetiques;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population<Indiv extends Individu> {

    // Liste contenant les différents individus d'une génération
    private List<Indiv> population;
    private static final Random RANDOM = new Random(); // Instance unique pour la randomisation

    /**
     * Construit une population à partir d'un tableau d'individus.
     */
    public Population(Indiv[] popu) {
        population = new ArrayList<>();
        for (Indiv indiv : popu) {
            population.add(indiv);
        }
    }

    /**
     * Sélectionne un individu par la méthode de la roulette.
     *
     * @param adapt_totale somme des adaptations de tous les individus.
     * @return l'indice de l'individu sélectionné.
     */
    public int selection(double adapt_totale) {
        double seuil = RANDOM.nextDouble() * adapt_totale;
        double somme = 0.0;

        for (int i = 0; i < population.size(); i++) {
            somme += population.get(i).adaptation();
            if (somme >= seuil) {
                return i;
            }
        }
        return population.size() - 1; // Retourne le dernier individu en cas de dépassement
    }

    /**
     * Remplace la génération actuelle par une nouvelle (croisement + mutation).
     *
     * @param prob_mut probabilité de mutation.
     */
    @SuppressWarnings("unchecked")
    public void reproduction(double prob_mut) {
        List<Indiv> new_generation = new ArrayList<>();

        // Élitisime : Conserve le meilleur individu de la génération précédente
        Indiv meilleur = individu_maximal();
        new_generation.add(meilleur);

        // Tant qu'on n'a pas assez d'individus pour une nouvelle génération
        while (new_generation.size() < population.size()) {
            // Sélection des parents
            int parent1Index = selection(adaptation_totale());
            int parent2Index = selection(adaptation_totale());

            Indiv parent1 = population.get(parent1Index);
            Indiv parent2 = population.get(parent2Index);

            // Croisement
            Indiv[] enfants = (Indiv[]) parent1.croisement(parent2);

            // Mutation
            enfants[0].mutation(prob_mut);
            enfants[1].mutation(prob_mut);

            // Ajout des enfants à la nouvelle génération
            new_generation.add(enfants[0]);
            if (new_generation.size() < population.size()) {
                new_generation.add(enfants[1]);
            }
        }

        // Remplacement de l'ancienne population par la nouvelle
        population = new_generation;
    }

    /**
     * Retourne l'individu avec la meilleure adaptation.
     */
    public Indiv individu_maximal() {
        Indiv meilleur = population.get(0);
        for (Indiv indiv : population) {
            if (indiv.adaptation() > meilleur.adaptation()) {
                meilleur = indiv;
            }
        }
        return meilleur;
    }

    /**
     * Calcule et renvoie l'adaptation moyenne de la population.
     */
    public double adaptation_moyenne() {
        double somme = 0.0;
        for (Indiv indiv : population) {
            somme += indiv.adaptation();
        }
        return somme / population.size();
    }

    /**
     * Calcule et renvoie l'adaptation maximale de la population.
     */
    public double adaptation_maximale() {
        return individu_maximal().adaptation();
    }

    /**
     * Calcule la somme totale des adaptations.
     */
    private double adaptation_totale() {
        double somme = 0.0;
        for (Indiv indiv : population) {
            somme += indiv.adaptation();
        }
        return somme;
    }

    /**
     * Affiche les statistiques de la population.
     */
    public void afficherStatistiques() {
        System.out.println("Adaptation moyenne : " + adaptation_moyenne());
        System.out.println("Adaptation maximale : " + adaptation_maximale());
    }
}
