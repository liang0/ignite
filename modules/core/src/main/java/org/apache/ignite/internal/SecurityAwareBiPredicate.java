/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal;

import java.util.UUID;
import org.apache.ignite.internal.processors.security.AbstractSecurityAwareExternalizable;
import org.apache.ignite.internal.processors.security.OperationSecurityContext;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.ignite.resources.IgniteInstanceResource;

/**
 * Security aware IgniteBiPredicate.
 */
public class SecurityAwareBiPredicate<E1, E2> extends AbstractSecurityAwareExternalizable<IgniteBiPredicate<E1, E2>>
    implements IgniteBiPredicate<E1, E2> {
    /** */
    private static final long serialVersionUID = 0L;

    /** Ignite. */
    @IgniteInstanceResource
    private transient IgniteEx ignite;

    /**
     * Default constructor.
     */
    public SecurityAwareBiPredicate() {
        // No-op.
    }

    /**
     * @param subjectId Security subject id.
     * @param original Original predicate.
     */
    public SecurityAwareBiPredicate(UUID subjectId, IgniteBiPredicate<E1, E2> original) {
        super(subjectId, original);
    }

    /** {@inheritDoc} */
    @Override public boolean apply(E1 e1, E2 e2) {
        try (OperationSecurityContext c = ignite.context().security().withContext(subjectId)) {
            return original.apply(e1, e2);
        }
    }
}
