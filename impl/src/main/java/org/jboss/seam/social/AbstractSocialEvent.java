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

/**
 * @author Antoine Sabot-Durand
 * 
 */
public abstract class AbstractSocialEvent implements SocialEvent {

    private final Status status;

    private final String message;

    private final Object payload;

    public AbstractSocialEvent(Status status, String message) {
        this(status, message, null);
    }

    public AbstractSocialEvent(Status status, String message, Object payload) {
        super();
        this.status = status;
        this.message = message;
        this.payload = payload;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Object getPayload() {
        return payload;
    }

}