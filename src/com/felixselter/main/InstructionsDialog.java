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
import java.io.FileWriter;
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
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;

import com.felixselter.datatypes.Line;

public class InstructionsDialog extends JPanel {

	private SimpleCheckboxStyle style = new SimpleCheckboxStyle(10);
	private JLabel generate1 = new JLabel("1mm in Pixel:", SwingConstants.CENTER);
	private JLabel generate2 = new JLabel("Generate Vanishing points:", SwingConstants.CENTER);
	private JTextField option1 = new JTextField("10");
	private JCheckBox option2 = new JCheckBox(style);
	private JButton button = new JButton("Generate!");

	public InstructionsDialog(CardPanel cp) {

		setLayout(new BorderLayout());
		setBackground(new Color(100, 151, 177));
		
		((PlainDocument) option1.getDocument()).setDocumentFilter(new OnlyIntegerFilter());
		option1.setHorizontalAlignment(JTextField.CENTER);

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new GridLayout(0, 1));

		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout());
		p1.add(generate1);
		p1.add(option1);

		JPanel p2 = new JPanel();
		p2.setLayout(new GridLayout());
		p2.add(generate2);
		p2.add(option2);

		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout());
		p3.setBorder(new EmptyBorder(10, 10, 10, 10));
		p3.add(button);

		btnPanel.add(p1);
		btnPanel.add(p2);
		btnPanel.add(p3);
		add(btnPanel, BorderLayout.CENTER);

		button.addActionListener(e -> {

			int mmInPixel = Integer.parseInt(option1.getText());
			JFileChooser filechooser = new JFileChooser();
			int input = filechooser.showSaveDialog(null);
			if (input == JFileChooser.APPROVE_OPTION) {
				File file = filechooser.getSelectedFile();
				if (file.getName().endsWith(".txt"))
					file = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 4));
				int i = 1;
				File tocreate = new File(file.getAbsolutePath() + ".txt");
				while (tocreate.exists()) {
					i++;
					tocreate = new File(file.getAbsolutePath() + " V(" + String.valueOf(i) + ").txt");
				}
				try {
					FileWriter writer = new FileWriter(tocreate);
					DrawPanel dp = cp.open.drawPanel;
					int width = dp.getPaintingWidth();
					int height = dp.getPaintingHeight();
					

					if (option2.isSelected()) {

						writer.write("Draw horizon at " + Math.round((height - height * dp.horizon) / mmInPixel) + "mm\n");

						writer.write("Draw VanishingPoint 1 at " + Math.round((width * dp.vanishingpoint1) / mmInPixel) + "mm|"
								+ Math.round((height - height * dp.horizon) / mmInPixel) + "mm\n");

						writer.write("Draw VanishingPoint 2 at " + Math.round((width * dp.vanishingpoint2) / mmInPixel) + "mm|"
								+ Math.round((height - height * dp.horizon) / mmInPixel) + "mm\n\n");
					}

					for (Line line : dp.lines) {
						if (line.getP2() != null)
							writer.write("Draw a line beetween " + Math.round((line.getP1().x * width) / mmInPixel) + "mm|"
									+ Math.round((height-line.getP1().y * height) / mmInPixel) + "mm and "
									+ Math.round((line.getP2().x * width) / mmInPixel) + "mm|"
									+ Math.round((height-line.getP2().y * height) / mmInPixel) + "mm\n");
					}

					writer.flush();
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
			cp.layout.show(cp, "WORKSPACE" + String.valueOf(cp.workspaces.indexOf(cp.open)));
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

		size = labelWidth;
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

		style.setSize(labelWidth > labelHeight ? labelHeight / 5 : labelWidth / 5);
		
		size = labelWidth;
		do {
			size--;
			stringWidth = option1.getFontMetrics(new Font("", 0, size)).stringWidth(option1.getText());
		} while (stringWidth > labelWidth);

		do {
			size--;
			stringHeight = (int) option1.getFontMetrics(new Font("", 0, size))
					.getStringBounds(option1.getText(), option1.getGraphics()).getHeight();
		} while (stringHeight > labelHeight);
		option1.setFont(new Font("", 0, size));
	}

}
