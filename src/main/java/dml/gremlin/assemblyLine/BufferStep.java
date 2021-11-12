package dml.gremlin.assemblyLine;

import java.util.NoSuchElementException;

import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal.Admin;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.AbstractStep;

public class BufferStep<S> extends AbstractStep<S,S> {

	public BufferStep(final Traversal.Admin traversal) {
		super(traversal);
	}

	@Override
	protected Traverser.Admin<S> processNextStart() throws NoSuchElementException {
		final Traverser.Admin<S> traverser = this.starts.next();
		return null;
	}

}
