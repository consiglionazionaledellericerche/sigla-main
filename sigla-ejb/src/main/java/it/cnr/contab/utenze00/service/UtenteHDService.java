/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.utenze00.service;

import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bulk.ExternalUser;
import it.cnr.jada.comp.ApplicationException;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;


public class UtenteHDService {
	private static String targetEndpoint; //="http://helpwildfly.si.cnr.it:8080/rest/user/";
	private static String siglaRestClientUser; //"app.sigla";
	private static String siglaRestClientPassword;//"pippa";
	
	public static String newUser(ExternalUser xu, String instance) throws Exception {
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		String id = "";
		try {
			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(siglaRestClientUser, siglaRestClientPassword);
			provider.setCredentials(AuthScope.ANY, credentials);
			client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
			HttpPut request = new HttpPut(targetEndpoint+"/rest/user/"+instance);
			request.addHeader(new BasicScheme().authenticate(credentials, request, null));
			StringEntity entity = new StringEntity(new Gson().toJson(xu), ContentType.APPLICATION_JSON);
			request.setEntity(entity);		
			response = client.execute(request);
			if (HttpStatus.SC_CREATED!=response.getStatusLine().getStatusCode())
				throw new Exception(response.getStatusLine().getStatusCode()+" - "+response.getStatusLine().getReasonPhrase());
			HttpEntity resEntity = response.getEntity();
		    if (resEntity != null){
		    	id = EntityUtils.toString(resEntity);
		    	EntityUtils.consume(resEntity);
		    }
		} catch (AuthenticationException e) {
			e.printStackTrace();

		} catch (ClientProtocolException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if (response!=null) response.close();
				if (client!=null) client.close();
			} catch (IOException e) {
				// Do nothing
				e.printStackTrace();
			}
		}
		return id;
	}
	
	public static boolean disableUser(String login, String instance) throws Exception {
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		boolean success = false;
		try {
			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(siglaRestClientUser, siglaRestClientPassword);
			provider.setCredentials(AuthScope.ANY, credentials);
			client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
			HttpDelete request = new HttpDelete(targetEndpoint+"/rest/user/"+instance+"/"+login);
			request.addHeader(new BasicScheme().authenticate(credentials, request, null));
			response = client.execute(request);
			if (HttpStatus.SC_NO_CONTENT!=response.getStatusLine().getStatusCode())
				throw new Exception(response.getStatusLine().getStatusCode()+" - "+response.getStatusLine().getReasonPhrase());
			HttpEntity resEntity = response.getEntity();
			success = true;
			if (resEntity != null){
				EntityUtils.consume(resEntity);
			}				
		} catch (AuthenticationException e) {
			e.printStackTrace();
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if (response!=null) response.close();
				if (client!=null) client.close();
			} catch (IOException e) {
				// Do nothing
				e.printStackTrace();
			}
		}
		return success;
	}
	
	public static ExternalUser getUser(String login, String instance) throws Exception {
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		ExternalUser xu = null;
		
		try {
			
			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(siglaRestClientUser, siglaRestClientPassword);
			provider.setCredentials(AuthScope.ANY, credentials);
			client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
			HttpGet request = new HttpGet(targetEndpoint+"/rest/user/"+instance+"/"+login);
			request.addHeader(new BasicScheme().authenticate(credentials, request, null));
			response = client.execute(request);
			if (HttpStatus.SC_OK!=response.getStatusLine().getStatusCode())
				if (HttpStatus.SC_NOT_FOUND==response.getStatusLine().getStatusCode())
				  return null; 
				else
				  throw new Exception(response.getStatusLine().getStatusCode()+" - "+response.getStatusLine().getReasonPhrase());
			HttpEntity resEntity = response.getEntity();
	    	if (resEntity != null) {
		    	String res = EntityUtils.toString(resEntity);
		    	xu = new Gson().fromJson(res , ExternalUser.class);
		    	EntityUtils.consume(resEntity);
		    }
		} catch (AuthenticationException e) {
			throw new ApplicationException("Errore di autenticazione"+e.getMessage());

		} catch (ClientProtocolException e) {
			e.printStackTrace();

		} catch (IOException e) {
			throw new ApplicationException("Errore di connessione "+e.getMessage());

		} finally {
			try {
				if (response!=null) response.close();
				if (client!=null) client.close();
			} catch (IOException e) {
				// Do nothing
				e.printStackTrace();
			}
		}
		return xu;
	}
	public static void loadProperties() throws FileNotFoundException, IOException {
		HelpdeskProperties helpdeskProperties = recuperoHelpdeskProperties();
		setTargetEndpoint(helpdeskProperties.getHelpdeskTargetEndpoint());
		setSiglaRestClientUser(helpdeskProperties.getHelpdeskSiglaRestClientUser());
		setSiglaRestClientPassword(helpdeskProperties.getHelpdeskSiglaRestClientPassword());
	}
private static HelpdeskProperties recuperoHelpdeskProperties() {
		HelpdeskProperties helpdeskProperties = SpringUtil.getBean("helpdeskProperties",HelpdeskProperties.class);
		return helpdeskProperties;
	}

public static String getTargetEndpoint() {
	return targetEndpoint;
}

public static void setTargetEndpoint(String targetEndpoint) {
	UtenteHDService.targetEndpoint = targetEndpoint;
}

public static String getSiglaRestClientUser() {
	return siglaRestClientUser;
}

public static void setSiglaRestClientUser(String siglaRestClientUser) {
	UtenteHDService.siglaRestClientUser = siglaRestClientUser;
}

public static String getSiglaRestClientPassword() {
	return siglaRestClientPassword;
}

public static void setSiglaRestClientPassword(String siglaRestClientPassword) {
	UtenteHDService.siglaRestClientPassword = siglaRestClientPassword;
}

}
