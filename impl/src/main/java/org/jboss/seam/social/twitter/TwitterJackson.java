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
package org.jboss.seam.social.twitter;



import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.jboss.seam.social.core.HttpResponse;
import org.jboss.seam.social.core.JsonMapper;
import org.jboss.seam.social.core.OAuthService;
import org.jboss.seam.social.core.OAuthServiceBase;
import org.jboss.seam.social.core.OAuthServiceSettings;
import org.jboss.seam.social.core.RelatedTo;
import org.jboss.seam.social.core.RestVerb;
import org.jboss.seam.social.core.UserProfile;
import org.jboss.seam.social.twitter.model.CredentialJackson;
import org.jboss.seam.social.twitter.model.Tweet;
import org.jboss.seam.social.twitter.model.TweetJackson;

/**
 * @author Antoine Sabot-Durand
 */


public class TwitterJackson extends OAuthServiceBase implements Twitter {

    private static final long serialVersionUID = 6806035986656777834L;
    static final String VERIFY_CREDENTIALS_URL = "https://api.twitter.com/1/account/verify_credentials.json";
    static final String FRIENDS_STATUSES_URL = "https://api.twitter.com/1/statuses/friends.json?screen_name={screen_name}";
    static final String SEARCH_URL = "https://search.twitter.com/search.json?q={query}&rpp={rpp}&page={page}";
    static final String TWEET_URL = "https://api.twitter.com/1/statuses/update.json";
    static final String RETWEET_URL = "https://api.twitter.com/1/statuses/retweet/{tweet_id}.json";
    static final String LOGO_URL = "https://d2l6uygi1pgnys.cloudfront.net/2-2-08/images/buttons/twitter_connect.png";
    
    @Inject
    Logger log;

    @Inject
    private JsonMapper jsonMapper;

   
    
    @Produces
    @RelatedTo(TwitterJackson.TYPE)
    protected OAuthService qualifiedTwitterProducer(@New TwitterJackson service) {
        return service;
    }
   
    @Override
    public Tweet updateStatus(String message) {
        HttpResponse resp = sendSignedRequest(RestVerb.POST, TWEET_URL, "status", message);
        System.out.println("update satus is " + message);
        setStatus("");
        return jsonMapper.readValue(resp, TweetJackson.class);

    }

   

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.seam.social.oauth.OAuthService#getServiceLogo()
     */
    @Override
    public String getServiceLogo() {
        return LOGO_URL;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.seam.social.oauth.OAuthService#getUserProfile()
     */
    @Override
    protected UserProfile getUser() {
            HttpResponse resp = sendSignedRequest(RestVerb.GET, VERIFY_CREDENTIALS_URL);
            return jsonMapper.readValue(resp, CredentialJackson.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.seam.social.oauth.OAuthService#getType()
     */
    @Override
    public String getType() {
        return TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.seam.social.twitter.Twitter#updateStatus()
     */
    @Override
    public Tweet updateStatus() {
        return updateStatus(getStatus());
    }
    
    @PostConstruct
    void init()
    {
        log.info("== Creating a new Twitter Bean : " + this.hashCode());
    }
    
    
    @PreDestroy
    void destroy()
    {
        log.info("== Destroying Twitter Bean : " + this.hashCode());
    }

}
