/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: t; c-basic-offset: 4 -*- */
/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.slir;

import org.jdeferred2.Promise;
import org.jdeferred2.impl.DeferredObject;
import tripleo.elijah.comp.Compilation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created 11/6/21 8:23 AM
 */
public class RootSlirNode {
	private final Compilation compilation;
	private final List<SlirSourceNode> sourceNodes = new ArrayList<>();

	public RootSlirNode(final Compilation aCompilation) {
		compilation = aCompilation;
	}

	public SlirSourceNode newSourceNode(final SlirSourceFile aSourceFile) {
		final SlirSourceNode sourceNode = new SlirSourceNode(aSourceFile, this);
		sourceNodes.add(sourceNode);
		if (findFileNames.get(sourceNode.sourceFile().getFilename())!=null)
			findFileNames.get(sourceNode.sourceFile().getFilename()).resolve(sourceNode);
			// TODO consider removing, if that makes sense
		return sourceNode;
	}

	HashMap<String, DeferredObject<SlirSourceNode, Void, Void>> findFileNames = new HashMap<String, DeferredObject<SlirSourceNode, Void, Void>>();
	public Promise<SlirSourceNode, Void, Void> findFileName(final String aFileName) {
//		for (SlirSourceNode sourceNode : sourceNodes) {
//			if (sourceNode.sourceFile().getFilename().equals(aFileName))
//				return sourceNode;
//		}
//		return null;
		final DeferredObject<SlirSourceNode, Void, Void> p = new DeferredObject<SlirSourceNode, Void, Void>();
		findFileNames.put(aFileName, p);
		return p;
	}
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab:
//
