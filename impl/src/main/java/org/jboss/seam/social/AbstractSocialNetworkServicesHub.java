/**
 * 
 */
package org.jboss.seam.social;

import org.jboss.seam.social.event.OAuthComplete;
import org.jboss.seam.social.oauth.OAuthSession;

/**
 * @author Antoine Sabot-Durand
 * 
 */
public abstract class AbstractSocialNetworkServicesHub extends AbstractOAuthServiceAwareImpl implements
        SocialNetworkServicesHub {

    @Override
    public UserProfile getMyProfile() {
        return getSession().getUserProfile();
    }

    @Override
    public void resetConnection() {

        OAuthSession session = getSession();
        session.setAccessToken(null);
        session.setVerifier(null);
        session.setUserProfile(null);

    }

    @Override
    public boolean isConnected() {
        return getSession().isConnected();
    }

    protected abstract void initMyProfile(OAuthComplete oauthComplete);

}
