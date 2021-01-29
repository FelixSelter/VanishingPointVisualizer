package com.felixselter.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import com.felixselter.datatypes.Line;
import com.felixselter.datatypes.PercentagePoint;

public class FileManager {

	public static void save(DrawPanel dp, File output) {

		Properties properties = new Properties();
		properties.setProperty("width", String.valueOf(dp.painting.getWidth()));
		properties.setProperty("height", String.valueOf(dp.painting.getHeight()));
		properties.setProperty("horizon", String.valueOf(dp.horizon));
		properties.setProperty("vanishingpoint1", String.valueOf(dp.vanishingpoint1));
		properties.setProperty("vanishingpoint2", String.valueOf(dp.vanishingpoint2));
		properties.setProperty("pointsWithLines", String.valueOf(Arrays.toString(dp.pointsWithLines.toArray())));
		properties.setProperty("points", String.valueOf(Arrays.toString(dp.points.toArray())));
		properties.setProperty("lines", String.valueOf(Arrays.toString(dp.lines.toArray())));

		try {
			properties.storeToXML(new FileOutputStream(output), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void load(File file, ToolPanel toolPanel, CardPanel cardPanel) {
		Properties properties = new Properties();
		ArrayList<PercentagePoint> pointsWithLines = new ArrayList<PercentagePoint>();
		ArrayList<PercentagePoint> points = new ArrayList<PercentagePoint>();
		ArrayList<Line> lines = new ArrayList<Line>();

		try {
			properties.loadFromXML(new FileInputStream(file));
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			int width = Integer.parseInt(properties.getProperty("width"));
			int height = Integer.parseInt(properties.getProperty("height"));
			float horizon = Float.parseFloat(properties.getProperty("horizon"));
			float vanishingpoint1 = Float.parseFloat(properties.getProperty("vanishingpoint1"));
			float vanishingpoint2 = Float.parseFloat(properties.getProperty("vanishingpoint2"));
			String stringPointWithLines = properties.getProperty("pointsWithLines");
			String stringPoints = properties.getProperty("points");
			String stringLines = properties.getProperty("lines");

			stringPointWithLines = stringPointWithLines.substring(1, stringPointWithLines.length() - 1);
			String[] splitted = stringPointWithLines.split("PercentagePoint ");
			for (int i = 1; i < splitted.length; i++) {
				splitted[i] = splitted[i].replace("[x=", "").replace(" y=", "").replace("], ", "").replace("]", "");
				PercentagePoint p = new PercentagePoint(Float.parseFloat(splitted[i].split(",")[0]),
						Float.parseFloat(splitted[i].split(",")[1]));
				pointsWithLines.add(p);
			}

			splitted = stringPoints.split("PercentagePoint ");
			for (int i = 1; i < splitted.length; i++) {
				splitted[i] = splitted[i].replace("[x=", "").replace(" y=", "").replace("], ", "").replace("]", "");
				PercentagePoint p = new PercentagePoint(Float.parseFloat(splitted[i].split(",")[0]),
						Float.parseFloat(splitted[i].split(",")[1]));
				points.add(p);
			}

			Line lastline = null;
			splitted = stringLines.split("Line ");
			for (int i = 1; i < splitted.length; i++) {
				String[] datas = new String[2];
				for (int j = 1; j < splitted[i].split("PercentagePoint ").length; j++) {
					String pointData = splitted[i].split("PercentagePoint ")[j];
					pointData = pointData.replace("[", "").replace("]", "").replace(", p2=", "").replace("x=", "")
							.replace(" y=", "");
					datas[j - 1] = pointData.replace("null", "");
				}

				Line line = new Line();
				line.setP1(new PercentagePoint(Float.parseFloat(datas[0].split(",")[0]),
						Float.parseFloat(datas[0].split(",")[1])));

				if (datas[1] != null) {
					line.setP2(new PercentagePoint(Float.parseFloat(datas[1].split(",")[0]),
							Float.parseFloat(datas[1].split(",")[1])));
					lines.add(line);
				} else {
					lastline = line;
				}
			}
			
			WorkSpaceDialog workspace = new WorkSpaceDialog(toolPanel, width, height);
			if (lastline != null) {
				lines.add(lastline);
				workspace.drawPanel.tp.makeLine=true;
			}

			
			workspace.drawPanel.horizon = horizon;
			workspace.drawPanel.vanishingpoint1 = vanishingpoint1;
			workspace.drawPanel.vanishingpoint2 = vanishingpoint2;
			workspace.drawPanel.pointsWithLines = pointsWithLines;
			workspace.drawPanel.points = points;
			workspace.drawPanel.lines=lines;

			cardPanel.workspaces.add(workspace);
			cardPanel.open = workspace;
			cardPanel.add(workspace, "WORKSPACE" + cardPanel.workspaces.indexOf(workspace));
			cardPanel.layout.show(cardPanel, "WORKSPACE" + cardPanel.workspaces.indexOf(workspace));
			cardPanel.cp.updateGraphics();
		} catch (Exception e) {
			System.err.println("The File is not valid");
		}
	}

}
