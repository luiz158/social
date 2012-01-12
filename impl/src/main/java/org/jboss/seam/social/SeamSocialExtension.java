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

import static com.google.common.collect.Sets.newHashSet;

import java.lang.annotation.Annotation;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;

import org.apache.commons.lang3.StringUtils;
import org.jboss.seam.social.oauth.OAuthApplication;
import org.jboss.seam.social.oauth.OAuthService;
import org.jboss.solder.logging.Logger;
import org.jboss.solder.reflection.AnnotationInspector;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * @author Antoine Sabot-Durand
 * 
 */
@ApplicationScoped
public class SeamSocialExtension implements Extension {

    private Set<String> servicesNames = newHashSet();
    private Set<Annotation> servicesQualifiersConfigured = newHashSet();
    private static Set<Annotation> servicesQualifiersAvailable = newHashSet();
    private static BiMap<Annotation, String> servicesToQualifier = HashBiMap.create();
    private boolean multiSession = false;

    private static final Logger log = Logger.getLogger(SeamSocialExtension.class);

    /**
     * This observer methods build the list of existing Qualifiers having the ServiceRelated meta Annotation on configured
     * service (having the {@link OAuthApplication} Annotation)
     * 
     * @param pbean
     * @param beanManager
     */
    public void processSettingsBeans(@Observes ProcessBean<OAuthService> pbean, BeanManager beanManager) {

        log.info("Starting enumeration of existing service settings");
        Annotated annotated = pbean.getAnnotated();
        if (annotated.isAnnotationPresent(OAuthApplication.class)) {
            servicesQualifiersConfigured.addAll(AnnotationInspector.getAnnotations(annotated, ServiceRelated.class));
        }
    }

    /**
     * This Listener build the List of existing OAuthServices with a Qualifier having the meta annotation @ServiceRelated
     * 
     * @param pbean
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void processServicesBeans(@Observes ProcessBean<ServiceConfiguration> pbean) throws InstantiationException,
            IllegalAccessException {
        Set<Annotation> qualifiers = AnnotationInspector.getAnnotations(pbean.getAnnotated(), ServiceRelated.class);
        servicesQualifiersAvailable.addAll(qualifiers);
    }

    public Set<String> getSocialRelated() {
        return servicesNames;
    }

    public void processAfterDeploymentValidation(@Observes AfterDeploymentValidation adv) {
        log.info("validation phase");
        for (Annotation qual : servicesQualifiersAvailable) {
            String path = qual.annotationType().getName();
            String name = "";
            log.infof("Found service qualifier : %s", path);
            try {
                ResourceBundle bundle = ResourceBundle.getBundle(path);
                name = bundle.getString("service.name");
            } catch (MissingResourceException e) {
                log.warnf("No properties configuration file found for %s creating default service name", path);
                name = StringUtils.substringAfterLast(path, ".");
            } finally {
                servicesToQualifier.put(qual, name);
            }

        }
        for (Annotation annot : servicesQualifiersConfigured) {
            servicesNames.add(servicesToQualifier.get(annot));
        }

    }

    public static BiMap<Annotation, String> getServicesToQualifier() {
        return servicesToQualifier;
    }

    public static Set<Annotation> getServicesQualifiersAvailable() {
        return servicesQualifiersAvailable;
    }

    public boolean isMultiSession() {
        return multiSession;
    }

    public void setMultiSession(boolean multiSession) {
        this.multiSession = multiSession;
    }

}
