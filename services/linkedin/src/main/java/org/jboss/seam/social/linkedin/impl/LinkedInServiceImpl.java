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
package org.jboss.seam.social.linkedin.impl;

import static java.util.Collections.singletonMap;
import static org.jboss.seam.social.LinkedInLiteral.INSTANCE;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.seam.social.UserProfile;
import org.jboss.seam.social.linkedin.api.model.Comment;
import org.jboss.seam.social.linkedin.api.model.Comments;
import org.jboss.seam.social.linkedin.api.model.CurrentShare;
import org.jboss.seam.social.linkedin.api.model.Likes;
import org.jboss.seam.social.linkedin.api.model.LinkedInNetworkUpdate;
import org.jboss.seam.social.linkedin.api.model.LinkedInNetworkUpdates;
import org.jboss.seam.social.linkedin.api.model.LinkedInProfile;
import org.jboss.seam.social.linkedin.api.model.LinkedInProfileFull;
import org.jboss.seam.social.linkedin.api.model.LinkedInProfiles;
import org.jboss.seam.social.linkedin.api.model.NetworkUpdateParameters;
import org.jboss.seam.social.linkedin.api.model.NewShare;
import org.jboss.seam.social.linkedin.api.model.ProfileField;
import org.jboss.seam.social.linkedin.api.model.SearchParameters;
import org.jboss.seam.social.linkedin.api.model.UpdateContentShare;
import org.jboss.seam.social.linkedin.api.model.UpdateTypeInput;
import org.jboss.seam.social.linkedin.api.services.LinkedInBaseService;
import org.jboss.seam.social.linkedin.api.services.NetworkUpdateService;
import org.jboss.seam.social.linkedin.api.services.ProfileService;
import org.jboss.seam.social.oauth.OAuthRequest;
import org.jboss.seam.social.oauth.OAuthServiceImpl;
import org.jboss.seam.social.rest.RestResponse;

/**
 * @author Antoine Sabot-Durand
 */
public class LinkedInServiceImpl extends OAuthServiceImpl implements LinkedInBaseService, ProfileService, NetworkUpdateService {

    private static final long serialVersionUID = -6718362913575146613L;

    static final String BASE_URL = "https://api.linkedin.com/v1/people/";

    static final String LOGO_URL = "https://d2l6uygi1pgnys.cloudfront.net/1-9-05/images/buttons/linkedin_connect.png";

    static final String NETWORK_UPDATE_URL = "http://api.linkedin.com/v1/people/~/person-activities";

    @Override
    protected RestResponse sendSignedRequest(OAuthRequest request) {
        request.addHeader("x-li-format", "json");
        return super.sendSignedRequest(request);
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
    public UserProfile getMyProfile() {
        return getSession().getUserProfile();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.seam.social.OAuthServiceBase#initMyProfile()
     */
    @Override
    protected void initMyProfile() {
        getSession().setUserProfile(getUserProfile());

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.seam.social.rest.RestService#getQualifier()
     */
    @Override
    public Annotation getQualifier() {
        return INSTANCE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.seam.social.oauth.OAuthServiceImpl#getApiRootUrl()
     */
    @Override
    public String getApiRootUrl() {
        // TODO Auto-generated method stub
        return null;
    }

    // ***** Profile Service

    static {
        StringBuffer b = new StringBuffer();
        b.append(BASE_URL).append("{id}:(");
        boolean first = true;
        for (ProfileField f : ProfileField.values()) {
            switch (f) {
                case CONNECTIONS:
                    break;
                default:
                    if (first) {
                        first = false;
                    } else {
                        b.append(',');
                    }
                    b.append(f);
            }
        }
        b.append(")?format=json");

        PROFILE_URL_FULL = b.toString();
    }

    static final String PROFILE_URL = BASE_URL
            + "{0}:(id,first-name,last-name,headline,industry,site-standard-profile-request,public-profile-url,picture-url,summary)?format=json";

    static final String PROFILE_URL_FULL;

    static final String PEOPLE_SEARCH_URL = "https://api.linkedin.com/v1/people-search:(people:(id,first-name,last-name,headline,industry,site-standard-profile-request,public-profile-url,picture-url,summary,api-standard-profile-request))?{&keywords}{&first-name}{&last-name}{&company-name}{&current-company}{&title}{&current-title}{&school-name}{&current-school}{&country-code}{&postal-code}{&distance}{&start}{&count}{&sort}";

    @Override
    public String getProfileId() {
        return getUserProfile().getId();
    }

    @Override
    public String getProfileUrl() {
        return getUserProfile().getPublicProfileUrl();
    }

    @Override
    public LinkedInProfile getUserProfile() {
        return getForObject(PROFILE_URL, LinkedInProfile.class, "~");
    }

    @Override
    public LinkedInProfileFull getUserProfileFull() {
        return getForObject(PROFILE_URL_FULL, LinkedInProfileFull.class, "~");
    }

    @Override
    public LinkedInProfile getProfileById(String id) {
        return getForObject(PROFILE_URL, LinkedInProfile.class, id);
    }

    @Override
    public LinkedInProfile getProfileByPublicUrl(String url) {
        return getForObject(PROFILE_URL, LinkedInProfile.class, url);
    }

    @Override
    public LinkedInProfileFull getProfileFullById(String id) {
        return getForObject(PROFILE_URL_FULL, LinkedInProfileFull.class, id);
    }

    @Override
    public LinkedInProfileFull getProfileFullByPublicUrl(String url) {
        return getForObject(PROFILE_URL_FULL, LinkedInProfileFull.class, url);
    }

    @Override
    public LinkedInProfiles search(SearchParameters parameters) {
        // TODO : create a linkedIn Search Api 2.0 compatible code
        throw new UnsupportedOperationException("Search not implemented yet");

        /*
         * String uri = StringUtils.processPlaceHolders(PEOPLE_SEARCH_URL, parameters); JsonNode node =
         * jsonService.mapToObject(expand(PEOPLE_SEARCH_URL, parameters), JsonNode.class); try { return
         * objectMapper.readValue(node.path("people"), LinkedInProfiles.class); } catch (Exception e) { throw new
         * RuntimeException(e); }
         */

    }

    // ***** Network update Service

    static {
        /*
         * The UPDATE_TYPE_ALL is required because by default the linked in Network Updates API does not return VIRL (Viral)
         * Updates such as User Liking or Commenting on another Users post
         */

        StringBuffer b = new StringBuffer();
        for (UpdateTypeInput t : UpdateTypeInput.values()) {
            b.append("&type=").append(t);
        }
        UPDATE_TYPE_ALL_STRING = b.toString();
    }

    @Override
    public List<LinkedInNetworkUpdate> getNetworkUpdates() {
        NetworkUpdateParameters parameters = new NetworkUpdateParameters(null, false, DEFAULT_START, DEFAULT_COUNT, null, null,
                true, false, Collections.<UpdateTypeInput> emptyList());
        return getNetworkUpdates(parameters);
    }

    @Override
    public List<LinkedInNetworkUpdate> getNetworkUpdates(int start, int count) {
        NetworkUpdateParameters parameters = new NetworkUpdateParameters(null, false, start, count, null, null, true, false,
                Collections.<UpdateTypeInput> emptyList());
        return getNetworkUpdates(parameters);
    }

    @Override
    public List<LinkedInNetworkUpdate> getNetworkUpdates(NetworkUpdateParameters parameters) {
        return getNetworkUpdates(parameters, LinkedInNetworkUpdates.class).getUpdates();
    }

    @Override
    public List<Comment> getNetworkUpdateComments(String updateKey) {
        return getForObject(UPDATE_COMMENTS_URL, Comments.class, updateKey).getComments();
    }

    @Override
    public void createNetworkUpdate(String update) {
        Map<String, String> activity = new HashMap<String, String>();
        activity.put("contentType", "linkedin-html");
        activity.put("body", update);
        put(ACTIVITY_URL, activity);
    }

    @Override
    public CurrentShare getCurrentShare() {
        return getForObject(CURRENT_SHARE_URL, UpdateContentShare.class).getCurrentShare();
    }

    @Override
    public String share(NewShare share) {
        return postForLocation(SHARE_URL, share);
    }

    @Override
    public void likeNetworkUpdate(String updateKey) {
        put(UPDATE_IS_LIKED_URL, Boolean.TRUE, updateKey);
    }

    @Override
    public void unlikeNetworkUpdate(String updateKey) {
        put(UPDATE_IS_LIKED_URL, Boolean.FALSE, updateKey);
    }

    @Override
    public void commentOnNetworkUpdate(String updateKey, String comment) {
        put(UPDATE_COMMENTS_URL, singletonMap("comment", comment), updateKey);
    }

    @Override
    public List<LinkedInProfile> getNetworkUpdateLikes(String updateKey) {
        return getForObject(UPDATE_LIKES_URL, Likes.class, updateKey).getLikes();
    }

    @Override
    public String getNetworkUpdatesJson(NetworkUpdateParameters parameters) {
        return getNetworkUpdates(parameters, String.class);
    }

    private <T> T getNetworkUpdates(NetworkUpdateParameters parameters, Class<T> responseType) {

        return getForObject(expand(UPDATES_URL, parameters), responseType);
    }

    public enum networkUpdatesFields {

        COUNT("count"), START("start"), SCOPE("scope"), BEFORE("before"), AFTER("after"), SHOW_HIDDEN_MEMBERS(
                "show-hidden-members");

        private final String urlName;

        private networkUpdatesFields(String urlName) {
            this.urlName = urlName;
        }

        @Override
        public String toString() {
            return urlName;
        }

    }

    static final String UPDATES_URL = BASE_URL
            + "{0}/network/updates?{&count}{&start}{&scope}{type}{&before}{&after}{&show-hidden-members}&format=json";

    static final String UPDATE_COMMENTS_URL = BASE_URL + "~/network/updates/key={0}/update-comments?format=json";

    static final String UPDATE_LIKES_URL = BASE_URL + "~/network/updates/key={0}/likes?format=json";

    static final String UPDATE_IS_LIKED_URL = BASE_URL + "~/network/updates/key={0}/is-liked?format=json";

    static final String ACTIVITY_URL = BASE_URL + "~/person-activities";

    static final String CURRENT_SHARE_URL = BASE_URL + "~:(current-share)";

    static final String SHARE_URL = BASE_URL + "~/shares";

    public static final int DEFAULT_START = 0;

    public static final int DEFAULT_COUNT = 10;

    private static final String UPDATE_TYPE_ALL_STRING;

}
