/*
 * (C) Copyright 2015 Nuxeo SA (http://nuxeo.com/) and others.
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
package org.nuxeo.ecm.core.io.download;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.runtime.model.Descriptor;

/**
 * Descriptor for the permissions associated to a blob download.
 *
 * @since 7.4
 */
@XObject("permission")
public class DownloadPermissionDescriptor implements Descriptor {

    public static final String DEFAULT_SCRIPT_LANGUAGE = "JavaScript";

    @XNode("@name")
    public String name;

    @XNode("script")
    public String script;

    @XNode("script@language")
    public String scriptLanguage = DEFAULT_SCRIPT_LANGUAGE;

    @Override
    public String getId() {
        return name;
    }

    @Override
    public Descriptor merge(Descriptor other) {
        if (other == null || !(other instanceof DownloadPermissionDescriptor)) {
            return this;
        }
        DownloadPermissionDescriptor casted = (DownloadPermissionDescriptor) other;
        DownloadPermissionDescriptor merged = new DownloadPermissionDescriptor();
        merged.name = casted.name != null ? casted.name : name;
        merged.script = casted.script != null ? casted.script : script;
        merged.scriptLanguage = casted.scriptLanguage != null ? casted.scriptLanguage : scriptLanguage;
        return merged;
    }

}
