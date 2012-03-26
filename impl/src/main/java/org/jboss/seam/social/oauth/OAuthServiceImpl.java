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
package org.jboss.seam.social.oauth;

import static org.jboss.seam.social.SeamSocialExtension.getServicesToQualifier;
import static org.jboss.seam.social.rest.RestVerb.GET;
import static org.jboss.seam.social.rest.RestVerb.POST;
import static org.jboss.seam.social.rest.RestVerb.PUT;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jboss.seam.social.Current;
import org.jboss.seam.social.JsonMapper;
import org.jboss.seam.social.SeamSocialExtension;
import org.jboss.seam.social.UserProfile;
import org.jboss.seam.social.event.OAuthComplete;
import org.jboss.seam.social.event.SocialEvent;
import org.jboss.seam.social.exception.SeamSocialException;
import org.jboss.seam.social.rest.RestResponse;
import org.jboss.seam.social.rest.RestVerb;
import org.jboss.seam.social.utils.URLUtils;
import org.jboss.solder.logging.Logger;

/**
 * This Abstract implementation of {@link OAuthService} uses an {@link OAuthProvider} to deal with remote OAuth Services
 * 
 * 
 * @author Antoine Sabot-Durand
 */

public class OAuthServiceImpl implements OAuthService {

    private static final long serialVersionUID = -8423894021913341674L;
    private static final String VERIFIER_PARAM_NAME = "oauth_verifier";
    private static Annotation currentLiteral = new AnnotationLiteral<Current>() {
        private static final long serialVersionUID = -2929657732814790025L;
    };

    @Inject
    @Any
    private Instance<OAuthProvider> providers;

    @Inject
    @Any
    private Event<OAuthComplete> completeEventProducer;

    private Annotation qualifier;

    void setQualifier(Annotation qualifier) {
        this.qualifier = qualifier;
    }

    @Inject
    protected JsonMapper jsonService;

    @Inject
    private Logger log;

    @Inject
    @Any
    protected Instance<OAuthSession> sessionInstances;

    @Inject
    protected SeamSocialExtension socialConfig;

    private String type;

    @Override
    public String getType() {
        if (StringUtils.isEmpty(type))
            type = getServicesToQualifier().get(getQualifier());
        return type;
    }

    @Override
    public String getAuthorizationUrl() {
        return getProvider().getAuthorizationUrl(getRequestToken());
    }

    private OAuthProvider getProvider() {
        return providers.select(getQualifier()).get();
    }

    protected OAuthToken getRequestToken() {
        OAuthSession session = getSession();
        if (session.getRequestToken() == null)
            session.setRequestToken(getProvider().getRequestToken());
        return session.getRequestToken();
    }

    @Override
    public void initAccessToken() {
        OAuthSession session = getSession();
        if (session.getAccessToken() == null)
            session.setAccessToken(getProvider().getAccessToken(getRequestToken(), session.getVerifier()));
        if (session.getAccessToken() != null) {
            session.setRequestToken(null);
            log.info("firing event for " + getQualifier() + " OAuth complete cycle");
            Event<OAuthComplete> event = completeEventProducer.select(getQualifier());

            event.fire(new OAuthComplete(SocialEvent.Status.SUCCESS, "", session));
            log.info("After OAuth cycle completion");

        } else {
            // FIXME Launch an exception !!
        }

    }

    @Override
    public void resetConnection() {

        OAuthSession session = getSession();
        session.setAccessToken(null);
        session.setVerifier(null);
        session.setUserProfile(null);

    }

    @Override
    public RestResponse sendSignedRequest(OAuthRequest request) {
        getProvider().signRequest(getAccessToken(), request);
        return request.send();
    }

    @Override
    public RestResponse sendSignedRequest(RestVerb verb, String uri) {
        OAuthRequest request = getProvider().requestFactory(verb, uri);
        return sendSignedRequest(request);

    }

    @Override
    public RestResponse sendSignedRequest(RestVerb verb, String uri, String key, Object value) {
        OAuthRequest request = getProvider().requestFactory(verb, uri);

        request.addBodyParameter(key, value.toString());

        return sendSignedRequest(request);

    }

    @Override
    public RestResponse sendSignedXmlRequest(RestVerb verb, String uri, String payload) {
        OAuthRequest request = getProvider().requestFactory(verb, uri);
        request.addPayload(payload);
        return sendSignedRequest(request);

    }

    @Override
    public RestResponse sendSignedRequest(RestVerb verb, String uri, Map<String, ? extends Object> params) {
        OAuthRequest request = getProvider().requestFactory(verb, uri);
        for (Entry<String, ? extends Object> ent : params.entrySet()) {
            request.addBodyParameter(ent.getKey(), ent.getValue().toString());
        }
        return sendSignedRequest(request);

    }

    @Override
    public void setVerifier(String verifierStr) {
        OAuthSession session = getSession();
        session.setVerifier(verifierStr);
    }

    @Override
    public String getVerifier() {
        OAuthSession session = getSession();
        return session.getVerifier();
    }

    @Override
    public OAuthToken getAccessToken() {
        OAuthSession session = getSession();
        return session.getAccessToken();
    }

    @Override
    public boolean isConnected() {
        return getSession().isConnected();
    }

    @Override
    public void requireAuthorization() {
        if (!isConnected()) {
            throw new SeamSocialException("This action requires an OAuth connexion");
        }
    }

    @Override
    public void setAccessToken(String token, String secret) {
        OAuthSession session = getSession();
        session.setAccessToken(getProvider().tokenFactory(token, secret));

    }

    @Override
    public void setAccessToken(OAuthToken token) {
        OAuthSession session = getSession();
        session.setAccessToken(token);

    }

    @Override
    public String getVerifierParamName() {
        return VERIFIER_PARAM_NAME;
    }

    @Override
    public UserProfile getMyProfile() {
        return getSession().getUserProfile();
    }

    @Override
    public OAuthSession getSession() {
        OAuthSession session;
        if (socialConfig.isMultiSession())
            session = sessionInstances.select(currentLiteral).get();
        else
            session = sessionInstances.select(getQualifier()).get();
        return session;

    }

    @Override
    public <T> T getForObject(String uri, Class<T> clazz) {
        return jsonService.mapToObject(sendSignedRequest(GET, uri), clazz);
    }

    @Override
    public <T> T getForObject(String uri, Class<T> clazz, Map<String, ? extends Object> params) {
        return jsonService.mapToObject(sendSignedRequest(GET, uri, params), clazz);
    }

    @Override
    public <T> T getForObject(String uri, Class<T> clazz, Object... urlParams) {
        String url = MessageFormat.format(uri, urlParams);
        return jsonService.mapToObject(sendSignedRequest(GET, url), clazz);
    }

    @Override
    public <T> T postForObject(String uri, Map<String, ? extends Object> params, Class<T> clazz) {
        OAuthRequest request = getProvider().requestFactory(POST, uri);
        request.addBodyParameters(params);
        return jsonService.mapToObject(sendSignedRequest(request), clazz);
    }

    @Override
    public String postForLocation(String uri, Object toPost, Object... urlParams) {

        uri = MessageFormat.format(uri, urlParams);
        OAuthRequest request = getProvider().requestFactory(POST, uri);

        request.addPayload(jsonService.ObjectToJsonString(toPost));
        RestResponse response = sendSignedRequest(request);
        return response.getHeader("Location");
    }

    @Override
    public String postForLocation(String uri, Object toPost, Map<String, String> queryStringData, Object... urlParams) {
        if (queryStringData != null && !queryStringData.isEmpty()) {
            String encodedParams = URLUtils.doFormUrlEncode(queryStringData);
            if (uri.indexOf('?') == uri.length() - 1)
                uri += encodedParams;
            else if (uri.indexOf('?') == -1)
                uri += "?" + encodedParams;
            else
                uri += "&" + encodedParams;
        }
        return postForLocation(uri, toPost);
    }

    @Override
    public void put(String uri, Object toPut, Object... urlParams) {
        uri = MessageFormat.format(uri, urlParams);
        OAuthRequest request = getProvider().requestFactory(PUT, uri);

        request.addPayload(jsonService.ObjectToJsonString(toPut));
        sendSignedRequest(request);

    }

    @Override
    public void delete(String uri) {
        sendSignedRequest(RestVerb.DELETE, uri);
    }

    @Override
    public Annotation getQualifier() {
        return qualifier;
    }

}