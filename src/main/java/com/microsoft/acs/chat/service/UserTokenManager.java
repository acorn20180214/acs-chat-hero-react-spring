package com.microsoft.acs.chat.service;

import java.util.ArrayList;
import java.util.List;

import com.azure.communication.common.CommunicationUserIdentifier;
import com.azure.communication.identity.CommunicationIdentityClient;
import com.azure.communication.identity.CommunicationIdentityClientBuilder;
import com.azure.communication.identity.models.CommunicationTokenScope;
import com.azure.communication.identity.models.CommunicationUserIdentifierAndToken;
import com.azure.core.credential.AccessToken;

public class UserTokenManager implements IUserTokenManager {

	@Override
	public CommunicationUserIdentifierAndToken GenerateToken(String resourceConnectionString) {
		List<CommunicationTokenScope> scopes = new ArrayList<CommunicationTokenScope>();
		scopes.add(CommunicationTokenScope.CHAT);
		CommunicationIdentityClient communicationIdentityClient = new CommunicationIdentityClientBuilder().connectionString(resourceConnectionString).buildClient();
		CommunicationUserIdentifierAndToken token = communicationIdentityClient.createUserAndToken(scopes);
		return token;
	}

	@Override
	public AccessToken GenerateToken(String resourceConnectionString, String identity) {
		List<CommunicationTokenScope> scopes = new ArrayList<CommunicationTokenScope>();
		scopes.add(CommunicationTokenScope.CHAT);
		CommunicationIdentityClient communicationIdentityClient = new CommunicationIdentityClientBuilder().connectionString(resourceConnectionString).buildClient();
		AccessToken token = communicationIdentityClient.getToken(new CommunicationUserIdentifier(identity), scopes);
		return token;
	}

	@Override
	public AccessToken RefreshToken(String resourceConnectionString, String identity) {
		List<CommunicationTokenScope> scopes = new ArrayList<CommunicationTokenScope>();
		scopes.add(CommunicationTokenScope.CHAT);
		CommunicationIdentityClient communicationIdentityClient = new CommunicationIdentityClientBuilder().connectionString(resourceConnectionString).buildClient();
		AccessToken token = communicationIdentityClient.getToken(new CommunicationUserIdentifier(identity), scopes);
		return token;
	}
	
}
