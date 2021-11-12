package dml.gremlin.assemblyLine;

/*让step接口 extends 这个接口*/
public interface Compute<S,E> {
	default E compute(S e) {
		throw new UnsupportedOperationException("compute");
	}
	
	default void compute(Buffer<S> up, Buffer<S> down) {
		throw new UnsupportedOperationException("compute");
	}
}
