/*******************************************************************************
 * Copyright (c) 2018 itemis AG and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Zoey Gerrit Prigge (itemis AG) - initial API and implementation (bug #532244)
 *******************************************************************************/
package org.eclipse.gef.dot.internal.ui.language.doubleclicking;

import org.eclipse.gef.dot.internal.ui.language.editor.DotHtmlLabelTerminalsTokenTypeToPartitionMapper;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.xtext.ui.editor.doubleClicking.AbstractWordAwareDoubleClickStrategy;
import org.eclipse.xtext.ui.editor.doubleClicking.DoubleClickStrategyProvider;

public class DotHtmlLabelDoubleClickStrategyProvider
		extends DoubleClickStrategyProvider {

	@Override
	public ITextDoubleClickStrategy getStrategy(ISourceViewer sourceViewer,
			String contentType, String documentPartitioning) {
		if (DotHtmlLabelTerminalsTokenTypeToPartitionMapper.TEXT_PARTITION
				.equals(contentType)) {
			return new AbstractWordAwareDoubleClickStrategy();
		}
		return super.getStrategy(sourceViewer, contentType,
				documentPartitioning);
	}

}
