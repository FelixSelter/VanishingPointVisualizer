package com.felixselter.datatypes;

import java.awt.Point;

public class Line {

	private PercentagePoint p1, p2;

	@Override
	public String toString() {
		return "Line [p1=" + p1 + ", p2=" + p2 + "]";
	}

	public Line() {
	}

	public Line(PercentagePoint p1, PercentagePoint p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public PercentagePoint getP1() {
		return p1;
	}

	public void setP1(PercentagePoint p1) {
		this.p1 = p1;
	}

	public PercentagePoint getP2() {
		return p2;
	}

	public void setP2(PercentagePoint p2) {
		this.p2 = p2;
	}

}
