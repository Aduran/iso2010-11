package com.umbrella.worldconq.ui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import com.umbrella.worldconq.WorldConqApp;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = -5107198177153703399L;

	private JToolBar mGameListToolBar = null;
	private JPanel mGameListPanel = null;

	private JToolBar mPlayToolBar = null;

	public MainWindow() {
		super();
		initGUI();
	}

	private void initGUI() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		try {
			this.setIconImage(new ImageIcon(ClassLoader
					.getSystemResource("image/logo.png")).getImage());
		} catch (Exception e) {
			System.out.println("Imagen no encontrada");
		}
		this.setResizable(false);
		this.setTitle("La Conquista del Mundo");
		this.setSize(800, 500);

		mGameListToolBar = new JToolBar();
		mPlayToolBar = new JToolBar();

		JButton updateListButton = new JButton("Actualizar lista");
		updateListButton.addMouseListener(new UpdateListMouseAdapter());
		mGameListToolBar.add(updateListButton);

		JButton createGameButton = new JButton("Crear partida");
		createGameButton.addMouseListener(new CreateGameMouseAdapter(this));
		mGameListToolBar.add(createGameButton);

		getContentPane().add(mGameListToolBar, BorderLayout.NORTH);
//		getContentPane().add(mPlayToolBar, BorderLayout.NORTH); FIXME Esto no funciona.
		getContentPane().add(getGameListPanel(), BorderLayout.CENTER);
		setGameListMode();
	}

	public void setGameListMode() {
		mGameListToolBar.setVisible(true);
		mPlayToolBar.setVisible(false);
		getGameListPanel().setVisible(true);
	}

	public void setPlayMode() {
		// TODO : Creo ya la función. A completar en próximas iteraciones.
		mGameListToolBar.setVisible(false);
		mPlayToolBar.setVisible(true);
		getGameListPanel().setVisible(false);
	}

	private JPanel getGameListPanel() {
		if (mGameListPanel == null) {
			mGameListPanel = new JPanel();
			mGameListPanel.setLayout(new BoxLayout(mGameListPanel, BoxLayout.Y_AXIS));
			JTable currentList = new JTable(WorldConqApp.getGameManager().getCurrentGameListModel());
			JTable openList = new JTable(WorldConqApp.getGameManager().getCurrentGameListModel());
			currentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			openList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			mGameListPanel.add(new JLabel("Mis partidas actuales"));
			mGameListPanel.add(currentList);
			mGameListPanel.add(new JLabel("Partidas disponibles"));
			mGameListPanel.add(openList);
		}
		return mGameListPanel;
	}

	private class CreateGameMouseAdapter extends MouseAdapter {

		private MainWindow mw;

		public CreateGameMouseAdapter(MainWindow mw) {
			this.mw = mw;
		}

		public void mouseClicked(MouseEvent evt) {
			JFrame f = new JFrame();
			CreateGameDialog dlg = new CreateGameDialog(f,
					"La Conquista del Mundo - Nueva partida", true);
			dlg.setLocationRelativeTo(null);
			dlg.setVisible(true);

			if (dlg.getSelection() == true) {
				try {
					// /* mw.Manager.registerUser(dlg.getUser(),
					// dlg.getPasswd(), dlg.getEmail());
					// mw.NoticeLabel.setText("Usuario :" +
					// dlg.getUser()+" registrado");
					// NoticeLabel.setForeground(new Color (0, 200, 0));*/
				} catch (Exception e) {
					// stw.NoticeLabel.setText(" Error en el registro");
					// NoticeLabel.setForeground(new Color (255, 0, 0));
				}
			}

			mw.setVisible(true);
			f.dispose();
		}
	}

	private class UpdateListMouseAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent evt) {
			try {
				WorldConqApp.getGameManager().updateGameList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
