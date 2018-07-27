/*
 * (C) Copyright 2018 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     pierre
 */
package org.nuxeo.runtime.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Default generic descriptor registry.
 * <p>
 * It handles (un)registering and merged retrieval.
 * <p>
 * Merge algorithm depends on {@code Descriptor} implementations.
 * <p>
 *
 * @since 10.3
 */
public class DescriptorRegistry {

    // target -> xp -> id -> list of descriptors
    protected Map<String, Map<String, Map<String, List<Descriptor>>>> descriptors = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends Descriptor> T getDescriptor(String target, String xp, String id) {
        return (T) merge(descriptors.getOrDefault(target, Collections.emptyMap())
                                    .getOrDefault(xp, Collections.emptyMap())
                                    .getOrDefault(id, Collections.emptyList()));

    }

    @SuppressWarnings("unchecked")
    public <T extends Descriptor> List<T> getDescriptors(String target, String xp) {
        Map<String, List<Descriptor>> descriptorsByXP = descriptors.getOrDefault(target, Collections.emptyMap())
                                                                   .getOrDefault(xp, Collections.emptyMap());
        List<T> result = new ArrayList<>(descriptorsByXP.size());
        descriptorsByXP.values()
                       .stream()
                       .forEachOrdered(l -> {
                           T o = (T) merge(l);
                           if (o != null) {
                               result.add(o);
                           }
                       });
        return result;
    }

    public boolean register(String target, String xp, Descriptor desciptor) {
        return descriptors.computeIfAbsent(target, k -> new HashMap<>())
                          .computeIfAbsent(xp, k -> new LinkedHashMap<>())
                          .computeIfAbsent(desciptor.getId(), k -> new ArrayList<>())
                          .add(desciptor);

    }

    public boolean unregister(String target, String xp, Descriptor descriptor) {
        return descriptors.getOrDefault(target, Collections.emptyMap())
                          .getOrDefault(xp, Collections.emptyMap())
                          .getOrDefault(descriptor.getId(), Collections.emptyList())
                          .remove(descriptor);
    }

    protected Descriptor merge(Collection<Descriptor> descriptors) {
        Descriptor descriptor = null;
        for (Descriptor d : descriptors) {
            if (d.remove()) {
                descriptor = null;
                continue;
            }
            descriptor = descriptor == null ? d : descriptor.merge(d);
        }
        return descriptor;
    }

    public void clear() {
        descriptors.clear();
    }

}
