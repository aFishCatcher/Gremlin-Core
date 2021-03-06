/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tinkerpop.gremlin.process.traversal.step.map;

import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.AbstractStep;
import org.apache.tinkerpop.gremlin.structure.util.CloseableIterator;
import org.apache.tinkerpop.gremlin.util.iterator.EmptyIterator;

import dml.gremlin.myThreadPool.TaskDataBuffer;

import java.util.Iterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class FlatMapStep<S, E> extends AbstractStep<S, E> {

    private Traverser.Admin<S> head = null;
    private Iterator<E> iterator = EmptyIterator.instance();

    public FlatMapStep(final Traversal.Admin traversal) {
        super(traversal);
    }

    @Override
    protected Traverser.Admin<E> processNextStart() {
        while (true) {
            if (this.iterator.hasNext()) {
                return this.head.split(this.iterator.next(), this);
            } else {
                closeIterator();
                this.head = this.starts.next();
                this.iterator = this.flatMap(this.head);
            }
        }
    }

    protected abstract Iterator<E> flatMap(final Traverser.Admin<S> traverser);

    @Override
    public void reset() {
        super.reset();
        closeIterator();
        this.iterator = EmptyIterator.instance();
    }

    protected void closeIterator() {
        CloseableIterator.closeIterator(iterator);
    }
    
    @Override
    public TaskDataBuffer<Traverser.Admin<E>> work(TaskDataBuffer<Traverser.Admin<S>> in) {
    	TaskDataBuffer<Traverser.Admin<E>> out = new TaskDataBuffer<>(in.getCurNum(), in.isEnd());
    	Iterator<Traverser.Admin<S>> it = in.iterator();
    	while(it.hasNext()) {
    		Traverser.Admin<S> t = it.next();  //here t equals to this.head
    		Iterator<E> iterator = this.flatMap(t);
    		while(iterator.hasNext())
        		out.add(t.split(iterator.next(),this));
    		CloseableIterator.closeIterator(iterator);
    	}
    	return out;
    }
}
