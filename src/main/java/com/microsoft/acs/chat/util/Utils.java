package com.microsoft.acs.chat.util;

import java.net.URI;
import java.net.URISyntaxException;

public class Utils {
	
	public static String ExtractApiChatGatewayUrl(String resourceConnectionString)
    {
		try {
			URI uri = new URI(resourceConnectionString.replace("endpoint=", ""));
			return String.format("%s://%s", uri.getScheme(), uri.getHost());
		} catch (URISyntaxException e) {
			// logging error
		}
		return null;
    }
}
