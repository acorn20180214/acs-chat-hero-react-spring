package com.microsoft.acs.chat.service;

import com.azure.communication.identity.models.CommunicationUserIdentifierAndToken;
import com.azure.core.credential.AccessToken;

public interface IUserTokenManager {

	CommunicationUserIdentifierAndToken GenerateToken(String resourceConnectionString);

	AccessToken GenerateToken(String resourceConnectionString, String identity);

	AccessToken RefreshToken(String resourceConnectionString, String expiredToken);

}
