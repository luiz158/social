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
package org.jboss.seam.social.linkedin.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * A model class containing a list of a user's connections on LinkedIn.
 * 
 * @author Craig Walls
 * @author Antoine Sabot-Durand
 */
public class LinkedInConnections implements Serializable {

    private static final long serialVersionUID = -7227377144254629506L;

    private final List<LinkedInProfile> connections;

    public LinkedInConnections(List<LinkedInProfile> connections) {
        this.connections = connections;
    }

    /**
     * Retrieves the list of connected profiles.
     * 
     * @return a list of connected profiles
     */
    public List<LinkedInProfile> getConnections() {
        return connections;
    }

}
