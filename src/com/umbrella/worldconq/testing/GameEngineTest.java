package com.umbrella.worldconq.testing;

import java.net.InetAddress;
import java.net.URL;
import java.net.URLClassLoader;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

import com.umbrella.worldconq.comm.ClientAdapter;
import com.umbrella.worldconq.comm.ServerAdapter;
import com.umbrella.worldconq.domain.GameEngine;
import com.umbrella.worldconq.domain.GameManager;
import com.umbrella.worldconq.domain.UserManager;
import com.umbrella.worldconq.exceptions.InvalidArgumentException;
import com.umbrella.worldconq.exceptions.PendingAttackException;

public class GameEngineTest extends TestCase {

	Process ServerProcess;
	private ServerAdapter srvAdapter;
	private ClientAdapter cltAdapter;
	private GameManager gameMgr;
	private UserManager usrMgr;
	private GameEngine gameEngine;

	@Override
	@Before
	public void setUp() throws Exception {
		System.out.println("TestCase::setUp");
		final String comand = "java -cp " + this.getClasspath()
				+ " com.umbrella.worldconq.stubserver.Server";

		try {
			ServerProcess = Runtime.getRuntime().exec(comand);
			Thread.sleep(1000);
		} catch (final Exception e) {
			fail(e.toString());
		}

		try {
			System.setProperty("java.security.policy",
				ClassLoader.getSystemResource("data/open.policy").toString());

			cltAdapter = new ClientAdapter();

			srvAdapter = new ServerAdapter();
			srvAdapter.setRemoteInfo(
				"WorldConqStubServer",
				InetAddress.getByName("localhost"),
				3234);
			srvAdapter.connect();

			gameMgr = new GameManager(srvAdapter, cltAdapter);
			usrMgr = new UserManager(srvAdapter, gameMgr);
			gameMgr.setUserManager(usrMgr);
			usrMgr.createSession("JorgeCA", "jorge");

			//Conectar a la partida
			assertTrue(gameMgr.getCurrentGameListModel().getRowCount() == 0);
			assertTrue(gameMgr.getOpenGameListModel().getRowCount() == 0);
			gameMgr.updateGameList();
			assertTrue(gameMgr.getCurrentGameListModel() != null);
			assertTrue(gameMgr.getOpenGameListModel() != null);
			assertTrue(gameMgr.getGameEngine() == null);
			gameMgr.connectToGame(0, null);
			assertTrue(gameMgr.getGameEngine() != null);

		} catch (final Exception e) {
			fail(e.toString());
		}
	}

	public void testAttackTerritory1() {
		System.out.println("TestCase::testAttackTerritory1");
		try {
			gameEngine = gameMgr.getGameEngine();
			Object o = PrivateAccessor.getPrivateField(gameEngine,
				"mCurrentAttack");
			assertNull(o);
			//Territorio 0 de jorge
			assertEquals(
				gameEngine.getMapListModel().getTerritoryAt(0).getPlayer().getName(),
				"JorgeCA");
			//Territorio 2 de angel
			assertEquals(
				gameEngine.getMapListModel().getTerritoryAt(2).getPlayer().getName(),
				"Aduran");
			//Territorio 2 no es de jorge
			assertTrue(!gameEngine.getMapListModel().getTerritoryAt(2).getPlayer().getName().equals(
				"JorgeCA"));
			//El territorio 2 es adyacente al territorio 0
			assertTrue(gameEngine.getMapListModel().getTerritoryAt(0).getAdjacentTerritories().contains(
				gameEngine.getMapListModel().getTerritoryAt(2)));
			//attackTerritory(src, dst, soldiers, cannons, missiles, icbm)
			gameEngine.attackTerritory(0, 2, 1, 1, 1, 1);
			System.out.println("TestCase::4");
			o = PrivateAccessor.getPrivateField(gameEngine, "mCurrentAttack");
			assertNotNull(o);
		} catch (final PendingAttackException e) {
			System.out.println("PendingAttackException");
		} catch (final InvalidArgumentException e) {
			System.out.println("InvalidArgumentException");
		} catch (final Exception e) {
			fail(e.toString());
		}
	}

	//Terminado AcceptAttack

	public void testAcceptAttack1() {
		System.out.println("TestCase::testAcceptAttack1");
		try {
			gameEngine = gameMgr.getGameEngine();
			Object o = PrivateAccessor.getPrivateField(gameEngine,
				"mCurrentAttack");
			assertNull(o);
			//Hacer el ataque
			gameEngine.attackTerritory(0, 2, 1, 1, 1, 1);
			o = PrivateAccessor.getPrivateField(gameEngine,
				"mCurrentAttack");
			assertNotNull(o);
			//Aceptar el ataque
			gameEngine.acceptAttack();
			o = PrivateAccessor.getPrivateField(gameEngine, "mCurrentAttack");
			assertNull(o);
		} catch (final Exception e) {
			fail(e.toString());
		}
	}

	public void testAcceptAttack2() {
		System.out.println("TestCase::testAcceptAttack2");
		try {
			gameEngine = gameMgr.getGameEngine();
			final Object o = PrivateAccessor.getPrivateField(gameEngine,
				"mCurrentAttack");
			assertNull(o);
			//Aceptar el ataque
			gameEngine.acceptAttack();
			fail("Exception");
		} catch (final Exception e) {
			System.out.println(e.toString());
		}
	}

	// Faltan con los parámetos

	public void testRequestNegotiation1() {
		System.out.println("TestCase::requestNegotiation1");
		try {
			gameEngine = gameMgr.getGameEngine();
			Object o = PrivateAccessor.getPrivateField(gameEngine,
				"mCurrentAttack");
			assertNull(o);
			//Hacer el ataque
			gameEngine.attackTerritory(0, 2, 1, 1, 1, 1);
			o = PrivateAccessor.getPrivateField(gameEngine,
				"mCurrentAttack");
			assertNotNull(o);
			//Pedir Negociacion
			gameEngine.requestNegotiation(100, 4);
			o = PrivateAccessor.getPrivateField(gameEngine, "mCurrentAttack");
			assertNull(o);
		} catch (final Exception e) {
			fail(e.toString());
		}
	}

	public void testRequestNegotiation2() {
		System.out.println("TestCase::testRequestNegotiation2");
		try {
			gameEngine = gameMgr.getGameEngine();
			final Object o = PrivateAccessor.getPrivateField(gameEngine,
				"mCurrentAttack");
			assertNull(o);
			//Pedir Negociacion
			gameEngine.requestNegotiation(100, 4);
			fail("Exception");
		} catch (final Exception e) {
			System.out.println(e.toString());
		}
	}

	public void testResolveAttack1() {
		System.out.println("TestCase::testResolveAttack1");
		try {
			gameEngine = gameMgr.getGameEngine();
			Object o = PrivateAccessor.getPrivateField(gameEngine,
				"mCurrentAttack");
			assertNull(o);
			//Hacer el ataque
			gameEngine.attackTerritory(0, 2, 1, 1, 1, 1);
			o = PrivateAccessor.getPrivateField(gameEngine,
				"mCurrentAttack");
			assertNotNull(o);
			//Resolver el ataque
			gameEngine.resolveAttack();
			o = PrivateAccessor.getPrivateField(gameEngine, "mCurrentAttack");
			assertNull(o);
		} catch (final InvalidArgumentException e) {
			fail("InvalidArgumentException");
		} catch (final Exception e) {
			fail(e.toString());
		}
	}

	@Override
	@After
	public void tearDown() throws Exception {
		System.out.println("TestCase::tearDown");
		ServerProcess.destroy();
		try {
			ServerProcess.destroy();
			ServerProcess.waitFor();
			srvAdapter.disconnect();
		} catch (final Exception e) {
		}
	}

	public String getClasspath() {
		final ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
		final URL[] urls = ((URLClassLoader) sysClassLoader).getURLs();
		return urls[0].getFile();
	}
}
