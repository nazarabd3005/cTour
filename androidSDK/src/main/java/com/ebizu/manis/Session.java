package com.ebizu.manis;

/**
 *
 * @author kazao
 */
public class Session {

    private static Session INSTANCE;
    private static final String HOST = "api.ebizu.com";
    private static final String HOST_STAGING = "apis.ebizu.com";
    private static final String HOST_SANDBOX = "apix.ebizu.com";
    private static final String HOST_DEMO = "demoapi.ebizu.com";
    private boolean sandbox;
    private boolean staging;
    private boolean demo;
    private boolean debug = true;
    private boolean https = true; // used https by default
    private boolean loggedIn;
    private String token;
    private String key;
    private String apiUrl;

    public Session() {
    }

    public void destroy() {
        // todo destroy session
    }

    public void logout() {
        // todo logout
    }

    public void login(String username, String password) {
        // todo login
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean isSandbox() {
        return sandbox;
    }

    public void setSandbox(boolean sandbox) {
        this.sandbox = sandbox;
    }

    public boolean isStaging() {
        return staging;
    }

    public void setStaging(boolean staging) {
        this.staging = staging;
    }
    
    public void setDemo(boolean demo){
    	this.demo = demo;
    }
    
    public boolean isDemo(){
    	return demo;
    }
    
    public void setDebug(boolean debug){
    	this.debug = debug;
    }
    
    public boolean isDebug(){
    	return debug;
    }

    public boolean isHttps() {
        return https;
    }

    public String getUrl() {
        if (getApiUrl() != null) {
            return getApiUrl();
        }
        String protocol = https ? "https://" : "http://";
        return protocol + (sandbox ? HOST_SANDBOX : (staging ? HOST_STAGING : (demo ? HOST_DEMO : HOST)));
    }

    public void setHttps(boolean https) {
        this.https = https;
    }

    public static Session getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Session();
        }
        return INSTANCE;
    }

    public String getApiUrl() {
        
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
    
    
    
    

}
