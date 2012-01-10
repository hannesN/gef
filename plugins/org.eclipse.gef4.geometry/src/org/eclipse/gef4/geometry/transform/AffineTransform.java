/*******************************************************************************
 * Copyright (c) 2011 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alexander Nyßen (itemis AG) - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.gef4.geometry.transform;

import java.awt.geom.NoninvertibleTransformException;

import org.eclipse.gef4.geometry.Point;
import org.eclipse.gef4.geometry.convert.AWT2Geometry;
import org.eclipse.gef4.geometry.convert.Geometry2AWT;

public class AffineTransform {
	// TODO: implement affine transform locally to get rid of dependency on
	// awt.geom.

	private java.awt.geom.AffineTransform delegate = new java.awt.geom.AffineTransform();

	public double[] getMatrix() {
		double[] flatmatrix = new double[6];
		delegate.getMatrix(flatmatrix);
		return flatmatrix;
	}

	public AffineTransform() {
	}

	public AffineTransform(double m00, double m10, double m01, double m11,
			double m02, double m12) {
		delegate = new java.awt.geom.AffineTransform(m00, m10, m01, m11, m02,
				m12);
	}

	public AffineTransform(double[] flatmatrix) {
		delegate = new java.awt.geom.AffineTransform(flatmatrix);
	}

	public int getType() {
		return delegate.getType();
	}

	public double getDeterminant() {
		return delegate.getDeterminant();
	}

	public double getScaleX() {
		return delegate.getScaleX();
	}

	public double getScaleY() {
		return delegate.getScaleY();
	}

	public double getShearX() {
		return delegate.getShearX();
	}

	public double getShearY() {
		return delegate.getShearY();
	}

	public double getTranslateX() {
		return delegate.getTranslateX();
	}

	public double getTranslateY() {
		return delegate.getTranslateY();
	}

	public void translate(double tx, double ty) {
		delegate.translate(tx, ty);
	}

	public void rotate(double theta) {
		delegate.rotate(theta);
	}

	public void rotate(double theta, double anchorx, double anchory) {
		delegate.rotate(theta, anchorx, anchory);
	}

	public void rotate(double vecx, double vecy) {
		delegate.rotate(vecx, vecy);
	}

	public void rotate(double vecx, double vecy, double anchorx, double anchory) {
		delegate.rotate(vecx, vecy, anchorx, anchory);
	}

	public void quadrantRotate(int numquadrants) {
		delegate.quadrantRotate(numquadrants);
	}

	public void quadrantRotate(int numquadrants, double anchorx, double anchory) {
		delegate.quadrantRotate(numquadrants, anchorx, anchory);
	}

	public void scale(double sx, double sy) {
		delegate.scale(sx, sy);
	}

	public void shear(double shx, double shy) {
		delegate.shear(shx, shy);
	}

	public void setToIdentity() {
		delegate.setToIdentity();
	}

	public void setToTranslation(double tx, double ty) {
		delegate.setToTranslation(tx, ty);
	}

	public void setToRotation(double theta) {
		delegate.setToRotation(theta);
	}

	public void setToRotation(double theta, double anchorx, double anchory) {
		delegate.setToRotation(theta, anchorx, anchory);
	}

	public void setToRotation(double vecx, double vecy) {
		delegate.setToRotation(vecx, vecy);
	}

	public void setToRotation(double vecx, double vecy, double anchorx,
			double anchory) {
		delegate.setToRotation(vecx, vecy, anchorx, anchory);
	}

	public void setToQuadrantRotation(int numquadrants) {
		delegate.setToQuadrantRotation(numquadrants);
	}

	public void setToQuadrantRotation(int numquadrants, double anchorx,
			double anchory) {
		delegate.setToQuadrantRotation(numquadrants, anchorx, anchory);
	}

	public void setToScale(double sx, double sy) {
		delegate.setToScale(sx, sy);
	}

	public void setToShear(double shx, double shy) {
		delegate.setToShear(shx, shy);
	}

	public void setTransform(AffineTransform Tx) {
		delegate.setTransform(Tx.delegate);
	}

	public void setTransform(double m00, double m10, double m01, double m11,
			double m02, double m12) {
		delegate.setTransform(m00, m10, m01, m11, m02, m12);
	}

	public void concatenate(AffineTransform Tx) {
		delegate.concatenate(Tx.delegate);
	}

	public void preConcatenate(AffineTransform Tx) {
		delegate.preConcatenate(Tx.delegate);
	}

	public java.awt.geom.AffineTransform createInverse()
			throws NoninvertibleTransformException {
		return delegate.createInverse();
	}

	public void invert() throws NoninvertibleTransformException {
		delegate.invert();
	}

	public Point getTransformed(Point ptSrc) {
		return AWT2Geometry.toPoint(delegate.transform(
				Geometry2AWT.toAWTPoint(ptSrc), null));
	}

	public Point[] getTransformed(Point[] points) {
		Point[] result = new Point[points.length];

		System.out.println("Points before transformation:");
		for (int i = 0; i < points.length; i++) {
			System.out.println("... " + points[i]);
		}

		for (int i = 0; i < points.length; i++) {
			result[i] = getTransformed(points[i]);
		}

		System.out.println("Points after transformation:");
		for (int i = 0; i < result.length; i++) {
			System.out.println("... " + result[i]);
		}
		return result;
	}

	public Point inverseTransform(Point ptSrc, Point ptDst)
			throws NoninvertibleTransformException {
		return AWT2Geometry
				.toPoint(delegate.inverseTransform(
						Geometry2AWT.toAWTPoint(ptSrc),
						Geometry2AWT.toAWTPoint(ptDst)));
	}

	public void inverseTransform(double[] srcPts, int srcOff, double[] dstPts,
			int dstOff, int numPts) throws NoninvertibleTransformException {
		delegate.inverseTransform(srcPts, srcOff, dstPts, dstOff, numPts);
	}

	public Point deltaTransform(Point ptSrc, Point ptDst) {
		return AWT2Geometry
				.toPoint(delegate.deltaTransform(
						Geometry2AWT.toAWTPoint(ptSrc),
						Geometry2AWT.toAWTPoint(ptDst)));
	}

	public void deltaTransform(double[] srcPts, int srcOff, double[] dstPts,
			int dstOff, int numPts) {
		delegate.deltaTransform(srcPts, srcOff, dstPts, dstOff, numPts);
	}

	public String toString() {
		return delegate.toString();
	}

	public boolean isIdentity() {
		return delegate.isIdentity();
	}

	public Object clone() {
		return delegate.clone();
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public boolean equals(Object obj) {
		return delegate.equals(obj);
	}
}
