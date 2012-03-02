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
package org.jboss.seam.social.linkedin.impl.jackson;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.module.SimpleModule;
import org.jboss.seam.social.LinkedIn;
import org.jboss.seam.social.linkedin.api.model.ApiStandardProfileRequest;
import org.jboss.seam.social.linkedin.api.model.CodeAndName;
import org.jboss.seam.social.linkedin.api.model.Comment;
import org.jboss.seam.social.linkedin.api.model.Comments;
import org.jboss.seam.social.linkedin.api.model.Companies;
import org.jboss.seam.social.linkedin.api.model.Company;
import org.jboss.seam.social.linkedin.api.model.Company.CompanyAddress;
import org.jboss.seam.social.linkedin.api.model.Company.CompanyContactInfo;
import org.jboss.seam.social.linkedin.api.model.Company.CompanyLocation;
import org.jboss.seam.social.linkedin.api.model.CompanyJobUpdate;
import org.jboss.seam.social.linkedin.api.model.CurrentShare;
import org.jboss.seam.social.linkedin.api.model.Education;
import org.jboss.seam.social.linkedin.api.model.Group;
import org.jboss.seam.social.linkedin.api.model.Group.GroupCount;
import org.jboss.seam.social.linkedin.api.model.Group.GroupPosts;
import org.jboss.seam.social.linkedin.api.model.Group.GroupRelation;
import org.jboss.seam.social.linkedin.api.model.GroupMemberships;
import org.jboss.seam.social.linkedin.api.model.GroupSettings;
import org.jboss.seam.social.linkedin.api.model.GroupSuggestions;
import org.jboss.seam.social.linkedin.api.model.ImAccount;
import org.jboss.seam.social.linkedin.api.model.Job;
import org.jboss.seam.social.linkedin.api.model.JobBookmark;
import org.jboss.seam.social.linkedin.api.model.JobBookmarks;
import org.jboss.seam.social.linkedin.api.model.JobPosition;
import org.jboss.seam.social.linkedin.api.model.Jobs;
import org.jboss.seam.social.linkedin.api.model.Likes;
import org.jboss.seam.social.linkedin.api.model.LinkedInConnections;
import org.jboss.seam.social.linkedin.api.model.LinkedInDate;
import org.jboss.seam.social.linkedin.api.model.LinkedInNetworkUpdate;
import org.jboss.seam.social.linkedin.api.model.LinkedInNetworkUpdates;
import org.jboss.seam.social.linkedin.api.model.LinkedInProfile;
import org.jboss.seam.social.linkedin.api.model.LinkedInProfileFull;
import org.jboss.seam.social.linkedin.api.model.LinkedInProfiles;
import org.jboss.seam.social.linkedin.api.model.Location;
import org.jboss.seam.social.linkedin.api.model.MemberGroup;
import org.jboss.seam.social.linkedin.api.model.NetworkStatistics;
import org.jboss.seam.social.linkedin.api.model.PersonActivity;
import org.jboss.seam.social.linkedin.api.model.PhoneNumber;
import org.jboss.seam.social.linkedin.api.model.Position;
import org.jboss.seam.social.linkedin.api.model.Post;
import org.jboss.seam.social.linkedin.api.model.Post.Attachment;
import org.jboss.seam.social.linkedin.api.model.Post.PostRelation;
import org.jboss.seam.social.linkedin.api.model.PostComment;
import org.jboss.seam.social.linkedin.api.model.PostComments;
import org.jboss.seam.social.linkedin.api.model.Product;
import org.jboss.seam.social.linkedin.api.model.Product.ProductRecommendation;
import org.jboss.seam.social.linkedin.api.model.Products;
import org.jboss.seam.social.linkedin.api.model.Recommendation;
import org.jboss.seam.social.linkedin.api.model.Relation;
import org.jboss.seam.social.linkedin.api.model.Share;
import org.jboss.seam.social.linkedin.api.model.TwitterAccount;
import org.jboss.seam.social.linkedin.api.model.UpdateAction;
import org.jboss.seam.social.linkedin.api.model.UpdateContent;
import org.jboss.seam.social.linkedin.api.model.UpdateContentCompany;
import org.jboss.seam.social.linkedin.api.model.UpdateContentConnection;
import org.jboss.seam.social.linkedin.api.model.UpdateContentFollow;
import org.jboss.seam.social.linkedin.api.model.UpdateContentGroup;
import org.jboss.seam.social.linkedin.api.model.UpdateContentPersonActivity;
import org.jboss.seam.social.linkedin.api.model.UpdateContentRecommendation;
import org.jboss.seam.social.linkedin.api.model.UpdateContentShare;
import org.jboss.seam.social.linkedin.api.model.UpdateContentStatus;
import org.jboss.seam.social.linkedin.api.model.UpdateContentViral;
import org.jboss.seam.social.linkedin.api.model.UrlResource;

/**
 * Jackson module for registering mixin annotations against LinkedIn model classes.
 * 
 * @author Antoine Sabot-Durand
 */
@LinkedIn
public class LinkedInModule extends SimpleModule {

    public LinkedInModule() {
        super("LinkedInModule", new Version(1, 0, 0, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(LinkedInConnections.class, LinkedInConnectionsMixin.class);
        context.setMixInAnnotations(LinkedInProfile.class, LinkedInProfileMixin.class);
        context.setMixInAnnotations(LinkedInProfileFull.class, LinkedInProfileFullMixin.class);
        context.setMixInAnnotations(MemberGroup.class, MemberGroupMixin.class);
        context.setMixInAnnotations(Recommendation.class, RecommendationMixin.class);
        context.setMixInAnnotations(PersonActivity.class, PersonActivityMixin.class);
        context.setMixInAnnotations(Job.class, JobMixin.class);
        context.setMixInAnnotations(JobPosition.class, JobPositionMixin.class);
        context.setMixInAnnotations(JobBookmark.class, JobBookmarkMixin.class);
        context.setMixInAnnotations(JobBookmarks.class, JobBookmarksMixin.class);
        context.setMixInAnnotations(Company.class, CompanyMixin.class);
        context.setMixInAnnotations(CompanyLocation.class, CompanyLocationMixin.class);
        context.setMixInAnnotations(CompanyAddress.class, CompanyAddressMixin.class);
        context.setMixInAnnotations(CompanyContactInfo.class, CompanyContactInfoMixin.class);
        context.setMixInAnnotations(CompanyJobUpdate.class, CompanyJobUpdateMixin.class);
        context.setMixInAnnotations(CodeAndName.class, CodeAndNameMixin.class);
        context.setMixInAnnotations(UpdateAction.class, UpdateActionMixin.class);
        context.setMixInAnnotations(CurrentShare.class, CurrentShareMixin.class);
        context.setMixInAnnotations(Share.class, ShareMixin.class);
        context.setMixInAnnotations(Share.ShareContent.class, ShareContentMixin.class);
        context.setMixInAnnotations(Share.ShareSource.class, ShareSourceMixin.class);
        context.setMixInAnnotations(Comment.class, CommentMixin.class);
        context.setMixInAnnotations(Comments.class, CommentsMixin.class);
        context.setMixInAnnotations(Likes.class, LikesMixin.class);
        context.setMixInAnnotations(Position.class, PositionMixin.class);
        context.setMixInAnnotations(ImAccount.class, ImAccountMixin.class);
        context.setMixInAnnotations(TwitterAccount.class, TwitterAccountMixin.class);
        context.setMixInAnnotations(UrlResource.class, UrlResourceMixin.class);
        context.setMixInAnnotations(PhoneNumber.class, PhoneNumberMixin.class);
        context.setMixInAnnotations(Education.class, EducationMixin.class);
        context.setMixInAnnotations(Location.class, LocationMixin.class);
        context.setMixInAnnotations(LinkedInDate.class, LinkedInDateMixin.class);
        context.setMixInAnnotations(Relation.class, RelationMixin.class);
        context.setMixInAnnotations(NetworkStatistics.class, NetworkStatisticsMixin.class);
        context.setMixInAnnotations(Companies.class, CompaniesMixin.class);
        context.setMixInAnnotations(LinkedInProfiles.class, LinkedInProfilesMixin.class);
        context.setMixInAnnotations(Jobs.class, JobsMixin.class);
        context.setMixInAnnotations(Product.class, ProductMixin.class);
        context.setMixInAnnotations(ProductRecommendation.class, ProductRecommendationMixin.class);
        context.setMixInAnnotations(Products.class, ProductsMixin.class);
        context.setMixInAnnotations(ApiStandardProfileRequest.class, ApiStandardProfileRequestMixin.class);
        context.setMixInAnnotations(LinkedInNetworkUpdate.class, LinkedInNetworkUpdateMixin.class);
        context.setMixInAnnotations(LinkedInNetworkUpdates.class, LinkedInNetworkUpdatesMixin.class);
        context.setMixInAnnotations(UpdateContent.class, UpdateContentMixin.class);
        context.setMixInAnnotations(UpdateContentConnection.class, UpdateContentConnectionMixin.class);
        context.setMixInAnnotations(UpdateContentStatus.class, UpdateContentStatusMixin.class);
        context.setMixInAnnotations(UpdateContentGroup.class, UpdateContentGroupMixin.class);
        context.setMixInAnnotations(UpdateContentRecommendation.class, UpdateContentRecommendationMixin.class);
        context.setMixInAnnotations(UpdateContentPersonActivity.class, UpdateContentPersonActivityMixin.class);
        context.setMixInAnnotations(UpdateContentFollow.class, UpdateContentFollowMixin.class);
        context.setMixInAnnotations(UpdateContentViral.class, UpdateContentViralMixin.class);
        context.setMixInAnnotations(UpdateContentShare.class, UpdateContentShareMixin.class);
        context.setMixInAnnotations(UpdateContentCompany.class, UpdateContentCompanyMixin.class);
        context.setMixInAnnotations(Group.class, GroupMixin.class);
        context.setMixInAnnotations(GroupCount.class, GroupCountMixin.class);
        context.setMixInAnnotations(GroupPosts.class, GroupPostsMixin.class);
        context.setMixInAnnotations(GroupRelation.class, GroupRelationMixin.class);
        context.setMixInAnnotations(Post.class, PostMixin.class);
        context.setMixInAnnotations(PostRelation.class, PostRelationMixin.class);
        context.setMixInAnnotations(Attachment.class, AttachmentMixin.class);
        context.setMixInAnnotations(PostComments.class, PostCommentsMixin.class);
        context.setMixInAnnotations(PostComment.class, PostCommentMixin.class);
        context.setMixInAnnotations(GroupSuggestions.class, GroupSuggestionsMixin.class);
        context.setMixInAnnotations(GroupMemberships.class, GroupMembershipsMixin.class);
        context.setMixInAnnotations(GroupSettings.class, GroupSettingsMixin.class);
    }

}
