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
package org.jboss.seam.social.twitter.model;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.social.twitter.TwitterGeoService;

/**
 * Represents the results of a similar places search. Includes places that match the search criteria and a
 * {@link PlacePrototype} that can be used to create a new place.
 * 
 * @author Craig Walls
 * @author Antoine Sabot-Durand
 */
@SuppressWarnings("serial")
public class SimilarPlaces extends ArrayList<Place> {

    private final PlacePrototype placePrototype;

    public SimilarPlaces(List<Place> places, PlacePrototype placePrototype) {
        super(places);
        this.placePrototype = placePrototype;
    }

    /**
     * A prototype place that matches the criteria for the call to
     * {@link TwitterGeoService#findSimilarPlaces(double, double, String)}, including a create token that can be used to create
     * the place.
     */
    public PlacePrototype getPlacePrototype() {
        return placePrototype;
    }

}
