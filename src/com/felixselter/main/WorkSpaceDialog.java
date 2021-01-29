package com.felixselter.main;

import java.awt.Color;

import javax.swing.JPanel;

public class WorkSpaceDialog extends JPanel {

	public DrawPanel drawPanel;

	public WorkSpaceDialog(ToolPanel toolPanel, int width, int height) {
		setBackground(new Color(100, 151, 177));
		drawPanel = new DrawPanel(toolPanel, width, height);
		setLayout(null);
		add(drawPanel);
	}

	public void updateGraphics(int toolPanelWidth) {

		int workspaceWidth = drawPanel.getPaintingWidth() / drawPanel.getFactor();
		int workspaceHeight = drawPanel.getPaintingHeight() / drawPanel.getFactor();
		float factorWidth = (getWidth() - toolPanelWidth) / workspaceWidth;
		float factorHeight = getHeight() / workspaceHeight;
		float factor = factorWidth < factorHeight ? factorWidth : factorHeight;

		drawPanel.setBounds(
				toolPanelWidth + (int) (((getWidth() - toolPanelWidth) / 2) - (factor * workspaceWidth / 2)),
				(int) ((getHeight() / 2) - (factor * workspaceHeight / 2)), (int) (workspaceWidth * factor),
				(int) (workspaceHeight * factor));
	}

}
