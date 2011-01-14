package it.cnr.contab.reports.service;

import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.ejb.OfflineReportComponentSession;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import com.google.gson.Gson;

public class PrintService {
	private HttpClient httpClient;
	private Gson gson;
	private OfflineReportComponentSession offlineReportComponent;
	
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	public void setGson(Gson gson) {
		this.gson = gson;
	}
	
	public void setOfflineReportComponent(
			OfflineReportComponentSession offlineReportComponent) {
		this.offlineReportComponent = offlineReportComponent;
	}
	
	public Report executeReport(UserContext userContext, Print_spoolerBulk printSpooler) throws IOException, ComponentException{
		PostMethod method = null;
		try{
			method = new PostMethod(offlineReportComponent.getLastServerActive(userContext));
	        method.setRequestHeader("Accept-Language", Locale.getDefault().toString());
	        RequestEntity requestEntity = new ByteArrayRequestEntity(gson.toJson(printSpooler).getBytes());
	        method.setRequestEntity(requestEntity);
	        httpClient.executeMethod(method);
			int status = method.getStatusCode();
			if (status != HttpStatus.SC_OK)
				throw new ComponentException("Webscript response width status error: "+status);
			return new Report(printSpooler.getNomeFile(),method.getResponseBody(), method.getResponseHeader("Content-Type").getValue(), method.getResponseContentLength());
		}finally{
			if (method != null)
				method.releaseConnection();
		}
	}
}
