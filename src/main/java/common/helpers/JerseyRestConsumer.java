package common.helpers;

import java.io.IOException;
import java.net.URI;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

/**
 * @author sabarinath.s
 * Date: 19-Mar-2015	
 * Time: 12:59:37 am 
 */

public class JerseyRestConsumer {

	protected Client client = null;
	protected MediaType requestMediaType = null;
	protected MediaType responseMediaType = null;
	protected String baseEndPoint = null;


	public JerseyRestConsumer(MediaType requestType, MediaType responseType, String endPoint){
		this(requestType,responseType,endPoint, false );
	}

	public JerseyRestConsumer(MediaType requestType, MediaType responseType, String endPoint, boolean isHttps){

		
		Log.info("initializing secure Jersey Rest consumer - endpoint: "+ endPoint+" Default Request Mime type - "+requestType.toString()+" Default Response type - " + responseType.toString() );
		
		if(isHttps)
			client= Client.create(customConfigforHttpsConnection(HttpsProtocol.SSL));
		else
			client= Client.create(customConfig());
		requestMediaType = requestType;
		responseMediaType = responseType;
		baseEndPoint = endPoint;
	}

	public JerseyRestConsumer(MediaType requestType, MediaType responseType, String endPoint, boolean isHttps, HttpsProtocol protocol){

		Log.info("initializing secure Jersey Rest consumer - endpoint: "+ endPoint+" Default Request Mime type - "+requestType.toString()+" Default Response type - " + responseType.toString() );
		client = Client.create(customConfigforHttpsConnection(protocol));
		requestMediaType = requestType;
		responseMediaType = responseType;
		baseEndPoint = endPoint;
	}

	protected MediaType getRequestMediaType() {
		return requestMediaType;
	}

	protected void setRequestMediaType(MediaType requestMediaType) {
		this.requestMediaType = requestMediaType;
	}

	protected MediaType getResponseMediaType() {
		return responseMediaType;
	}

	protected void setResponseMediaType(MediaType responseMediaType) {
		this.responseMediaType = responseMediaType;
	}

	protected String getBaseEndPoint() {
		return baseEndPoint;
	}

	protected void setBaseEndPoint(String baseEndPoint) {
		this.baseEndPoint = baseEndPoint;
	}

	protected ClientConfig customConfig(){

		ClientConfig config = new DefaultClientConfig();
		JacksonJsonProvider jacksonJsonProvider =
				new JacksonJsonProvider()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		config.getSingletons().add(jacksonJsonProvider);
		return config;
	}

	protected ClientConfig customConfigforHttpsConnection(HttpsProtocol protocol){


		ClientConfig config = customConfig();
		SSLContext ctx;
		try {

			X509TrustManager x509TrustManager = new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
				}
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
				}
			};


			ctx = SSLContext.getInstance(protocol.name());

			TrustManager[] trustManagers = {x509TrustManager};
			ctx.init(null, trustManagers , new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
			HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
			config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(hostnameVerifier, ctx));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}

	protected URI constructURI(String path, Map<String, String> queryParams){

		UriBuilder uriBuilder = null;
		uriBuilder = UriBuilder.fromPath(baseEndPoint+"/"+path);
		if(queryParams!=null)
			for(Entry<String, String> e : queryParams.entrySet()){
				uriBuilder = uriBuilder.queryParam(e.getKey(), e.getValue());
			}
		return uriBuilder.build();
	}	

	protected ClientRequest buildRequest(URI uri,Method m, Map<String, String> headers, Object requestEntity) {

		Log.info("Creating request... Url - "+ uri + "and method - "+m.getMethodName() );
		ClientRequest request = ClientRequest.create().type(requestMediaType).accept(responseMediaType).build(uri, m.getMethodName());
		if(requestEntity!=null)
			request.setEntity(requestEntity);
		if(headers!=null)
			for(Entry<String, String> e : headers.entrySet()){
				request.getHeaders().add(e.getKey(), e.getValue());
			}
		return request;
	}

	protected ClientRequest buildRequest(URI uri,Method m, Map<String, String> headers, Object requestEntity, MediaType requestType,MediaType responseType ) {

		//Log.info("Creating request... Url - "+ uri + "and method - "+m.getMethodName()+ " with  specific mimetypes - Request Mime type - "+(requestType==null?this.getRequestMediaType():requestType)+", Response type - " + (responseType==null?this.getResponseMediaType():responseType) );

		ClientRequest request = ClientRequest.create().type(requestType!=null?requestType:requestMediaType).accept(responseType!=null?responseType:responseMediaType).build(uri, m.getMethodName());
		if(requestEntity!=null)
			request.setEntity(requestEntity);
		if(headers!=null)
			for(Entry<String, String> e : headers.entrySet()){
				request.getHeaders().add(e.getKey(), e.getValue());
			}
		return request;
	}

	protected <T> T executeMethod(ClientRequest request, Class<T> responseClass) throws RestServiceException{

		ClientResponse response = executeMethod(request);
		return response.getEntity(responseClass);
	}


	/*	protected Object executeMethod(ClientRequest request, Class<?> responseClass) throws RestServiceException{

		ClientResponse response = executeMethod(request);
		return response.getEntity(responseClass);
	}*/

	protected ClientResponse executeMethod(ClientRequest request) throws RestServiceException{


		Log.actionLog("making "+request.getMethod()+" call to "+request.getURI().toString());
		ClientResponse response = null;
		try{
			response = execute(request);
			response.bufferEntity();
			if(response.getClientResponseStatus().getFamily().equals(Response.Status.Family.SUCCESSFUL)){
				Log.apiSuccessFulLog(request, response);

				try {
					response.getEntityInputStream().reset();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return response;
			}
			else{
				String string =response.getEntity(String.class);
				Log.apiUnsuccessFulLog(request, response);
				throw new RestServiceException("Api call failed with status code: "+response.getStatus(), response);
			}

		}catch(RestServiceException re){
			Log.apiError(re);
			throw re;
		}

	}


	private ClientResponse execute(ClientRequest request)  {

		try{
			return client.handle(request);
		}catch(Exception e){
			throw new RestServiceException(e);
		}
	}

	public enum Method{
		GET("GET"),POST("POST"),PUT("PUT");

		private String methodName = null;
		private Method(String name){
			this.methodName = name;

		}
		public String getMethodName() {
			return methodName;
		}

	}

	public enum HttpsProtocol{
		SSL, TLS
	}

}
