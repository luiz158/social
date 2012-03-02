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
package org.jboss.seam.social.scribe;

import java.io.InputStream;
import java.util.Map;

import org.jboss.seam.social.rest.RestResponse;
import org.scribe.model.Response;

/**
 * @author Antoine Sabot-Durand
 */
public class RestResponseScribe implements RestResponse {

    private Response getDelegate() {
        if (delegate == null) {
            throw new IllegalStateException("Http Response is invalid");
        }
        return delegate;
    }

    private final Response delegate;
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    protected RestResponseScribe(Response response, String url) {
        delegate = response;
        this.url = url;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.seam.social.oauth.HttpResponse#getBody()
     */
    @Override
    public String getBody() {
        return getDelegate().getBody();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.seam.social.oauth.HttpResponse#getStream()
     */
    @Override
    public InputStream getStream() {
        return getDelegate().getStream();
    }

    @Override
    public int hashCode() {
        return getDelegate().hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.seam.social.oauth.HttpResponse#getCode()
     */
    @Override
    public int getCode() {
        return getDelegate().getCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.seam.social.oauth.HttpResponse#getHeaders()
     */
    @Override
    public Map<String, String> getHeaders() {
        return getDelegate().getHeaders();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.seam.social.oauth.HttpResponse#getHeader(java.lang.String)
     */
    @Override
    public String getHeader(String name) {
        return getDelegate().getHeader(name);
    }

    @Override
    public boolean equals(Object obj) {
        return getDelegate().equals(obj);
    }

    @Override
    public String toString() {
        return getDelegate().toString();
    }

    @Override
    public String getUrl() {
        return url;
    }

}
