package es.uclm.iso2.rmi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class GameInfo implements Serializable {

	private static final long serialVersionUID = 2499820007479700300L;
	private UUID id;
	private String name;
	private String description;
	private ArrayList<String> players;
	private ArrayList<Calendar> gameSessions;
	private int nFreeTerritories;
	private int turnTime;
	private int defenseTime;
	private int negotiationTime;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<String> players) {
		this.players = players;
	}

	public ArrayList<Calendar> getGameSessions() {
		return gameSessions;
	}

	public void setGameSessions(ArrayList<Calendar> gameSessions) {
		this.gameSessions = gameSessions;
	}

	public int getnFreeTerritories() {
		return nFreeTerritories;
	}

	public void setnFreeTerritories(int nFreeTerritories) {
		this.nFreeTerritories = nFreeTerritories;
	}

	public int getTurnTime() {
		return turnTime;
	}

	public void setTurnTime(int turnTime) {
		this.turnTime = turnTime;
	}

	public int getDefenseTime() {
		return defenseTime;
	}

	public void setDefenseTime(int defenseTime) {
		this.defenseTime = defenseTime;
	}

	public int getNegotiationTime() {
		return negotiationTime;
	}

	public void setNegotiationTime(int negotiationTime) {
		this.negotiationTime = negotiationTime;
	}

}
