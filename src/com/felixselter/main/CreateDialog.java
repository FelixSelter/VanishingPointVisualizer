package com.felixselter.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;

public class CreateDialog extends JPanel {

	private JLabel generate1 = new JLabel("Width:", SwingConstants.CENTER);
	private JLabel generate2 = new JLabel("Height:", SwingConstants.CENTER);
	private JTextField width = new JTextField("2970");
	private JTextField height = new JTextField("2100");
	private JButton button = new JButton("Create!");

	public CreateDialog(CardPanel cardPanel,ToolPanel toolPanel,ContentPanel contentPanel) {
		
		((PlainDocument) width.getDocument()).setDocumentFilter(new OnlyIntegerFilter());
		((PlainDocument) height.getDocument()).setDocumentFilter(new OnlyIntegerFilter());
		width.setHorizontalAlignment(JTextField.CENTER);
		height.setHorizontalAlignment(JTextField.CENTER);

			setLayout(new BorderLayout());
			setBackground(new Color(100, 151, 177));

			JPanel btnPanel = new JPanel();
			btnPanel.setLayout(new GridLayout(0, 1));

			JPanel p1 = new JPanel();
			p1.setLayout(new GridLayout());
			p1.add(generate1);
			p1.add(width);

			JPanel p2 = new JPanel();
			p2.setLayout(new GridLayout());
			p2.add(generate2);
			p2.add(height);

			JPanel p3 = new JPanel();
			p3.setLayout(new BorderLayout());
			p3.setBorder(new EmptyBorder(10, 10, 10, 10));
			p3.add(button);

			btnPanel.add(p1);
			btnPanel.add(p2);
			btnPanel.add(p3);
			add(btnPanel, BorderLayout.CENTER);
			
			button.addActionListener(e -> {
				WorkSpaceDialog workspace = new WorkSpaceDialog(toolPanel, Integer.parseInt(width.getText()), Integer.parseInt(height.getText()));
				cardPanel.workspaces.add(workspace);
				cardPanel.open=workspace;
				cardPanel.add(workspace, "WORKSPACE" + cardPanel.workspaces.indexOf(workspace));
				cardPanel.layout.show(cardPanel, "WORKSPACE" + cardPanel.workspaces.indexOf(workspace));
				contentPanel.updateGraphics();
			});

	}

	public void updateGraphics() {
		setBorder(new EmptyBorder(getHeight() / 10, getWidth() / 3, getHeight() / 10, getWidth() / 3));
		
		
		int width = getWidth() - ((getWidth() / 3) * 2);
		int height = getHeight() - ((getHeight() / 10) * 2);
		int labelWidth = width / 2;
		int labelHeight = height / 3;

		int size = labelWidth;
		int stringWidth, stringHeight;

		do {
			size--;
			stringWidth = generate1.getFontMetrics(new Font("", 0, size)).stringWidth(generate1.getText());
		} while (stringWidth > labelWidth);

		do {
			size--;
			stringHeight = (int) generate1.getFontMetrics(new Font("", 0, size))
					.getStringBounds(generate1.getText(), generate1.getGraphics()).getHeight();
		} while (stringHeight > labelHeight);
		generate1.setFont(new Font("", 0, size));

		size = labelWidth;
		do {
			size--;
			stringWidth = generate2.getFontMetrics(new Font("", 0, size)).stringWidth(generate2.getText());
		} while (stringWidth > labelWidth);

		do {
			size--;
			stringHeight = (int) generate2.getFontMetrics(new Font("", 0, size))
					.getStringBounds(generate2.getText(), generate2.getGraphics()).getHeight();
		} while (stringHeight > labelHeight);
		generate2.setFont(new Font("", 0, size));
		
		size=labelWidth;
		do {
			size--;
			stringWidth = button.getFontMetrics(new Font("", 0, size)).stringWidth(button.getText());
		} while (stringWidth > labelWidth);

		do {
			size--;
			stringHeight = (int) button.getFontMetrics(new Font("", 0, size))
					.getStringBounds(button.getText(), button.getGraphics()).getHeight();
		} while (stringHeight > labelHeight);
		button.setFont(new Font("", 0, size));
		
		size = labelWidth;
		do {
			size--;
			stringWidth = this.width.getFontMetrics(new Font("", 0, size)).stringWidth(this.width.getText());
		} while (stringWidth > labelWidth);

		do {
			size--;
			stringHeight = (int) this.width.getFontMetrics(new Font("", 0, size))
					.getStringBounds(this.width.getText(), this.width.getGraphics()).getHeight();
		} while (stringHeight > labelHeight);
		this.width.setFont(new Font("", 0, size));
		
		size = labelWidth;
		do {
			size--;
			stringWidth = this.height.getFontMetrics(new Font("", 0, size)).stringWidth(this.height.getText());
		} while (stringWidth > labelWidth);

		do {
			size--;
			stringHeight = (int) this.height.getFontMetrics(new Font("", 0, size))
					.getStringBounds(this.height.getText(), this.height.getGraphics()).getHeight();
		} while (stringHeight > labelHeight);
		this.height.setFont(new Font("", 0, size));
		
	}



}
