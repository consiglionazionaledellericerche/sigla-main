package it.cnr.contab.reports.service;

import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.ejb.OfflineReportComponentSession;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import it.cnr.jada.util.ejb.EJBCommonServices;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import org.springframework.beans.factory.InitializingBean;

public class PrintService implements InitializingBean {
	private Gson gson;
	private OfflineReportComponentSession offlineReportComponent;

	public void setGson(Gson gson) {
		this.gson = gson;
	}
	
	public void setOfflineReportComponent(
			OfflineReportComponentSession offlineReportComponent) {
		this.offlineReportComponent = offlineReportComponent;
	}
	
	public Report executeReport(UserContext userContext, Print_spoolerBulk printSpooler) throws IOException, ComponentException{
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost method = null;
		try{
			method = new HttpPost(offlineReportComponent.getLastServerActive(userContext));
	        method.addHeader("Accept-Language", Locale.getDefault().toString());
	        method.setHeader("Content-Type", "application/json;charset=UTF-8");
	        HttpEntity requestEntity = new ByteArrayEntity(gson.toJson(printSpooler).getBytes());
	       
	        method.setEntity(requestEntity);
	        HttpResponse httpResponse = httpclient.execute(method);				
			int status = httpResponse.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK)
				throw new ComponentException("Webscript response width status error: "+status);
			return new Report(printSpooler.getNomeFile(), 
					IOUtils.toByteArray(httpResponse.getEntity().getContent()),
					httpResponse.getEntity().getContentType().getValue(), 
					httpResponse.getEntity().getContentLength());
		}finally{
			if (method != null)
				method.releaseConnection();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.offlineReportComponent = Optional.ofNullable(EJBCommonServices.createEJB("BREPORTS_EJB_OfflineReportComponentSession"))
				.filter(OfflineReportComponentSession.class::isInstance)
				.map(OfflineReportComponentSession.class::cast)
				.orElseThrow(() -> new DetailedRuntimeException("cannot find ejb BREPORTS_EJB_OfflineReportComponentSession"));
	}
}