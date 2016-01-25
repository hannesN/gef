/*******************************************************************************
 * Copyright (c) 2016 itemis AG and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alexander Nyßen (itemis AG) - initial API & implementation
 *
 *******************************************************************************/
package org.eclipse.gef4.common.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.gef4.common.beans.property.ReadOnlySetMultimapProperty;
import org.eclipse.gef4.common.beans.property.ReadOnlySetMultimapWrapper;
import org.eclipse.gef4.common.beans.property.SetMultimapProperty;
import org.eclipse.gef4.common.beans.property.SimpleSetMultimapProperty;
import org.eclipse.gef4.common.collections.ObservableSetMultimap;
import org.eclipse.gef4.common.collections.ObservableSetMultimapWrapper;
import org.eclipse.gef4.common.tests.ObservableSetMultimapTests.ChangeExpector;
import org.eclipse.gef4.common.tests.ObservableSetMultimapTests.InvalidationExpector;
import org.eclipse.gef4.common.tests.ObservableSetMultimapTests.SetMultimapChangeExpector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.inject.Provider;

@RunWith(Parameterized.class)
public class SetMultimapPropertyTests {

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ new Provider<SetMultimapProperty<Integer, String>>() {

					@Override
					public SetMultimapProperty<Integer, String> get() {
						// test SimpleSetMultimapProperty, which is the
						// 'default' implementation of the related
						// ObservableValue.
						return new SimpleSetMultimapProperty<>(
								new ObservableSetMultimapWrapper<>(HashMultimap
										.<Integer, String> create()));
					}
				} }, { new Provider<SetMultimapProperty<Integer, String>>() {

					@Override
					public SetMultimapProperty<Integer, String> get() {
						// test ReadOnlySetMultimapWrapper, which is the
						// 'default' implementation of the related
						// read-only property support.
						return new ReadOnlySetMultimapWrapper<>(
								new ObservableSetMultimapWrapper<>(HashMultimap
										.<Integer, String> create()));
					}
				} } });
	}

	private Provider<SetMultimapProperty<Integer, String>> propertyProvider;

	public SetMultimapPropertyTests(
			Provider<SetMultimapProperty<Integer, String>> propertyProvider) {
		this.propertyProvider = propertyProvider;
	}

	@Test
	public void bidirectionalBinding() {
		SetMultimapProperty<Integer, String> property1 = propertyProvider.get();
		SetMultimapProperty<Integer, String> property2 = propertyProvider.get();

		// XXX: According to JavaFX contract, a bidirectional binding does not
		// lead to the properties being regarded as bound.
		property2.bindBidirectional(property1);
		assertFalse(property1.isBound());
		assertFalse(property2.isBound());

		// change value of first property
		ObservableSetMultimap<Integer, String> newValue = new ObservableSetMultimapWrapper<>(
				HashMultimap.<Integer, String> create());
		newValue.put(1, "1-1");
		property1.set(newValue);
		assertEquals(newValue, property1.get());
		assertEquals(newValue, property2.get());
		assertEquals(property1, property2);

		// change value of second property
		newValue = new ObservableSetMultimapWrapper<>(
				HashMultimap.<Integer, String> create());
		newValue.put(2, "2-1");
		property2.set(newValue);
		assertEquals(property1, property2);
		assertEquals(newValue, property1.get());
		assertEquals(newValue, property2.get());

		// unbind (ensure values are no longer synchronized)
		property2.unbindBidirectional(property1);
		assertFalse(property1.isBound());
		assertFalse(property2.isBound());
		newValue = new ObservableSetMultimapWrapper<>(
				HashMultimap.<Integer, String> create());
		newValue.put(3, "3-1");
		property1.set(newValue);
		assertNotEquals(property1, property2);
		assertEquals(newValue, property1.get());
		assertNotEquals(newValue, property2.get());

		// bind on null (yields NPE)
		try {
			property2.bindBidirectional(null);
			fail("Expected NullPointerException because binding to null is not valid.");
		} catch (NullPointerException e) {
			// expected
		}

		// unbind from null (yields NPE)
		try {
			property2.unbindBidirectional(null);
			fail("Expected NullPointerException because binding to null is not valid.");
		} catch (NullPointerException e) {
			// expected
		}

		// bind on itself (yields IAE)
		try {
			property2.bindBidirectional(property2);
			fail("Expected IllegalArgumentException because binding to itself is not valid.");
		} catch (IllegalArgumentException e) {
			// exptected
		}

		// unbind from itself (yields IAE)
		try {
			property2.unbindBidirectional(property2);
			fail("Expected IllegalArgumentException because binding to itself is not valid.");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	/**
	 * Test the bidirectional content bindings as offered by
	 * {@link ReadOnlySetMultimapProperty}.
	 */
	@Test
	public void bidirectionalContentBinding() {
		SetMultimapProperty<Integer, String> property1 = propertyProvider.get();
		SetMultimapProperty<Integer, String> property2 = propertyProvider.get();
		ObservableSetMultimap<Integer, String> backupMap = new ObservableSetMultimapWrapper<>(
				HashMultimap.<Integer, String> create());

		property1.bindContentBidirectional(property2);
		// XXX: According to JavaFX contract, a content binding does not lead to
		// the properties being regarded as being bound
		assertFalse(property1.isBound());
		assertFalse(property2.isBound());

		// put
		property1.put(1, "1-1");
		backupMap.put(1, "1-1");
		check(property1, property2, backupMap);

		// putAll - single key
		property1.putAll(1, Sets.newHashSet("1-2", "1-3"));
		backupMap.putAll(1, Sets.newHashSet("1-2", "1-3"));
		check(property1, property2, backupMap);

		// putAll - multiple keys
		SetMultimap<Integer, String> toAdd = HashMultimap.create();
		toAdd.putAll(2, Sets.newHashSet("2-1", "2-2"));
		toAdd.putAll(3, Sets.newHashSet("3-1", "3-2"));
		property1.putAll(toAdd);
		backupMap.putAll(toAdd);
		check(property1, property2, backupMap);

		// remove
		property1.remove(1, "1-1");
		backupMap.remove(1, "1-1");
		check(property1, property2, backupMap);

		// remove all - multiple keys
		property1.removeAll(2);
		backupMap.removeAll(2);
		check(property1, property2, backupMap);

		// replace values
		property1.replaceValues(3, Sets.newHashSet("3-3", "3-4"));
		backupMap.replaceValues(3, Sets.newHashSet("3-3", "3-4"));
		check(property1, property2, backupMap);

		// clear
		property1.clear();
		backupMap.clear();
		check(property1, property2, backupMap);

		// put
		property2.put(1, "1-1");
		backupMap.put(1, "1-1");
		check(property1, property2, backupMap);

		// putAll - single key
		property2.putAll(1, Sets.newHashSet("1-2", "1-3"));
		backupMap.putAll(1, Sets.newHashSet("1-2", "1-3"));
		check(property1, property2, backupMap);

		// putAll - multiple keys
		toAdd = HashMultimap.create();
		toAdd.putAll(2, Sets.newHashSet("2-1", "2-2"));
		toAdd.putAll(3, Sets.newHashSet("3-1", "3-2"));
		property2.putAll(toAdd);
		backupMap.putAll(toAdd);
		check(property1, property2, backupMap);

		// remove
		property2.remove(1, "1-1");
		backupMap.remove(1, "1-1");
		check(property1, property2, backupMap);

		// remove all - multiple keys
		property2.removeAll(2);
		backupMap.removeAll(2);
		check(property1, property2, backupMap);

		// replace values
		property2.replaceValues(3, Sets.newHashSet("3-3", "3-4"));
		backupMap.replaceValues(3, Sets.newHashSet("3-3", "3-4"));
		check(property1, property2, backupMap);

		// clear
		property2.clear();
		backupMap.clear();
		check(property1, property2, backupMap);

		// unbind property2, ensure values are no longer synchronized
		property2.unbindContentBidirectional(property1);
		property1.put(1, "1-1");
		assertNotEquals(property2.get(), property1.get());
		assertNotEquals(property2.sizeProperty().get(),
				property1.sizeProperty().get());
		assertNotEquals(property2.emptyProperty().get(),
				property1.emptyProperty().get());

		// unbind property2 from null (yields NPE)
		try {
			property2.unbindContentBidirectional(null);
			fail("Expected NullPointerException.");
		} catch (NullPointerException e) {
			assertEquals("Cannot bind to null value.", e.getMessage());
		}

		// unbind property2 from itself (yields IAE)
		try {
			property2.unbindContentBidirectional(property2);
			fail("Expected IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot bind source to itself.", e.getMessage());
		}

		// bind property2 to null (yields NPE)
		try {
			property2.bindContentBidirectional(null);
			fail("Expected NullPointerException.");
		} catch (NullPointerException e) {
			assertEquals("Cannot bind to null value.", e.getMessage());
		}

		// bind property2 to itself (yields IAE)
		try {
			property2.bindContentBidirectional(property2);
			fail("Expected IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot bind source to itself.", e.getMessage());
		}
	}

	/**
	 * Check change notifications for observed value changes are properly fired.
	 */
	@Test
	public void changeNotifications() {
		SetMultimapProperty<Integer, String> property = propertyProvider.get();

		// initialize property
		property.putAll(1, Sets.newHashSet("1-1", "1-2", "1-3"));
		property.putAll(2, Sets.newHashSet("2-1", "2-2", "2-3"));

		// register listener
		InvalidationExpector invalidationListener = new InvalidationExpector();
		SetMultimapChangeExpector<Integer, String> setMultimapChangeListener = new SetMultimapChangeExpector<>(
				property);
		ChangeExpector<Integer, String> changeListener = new ChangeExpector<>(
				property);
		property.addListener(invalidationListener);
		property.addListener(setMultimapChangeListener);
		property.addListener(changeListener);

		// change property value (disjoint values)
		ObservableSetMultimap<Integer, String> newValue = new ObservableSetMultimapWrapper<>(
				HashMultimap.<Integer, String> create());
		newValue.putAll(3, Sets.newHashSet("3-1", "3-2", "3-3"));
		newValue.putAll(4, Sets.newHashSet("4-1", "4-2", "4-3"));
		invalidationListener.expect(1);
		changeListener.addExpectation(property.get(), newValue);
		setMultimapChangeListener.addAtomicExpectation();
		setMultimapChangeListener.addElementaryExpectation(1,
				Sets.newHashSet("1-1", "1-2", "1-3"),
				Collections.<String> emptySet());
		setMultimapChangeListener.addElementaryExpectation(2,
				Sets.newHashSet("2-1", "2-2", "2-3"),
				Collections.<String> emptySet());
		setMultimapChangeListener.addElementaryExpectation(3,
				Collections.<String> emptySet(),
				Sets.newHashSet("3-1", "3-2", "3-3"));
		setMultimapChangeListener.addElementaryExpectation(4,
				Collections.<String> emptySet(),
				Sets.newHashSet("4-1", "4-2", "4-3"));
		property.set(newValue);
		invalidationListener.check();
		setMultimapChangeListener.check();
		changeListener.check();

		// change property value (overlapping values)
		newValue = new ObservableSetMultimapWrapper<>(
				HashMultimap.<Integer, String> create());
		newValue.putAll(1, Sets.newHashSet("1-1", "1-2", "1-3"));
		newValue.putAll(3, Sets.newHashSet("3-2", "3-4"));
		newValue.putAll(4, Sets.newHashSet("4-1", "4-2", "4-3"));
		invalidationListener.expect(1);
		changeListener.addExpectation(property.get(), newValue);
		setMultimapChangeListener.addAtomicExpectation();
		setMultimapChangeListener.addElementaryExpectation(3,
				Sets.newHashSet("3-1", "3-3"), Sets.newHashSet("3-4"));
		setMultimapChangeListener.addElementaryExpectation(1,
				Collections.<String> emptySet(),
				Sets.newHashSet("1-1", "1-2", "1-3"));
		property.set(newValue);
		invalidationListener.check();
		setMultimapChangeListener.check();
		changeListener.check();

		// change property value (change to null)
		invalidationListener.expect(1);
		changeListener.addExpectation(property.get(), null);
		setMultimapChangeListener.addAtomicExpectation();
		setMultimapChangeListener.addElementaryExpectation(1,
				Sets.newHashSet("1-1", "1-2", "1-3"),
				Collections.<String> emptySet());
		setMultimapChangeListener.addElementaryExpectation(3,
				Sets.newHashSet("3-2", "3-4"), Collections.<String> emptySet());
		setMultimapChangeListener.addElementaryExpectation(4,
				Sets.newHashSet("4-1", "4-2", "4-3"),
				Collections.<String> emptySet());
		property.set(null);
		invalidationListener.check();
		setMultimapChangeListener.check();
		changeListener.check();

		// change property value (change from null)
		newValue = new ObservableSetMultimapWrapper<>(
				HashMultimap.<Integer, String> create());
		newValue.putAll(1, Sets.newHashSet("1-1", "1-2", "1-3"));
		invalidationListener.expect(1);
		changeListener.addExpectation(null, newValue);
		setMultimapChangeListener.addAtomicExpectation();
		setMultimapChangeListener.addElementaryExpectation(1,
				Collections.<String> emptySet(),
				Sets.newHashSet("1-1", "1-2", "1-3"));
		property.set(newValue);
		invalidationListener.check();
		setMultimapChangeListener.check();
		changeListener.check();
	}

	protected void check(SetMultimapProperty<Integer, String> property1,
			SetMultimapProperty<Integer, String> property2,
			ObservableSetMultimap<Integer, String> backupMap) {
		assertEquals(property1, property2);
		assertEquals(property1.get(), property2.get());
		assertEquals(backupMap, property1.get());
		assertEquals(backupMap, property2.get());
		assertEquals(backupMap.size(), property1.sizeProperty().get());
		assertEquals(backupMap.size(), property2.sizeProperty().get());
		assertEquals(backupMap.isEmpty(), property1.emptyProperty().get());
		assertEquals(backupMap.isEmpty(), property2.emptyProperty().get());
	}

	@Test
	public void unidirectionalBinding() {
		SetMultimapProperty<Integer, String> property1 = propertyProvider.get();
		SetMultimapProperty<Integer, String> property2 = propertyProvider.get();

		// bind properly
		property2.bind(property1);
		assertFalse(property1.isBound());
		assertTrue(property2.isBound());

		// change value of first property
		ObservableSetMultimap<Integer, String> newValue = new ObservableSetMultimapWrapper<>(
				HashMultimap.<Integer, String> create());
		newValue.put(1, "1-1");
		property1.set(newValue);
		assertEquals(newValue, property1.get());
		assertEquals(newValue, property2.get());
		assertEquals(property1, property2);

		// set value on second (bound) property (yields IAE)
		try {
			property2.set(new ObservableSetMultimapWrapper<>(
					HashMultimap.<Integer, String> create()));
			fail("Expected IllegalArgumentException because property is bound.");
		} catch (IllegalArgumentException e) {
			assertEquals("A bound value cannot be set.", e.getMessage());
		}

		// unbind
		property2.unbind();
		assertFalse(property1.isBound());
		assertFalse(property2.isBound());

		// change value after binding has been removed
		newValue = new ObservableSetMultimapWrapper<>(
				HashMultimap.<Integer, String> create());
		newValue.put(3, "3-1");
		property1.set(newValue);
		assertNotEquals(property1, property2);
		assertEquals(newValue, property1.get());
		assertNotEquals(newValue, property2.get());

		// bind on null (yields NPE)
		try {
			property2.bind(null);
			fail("Expected NullPointerException because binding to null is not valid.");
		} catch (NullPointerException e) {
			assertEquals("Cannot bind to null.", e.getMessage());
		}

		// according to JavaFX, binding on itself does not yield an IAE here
	}

	/**
	 * Test the unidirectional content bindings as offered by
	 * {@link ReadOnlySetMultimapProperty}.
	 */
	@Test
	public void unidirectionalContentBinding() {
		SetMultimapProperty<Integer, String> property1 = propertyProvider.get();
		SetMultimapProperty<Integer, String> property2 = propertyProvider.get();
		ObservableSetMultimap<Integer, String> backupMap = new ObservableSetMultimapWrapper<>(
				HashMultimap.<Integer, String> create());

		property2.bindContent(property1);
		// XXX: According to JavaFX contract, content binding does not lead to
		// the properties being regarded as being bound.
		assertFalse(property1.isBound());
		assertFalse(property2.isBound());

		// put
		property1.put(1, "1-1");
		backupMap.put(1, "1-1");
		check(property1, property2, backupMap);

		// putAll - single key
		property1.putAll(1, Sets.newHashSet("1-2", "1-3"));
		backupMap.putAll(1, Sets.newHashSet("1-2", "1-3"));
		check(property1, property2, backupMap);

		// putAll - multiple keys
		SetMultimap<Integer, String> toAdd = HashMultimap.create();
		toAdd.putAll(2, Sets.newHashSet("2-1", "2-2"));
		toAdd.putAll(3, Sets.newHashSet("3-1", "3-2"));
		property1.putAll(toAdd);
		backupMap.putAll(toAdd);
		check(property1, property2, backupMap);

		// remove
		property1.remove(1, "1-1");
		backupMap.remove(1, "1-1");
		check(property1, property2, backupMap);

		// remove all - multiple keys
		property1.removeAll(2);
		backupMap.removeAll(2);
		check(property1, property2, backupMap);

		// replace values
		property1.replaceValues(3, Sets.newHashSet("3-3", "3-4"));
		backupMap.replaceValues(3, Sets.newHashSet("3-3", "3-4"));
		check(property1, property2, backupMap);

		// clear
		property1.clear();
		backupMap.clear();
		check(property1, property2, backupMap);

		// unbind property2, ensure values are no longer synchronized
		property2.unbindContent(property1);
		property1.put(1, "1-1");
		assertNotEquals(property2.get(), property1.get());
		assertNotEquals(property2.sizeProperty().get(),
				property1.sizeProperty().get());
		assertNotEquals(property2.emptyProperty().get(),
				property1.emptyProperty().get());

		// unbind property2 from null (yields NPE)
		try {
			property2.unbindContent(null);
			fail("Expected NullPointerException.");
		} catch (NullPointerException e) {
			assertEquals("Cannot unbind from null value.", e.getMessage());
		}

		// unbind property2 from itself (yields IAE)
		try {
			property2.unbindContent(property2);
			fail("Expected IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot unbind source to itself.", e.getMessage());
		}

		// bind property2 to null (yields NPE)
		try {
			property2.bindContent(null);
			fail("Expected NullPointerException.");
		} catch (NullPointerException e) {
			assertEquals("Cannot bind to null value.", e.getMessage());
		}

		// bind property2 to itself (yields IAE)
		try {
			property2.bindContent(property2);
			fail("Expected IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot bind source to itself.", e.getMessage());
		}
	}
}
