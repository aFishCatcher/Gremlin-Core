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
    
    @Override
    public List<Traverser> compute(Traverser t) {
    	this.sideEffect(t.asAdmin());
    	return Arrays.asList(t);
    }
}
