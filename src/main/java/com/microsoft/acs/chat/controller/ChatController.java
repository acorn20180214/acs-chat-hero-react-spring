package com.microsoft.acs.chat.controller;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.azure.communication.chat.ChatClient;
import com.azure.communication.chat.ChatClientBuilder;
import com.azure.communication.chat.ChatThreadClient;
import com.azure.communication.chat.models.ChatParticipant;
import com.azure.communication.chat.models.ChatThreadProperties;
import com.azure.communication.chat.models.CreateChatThreadOptions;
import com.azure.communication.chat.models.CreateChatThreadResult;
import com.azure.communication.common.CommunicationTokenCredential;
import com.azure.communication.common.CommunicationUserIdentifier;
import com.azure.communication.identity.models.CommunicationUserIdentifierAndToken;
import com.azure.core.credential.AccessToken;
import com.microsoft.acs.chat.data.InMemoryChatData;
import com.microsoft.acs.chat.model.UserConfigModel;
import com.microsoft.acs.chat.model.UserModel;
import com.microsoft.acs.chat.service.IUserTokenManager;
import com.microsoft.acs.chat.service.UserTokenManager;
import com.microsoft.acs.chat.util.Utils;

@RestController
public class ChatController {

	private IUserTokenManager userTokenManager;

	@Value("${app.connectionstring}")
	private String connectionString;

	private InMemoryChatData store;

	private static final String GUID_FOR_INITIAL_TOPIC_NAME = "c774da81-94d5-4652-85c7-6ed0e8dc67e6";

	public ChatController() {
		this.userTokenManager = new UserTokenManager();
		this.store = new InMemoryChatData();
	}

	@PostMapping("/createThread")
	public String createNewThread() {
		CommunicationUserIdentifierAndToken user = userTokenManager.GenerateToken(this.connectionString);
		CommunicationUserIdentifier userIdentifier = user.getUser();
		String userToken = user.getUserToken().getToken();
		ChatParticipant chatParticipant = new ChatParticipant().setCommunicationIdentifier(userIdentifier);
		ChatClient chatClient = new ChatClientBuilder().endpoint(Utils.ExtractApiChatGatewayUrl(this.connectionString))
				.credential(new CommunicationTokenCredential(userToken)).buildClient();
		List<ChatParticipant> chatParticipants = Arrays.asList(new ChatParticipant[] { chatParticipant });
		CreateChatThreadResult result = chatClient.createChatThread(
				new CreateChatThreadOptions(GUID_FOR_INITIAL_TOPIC_NAME).setParticipants(chatParticipants));
		String threadId = result.getChatThread().getId();
		this.store.getThreadStore().put(threadId, userIdentifier.getId());
		return threadId;
	}

	@GetMapping("/isValidThread/{threadId}")
	public boolean isValidThread(@PathVariable String threadId) {
		return this.store.getThreadStore().containsKey(threadId);
	}

	@PostMapping("/addUser/{threadId}")
	public ResponseEntity addUserToThread(@PathVariable String threadId, @RequestBody UserModel user) {
		String moderatorId = this.store.getThreadStore().get(threadId);
		AccessToken moderatorToken = this.userTokenManager.GenerateToken(this.connectionString, moderatorId);
		ChatClient chatClient = new ChatClientBuilder().endpoint(Utils.ExtractApiChatGatewayUrl(this.connectionString))
				.credential(new CommunicationTokenCredential(moderatorToken.getToken())).buildClient();

		ChatThreadClient chatThreadClient = chatClient.getChatThreadClient(threadId);

		ChatThreadProperties threadProperties = chatThreadClient.getProperties();
		ChatParticipant chatParticipant = new ChatParticipant()
				.setCommunicationIdentifier(new CommunicationUserIdentifier(user.getId()))
				.setDisplayName(user.getDisplayName()).setShareHistoryTime(threadProperties.getCreatedOn());

		chatThreadClient.addParticipant(chatParticipant);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/getEnvironmentUrl")
	public String getEnvironmentUrl() {
		return Utils.ExtractApiChatGatewayUrl(this.connectionString);
	}

	@PostMapping("/token")
	public ResponseEntity getToken() {
		CommunicationUserIdentifierAndToken userIdentifierAndToken = this.userTokenManager.GenerateToken(this.connectionString);
		JSONObject object = new JSONObject();
		try {
			object.put("token", userIdentifierAndToken.getUserToken().getToken());
			object.put("expiresOn", userIdentifierAndToken.getUserToken().getExpiresAt().toString());
			JSONObject userJsonObject = new JSONObject();
			userJsonObject.put("communicationUserId", userIdentifierAndToken.getUser().getId());
			object.put("user", userJsonObject);
		} catch (Exception e) {
			// logging error
		}
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(object.toString());
	}

	@PostMapping("/userConfig/{userId}")
	public String setUserConfiguration(@PathVariable String userId, @RequestBody UserConfigModel userConfig) {
		this.store.getUseConfigStore().put(userId, userConfig);
		return userId;
	}

	@GetMapping("/userConfig/{userId}")
	public UserConfigModel GetUserConfiguration(@PathVariable String userId) {
		return this.store.getUseConfigStore().get(userId);
	}
	
}
