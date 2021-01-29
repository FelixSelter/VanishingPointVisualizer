package com.felixselter.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;

public class SimpleCheckboxStyle implements Icon {

	private int size;

	public SimpleCheckboxStyle(int size) {
		this.size = size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	protected int getDimension() {
		return size;
	}

	public void paintIcon(Component component, Graphics g, int x, int y) {

		ButtonModel buttonModel = ((AbstractButton) component).getModel();

		int y_offset = (int) (component.getSize().getHeight() / 2) - (int) (getDimension() / 2);
		int x_offset = (int) (component.getSize().getWidth() / 2) - (int) (getDimension() / 2);

		if (buttonModel.isRollover()) {
			g.setColor(new Color(0, 60, 120));
		} else if (buttonModel.isRollover()) {
			g.setColor(Color.BLACK);
		} else {
			g.setColor(Color.DARK_GRAY);
		}
		g.fillRect(x_offset, y_offset, size, size);
		if (buttonModel.isPressed()) {
			g.setColor(Color.GRAY);
		} else if (buttonModel.isRollover()) {
			g.setColor(new Color(240, 240, 250));
		} else {
			g.setColor(Color.WHITE);
		}
		g.fillRect(1 + x_offset, y_offset + 1, size - 2, size - 2);
		if (buttonModel.isSelected()) {
			int r_x = 1;
			g.setColor(Color.GRAY);
			g.fillRect(x_offset + r_x + 3, y_offset + 3 + r_x, size - (7 + r_x), size - (7 + r_x));
		}
	}

	public int getIconWidth() {
		return getDimension();
	}

	public int getIconHeight() {
		return getDimension();
	}

}
