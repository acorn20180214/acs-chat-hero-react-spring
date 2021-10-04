package com.microsoft.acs.chat.data;

import java.util.HashMap;
import java.util.Map;

import com.microsoft.acs.chat.model.UserConfigModel;

public class InMemoryChatData {

	// [thread id -> moderator id]
	private Map<String, String> threadStore;

	// [mri -> emoji]
	private Map<String, UserConfigModel> useConfigStore;
	
	public InMemoryChatData()
	{
		threadStore = new HashMap<String, String>();
		useConfigStore = new HashMap<String, UserConfigModel>();
	}
	
	public Map<String, String> getThreadStore() {
		return threadStore;
	}

	public Map<String, UserConfigModel> getUseConfigStore() {
		return useConfigStore;
	}
}
