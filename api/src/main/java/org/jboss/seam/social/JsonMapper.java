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

package org.jboss.seam.social;

import java.io.Serializable;

import org.jboss.seam.social.rest.RestResponse;

/**
 * 
 * Implementation of this Interface will manage Json Serialization / De-serialization to and from Object
 * 
 * @author Antoine Sabot-Durand
 * 
 */
public interface JsonMapper extends Serializable {

    /**
     * Parse the content of the provided {@link RestResponse} to de-serialize to an object of the given Class
     * 
     * @param resp the response to de-serialize
     * @param clazz the target class of the object
     * @return an object of the given Class with fields coming from the response
     */
    public <T> T mapToObject(RestResponse resp, Class<T> clazz);

    /**
     * @param obj
     * @return
     */
    public String ObjectToJsonString(Object obj);

}