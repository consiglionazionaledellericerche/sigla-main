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
import com.google.gson.Gson;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Print_spooler_paramKey;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.ejb.OfflineReportComponentSession;

import it.cnr.contab.reports.service.dataSource.PrintDataSourceOffline;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.ejb.EJBCommonServices;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class PrintService implements InitializingBean {

	private final static Logger logger = LoggerFactory.getLogger(PrintService.class);

  private String serverPrint;

	public String getServerPrint() {
		return serverPrint;
	}

	public void setServerPrint(String serverPrint) {
		this.serverPrint = serverPrint;
	}

	private OfflineReportComponentSession offlineReportComponent;

	private static Gson gson=null;
	static {
		gson = new GsonBuilder().
				registerTypeAdapter(Print_spooler_paramKey.class,new PrintSpoolerParamKeySerializer()).
				registerTypeAdapter(Timestamp.class,new JsonTimestampSeraializer())
      	setExclusionStrategies( new HiddenAnnotationExclusionStrategy())
        .create();

	}

  public void setOfflineReportComponent(
			OfflineReportComponentSession offlineReportComponent) {
		this.offlineReportComponent = offlineReportComponent;
	}

	private String resolveServerPrint(UserContext userContext){
		return Optional.ofNullable(getServerPrint())
				.orElseGet(() -> {
					try {
						return offlineReportComponent.getLastServerActive(userContext);
					} catch (ComponentException|RemoteException e) {
						throw new DetailedRuntimeException(e);
					}
				});
	}

	private static String dsOnBody="/dsOnBody";
	
  private String getExecuteHttpUrl( UserContext userContext,Print_spoolerBulk printSpooler){
		String url = resolveServerPrint(userContext);
		if (Optional.of( printSpooler).
				filter( p->Print_spoolerBulk.STATO_IN_CODA_WAITDS.equals(p.getStato())).isPresent())
			url = url.concat( dsOnBody);

		return url;
	}

	private HttpPost getHttPostExecute(UserContext userContext, Print_spoolerBulk printSpooler) {
		HttpPost method = null;
		method = new HttpPost(getExecuteHttpUrl(userContext, printSpooler));
		method.addHeader("Accept-Language", Locale.getDefault().toString());
		method.setHeader("Content-Type", "application/json;charset=UTF-8");
		if (Optional.of( printSpooler).
				filter( p->Print_spoolerBulk.STATO_IN_CODA_WAITDS.equals(p.getStato())).isPresent())
			method.setHeader("ds-utente", userContext.getUser());
		String json = gson.toJson(printSpooler);
		HttpEntity requestEntity = new ByteArrayEntity(json.getBytes());
		method.setEntity(requestEntity);
		return method;
	}

	public Report executeReport(UserContext userContext, Print_spoolerBulk printSpooler) throws IOException, ComponentException{
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost method = null;
		try {
			method = getHttPostExecute(userContext,printSpooler);
	    HttpResponse httpResponse = httpclient.execute(method);				
			int status = httpResponse.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK)
				throw new ComponentException("Webscript response width status error: "+status);
			return new Report(printSpooler.getNomeFile(), 
					IOUtils.toByteArray(httpResponse.getEntity().getContent()),
					httpResponse.getEntity().getContentType().getValue(), 
					httpResponse.getEntity().getContentLength());
		} finally {
			if (method != null)
				method.releaseConnection();
		}
	}

	public void executeReportDs(UserContext userContext,Print_spoolerBulk printSpooler) throws IOException, ComponentException {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost method = null;
		try {
			method = getHttPostExecute(userContext,printSpooler);
			HttpResponse httpResponse = httpclient.execute(method);
			int status = httpResponse.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK)
				throw new ComponentException("Webscript response width status error: "+status);

		} finally {
			if (method != null)
				method.releaseConnection();
		}
	}
	public Map<String, PrintDataSourceOffline> printDsOfflineImplemented= new HashMap<>();

	public Map<String, PrintDataSourceOffline> getPrintDsOfflineImplemented() {
		return printDsOfflineImplemented;
	}

	public void setPrintDsOfflineImplemented(Map<String, PrintDataSourceOffline> printDsOfflineImplemented) {
		this.printDsOfflineImplemented = printDsOfflineImplemented;
	}

	public void executeReportWithJsonDataSource() throws Exception {
		Print_spoolerBulk printSpooler =null;
		try {
			logger.info("Start executeReportWithJsonDataSource");
			UserContext userContextCal = new CNRUserContext("JOB_STAMPADS", "JOB_STAMPADS"
					, null, null, null, null);
			printSpooler = offlineReportComponent.getJobWaitToJsoDS(userContextCal);

			if (Optional.ofNullable(printSpooler).isPresent()) {
				PrintDataSourceOffline jsonDataSource = this.getPrintDsOfflineImplemented().get(printSpooler.getReport());
				printSpooler = jsonDataSource.getPrintSpooler(printSpooler);
				executeReportDs(userContextCal, printSpooler);
			}
		}catch (Exception e) {
			String error="Error executeReportWithDsJsonDataSource";
			if (Optional.ofNullable(Optional.ofNullable(printSpooler).
					map(Print_spoolerBulk::getPgStampa).orElse(null)).isPresent()){
				error.concat( "for pg_stampa="+Optional.ofNullable(Optional.ofNullable(printSpooler).
						map(Print_spoolerBulk::getPgStampa).get()).toString());
			}
			logger.error(error,e);
		}
		logger.info( "Finish executeReportWithJsonDataSource");
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