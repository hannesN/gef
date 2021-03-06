/*******************************************************************************
 * Copyright (c) 2016 itemis AG and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthias Wienand (itemis AG) - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.gef.geometry.tests;

import static org.junit.Assert.assertEquals;

import org.eclipse.gef.geometry.planar.BezierCurve;
import org.eclipse.gef.geometry.planar.Point;
import org.eclipse.gef.geometry.planar.PolyBezier;
import org.junit.Test;

public class PolyBezierTests {

	@Test
	public void test_same_points() {
		PolyBezier polyBezier = PolyBezier.interpolateCubic(new Point(),
				new Point(), new Point());
		BezierCurve[] beziers = polyBezier.toBezier();
		assertEquals(2, beziers.length);
		assertEquals(new Point(), beziers[0].getP1());
		assertEquals(new Point(), beziers[0].getP2());
		assertEquals(new Point(), beziers[1].getP1());
		assertEquals(new Point(), beziers[1].getP2());
	}

}
