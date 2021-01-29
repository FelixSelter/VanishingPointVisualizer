package com.felixselter.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Frame extends JFrame implements KeyListener{

	private ContentPanel cp = new ContentPanel();

	public Frame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setExtendedState(MAXIMIZED_BOTH);
		setTitle("Vanishing point visualizer by Felix Selter");
		setContentPane(cp);
		addKeyListener(this);
		setVisible(true);
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				cp.updateGraphics();
			}
		});
		cp.updateGraphics();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					repaint();
					cp.updateGraphics();
				}				
			}
		}).start();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
