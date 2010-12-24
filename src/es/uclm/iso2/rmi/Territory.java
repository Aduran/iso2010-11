package es.uclm.iso2.rmi;

import java.io.Serializable;

public class Territory implements Serializable {

	private static final long serialVersionUID = 4177888547129389571L;

	private enum Continent {
		Europe, Asia, Africa, NorthAmerica, SouthAmerica, Oceania
	}

	private int idTerritory;
	private Continent continent;
	private Player owner;
	private int numSoldiers;
	private int numCannons[] = new int[3]; // Cannons with one, two or three uses
	private int numMissiles;
	private int numICBMs; // Intercontinental Ballistic Missiles
	private int numAntiMissiles;

	public int getIdTerritory() {
		return idTerritory;
	}

	public void setIdTerritory(int idTerritory) {
		this.idTerritory = idTerritory;
	}

	public Continent getContinent() {
		return continent;
	}

	public void setContinent(Continent continent) {
		this.continent = continent;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public int getNumSoldiers() {
		return numSoldiers;
	}

	public void setNumSoldiers(int numSoldiers) {
		this.numSoldiers = numSoldiers;
	}

	public int[] getNumCannons() {
		return numCannons;
	}

	public void setNumCannons(int[] numCannons) {
		this.numCannons = numCannons;
	}

	public int getNumMissiles() {
		return numMissiles;
	}

	public void setNumMissiles(int numMissiles) {
		this.numMissiles = numMissiles;
	}

	public int getNumICBMs() {
		return numICBMs;
	}

	public void setNumICBMs(int numICBMs) {
		this.numICBMs = numICBMs;
	}

	public int getNumAntiMissiles() {
		return numAntiMissiles;
	}

	public void setNumAntiMissiles(int numAntiMissiles) {
		this.numAntiMissiles = numAntiMissiles;
	}

}
