package com.tomecode.soa.dependency.analyzer.view.graph.flow.parts;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * 
 * @author Tomas Frastia
 * @see http://www.tomecode.com
 *      http://code.google.com/p/bpel-esb-dependency-analyzer/
 * 
 */
final class TopAnchor extends AbstractConnectionAnchor {

	private int offset;

	TopAnchor(IFigure source, int offset) {
		super(source);
		this.offset = offset;
	}

	public Point getLocation(Point reference) {
		Rectangle r = getOwner().getBounds().getCopy();
		getOwner().translateToAbsolute(r);
		int off = offset;
		if (off == -1)
			off = r.width / 2;
		if (r.contains(reference) || r.y < reference.y)
			return r.getBottomLeft().translate(off, -1);
		else
			return r.getTopLeft().translate(off, 0);
	}

}
