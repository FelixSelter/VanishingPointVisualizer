package com.felixselter.main;

import java.awt.CardLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ContentPanel extends JPanel {

	private ToolPanel toolPanel = new ToolPanel();
	private CardPanel cardpanel;
	private ChoosePanel choosePanel;
	private JScrollPane scrollableChoosePanel;
	public int offset = 0;

	public ContentPanel() {
		cardpanel = new CardPanel(toolPanel, this);
		choosePanel = new ChoosePanel(cardpanel, this);
		scrollableChoosePanel = new JScrollPane(choosePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		choosePanel.parent = scrollableChoosePanel;
		setLayout(null);
		add(toolPanel);
		add(scrollableChoosePanel);
		add(cardpanel);

	}

	public void updateGraphics() {

		int toolPanelWidth = (int) (100 * (getHeight() / 1000f));
		int choosePanelHeight = 100 + offset;

		toolPanel.setBounds(0, 0, toolPanelWidth, getHeight());
		scrollableChoosePanel.setBounds(toolPanelWidth, 0, getWidth() - toolPanelWidth, choosePanelHeight);
		cardpanel.setBounds(toolPanelWidth, 100+offset, getWidth() - toolPanelWidth, getHeight() - choosePanelHeight);
		cardpanel.updateGraphics(toolPanelWidth);
		scrollableChoosePanel.revalidate();

	}

}

class CardPanel extends JPanel {

	public ContentPanel cp;
	private String current = "";
	public CardLayout layout = new CardLayout() {

		public void show(java.awt.Container parent, String name) {
			current = name;
			if (name.contains("WORKSPACE")) {
				open = workspaces.get(Integer.parseInt(name.replace("WORKSPACE", "")));
			}
			super.show(parent, name);
		};

	};
	public ArrayList<WorkSpaceDialog> workspaces = new ArrayList<WorkSpaceDialog>();
	public WorkSpaceDialog open;

	private CreateDialog create;
	private InstructionsDialog instructions = new InstructionsDialog(this);
	private SaveDialog save = new SaveDialog(this);

	public CardPanel(ToolPanel toolPanel, ContentPanel cp) {
		this.cp = cp;
		toolPanel.cardPanel = this;
		create = new CreateDialog(this, toolPanel, cp);

		setLayout(layout);
		add(create, "CREATE");
		add(instructions, "INSTRUCTIONS");
		add(save, "SAVE");
		layout.show(this, "CREATE");
	}

	public void updateGraphics(int toolPanelWidth) {

		switch (current) {
		case "CREATE": {
			create.updateGraphics();
			break;
		}
		case "INSTRUCTIONS": {
			instructions.updateGraphics();
			break;
		}
		case "SAVE": {
			save.updateGraphics();
			break;
		}
		default:
			open.updateGraphics(toolPanelWidth);
		}

		revalidate();

	}

}
