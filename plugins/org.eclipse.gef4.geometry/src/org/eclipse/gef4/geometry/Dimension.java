/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Alexander Nyßen (itemis AG) - migration to double precision
 *     Matthias Wienand (itemis AG) - contribution for Bugzilla #355997
 *     
 *******************************************************************************/
package org.eclipse.gef4.geometry;

import java.io.Serializable;

import org.eclipse.gef4.geometry.utils.PrecisionUtils;

/**
 * Represents a dimension (width, height) in 2-dimensional space. This class
 * provides various methods for manipulating this Dimension or creating new
 * derived Objects.
 * 
 * @author ebordeau
 * @author rhudson
 * @author pshah
 * @author ahunter
 * @author anyssen
 */
public class Dimension implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link Dimension} representing the maximum of two provided
	 * {@link Dimension}s.
	 * 
	 * @param d1
	 *            first dimension
	 * @param d2
	 *            second dimension
	 * @return A new Dimension representing the maximum of the given dimensions
	 */
	public static Dimension max(Dimension d1, Dimension d2) {
		return new Dimension(Math.max(d1.width, d2.width), Math.max(d1.height,
				d2.height));
	}

	/**
	 * Creates a new {@link Dimension} representing the minimum of two provided
	 * {@link Dimension}s.
	 * 
	 * @param d1
	 *            first dimension
	 * @param d2
	 *            second dimension
	 * @return A new {@link Dimension} representing the minimum
	 */
	public static Dimension min(Dimension d1, Dimension d2) {
		return new Dimension(Math.min(d1.width, d2.width), Math.min(d1.height,
				d2.height));
	}

	/**
	 * The width.
	 */
	public double width;

	/**
	 * The height.
	 */
	public double height;

	/**
	 * Constructs a {@link Dimension} of zero width and height.
	 * 
	 */
	public Dimension() {
	}

	/**
	 * Constructs a {@link Dimension} with the width and height of the passed
	 * {@link Dimension}.
	 * 
	 * @param d
	 *            The {@link Dimension} supplying the initial values for width
	 *            and height
	 */
	public Dimension(Dimension d) {
		width = d.width;
		height = d.height;
	}

	/**
	 * Constructs a {@link Dimension} with the supplied width and height values.
	 * 
	 * @param w
	 *            The width
	 * @param h
	 *            The height
	 */
	public Dimension(double w, double h) {
		width = w;
		height = h;
	}

	/**
	 * Constructs a {@link Dimension} with the width and height of the
	 * 
	 * @link{org.eclipse.swt.graphics.Image supplied as input.
	 * 
	 * @param image
	 *            The image supplying the width and height values
	 */
	public Dimension(org.eclipse.swt.graphics.Image image) {
		org.eclipse.swt.graphics.Rectangle r = image.getBounds();
		width = r.width;
		height = r.height;
	}

	/**
	 * Constructs a {@link Dimension} where the width and height are the x and y
	 * distances of the input point from the origin.
	 * 
	 * @param p
	 *            The {@link org.eclipse.swt.graphics.Point} whose x and y
	 *            values will be used to infer width and height
	 */
	public Dimension(org.eclipse.swt.graphics.Point p) {
		width = p.x;
		height = p.y;
	}

	/**
	 * Overwritten with public visibility as proposed in {@link Cloneable}.
	 */
	@Override
	public Dimension clone() {
		return getCopy();
	}

	/**
	 * Returns <code>true</code> if the input Dimension fits into this
	 * Dimension. A Dimension of the same size is considered to "fit".
	 * 
	 * @param d
	 *            the dimension being tested
	 * @return <code>true</code> if this Dimension contains <i>d</i>
	 */
	public boolean contains(Dimension d) {
		return width >= d.width && height >= d.height;
	}

	/**
	 * Returns <code>true</code> if this Dimension's width and height are equal
	 * to the given width and height.
	 * 
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @return <code>true</code> if this dimension's width and height are equal
	 *         to those given.
	 */
	public boolean equals(double width, double height) {
		return PrecisionUtils.equal(this.width, width)
				&& PrecisionUtils.equal(this.height, height);
	}

	/**
	 * Returns whether the input Object is equivalent to this Dimension.
	 * <code>true</code> if the Object is a Dimension and its width and height
	 * are equal to this Dimension's width and height, <code>false</code>
	 * otherwise.
	 * 
	 * @param o
	 *            the Object being tested for equality
	 * @return <code>true</code> if the given object is equal to this dimension
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Dimension) {
			Dimension d = (Dimension) o;
			return equals(d.width, d.height);
		}
		return false;
	}

	/**
	 * Expands the size of this Dimension by the specified amount.
	 * 
	 * @param d
	 *            the Dimension providing the expansion width and height
	 * @return <code>this</code> for convenience
	 */
	public Dimension expand(Dimension d) {
		return expand(d.width, d.height);
	}

	/**
	 * Expands the size of this Dimension by the specified width and height.
	 * 
	 * @param w
	 *            Value by which the width should be increased
	 * @param h
	 *            Value by which the height should be increased
	 * @return <code>this</code> for convenience
	 */
	public Dimension expand(double w, double h) {
		width += w;
		height += h;
		return this;
	}

	/**
	 * Creates and returns a copy of this {@link Dimension}.
	 * 
	 * @return a copy of this Dimension
	 */
	public Dimension getCopy() {
		return new Dimension(this);
	}

	/**
	 * Creates and returns a {@link Dimension} representing the sum of this
	 * {@link Dimension} and the one specified.
	 * 
	 * @param d
	 *            the dimension providing the expansion width and height
	 * @return a new dimension expanded by <i>d</i>
	 */
	public Dimension getExpanded(Dimension d) {
		return getCopy().expand(d);
	}

	/**
	 * Creates and returns a new Dimension representing the sum of this
	 * {@link Dimension} and the one specified.
	 * 
	 * @param w
	 *            value by which the width of this is to be expanded
	 * @param h
	 *            value by which the height of this is to be expanded
	 * @return a new Dimension expanded by the given values
	 */
	public Dimension getExpanded(double w, double h) {
		return getCopy().expand(w, h);
	}

	/**
	 * Creates and returns a new Dimension representing the intersection of this
	 * Dimension and the one specified.
	 * 
	 * @param d
	 *            the Dimension to intersect with
	 * @return A new Dimension representing the intersection
	 */
	public Dimension getIntersected(Dimension d) {
		return getCopy().intersect(d);
	}

	/**
	 * Creates and returns a new Dimension with negated values.
	 * 
	 * @return a new Dimension with negated values
	 */
	public Dimension getNegated() {
		return getCopy().negate();
	}

	/**
	 * Creates a new Dimension with its width and height scaled by the specified
	 * value.
	 * 
	 * @param amount
	 *            Value by which the width and height are scaled
	 * @return a new dimension with the scale applied
	 */
	public Dimension getScaled(double amount) {
		return getCopy().scale(amount);
	}

	/**
	 * Creates a new Dimension with its width and height scaled by the specified
	 * values.
	 * 
	 * @param widthFactor
	 *            the value by which the width is to be scaled
	 * @param heightFactor
	 *            the value by which the height is to be scaled
	 * @return a new dimension with the scale applied
	 */
	public Dimension getScaled(double widthFactor, double heightFactor) {
		return getCopy().scale(widthFactor, heightFactor);
	}

	/**
	 * Creates and returns a new Dimension whose size will be reduced by the
	 * width and height of the given Dimension.
	 * 
	 * @param d
	 *            the dimension whose width and height values will be considered
	 * @return a new dimension representing the difference
	 */
	public Dimension getShrinked(Dimension d) {
		return getCopy().shrink(d);
	}

	/**
	 * Creates and returns a new Dimension whose size will be reduced by the
	 * given width and height.
	 * 
	 * @param w
	 *            the value by which the width is to be reduced
	 * @param h
	 *            the value by which the height is to be reduced
	 * @return a new dimension representing the difference
	 */
	public Dimension getShrinked(double w, double h) {
		return getCopy().shrink(w, h);
	}

	/**
	 * Creates a new Dimension with its height and width swapped. Useful in
	 * orientation change calculations.
	 * 
	 * @return a new Dimension with its height and width swapped
	 */
	public Dimension getTransposed() {
		return getCopy().transpose();
	}

	/**
	 * Creates a new Dimension representing the union of this Dimension with the
	 * one specified. Union is defined as the max() of the values from each
	 * Dimension.
	 * 
	 * @param d
	 *            the Dimension to be unioned
	 * @return a new Dimension
	 */
	public Dimension getUnioned(Dimension d) {
		return getCopy().union(d);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// calculating a better hashCode is not possible, because due to the
		// imprecision, equals() is no longer transitive
		return 0;
	}

	/**
	 * Returns the height of this dimension.
	 * 
	 * @return The current height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * This Dimension is intersected with the one specified. Intersection is
	 * performed by taking the min() of the values from each dimension.
	 * 
	 * @param d
	 *            the Dimension used to perform the min()
	 * @return <code>this</code> for convenience
	 */
	public Dimension intersect(Dimension d) {
		width = Math.min(d.width, width);
		height = Math.min(d.height, height);
		return this;
	}

	/**
	 * Returns <code>true</code> if either dimension is less than or equal to 0.
	 * 
	 * @return <code>true</code> if either dimension is less than or equal to 0.
	 */
	public boolean isEmpty() {
		return width <= 0 || height <= 0;
	}

	/**
	 * Negates the width and height of this Dimension.
	 * 
	 * @return <code>this</code> for convenience
	 */
	public Dimension negate() {
		return scale(-1.0d);
	}

	/**
	 * Scales the width and height of this Dimension by the amount supplied, and
	 * returns this for convenience.
	 * 
	 * @param factor
	 *            value by which this Dimension's width and height are to be
	 *            scaled
	 * @return <code>this</code> for convenience
	 */
	public Dimension scale(double factor) {
		return scale(factor, factor);
	}

	/**
	 * Scales the width of this Dimension by <i>w</i> and scales the height of
	 * this Dimension by <i>h</i>. Returns this for convenience.
	 * 
	 * @param widthFactor
	 *            the value by which the width is to be scaled
	 * @param heightFactor
	 *            the value by which the height is to be scaled
	 * @return <code>this</code> for convenience
	 */
	public Dimension scale(double widthFactor, double heightFactor) {
		width *= widthFactor;
		height *= heightFactor;
		return this;
	}

	/**
	 * Sets the height of this Rectangle to the specified one.
	 * 
	 * @param height
	 *            The new height
	 * @return this for convenience
	 */
	public Dimension setHeight(int height) {
		this.height = height;
		return this;
	}

	/**
	 * Copies the width and height values of the input Dimension to this
	 * Dimension.
	 * 
	 * @param d
	 *            the dimension supplying the values
	 * @return <code>this</code> for convenience
	 */
	public Dimension setSize(Dimension d) {
		width = d.width;
		height = d.height;
		return this;
	}

	/**
	 * Sets the size of this dimension to the specified width and height.
	 * 
	 * @param w
	 *            The new width
	 * @param h
	 *            The new height
	 * @return <code>this</code> for convenience
	 */
	public Dimension setSize(double w, double h) {
		width = w;
		height = h;
		return this;
	}

	/**
	 * Sets the width of this Rectangle to the specified one.
	 * 
	 * @param width
	 *            The new width
	 * @return this for convenience
	 */
	public Dimension setWidth(double width) {
		this.width = width;
		return this;
	}

	/**
	 * Shrinks the size of this Dimension by the width and height values of the
	 * given Dimension.
	 * 
	 * @param d
	 *            The dimension whose width and height values are to be used
	 * @return <code>this</code> for convenience
	 */
	public Dimension shrink(Dimension d) {
		return shrink(d.width, d.height);
	}

	/**
	 * Reduces the width of this Dimension by <i>w</i>, and reduces the height
	 * of this Dimension by <i>h</i>. Returns this for convenience.
	 * 
	 * @param w
	 *            the value by which the width is to be reduced
	 * @param h
	 *            the value by which the height is to be reduced
	 * @return <code>this</code> for convenience
	 */
	public Dimension shrink(double w, double h) {
		width -= w;
		height -= h;
		return this;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return "Dimension(" + //$NON-NLS-1$
				width + ", " + //$NON-NLS-1$
				height + ")"; //$NON-NLS-1$
	}

	/**
	 * Swaps the width and height of this Dimension, and returns this for
	 * convenience. Can be useful in orientation changes.
	 * 
	 * @return <code>this</code> for convenience
	 */
	public Dimension transpose() {
		double temp = width;
		width = height;
		height = temp;
		return this;
	}

	/**
	 * Sets the width of this Dimension to the greater of this Dimension's width
	 * and <i>d</i>.width. Likewise for this Dimension's height.
	 * 
	 * @param d
	 *            the Dimension to union with this Dimension
	 * @return <code>this</code> for convenience
	 */
	public Dimension union(Dimension d) {
		width = Math.max(width, d.width);
		height = Math.max(height, d.height);
		return this;
	}

	/**
	 * Returns the width of this dimension
	 * 
	 * @return the current width of this dimension
	 */
	public double getWidth() {
		return width;
	}

}
