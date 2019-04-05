package org.brightblock.search.rest.models;


public class ForwardHeaderModel implements IApiModel {

	private static final long serialVersionUID = 8500069866847641096L;
	public static final String X_REAL_IP = "X-Real-IP";
	public static final String X_FORWARDED_HOST = "X-Forwarded-Host";
	public static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";
	public static final String X_FORWARDED_FOR = "X-Forwarded-For";
	public static final String X_FORWARDED_SERVER = "X-Forwarded-Server";
	private String headerForwardedHost;
	private String headerForwardedServer;
	private String headerForwardedFor;
	private String headerForwardedProto;
	private String headerRealIp;
	
	
	public ForwardHeaderModel() {
		super();
	}


	public String getHeaderForwardedHost() {
		return headerForwardedHost;
	}


	public void setHeaderForwardedHost(String headerForwardedHost) {
		this.headerForwardedHost = headerForwardedHost;
	}


	public String getHeaderForwardedServer() {
		return headerForwardedServer;
	}


	public void setHeaderForwardedServer(String headerForwardedServer) {
		this.headerForwardedServer = headerForwardedServer;
	}


	public String getHeaderForwardedFor() {
		return headerForwardedFor;
	}


	public void setHeaderForwardedFor(String headerForwardedFor) {
		this.headerForwardedFor = headerForwardedFor;
	}


	public String getHeaderRealIp() {
		return headerRealIp;
	}


	public void setHeaderRealIp(String headerRealIp) {
		this.headerRealIp = headerRealIp;
	}


	public String getHeaderForwardedProto() {
		return headerForwardedProto;
	}


	public void setHeaderForwardedProto(String headerForwardedProto) {
		this.headerForwardedProto = headerForwardedProto;
	}

}
