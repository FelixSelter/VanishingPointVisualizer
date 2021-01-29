package com.felixselter.main;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.felixselter.datatypes.Line;
import com.felixselter.datatypes.PercentagePoint;

public class DrawPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {

	public ToolPanel tp;
	private Point mouseCoordinate = null;
	private Rectangle visible;
	private float zoom = 1;

	public float horizon = -1;
	public float vanishingpoint1 = -1;
	public float vanishingpoint2 = -1;
	public ArrayList<PercentagePoint> pointsWithLines = new ArrayList<PercentagePoint>();
	public ArrayList<PercentagePoint> points = new ArrayList<PercentagePoint>();
	public ArrayList<Line> lines = new ArrayList<Line>();
	private ArrayList<Runnable> undos = new ArrayList<Runnable>();
	private ArrayList<Runnable> redos = new ArrayList<Runnable>();

	public BufferedImage painting;
	private Graphics painter;

	private BufferedImage pointer;
	private Robot robot;
	private int snappingTolerance = 10;

	public DrawPanel(ToolPanel tp, int width, int height) {
		this.tp = tp;
		tp.dp = this;

		try {
			pointer =ImageLoader.getLoader().load("pointer.png");

			robot = new Robot();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		painting = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		painter = painting.getGraphics();
		painter.setColor(Color.white);
		painter.fillRect(0, 0, width, height);
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		Point mouse = e.getPoint();

		float percentageX = (float) (mouse.x) / getWidth();
		float percentageY = (float) (mouse.y) / getHeight();

		int zoomX = Math.round(visible.x + (percentageX * visible.width));
		int zoomY = Math.round(visible.y + (percentageY * visible.height));

		zoom -= (e.getPreciseWheelRotation() / 5f);
		if (zoom < 1)
			zoom = 1;

		int newWidth = Math.round(getWidth() / zoom);
		int newHeight = Math.round(getHeight() / zoom);

		int newX = Math.round(percentageX * newWidth);
		int newY = Math.round(percentageY * newHeight);
		int offsetX = newWidth - (newWidth - newX);
		int offsetY = newHeight - (newHeight - newY);

		visible.x = zoomX - offsetX;
		visible.y = zoomY - offsetY;
		visible.width = newWidth;
		visible.height = newHeight;

		if (visible.x < 0)
			visible.x = 0;
		if (visible.x + visible.width > getWidth())
			visible.width = getWidth() - visible.x;
		if (visible.y < 0)
			visible.y = 0;
		if (visible.y + visible.height > getHeight())
			visible.height = getHeight() - visible.y;

	}

	public BufferedImage getDrawing(int width, int height) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.black);
		for (

		Line line : lines) {
			g.setColor(Color.black);
			Point p1 = percentageToPoint(line.getP1(), width, height);
			if (line.getP2() != null) {
				Point p2 = percentageToPoint(line.getP2(), width, height);
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
		}
		return img;
	}

	private Point percentageToPoint(PercentagePoint percentage, int width, int height) {
		return new Point((int) (percentage.x * width), (int) (percentage.y * height));
	}

	private PercentagePoint pointToPercentage(Point point, int width, int height) {
		return new PercentagePoint((float) (point.x) / width, (float) (point.y) / height);
	}

	public void undo() {
		if (undos.size() > 0) {
			undos.get(undos.size() - 1).run();
			undos.remove(undos.size() - 1);
		}
	}

	public void redo() {
		if (redos.size() > 0) {
			redos.get(redos.size() - 1).run();
			redos.remove(redos.size() - 1);
		}
	}

	private BufferedImage draw() {
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.black);

		if (horizon == -1 && mouseCoordinate != null)
			g.drawLine(0, mouseCoordinate.y, getWidth(), mouseCoordinate.y);

		if (tp.additionalInfo) {
			if (horizon != -1) {
				g.drawLine(0, (int) (horizon * getHeight()), getWidth(), (int) (horizon * getHeight()));
				if (vanishingpoint1 == -1 || vanishingpoint2 == -1)
					g.drawImage(pointer, mouseCoordinate.x - 23, (int) (horizon * getHeight()) - 60, 46, 60, null);
			}

			if (vanishingpoint1 != -1) {
				g.setColor(Color.red);
				g.fillOval((int) (vanishingpoint1 * getWidth()) - 3, (int) (horizon * getHeight()) - 3, 6, 6);

			}
			if (vanishingpoint2 != -1) {
				g.setColor(Color.BLUE);
				g.fillOval((int) (vanishingpoint2 * getWidth()) - 3, (int) (horizon * getHeight()) - 3, 6, 6);
			}

			for (PercentagePoint percentagePoint : pointsWithLines) {
				Point point = percentageToPoint(percentagePoint, getWidth(), getHeight());
				g.setColor(Color.red);
				g.drawLine((int) (vanishingpoint1 * getWidth()), (int) (horizon * getHeight()), point.x, point.y);
				g.setColor(Color.blue);
				g.drawLine((int) (vanishingpoint2 * getWidth()), (int) (horizon * getHeight()), point.x, point.y);

				if (tp.drawLines || tp.makePoint || tp.makeLine) {
					g.setColor(Color.black);
					g.drawString(String.valueOf(Math.round(Math.sqrt(Math.pow(Math.abs(mouseCoordinate.x - point.x), 2)
							+ Math.pow(Math.abs(mouseCoordinate.y - point.y), 2)))), point.x, point.y);
				}

				// Parallel check vp1
				g.setColor(Color.black);
				Point p2 = new Point((int) (vanishingpoint1 * getWidth()), (int) (horizon * getHeight()));
				Point p1 = point;

				if (lines.size() > 0) {
					Line lastLine = lines.get(lines.size() - 1);
					if (lastLine.getP2() == null) {
						try {

							float deltaX1 = p2.x - p1.x;
							float deltaY1 = p2.y - p1.y;
							float deltaX2 = mouseCoordinate.x
									- percentageToPoint(lastLine.getP1(), getWidth(), getHeight()).x;
							float deltaY2 = mouseCoordinate.y
									- percentageToPoint(lastLine.getP1(), getWidth(), getHeight()).y;

							String pitch1 = new DecimalFormat("0.00").format((float) (deltaY1) / deltaX1);
							String pitch2 = new DecimalFormat("0.00").format((float) (deltaY2) / deltaX2);

							g.drawString(pitch1, (int) (p2.x - deltaX1 / 2), (int) (p2.y - (deltaY1 / 2)));

							if ((deltaX1 == 0 && deltaX2 == 0) || (deltaY1 == 0 && deltaY2 == 0)
									|| pitch1.equals(pitch2)) {
								g.setColor(Color.magenta);
								g.drawLine(p1.x, p1.y, p2.x, p2.y);
							}

						} catch (ArithmeticException e) {
							System.out.println(mouseCoordinate.x + " "
									+ percentageToPoint(lastLine.getP1(), getWidth(), getHeight()).x);
						}
					}
				}

				// Parallel check vp2
				g.setColor(Color.black);
				p2 = new Point((int) (vanishingpoint2 * getWidth()), (int) (horizon * getHeight()));
				p1 = point;

				if (lines.size() > 0) {
					Line lastLine = lines.get(lines.size() - 1);
					if (lastLine.getP2() == null) {
						try {

							float deltaX1 = p2.x - p1.x;
							float deltaY1 = p2.y - p1.y;
							float deltaX2 = mouseCoordinate.x
									- percentageToPoint(lastLine.getP1(), getWidth(), getHeight()).x;
							float deltaY2 = mouseCoordinate.y
									- percentageToPoint(lastLine.getP1(), getWidth(), getHeight()).y;

							String pitch1 = new DecimalFormat("0.00").format((float) (deltaY1) / deltaX1);
							String pitch2 = new DecimalFormat("0.00").format((float) (deltaY2) / deltaX2);

							g.drawString(pitch1, (int) (p2.x - deltaX1 / 2), (int) (p2.y - (deltaY1 / 2)));
							g.drawString(pitch2, mouseCoordinate.x, mouseCoordinate.y);

							if ((deltaX1 == 0 && deltaX2 == 0) || (deltaY1 == 0 && deltaY2 == 0)
									|| pitch1.equals(pitch2)) {
								g.setColor(Color.magenta);
								g.drawLine(p1.x, p1.y, p2.x, p2.y);
							}

						} catch (ArithmeticException e) {
							System.out.println(mouseCoordinate.x + " "
									+ percentageToPoint(lastLine.getP1(), getWidth(), getHeight()).x);
						}
					}
				}
			}

			for (PercentagePoint percentagePoint : points) {
				g.setColor(Color.green);
				Point point = percentageToPoint(percentagePoint, getWidth(), getHeight());
				g.fillOval(point.x - 3, point.y - 3, 6, 6);

				if (tp.drawLines || tp.makePoint || tp.makeLine) {
					g.setColor(Color.black);
					g.drawString(String.valueOf(Math.round(Math.sqrt(Math.pow(Math.abs(mouseCoordinate.x - point.x), 2)
							+ Math.pow(Math.abs(mouseCoordinate.y - point.y), 2)))), point.x, point.y);
				}
			}
		}

		for (

		Line line : lines) {
			g.setColor(Color.black);
			Point p1 = percentageToPoint(line.getP1(), getWidth(), getHeight());
			if (line.getP2() != null) {
				Point p2 = percentageToPoint(line.getP2(), getWidth(), getHeight());

				Line lastLine = lines.get(lines.size() - 1);
				if (lastLine.getP2() == null) {
					try {

						float deltaX1 = p2.x - p1.x;
						float deltaY1 = p2.y - p1.y;
						float deltaX2 = mouseCoordinate.x
								- percentageToPoint(lastLine.getP1(), getWidth(), getHeight()).x;
						float deltaY2 = mouseCoordinate.y
								- percentageToPoint(lastLine.getP1(), getWidth(), getHeight()).y;

						String pitch1 = new DecimalFormat("0.00").format((float) (deltaY1) / deltaX1);
						String pitch2 = new DecimalFormat("0.00").format((float) (deltaY2) / deltaX2);

						g.drawString(pitch1, (int) (p2.x - deltaX1 / 2), (int) (p2.y - (deltaY1 / 2)));
						g.drawString(pitch2, mouseCoordinate.x, mouseCoordinate.y);

						if ((deltaX1 == 0 && deltaX2 == 0) || (deltaY1 == 0 && deltaY2 == 0) || pitch1.equals(pitch2)) {
							g.setColor(Color.green);
						}

					} catch (ArithmeticException e) {
						System.out.println(mouseCoordinate.x + " "
								+ percentageToPoint(lastLine.getP1(), getWidth(), getHeight()).x);
					}
				}

				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			} else {
				g.drawLine(p1.x, p1.y, mouseCoordinate.x, mouseCoordinate.y);
			}
		}

		return img;
	}

	@Override
	public void paintComponent(Graphics g) {

		if (visible == null)
			visible = new Rectangle(0, 0, getWidth(), getHeight());

		BufferedImage img = draw();
		if (img.getWidth() >= visible.x + visible.width && img.getHeight() >= visible.y + visible.height&&visible.x>=0&&visible.y>=0)
			img = img.getSubimage(visible.x, visible.y, visible.width, visible.height);

		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);

	}

	@Override
	public void mouseClicked(MouseEvent event) {

		int x = visible.x + Math.round(((float) (event.getX()) / getWidth()) * visible.width);
		int y = visible.y + Math.round(((float) (event.getY()) / getHeight()) * visible.height);

		MouseEvent e = new MouseEvent((Component) event.getSource(), event.getID(), event.getWhen(),
				event.getModifiers(), x, y, 0, 0, event.getClickCount(), event.isPopupTrigger(), event.getButton());

		if (horizon == -1) {
			horizon = (float) (e.getY()) / getHeight();
			undos.add(new Runnable() {

				@Override
				public void run() {
					horizon = -1;
					Runnable undo = this;
					redos.add(new Runnable() {

						@Override
						public void run() {
							horizon = (float) (e.getY()) / getHeight();
							undos.add(undo);
							redos.remove(this);
						}
					});
				}
			});

		} else if (vanishingpoint1 == -1) {
			vanishingpoint1 = (float) (e.getX()) / getWidth();
			undos.add(new Runnable() {

				@Override
				public void run() {
					vanishingpoint1 = -1;
					Runnable undo = this;
					redos.add(new Runnable() {

						@Override
						public void run() {
							vanishingpoint1 = (float) (e.getX()) / getWidth();
							undos.add(undo);
							redos.remove(this);
						}
					});
				}
			});

		} else if (vanishingpoint2 == -1) {
			vanishingpoint2 = (float) (e.getX()) / getWidth();
			undos.add(new Runnable() {

				@Override
				public void run() {
					vanishingpoint2 = -1;
					Runnable undo = this;
					redos.add(new Runnable() {

						@Override
						public void run() {
							vanishingpoint2 = (float) (e.getX()) / getWidth();
							undos.add(undo);
							redos.remove(this);
						}
					});
				}
			});

		} else if (tp.drawLines) {
			PercentagePoint p = pointToPercentage(e.getPoint(), getWidth(), getHeight());
			pointsWithLines.add(p);
			points.add(p);
			undos.add(new Runnable() {

				@Override
				public void run() {
					pointsWithLines.remove(p);
					points.remove(p);
					Runnable undo = this;
					redos.add(new Runnable() {

						@Override
						public void run() {
							pointsWithLines.add(p);
							points.add(p);
							undos.add(undo);
							redos.remove(this);
						}
					});
				}
			});

		} else if (tp.makePoint) {
			PercentagePoint p = pointToPercentage(e.getPoint(), getWidth(), getHeight());
			points.add(p);
			undos.add(new Runnable() {

				@Override
				public void run() {
					points.remove(p);
					Runnable undo = this;
					redos.add(new Runnable() {

						@Override
						public void run() {
							points.add(p);
							undos.add(undo);
							redos.remove(this);
						}
					});
				}
			});

		} else if (tp.makeLine) {
			PercentagePoint p = pointToPercentage(e.getPoint(), getWidth(), getHeight());
			Line newLine = new Line(p, null);
			Runnable newLineUndo = new Runnable() {

				@Override
				public void run() {
					points.remove(p);
					lines.remove(newLine);
					Runnable undo = this;
					redos.add(new Runnable() {

						@Override
						public void run() {
							points.add(p);
							lines.add(newLine);
							undos.add(undo);
							redos.remove(this);
						}
					});
				}
			};

			points.add(p);

			if (lines.size() > 0) {
				Line line = lines.get(lines.size() - 1);
				if (line.getP2() == null) {
					line.setP2(p);
					undos.add(new Runnable() {

						@Override
						public void run() {
							points.remove(p);
							line.setP2(null);
							Runnable undo = this;
							redos.add(new Runnable() {

								@Override
								public void run() {
									points.add(p);
									line.setP2(p);
									undos.add(undo);
									redos.remove(this);
								}
							});
						}

					});

				} else {
					lines.add(newLine);
					undos.add(newLineUndo);
				}
			} else {
				lines.add(newLine);
				undos.add(newLineUndo);
			}

		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {

		int x = visible.x + Math.round(((float) (event.getX()) / getWidth()) * visible.width);
		int y = visible.y + Math.round(((float) (event.getY()) / getHeight()) * visible.height);

		MouseEvent e = new MouseEvent((Component) event.getSource(), event.getID(), event.getWhen(),
				event.getModifiers(), x, y, event.getXOnScreen(), event.getYOnScreen(), event.getClickCount(),
				event.isPopupTrigger(), event.getButton());

		mouseCoordinate = e.getPoint();

		if (tp.snappingToPoint) {
			for (PercentagePoint percentagePoint : pointsWithLines) {
				Point point = percentageToPoint(percentagePoint, getWidth(), getHeight());
				int deltaX, deltaY;

				// snapping to point fixed y
				deltaX = point.x - e.getX();
				if (Math.abs(deltaX) <= snappingTolerance) {
					robot.mouseMove(e.getXOnScreen() + deltaX, e.getYOnScreen());
					break;
				}

//				deltaY = point.y - e.getY();
//				if (Math.abs(deltaY) <= snappingDistance) {
//					robot.mouseMove(e.getXOnScreen(), e.getYOnScreen() + deltaY);
//					break;
//				}

				// snapping to first line
				if ((e.getX() > (int) (vanishingpoint1 * getWidth()) && e.getX() < point.x)
						|| (e.getX() > point.x && e.getX() < (int) (vanishingpoint1 * getWidth()))) {
					deltaX = point.x - (int) (vanishingpoint1 * getWidth());
					deltaY = point.y - (int) (horizon * getHeight());
					float m = (float) (deltaY) / deltaX;

					int correctY = (int) (m * (mouseCoordinate.x - (int) (vanishingpoint1 * getWidth()))
							+ (int) (horizon * getHeight()));
					deltaY = e.getYOnScreen() - e.getY();

					if (Math.abs(mouseCoordinate.y - correctY) <= snappingTolerance) {
						robot.mouseMove(e.getXOnScreen(), correctY + deltaY);
					}
				}

				// snapping to second line
				if ((e.getX() > (int) (vanishingpoint2 * getWidth()) && e.getX() < point.x)
						|| (e.getX() > point.x && e.getX() < (int) (vanishingpoint2 * getWidth()))) {
					deltaX = point.x - (int) (vanishingpoint2 * getWidth());
					deltaY = point.y - (int) (horizon * getHeight());
					float m = (float) (deltaY) / deltaX;

					int correctY = (int) (m * (mouseCoordinate.x - (int) (vanishingpoint2 * getWidth()))
							+ (int) (horizon * getHeight()));
					deltaY = e.getYOnScreen() - e.getY();

					if (Math.abs(mouseCoordinate.y - correctY) <= snappingTolerance) {
						robot.mouseMove(e.getXOnScreen(), correctY + deltaY);
					}
				}
			}

			// snapping to point
			if (tp.makeLine || tp.drawLines) {
				for (PercentagePoint percentagePoint : points) {
					Point point = percentageToPoint(percentagePoint, getWidth(), getHeight());
					int deltaX, deltaY;
					deltaX = point.x - e.getX();
					deltaY = point.y - e.getY();

					if (Math.abs(deltaX) < snappingTolerance && Math.abs(deltaY) < snappingTolerance)
						robot.mouseMove(e.getXOnScreen() + deltaX, e.getYOnScreen() + deltaY);

				}
			}

		}

	}

	public int getPaintingWidth() {
		return painting.getWidth();
	}

	public int getPaintingHeight() {
		return painting.getHeight();
	}

	public int getFactor() {
		int a = painting.getWidth();
		int b = painting.getHeight();

		if (a == 0)
			return b;
		while (b != 0) {
			if (a > b)
				a = a - b;
			else
				b = b - a;
		}

		return a;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
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
