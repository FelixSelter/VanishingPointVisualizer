package com.felixselter.main;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageLoader {

	private static ImageLoader instance;

	public BufferedImage load(String file) throws IOException {

		URL url = getClass().getResource("/"+file);
			return ImageIO.read(url);
	}

	public static ImageLoader getLoader() {
		if (instance == null)
			instance = new ImageLoader();
		return instance;
	}

}
