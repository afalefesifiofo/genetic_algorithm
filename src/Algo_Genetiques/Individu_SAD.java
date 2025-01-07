package Algo_Genetiques;

import java.util.Random;

public class Individu_SAD implements Individu {

    private boolean[] genome;  // Tableau binaire (0 ou 1) pour représenter les objets sélectionnés
    private int[] poids;       // Tableau des poids des objets
    private int[] valeurs;     // Tableau des valeurs des objets
    private int capaciteMax;   // Capacité maximale du sac à dos
    private double fitness;    // Score d'adaptation (valeur totale si valide)

   
    public Individu_SAD(int[] poids, int[] valeurs, int capaciteMax) {
        this.poids = poids;
        this.valeurs = valeurs;
        this.capaciteMax = capaciteMax;
        this.genome = new boolean[poids.length];
        Random rand = new Random();
        for (int i = 0; i < genome.length; i++) {
            genome[i] = rand.nextBoolean();
        }
        calculerAdaptation();
    }

    //  Constructeur personnalisé
    public Individu_SAD(boolean[] genome, int[] poids, int[] valeurs, int capaciteMax) {
        this.genome = genome.clone();
        this.poids = poids;
        this.valeurs = valeurs;
        this.capaciteMax = capaciteMax;
        calculerAdaptation();
    }

    // Méthode d'adaptation
    @Override
    public double adaptation() {
        return fitness;
    }

    private void calculerAdaptation() {
        int poidsTotal = 0;
        int valeurTotale = 0;

        for (int i = 0; i < genome.length; i++) {
            if (genome[i]) {
                poidsTotal += poids[i];
                valeurTotale += valeurs[i];
            }
        }

        // Si le poids dépasse la capacité, l'adaptation est nulle
        fitness = (poidsTotal <= capaciteMax) ? valeurTotale : 0;
    }

    // Méthode de croisement
    @Override
    public Individu[] croisement(Individu conjoint) {
        Individu_SAD partenaire = (Individu_SAD) conjoint;
        Random rand = new Random();
        int pointCroisement = rand.nextInt(genome.length);

        boolean[] enfant1Genome = new boolean[genome.length];
        boolean[] enfant2Genome = new boolean[genome.length];

        for (int i = 0; i < genome.length; i++) {
            if (i < pointCroisement) {
                enfant1Genome[i] = this.genome[i];
                enfant2Genome[i] = partenaire.genome[i];
            } else {
                enfant1Genome[i] = partenaire.genome[i];
                enfant2Genome[i] = this.genome[i];
            }
        }

        Individu enfant1 = new Individu_SAD(enfant1Genome, poids, valeurs, capaciteMax);
        Individu enfant2 = new Individu_SAD(enfant2Genome, poids, valeurs, capaciteMax);

        return new Individu[]{enfant1, enfant2};
    }

    // Méthode de mutation
    @Override
    public void mutation(double prob) {
        Random rand = new Random();
        for (int i = 0; i < genome.length; i++) {
            if (rand.nextDouble() < prob) {
                genome[i] = !genome[i];  // Inversion du gène
            }
        }
        calculerAdaptation();
    }

    // Affichage pour le débogage
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Genome: ");
        for (boolean gene : genome) {
            sb.append(gene ? "1" : "0");
        }
        sb.append(" | Adaptation: ").append(fitness);
        return sb.toString();
    }
}
