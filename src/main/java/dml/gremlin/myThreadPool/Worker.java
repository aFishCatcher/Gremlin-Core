package dml.gremlin.myThreadPool;

public interface Worker<S, E> {
	/*
	 * S: input type,
	 * E: output type,
	 * take the input, then process, final output.
	 * Step will implements Worker<LinkedBuffer, LinkedBuffer>.
	 */
	default E work(S in) {
		throw new UnsupportedOperationException("Worker: work");
	}

}
