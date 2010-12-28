package com.umbrella.worldconq.stubserver;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import com.umbrella.worldconq.domain.Session;

import es.uclm.iso2.rmi.Arsenal;
import es.uclm.iso2.rmi.EventType;
import es.uclm.iso2.rmi.Game;
import es.uclm.iso2.rmi.GameInfo;
import es.uclm.iso2.rmi.IServer;
import es.uclm.iso2.rmi.Player;
import es.uclm.iso2.rmi.Territory;
import es.uclm.iso2.rmi.exceptions.GameNotFoundException;
import es.uclm.iso2.rmi.exceptions.InvalidGameInfoException;
import es.uclm.iso2.rmi.exceptions.InvalidSessionException;
import es.uclm.iso2.rmi.exceptions.InvalidTerritoryException;
import es.uclm.iso2.rmi.exceptions.InvalidTimeException;
import es.uclm.iso2.rmi.exceptions.NotCurrentPlayerGameException;
import es.uclm.iso2.rmi.exceptions.UserAlreadyExistsException;
import es.uclm.iso2.rmi.exceptions.WrongLoginException;

public class Server extends UnicastRemoteObject implements IServer {

	private static final long serialVersionUID = 8434201731175738674L;
	private final int puerto = 3234;
	private final String miIP;
	private final Registry reg;

	private final String[][] Users = {
			{
					"JorgeCA", "jorge", "jorge.colao@gmail.com"
			},
			{
					"ricki", "ricki", "ricardo.ruedas@gmail.com"
			},
			{
					"pobleteag", "antonio", "pobleteag@gmail.com"
			},
			{
					"DaniLR", "daniel", "daniel.leonromero@gmail.com"
			},
			{
					"Aduran", "angel", "anduraniz@gmail.com"
			},
			{
					"LauraN", "laura", "arualitan@gmail.com"
			},
			{
					"deejaytoni", "toni", "deejaytoni@gmail.com"
			}
	};

	private final ArrayList<String[]> registerUsers;

	private final ArrayList<GameInfo> gameList;

	private final ArrayList<Session> sessionsList;

	public Server() throws Exception, RemoteException {
		super();

		System.setProperty("java.security.policy",
			ClassLoader.getSystemResource("data/open.policy").toString());

		gameList = new ArrayList<GameInfo>();
		registerUsers = new ArrayList<String[]>();
		sessionsList = new ArrayList<Session>();

		for (final String[] user : Users)
			registerUsers.add(user);

		{ // GameInfo 01
			final ArrayList<String> player = new ArrayList<String>();
			// new Player("JorgeCA", 1000, true, true, new ArrayList<Spy>());
			// new Player("Aduran", 1000, true, true, new ArrayList<Spy>());
			player.add("JorgeCA");
			player.add("Aduran");
			final ArrayList<Calendar> session = new ArrayList<Calendar>();
			session.add(Calendar.getInstance());
			gameList.add(new GameInfo(UUID.randomUUID(), "game01",
				"desc from game01", player, session, 3, 0, 0, 0));
		}
		{ // GameInfo 02
			final ArrayList<String> player = new ArrayList<String>();
			// new Player("ricki", 1000, true, true, new ArrayList<Spy>());
			// new Player("DaniLR", 1000, true, false, new ArrayList<Spy>());
			// new Player("deejaytoni", 1000, true, false, new ArrayList<Spy>());
			player.add("ricki");
			player.add("DaniLR");
			player.add("deejaytoni");
			final ArrayList<Calendar> session = new ArrayList<Calendar>();
			session.add(Calendar.getInstance());
			gameList.add(new GameInfo(UUID.randomUUID(), "game02",
				"desc from game02", player, session, 6, 0, 0, 0));
		}
		{ // GameInfo 03
			final ArrayList<String> player = new ArrayList<String>();
			// new Player("pobleteag", 1000, true, true, new ArrayList<Spy>());
			// new Player("LauraN", 1000, true, false, new ArrayList<Spy>());
			player.add("pobleteag");
			player.add("LauraN");
			final ArrayList<Calendar> session = new ArrayList<Calendar>();
			session.add(Calendar.getInstance());
			gameList.add(new GameInfo(UUID.randomUUID(), "game03",
				"desc from game03", player, session, 6, 0, 0, 0));
		}

		miIP = (InetAddress.getLocalHost()).toString();
		System.out.println("Conexion establecida por:");
		System.out.println("IP=" + miIP + ", y puerto=" + puerto);

		reg = LocateRegistry.createRegistry(puerto);
		reg.rebind("WorldConqStubServer", this);

		System.out.println("Esperando peticiones...");
	}

	public static void main(String[] args) throws Exception {
		new Server();
	}

	@Override
	public void registerUser(String name, String password, String email) throws RemoteException, UserAlreadyExistsException {
		System.out.println("IServer::registerUser " + name);

		final String[] user = {
				name, password, email
		};

		for (int i = 0; i < registerUsers.size(); i++) {
			if (registerUsers.get(i)[0].compareTo(name) == 0)
				throw new UserAlreadyExistsException();
		}
		registerUsers.add(user);
	}

	@Override
	public UUID loginUser(String name, String password, Remote callback) throws RemoteException, WrongLoginException {
		System.out.println("IServer::loginUser " + name);
		boolean encontrado = false;
		UUID id = null;
		for (int i = 0; i < registerUsers.size() && encontrado == false; i++) {
			if (registerUsers.get(i)[0].compareTo(name) == 0
					&& registerUsers.get(i)[1].compareTo(password) == 0) {
				encontrado = true;
				id = UUID.randomUUID();
				final Session session = new Session(id, name);
				sessionsList.add(session);
			}
		}
		if (encontrado == false) throw new WrongLoginException();

		return id;
	}

	@Override
	public void logoutUser(UUID session) throws RemoteException, InvalidSessionException {
		System.out.println("IServer::logoutUser " + session);
	}

	@Override
	public ArrayList<GameInfo> listGames() throws RemoteException {
		System.out.println("IServer::listGames");
		return gameList;
	}

	@Override
	public UUID createGame(GameInfo info) throws RemoteException, InvalidGameInfoException {
		System.out.println("IServer::createGame");
		System.out.println("Nombre de la partida: " + info.getName());
		System.out.println("Descripción de la partida: "
				+ info.getDescription());
		for (final Calendar cal : info.getGameSessions()) {
			System.out.println("Sesión: " + cal.getTime());
		}
		return UUID.randomUUID();
	}

	@Override
	public void joinGame(UUID session, UUID partida) throws RemoteException, GameNotFoundException, InvalidSessionException {
		boolean foundGame = false;
		boolean foundSession = false;
		//compruebo que la sesion se encuentra dentro de la lista de sesiones activas.
		for (int i = 0; i < sessionsList.size() && foundSession == false; i++) {
			if (sessionsList.get(i).getId().compareTo(session) == 0) {
				foundSession = true;
				//La sesion existe, ahora compruebo que la partida existe.
				for (int j = 0; j < gameList.size() && foundGame == false; j++) {
					if (gameList.get(j).getId().compareTo(partida) == 0) {
						gameList.get(j).getPlayers().add(
							sessionsList.get(i).getUser());
						foundGame = true;
					}
				}
			}
		}
		if (foundSession == false) {
			throw new InvalidSessionException();
		} else if (foundGame == false) {
			throw new GameNotFoundException();
		}
	}

	@Override
	public void resignGame(UUID session, UUID partida) throws RemoteException, GameNotFoundException, InvalidSessionException {
		System.out.println("IServer::resignGame");
	}

	@Override
	public Game playGame(UUID session, UUID game) throws RemoteException, GameNotFoundException, InvalidSessionException, InvalidTimeException {
		System.out.println("IServer::playGame");
		return null;
	}

	@Override
	public void quitGame(UUID session, UUID game) throws RemoteException, GameNotFoundException, InvalidSessionException {
		System.out.println("IServer::quitGame");
	}

	@Override
	public void updateGame(UUID session, UUID game, ArrayList<Player> playerUpdate, ArrayList<Territory> territoryUpdate, EventType event) throws RemoteException, GameNotFoundException, InvalidSessionException, NotCurrentPlayerGameException {
		System.out.println("IServer::updateGame");
	}

	@Override
	public void attackTerritory(UUID session, UUID game, Territory src, Territory dst, Arsenal arsenal) throws RemoteException, GameNotFoundException, InvalidSessionException, InvalidTerritoryException {
		System.out.println("IServer::attackTerritory");
	}

	@Override
	public void acceptAttack(UUID idSession, UUID idPartida) throws RemoteException, GameNotFoundException, InvalidSessionException {
		System.out.println("IServer::acceptAttack");
	}

	@Override
	public void requestedNegotiation(UUID session, UUID game, int money, int soldiers) throws RemoteException, GameNotFoundException, InvalidSessionException {
		System.out.println("IServer::requestedNegotiation");
	}

	@Override
	public void endTurn(UUID session, UUID game) throws Exception, RemoteException {
		System.out.println("IServer::updateGame");
	}

}
