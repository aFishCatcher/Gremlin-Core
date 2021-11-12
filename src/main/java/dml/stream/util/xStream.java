package dml.stream.util;

import org.apache.tinkerpop.gremlin.process.traversal.Traverser;

public interface xStream {

	public default void setProducer(Producer<Traverser> buffer) {
		throw new UnsupportedOperationException("setProducer");
	}
    
    public default void setConsumer(Consumer<Traverser> buffer) {
    	throw new UnsupportedOperationException("setConsumer");
    }
    
    public default void init() {
    	throw new UnsupportedOperationException("init");
    }
    
    public default void work() {
    	throw new UnsupportedOperationException("work");
    }
}
