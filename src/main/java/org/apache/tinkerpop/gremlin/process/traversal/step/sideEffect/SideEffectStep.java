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
package org.apache.tinkerpop.gremlin.process.traversal.step.sideEffect;

import java.util.Arrays;
import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.AbstractStep;

import dml.gremlin.assemblyLine.Buffer;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class SideEffectStep<S> extends AbstractStep<S, S> {

    public SideEffectStep(final Traversal.Admin traversal) {
        super(traversal);
    }

    protected abstract void sideEffect(final Traverser.Admin<S> traverser);

    @Override
    protected Traverser.Admin<S> processNextStart() {
        final Traverser.Admin<S> traverser = this.starts.next();
        this.sideEffect(traverser);
        return traverser;
    }
    
    public void work() {
    	int size = this.getMyConsumer().getCopySize();
    	int put_num = 0;
    	for(int i=0; i<size; i++) {
    		Traverser.Admin t = this.getMyConsumer().pollElement().asAdmin();
    		this.sideEffect(t);
    		this.getMyProducer().offerElement(t);
    		put_num++;
    	}
    	this.getMyProducer().offerCopySize(put_num);
    }
    
    @Override
    public List<Traverser> compute(Traverser t) {
    	this.sideEffect(t.asAdmin());
    	return Arrays.asList(t);
    }
    
    @Override
    public void compute(Buffer<Traverser> up, Buffer<Traverser> down)  {
    	int N = up.takeNum();
    	int count = 0;
    	for(int i=0; i<N; i++) {
    		Traverser t = up.takeData();
    		this.sideEffect(t.asAdmin());
    		down.putData(t);
    		count++;
    	}
    	down.putNum(count);
    }
}
