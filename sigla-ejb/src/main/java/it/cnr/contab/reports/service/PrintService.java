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

package it.cnr.contab.reports.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.ejb.OfflineReportComponentSession;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Locale;
import java.util.Optional;

import it.cnr.jada.util.ejb.EJBCommonServices;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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

	private String serverPrint;

	public String getServerPrint() {
		return serverPrint;
	}

	public void setServerPrint(String serverPrint) {
		this.serverPrint = serverPrint;
	}

	private OfflineReportComponentSession offlineReportComponent;
	
	public void setOfflineReportComponent(
			OfflineReportComponentSession offlineReportComponent) {
		this.offlineReportComponent = offlineReportComponent;
	}
	
	public Report executeReport(UserContext userContext, Print_spoolerBulk printSpooler) throws IOException, ComponentException{
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost method = null;
		try {
			method = new HttpPost(
					Optional.ofNullable(getServerPrint())
						.orElseGet(() -> {
							try {
								return offlineReportComponent.getLastServerActive(userContext);
							} catch (ComponentException|RemoteException e) {
								throw new DetailedRuntimeException(e);
							}
						})
			);
	        method.addHeader("Accept-Language", Locale.getDefault().toString());
	        method.setHeader("Content-Type", "application/json;charset=UTF-8");
			GsonBuilder builder = new GsonBuilder();
			builder.setExclusionStrategies( new HiddenAnnotationExclusionStrategy() );
			Gson gson = builder.create();
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

	public class HiddenAnnotationExclusionStrategy implements ExclusionStrategy {
		public boolean shouldSkipClass(Class<?> clazz) {
			return clazz.getAnnotation(JsonIgnore.class) != null;
		}

		public boolean shouldSkipField(FieldAttributes f) {
			return f.getAnnotation(JsonIgnore.class) != null;
		}
	}
}