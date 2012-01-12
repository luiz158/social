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
package org.jboss.seam.social.twitter.jackson;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.seam.social.twitter.Trend;

/**
 * Holder of a list of weekly trends.
 * 
 * @author Craig Walls
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class WeeklyTrendsList extends AbstractTrendsList {

    @JsonCreator
    public WeeklyTrendsList(@JsonProperty("trends") Map<String, List<Trend>> trends) {
        super(trends, new SimpleDateFormat("yyyy-MM-dd"));
    }

}
