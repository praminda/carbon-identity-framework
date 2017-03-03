/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.gateway.test.unit;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.gateway.api.context.GatewayMessageContext;
import org.wso2.carbon.identity.gateway.api.exception.GatewayClientException;
import org.wso2.carbon.identity.gateway.dao.AsyncIdentityContextDAO;
import org.wso2.carbon.identity.gateway.dao.IdentityContextDAO;
import org.wso2.carbon.identity.gateway.model.FederatedUser;
import org.wso2.carbon.identity.gateway.model.LocalUser;
import org.wso2.carbon.identity.gateway.model.User;
import org.wso2.carbon.identity.gateway.model.UserClaim;
import org.wso2.carbon.identity.gateway.processor.request.local.LocalAuthenticationRequest;
import org.wso2.carbon.identity.gateway.processor.request.local.LocalAuthenticationRequestBuilderFactory;
import org.wso2.carbon.identity.gateway.service.GatewayClaimResolverService;
import org.wso2.carbon.identity.mgt.IdentityStore;
import org.wso2.carbon.identity.mgt.RealmService;
import org.wso2.carbon.identity.mgt.claim.Claim;
import org.wso2.carbon.identity.mgt.event.IdentityMgtMessageContext;
import org.wso2.carbon.identity.mgt.exception.DomainException;
import org.wso2.carbon.identity.mgt.exception.IdentityStoreException;
import org.wso2.carbon.identity.mgt.impl.Domain;
import org.wso2.carbon.identity.mgt.impl.IdentityStoreImpl;
import org.wso2.carbon.identity.mgt.impl.internal.IdentityMgtDataHolder;
import org.wso2.carbon.messaging.DefaultCarbonMessage;
import org.wso2.msf4j.Request;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Identity Store Tests.
 */
@PrepareForTest()
public class GatewayUnitTests {

    @Mock
    private RealmService realmService;

//    @Mock
//    private AuthorizationStore authorizationStore;

    @Mock
    private IdentityMgtDataHolder identityMgtDataHolder;

    @Mock
    private Domain domain;

    private IdentityStore identityStore;

    @BeforeClass
    public void initClass() {

        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void initMethod() throws DomainException {

    }

    @AfterMethod
    public void resetMocks() {

        Mockito.reset(realmService);
//      Mockito.reset(authorizationStore);
        Mockito.reset(identityMgtDataHolder);
    }

    @Test
    public void testFederatedUser() {
        User user = new FederatedUser("FederatedUser");
        user.getClaims().add(new Claim("dialect", "claimUri", "value"));
        Assert.assertNotNull(user.getClaims());
        Assert.assertTrue(user.getClaims().size() > 0);
    }

    @Test
    public void testLocalUser() {
        LocalUser localUser = new LocalUser(null);
    }

    @Test
    public void testUserClaim() {
        UserClaim userClaim = new UserClaim("http://org.wso2/claim/username", "testuser");
        userClaim.setUri("http://org.wso2/claim/username");
        Assert.assertEquals("http://org.wso2/claim/username", userClaim.getUri());
    }

    @Test
    public void testLocalAuthentication() {
        LocalAuthenticationRequest.LocalAuthenticationRequestBuilder localAuthenticationRequestBuilder = new
                LocalAuthenticationRequest.LocalAuthenticationRequestBuilder();
        localAuthenticationRequestBuilder.setAuthenticatorName("testAuthenticator");
        localAuthenticationRequestBuilder.setIdentityProviderName("testIdentityProvider");
        LocalAuthenticationRequest localAuthenticationRequest = localAuthenticationRequestBuilder.build();
        Assert.assertEquals("testAuthenticator" , localAuthenticationRequest.getAuthenticatorName());
        Assert.assertEquals("testIdentityProvider", localAuthenticationRequest.getIdentityProviderName());
    }

    @Test
    public void testLocalAuthenticationBuilderFactor() throws GatewayClientException {
        LocalAuthenticationRequestBuilderFactory factory = new LocalAuthenticationRequestBuilderFactory();
        Assert.assertNotNull(factory.getName());
        Response.ResponseBuilder builder = factory.handleException(new GatewayClientException("Error while validating" +
                " request"));
        Assert.assertNotNull(builder.build());
        Assert.assertNotNull(factory.getPriority());
    }
}
