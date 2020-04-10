/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

public class Citizen extends Unit {
	private int food;

	public Citizen () {
		this.food = 0;
		this.setType ("Citizen");
		this.setMoves(3);
	}

	public static int sumFood (Citizen[] citizens) {
		int sum = 0;

		for (int i = 0; i < citizens.length; i++) {
			sum += citizens[i].getFood();
		}
		return sum;
	}

	public void setFood (int newFood) {
		this.food = newFood;
	}
	public int getFood () {
		return this.food;
	}
	public void addFood (int addFood) {
		this.food += addFood;
	}

	public static Citizen[] addCitizen (Citizen[] population) {
		Citizen[] newPopulation = new Citizen [population.length + 1];

		for (int i = 0; i < population.length; i++) {
			population[i].setFood(0);
			newPopulation[i] = population[i];
		}
		newPopulation[newPopulation.length - 1] = new Citizen ();

		return newPopulation;
	}
}
