package com.felixselter.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ChoosePanel extends JPanel implements MouseListener {

	public JScrollPane parent;
	private CardPanel cardPanel;
	private ContentPanel contentPanel;
	private int width;

	public ChoosePanel(CardPanel cardPanel, ContentPanel contentPanel) {
		this.cardPanel = cardPanel;
		this.contentPanel = contentPanel;
		addMouseListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {

		if (getHeight() != 100)
			contentPanel.offset = 100 - getHeight();

		width = getWidth();

		g.setColor(new Color(69, 30, 62));
		g.fillRect(0, 0, getWidth(), getHeight());

		int offset = 10;
		for (WorkSpaceDialog workspace : cardPanel.workspaces) {
			DrawPanel drawPanel = workspace.drawPanel;

			int workspaceWidth = drawPanel.getPaintingWidth() / drawPanel.getFactor();
			int workspaceHeight = drawPanel.getPaintingHeight() / drawPanel.getFactor();
			float factor = 100f / workspaceHeight;

			g.drawImage(drawPanel.getDrawing((int) (workspaceWidth * factor), 100), offset, 0,
					(int) (workspaceWidth * factor), 100, null);

			offset += (workspaceWidth * factor) + 20;
			if (offset - 10 > width)
				width = offset - 10;
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, getHeight());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int offset = 10;
		for (WorkSpaceDialog workspace : cardPanel.workspaces) {
			DrawPanel drawPanel = workspace.drawPanel;

			int workspaceWidth = drawPanel.getPaintingWidth() / drawPanel.getFactor();
			int workspaceHeight = drawPanel.getPaintingHeight() / drawPanel.getFactor();
			float factor = 100f / workspaceHeight;
			
			if(new Rectangle(offset, 0, (int) (workspaceWidth * factor), 100).contains(e.getPoint())) {
				cardPanel.layout.show(cardPanel, "WORKSPACE"+String.valueOf(cardPanel.workspaces.indexOf(workspace)));
				break;
			}


			offset += (workspaceWidth * factor) + 20;
			if (offset - 10 > width)
				width = offset - 10;
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
