/*
 * (C) Copyright 2017-2018 Nuxeo (http://nuxeo.com/) and others.
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
 *     Florent Guillaume
 */
package org.nuxeo.runtime.migration;

import java.util.LinkedHashMap;
import java.util.Map;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeMap;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.runtime.model.Descriptor;

/**
 * Descriptor of a Migration, consisting of States and Steps.
 *
 * @since 9.3
 */
@XObject("migration")
public class MigrationDescriptor implements Descriptor {

    @XObject("state")
    public static class MigrationStateDescriptor implements Descriptor {

        @XNode("@id")
        public String id;

        @XNode("description@label")
        public String descriptionLabel;

        @XNode("description")
        public String description;

        @Override
        public String getId() {
            return id;
        }

    }

    @XObject("step")
    public static class MigrationStepDescriptor implements Descriptor {

        @XNode("@id")
        public String id;

        @XNode("@fromState")
        public String fromState;

        @XNode("@toState")
        public String toState;

        @XNode("description@label")
        public String descriptionLabel;

        @XNode("description")
        public String description;

        @Override
        public String getId() {
            return id;
        }
    }

    @XNode("@id")
    public String id;

    @XNode("description@label")
    public String descriptionLabel;

    @XNode("description")
    public String description;

    @XNode("class")
    public Class<?> klass;

    @XNode("defaultState")
    public String defaultState;

    @XNodeMap(value = "state", key = "@id", type = LinkedHashMap.class, componentType = MigrationStateDescriptor.class)
    public Map<String, MigrationStateDescriptor> states = new LinkedHashMap<>();

    @XNodeMap(value = "step", key = "@id", type = LinkedHashMap.class, componentType = MigrationStepDescriptor.class)
    public Map<String, MigrationStepDescriptor> steps = new LinkedHashMap<>();

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Descriptor merge(Descriptor other) {
        if (other == null || !(other instanceof MigrationDescriptor)) {
            return this;
        }
        MigrationDescriptor casted = (MigrationDescriptor) other;
        MigrationDescriptor merged = new MigrationDescriptor();
        merged.id = casted.id != null ? casted.id : id;
        merged.klass = casted.klass != null ? casted.klass : klass;
        merged.description = casted.description != null ? casted.description : description;
        merged.defaultState = casted.defaultState != null ? casted.defaultState : defaultState;
        merged.descriptionLabel = casted.descriptionLabel != null ? casted.descriptionLabel : descriptionLabel;
        merged.steps.putAll(steps);
        merged.steps.putAll(casted.steps);
        merged.states.putAll(states);
        merged.states.putAll(casted.states);
        return merged;
    }

}
