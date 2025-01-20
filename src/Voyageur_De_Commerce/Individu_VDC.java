package Voyageur_De_Commerce;

import Algo_Genetiques.Individu;
import java.util.Random;

public class Individu_VDC implements Individu {
    private double[] coord_x;
    private double[] coord_y;
    private int[] parcours; // The tour (permutation of cities)

    // Constructor: initializes the individual with city coordinates and a random path
    public Individu_VDC(double[] coord_x, double[] coord_y) {
        this.coord_x = coord_x;
        this.coord_y = coord_y;
        this.parcours = new int[coord_x.length];
        // Initialize with a random tour
        Random rand = new Random();
        for (int i = 0; i < coord_x.length; i++) {
            parcours[i] = i;  // Initial order, may be randomized later
        }
        shuffle(parcours);  // Shuffle the cities to randomize the initial path
    }

    // Randomly shuffles the array to generate a random path
    private void shuffle(int[] array) {
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    // Calculates the total distance of the tour (fitness function)
    @Override
    public double adaptation() {
        double totalDistance = 0;
        for (int i = 0; i < parcours.length - 1; i++) {
            int cityA = parcours[i];
            int cityB = parcours[i + 1];
            totalDistance += distance(cityA, cityB);
        }
        // Close the loop: return to the first city
        totalDistance += distance(parcours[parcours.length - 1], parcours[0]);
        return totalDistance;
    }

    // Calculates the Euclidean distance between two cities
    private double distance(int cityA, int cityB) {
        double dx = coord_x[cityA] - coord_x[cityB];
        double dy = coord_y[cityA] - coord_y[cityB];
        return Math.sqrt(dx * dx + dy * dy);
    }

    // Crossover: combines two parents to produce two children
    @Override
    public Individu[] croisement(Individu conjoint) {
        Individu_VDC parent1 = this;
        Individu_VDC parent2 = (Individu_VDC) conjoint;
        
        int n = parent1.parcours.length;
        Random rand = new Random();
        
        // Choose a random crossover point
        int crossoverPoint = rand.nextInt(n);
        
        // Create two offspring
        int[] child1 = new int[n];
        int[] child2 = new int[n];
        
        // Copy the first part from parent1
        System.arraycopy(parent1.parcours, 0, child1, 0, crossoverPoint);
        System.arraycopy(parent2.parcours, 0, child2, 0, crossoverPoint);
        
        // Fill the remaining part with cities from the other parent
        fillRemainingCities(child1, parent2.parcours, crossoverPoint);
        fillRemainingCities(child2, parent1.parcours, crossoverPoint);
        
        // Create and return the offspring
        Individu_VDC offspring1 = new Individu_VDC(parent1.coord_x, parent1.coord_y);
        Individu_VDC offspring2 = new Individu_VDC(parent1.coord_x, parent1.coord_y);
        offspring1.parcours = child1;
        offspring2.parcours = child2;
        
        return new Individu[]{offspring1, offspring2};
    }

    // Fill the remaining cities in a child from the other parent
    private void fillRemainingCities(int[] child, int[] parent, int crossoverPoint) {
        boolean[] visited = new boolean[child.length];
        for (int i = 0; i < crossoverPoint; i++) {
            visited[child[i]] = true;
        }
        int idx = crossoverPoint;
        for (int i = 0; i < parent.length; i++) {
            if (!visited[parent[i]]) {
                child[idx++] = parent[i];
            }
        }
    }

    // Mutation: swaps two random cities in the path
    @Override
    public void mutation(double prob) {
        Random rand = new Random();
        if (rand.nextDouble() < prob) {
            int i = rand.nextInt(parcours.length);
            int j = rand.nextInt(parcours.length);
            // Swap cities i and j
            int temp = parcours[i];
            parcours[i] = parcours[j];
            parcours[j] = temp;
        }
    }

    // Accessor methods
    public int[] get_parcours() {
        return parcours;
    }

    public double[] get_coord_x() {
        return coord_x;
    }

    public double[] get_coord_y() {
        return coord_y;
    }
}
