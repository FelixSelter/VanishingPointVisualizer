package com.felixselter.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class ToolPanel extends JPanel implements MouseListener {

	public DrawPanel dp;

	public boolean additionalInfo = true;
	public boolean snappingToPoint = false;
	public boolean drawLines = false;
	public boolean makePoint = false;
	public boolean makeLine = false;

	public CardPanel cardPanel;

	private BufferedImage checkMark;
	private BufferedImage magnet;
	private BufferedImage magnet_activated;
	private BufferedImage pointWithLines;
	private BufferedImage pointWithLines_activated;
	private BufferedImage point;
	private BufferedImage point_activated;
	private BufferedImage line;
	private BufferedImage line_activated;
	private BufferedImage undo;
	private BufferedImage redo;
	private BufferedImage instructions;
	private BufferedImage save;
	private BufferedImage open;
	private BufferedImage newProject;

	public ToolPanel() {
		addMouseListener(this);

		try {
			
			
			
			checkMark = ImageLoader.getLoader().load("checkmark.png");
			magnet = ImageLoader.getLoader().load("magnet.png");
			magnet_activated = ImageLoader.getLoader().load("magnetactivated.png");
			pointWithLines = ImageLoader.getLoader().load("pointwithlines.png");
			pointWithLines_activated = ImageLoader.getLoader().load("pointwithlinesactivated.png");
			point = ImageLoader.getLoader().load("point.png");
			point_activated = ImageLoader.getLoader().load("pointactivated.png");
			line = ImageLoader.getLoader().load("line.png");
			line_activated = ImageLoader.getLoader().load("lineactivated.png");

			undo = ImageLoader.getLoader().load("undo.png");
			redo = ImageLoader.getLoader().load("redo.png");
			instructions = ImageLoader.getLoader().load("instructions.png");
			save = ImageLoader.getLoader().load("save.png");
			open = ImageLoader.getLoader().load("open.png");
			newProject = ImageLoader.getLoader().load("new.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect((getWidth() / 10), (getWidth() / 10), getWidth() - (getWidth() / 5), getWidth() - (getWidth() / 5));
		g.fillRect((getWidth() / 10), getWidth(), getWidth() - (getWidth() / 5), getWidth() - (getWidth() / 5));
		g.fillRect((getWidth() / 10), getWidth() * 2 - (getWidth() / 10), getWidth() - (getWidth() / 5),
				getWidth() - (getWidth() / 5));
		g.fillRect((getWidth() / 10), getWidth() * 3 - (getWidth() / 5), getWidth() - (getWidth() / 5),
				getWidth() - (getWidth() / 5));
		g.fillRect((getWidth() / 10), getWidth() * 4 - (getWidth() / 10 * 3), getWidth() - (getWidth() / 5),
				getWidth() - (getWidth() / 5));
		g.fillRect((getWidth() / 10), getWidth() * 5 - (getWidth() / 10 * 4), getWidth() - (getWidth() / 5),
				getWidth() - (getWidth() / 5));
		g.fillRect((getWidth() / 10), getWidth() * 6 - (getWidth() / 10 * 5), getWidth() - (getWidth() / 5),
				getWidth() - (getWidth() / 5));
		g.fillRect((getWidth() / 10), getWidth() * 7 - (getWidth() / 10 * 6), getWidth() - (getWidth() / 5),
				getWidth() - (getWidth() / 5));
		g.fillRect((getWidth() / 10), getWidth() * 8 - (getWidth() / 10 * 7), getWidth() - (getWidth() / 5),
				getWidth() - (getWidth() / 5));
		g.fillRect((getWidth() / 10), getWidth() * 9 - (getWidth() / 10 * 8), getWidth() - (getWidth() / 5),
				getWidth() - (getWidth() / 5));
		g.fillRect((getWidth() / 10), getWidth() * 10 - (getWidth() / 10 * 9), getWidth() - (getWidth() / 5),
				getWidth() - (getWidth() / 5));

		if (additionalInfo) {
			g.drawImage(checkMark, (getWidth() / 10), (getWidth() / 10), getWidth() - (getWidth() / 5),
					getWidth() - (getWidth() / 5), null);
		}

		if (snappingToPoint) {
			g.drawImage(magnet_activated, (getWidth() / 10), getWidth(), getWidth() - (getWidth() / 5),
					getWidth() - (getWidth() / 5), null);
		} else {
			g.drawImage(magnet, (getWidth() / 10), getWidth(), getWidth() - (getWidth() / 5),
					getWidth() - (getWidth() / 5), null);
		}

		if (drawLines) {
			g.drawImage(pointWithLines_activated, (getWidth() / 10), getWidth() * 2 - (getWidth() / 10),
					getWidth() - (getWidth() / 5), getWidth() - (getWidth() / 5), null);
		} else {
			g.drawImage(pointWithLines, (getWidth() / 10), getWidth() * 2 - (getWidth() / 10),
					getWidth() - (getWidth() / 5), getWidth() - (getWidth() / 5), null);
		}

		if (makePoint) {
			g.drawImage(point_activated, (getWidth() / 10), getWidth() * 3 - (getWidth() / 5),
					getWidth() - (getWidth() / 5), getWidth() - (getWidth() / 5), null);
		} else {
			g.drawImage(point, (getWidth() / 10), getWidth() * 3 - (getWidth() / 5), getWidth() - (getWidth() / 5),
					getWidth() - (getWidth() / 5), null);
		}

		if (makeLine) {
			g.drawImage(line_activated, (getWidth() / 10), getWidth() * 4 - (getWidth() / 10 * 3),
					getWidth() - (getWidth() / 5), getWidth() - (getWidth() / 5), null);
		} else {
			g.drawImage(line, (getWidth() / 10), getWidth() * 4 - (getWidth() / 10 * 3), getWidth() - (getWidth() / 5),
					getWidth() - (getWidth() / 5), null);
		}

		g.drawImage(undo, (getWidth() / 10), getWidth() * 5 - (getWidth() / 10 * 4), getWidth() - (getWidth() / 5),
				getWidth() - (getWidth() / 5), null);
		g.drawImage(redo, (getWidth() / 10), getWidth() * 6 - (getWidth() / 10 * 5), getWidth() - (getWidth() / 5),
				getWidth() - (getWidth() / 5), null);
		g.drawImage(instructions, (getWidth() / 10), getWidth() * 7 - (getWidth() / 10 * 6),
				getWidth() - (getWidth() / 5), getWidth() - (getWidth() / 5), null);
		g.drawImage(save, (getWidth() / 10), getWidth() * 8 - (getWidth() / 10 * 7), getWidth() - (getWidth() / 5),
				getWidth() - (getWidth() / 5), null);
		g.drawImage(open, (getWidth() / 10), getWidth() * 9 - (getWidth() / 10 * 8), getWidth() - (getWidth() / 5),
				getWidth() - (getWidth() / 5), null);
		g.drawImage(newProject, (getWidth() / 10), getWidth() * 10 - (getWidth() / 10 * 9),
				getWidth() - (getWidth() / 5), getWidth() - (getWidth() / 5), null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (dp != null) {
			if (e.getX() > (getWidth() / 10) && e.getY() > (getWidth() / 10) && e.getX() < getWidth() - (getWidth() / 5)
					&& e.getY() < getWidth()) {
				additionalInfo = !additionalInfo;
			} else if (e.getX() > (getWidth() / 10) && e.getY() > getWidth() && e.getX() < getWidth() - (getWidth() / 5)
					&& e.getY() < (getWidth() - (getWidth() / 10)) * 2) {
				snappingToPoint = !snappingToPoint;
			} else if (e.getX() > (getWidth() / 10) && e.getY() > getWidth() * 2 - (getWidth() / 10)
					&& e.getX() < getWidth() - (getWidth() / 5) && e.getY() < (getWidth() - (getWidth() / 10)) * 3) {
				drawLines = !drawLines;
				makePoint = false;
				makeLine = false;
			} else if (e.getX() > (getWidth() / 10) && e.getY() > getWidth() * 3 - (getWidth() / 5)
					&& e.getX() < getWidth() - (getWidth() / 5) && e.getY() < (getWidth() - (getWidth() / 10)) * 4) {
				makePoint = !makePoint;
				drawLines = false;
				makeLine = false;
			} else if (e.getX() > (getWidth() / 10) && e.getY() > getWidth() * 4 - (getWidth() / 10 * 3)
					&& e.getX() < getWidth() - (getWidth() / 5) && e.getY() < (getWidth() - (getWidth() / 10)) * 5) {
				makeLine = !makeLine;
				drawLines = false;
				makePoint = false;

			} else if (e.getX() > (getWidth() / 10) && e.getY() > getWidth() * 5 - (getWidth() / 10 * 4)
					&& e.getX() < getWidth() - (getWidth() / 5) && e.getY() < (getWidth() - (getWidth() / 10)) * 6) {
				dp.undo();
			} else if (e.getX() > (getWidth() / 10) && e.getY() > getWidth() * 6 - (getWidth() / 10 * 5)
					&& e.getX() < getWidth() - (getWidth() / 5) && e.getY() < (getWidth() - (getWidth() / 10)) * 7) {
				dp.redo();
			} else if (e.getX() > (getWidth() / 10) && e.getY() > getWidth() * 7 - (getWidth() / 10 * 6)
					&& e.getX() < getWidth() - (getWidth() / 5) && e.getY() < (getWidth() - (getWidth() / 10)) * 8) {
				cardPanel.layout.show(cardPanel, "INSTRUCTIONS");
			} else if (e.getX() > (getWidth() / 10) && e.getY() > getWidth() * 8 - (getWidth() / 10 * 7)
					&& e.getX() < getWidth() - (getWidth() / 5) && e.getY() < (getWidth() - (getWidth() / 10)) * 9) {
				cardPanel.layout.show(cardPanel, "SAVE");
			}
		}
		if (e.getX() > (getWidth() / 10) && e.getY() > getWidth() * 9 - (getWidth() / 10 * 8)
				&& e.getX() < getWidth() - (getWidth() / 5)
				&& e.getY() < (getWidth() - (getWidth() / 10)) * (getWidth() / 10)) {
			open();
		} else if (e.getX() > (getWidth() / 10) && e.getY() > getWidth() * 10 - (getWidth() / 10 * 9)
				&& e.getX() < getWidth() - (getWidth() / 5) && e.getY() < (getWidth() - (getWidth() / 10)) * 11) {
			cardPanel.layout.show(cardPanel, "CREATE");
		}
	}

	private void open() {
		JFileChooser filechooser = new JFileChooser();
		int input = filechooser.showOpenDialog(null);
		if (input == JFileChooser.APPROVE_OPTION) {
			FileManager.load(filechooser.getSelectedFile(),this,cardPanel);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
