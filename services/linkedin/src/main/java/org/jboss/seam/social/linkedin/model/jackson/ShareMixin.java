/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.social.linkedin.model.jackson;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.jboss.seam.social.linkedin.model.Share.ShareContent;
import org.jboss.seam.social.linkedin.model.Share.ShareSource;

/**
 * 
 * @author Antoine Sabot-Durand
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class ShareMixin {

    @JsonCreator
    ShareMixin(@JsonProperty("comment") String comment, @JsonProperty("content") ShareContent content,
            @JsonProperty("id") String id, @JsonProperty("source") ShareSource source,
            @JsonProperty("timestamp") Date timestamp,
            @JsonProperty("visibility") @JsonDeserialize(using = CodeDeserializer.class) String visibility) {
    }

}
