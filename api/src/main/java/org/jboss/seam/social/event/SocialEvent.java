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
package org.jboss.seam.social.event;

import java.io.Serializable;

/**
 * @author Antoine Sabot-Durand
 * 
 */
public class SocialEvent<T> implements Serializable {

    public enum Status {
        SUCCESS, FAILURE
    }

    private final Status status;

    private final String message;

    private final T eventData;

    public SocialEvent(Status status, String message) {
        this(status, message, null);
    }

    public SocialEvent(Status status, String message, T payload) {
        super();
        this.status = status;
        this.message = message;
        this.eventData = payload;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getEventData() {
        return eventData;
    }

}
