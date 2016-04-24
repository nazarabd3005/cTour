/**
 * Base API Controller
 * @author egiadtya
 * 27 October 2014
 */
package com.giffar.ctour.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.UrlQuerySanitizer;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.giffar.ctour.APP;
import com.giffar.ctour.Config;
import com.giffar.ctour.base.interfaces.JSONParserInterface;
import com.giffar.ctour.callbacks.OnCallAPI;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;


public abstract class BaseAPIController implements OnCallAPI,JSONParserInterface {
	public static final String EMPTY_ENDPOINT = "";
	protected static AsyncHttpClient client;
	protected Context context;
	private HashMap<String, String> paramMap;
	protected String errorMessage;
	protected SharedPreferences preferences;
	protected Editor editor;
	
	public BaseAPIController(Context context) {
		if (client == null) {
			client = new AsyncHttpClient();
		}
		paramMap = new HashMap<String, String>();
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		editor = preferences.edit();
	}
	
	public void GET(String url,String endpoint) {
		url = url + endpoint;
		RequestParams params;
		if (paramMap.size() > 0) {
			params = new RequestParams(paramMap);
			client.get(url,params,responseHandler);
			url = AsyncHttpClient.getUrlWithQueryString(false,url, params);
		}else{
			client.get(url,responseHandler);
		}
		APP.log(url);
	}
	
	public void GET(String endpoint) {
		GET(Config.getAPIUrl(), endpoint);
	}
	
	public void POST(String url,String endpoint) {
		url = url + endpoint;
		RequestParams params;
		APP.log(url);
		if (paramMap.size() > 0) {
			params = new RequestParams(paramMap);
			APP.log(params.toString());
			client.post(url,params,responseHandler);
		}else{
			client.post(url,responseHandler);
		}
	}
	
	public void POST(String endpoint) {
		POST(Config.getAPIUrl(), endpoint);
	}
	
	public void POST_WITH_IMAGE(String endpoint,FileAttachment attachment) {
		String url = Config.getAPIUrl() + endpoint;
		RequestParams params;
		APP.log(url);
		if (paramMap.size() > 0) {
			params = new RequestParams(paramMap);
			try {
				params.put(attachment.getKey(),attachment.getFile());
			} catch (FileNotFoundException e) {e.printStackTrace();}
			APP.log(params.toString());
			client.post(url,params,responseHandler);
		}else{
			client.post(url,responseHandler);
		}
	}
	
	private AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			switch (statusCode) {
				case 200:
					UrlQuerySanitizer sanitizer = new UrlQuerySanitizer();
					APP.log(sanitizer.unescape(content));
					try {
						boolean parseSuccess = parse(content);
						if (parseSuccess) {
							onAPIsuccess();
						}else{
							onAPIFailed((errorMessage == null) ? "Something wrong when parse data" : errorMessage);
						}
					} catch (JSONException e) {
						e.printStackTrace();
						onAPIFailed(e.getMessage());
					}
					break;
				case 400:
					onAPIFailed("Client Error Bad Request");
					break;
				case 403:
					onAPIFailed("Client Error Forbidden");
					break;
				case 409:
					onAPIFailed("Client Error Conflict");
					break;
				case 401:
					onAPIFailed("Client Error Unauthorized");
					break;
				case 404:
					onAPIFailed("Client Error Not Found");
					break;
				case 500:
					onAPIFailed("Server Error Internal Server Error");
					break;
				case 503:
					onAPIFailed("Server Error Service Unavailable");
					break;
				default:
					onAPIFailed("Something when wrong");
					break;
			}
		}

		@Override
		public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
			if (responseBody != null) {
				String content = new String(responseBody);
				APP.log(content != null ? content : error.getMessage());
			}
			onAPIFailed(error.getMessage());
		}

	};
	
	public void onAPIFailed(String errorMessage) {
		Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
	};
	
	public void addParameter(String key,String value){
		if (paramMap != null) {
			paramMap.put(key, value);
		}
	}
	
	public class FileAttachment {
		private String key;
		private File file;
		private String title;
		private String contentType;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}
	}


	
}
