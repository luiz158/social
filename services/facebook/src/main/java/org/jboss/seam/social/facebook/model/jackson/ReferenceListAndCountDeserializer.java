/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.social.facebook.model.jackson;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.jboss.seam.social.facebook.model.ListAndCount;
import org.jboss.seam.social.facebook.model.Reference;

class ReferenceListAndCountDeserializer extends JsonDeserializer<ListAndCount<Reference>> {

    @SuppressWarnings("unchecked")
    @Override
    public ListAndCount<Reference> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
            JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDeserializationConfig(ctxt.getConfig());
        jp.setCodec(mapper);
        if (jp.hasCurrentToken()) {
            JsonNode node = jp.readValueAsTree();
            JsonNode dataNode = node.get("data");
            List<Reference> commentsList = dataNode != null ? (List<Reference>) mapper.readValue(dataNode,
                    new TypeReference<List<Reference>>() {
                    }) : Collections.<Reference> emptyList();
            JsonNode countNode = node.get("count");
            int referenceCount = countNode != null ? countNode.getIntValue() : 0;
            return new ListAndCount<Reference>(commentsList, referenceCount);
        }

        return null;
    }
}
