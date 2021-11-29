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

package it.cnr.contab.reports.bp;

import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.util.action.AbstractPrintBP;

public class ReportPrintBP extends AbstractPrintBP {
	private java.util.Properties printProps;
	private BulkList printSpoolerParam = new BulkList();
	private String reportName;

	private Boolean repotWhithDsOffLine=Boolean.FALSE;

	public Boolean getRepotWhithDsOffLine() {
		return repotWhithDsOffLine;
	}

	public void setRepotWhithDsOffLine(Boolean repotWhitDsOffLine) {
		this.repotWhithDsOffLine = repotWhitDsOffLine;
	}

	public static final java.text.Format DATE_FORMAT = new java.text.SimpleDateFormat("yyyy/MM/dd");
	public static final java.text.Format TIMESTAMP_FORMAT = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
/**
 * ReportPrintBP constructor comment.
 */
public ReportPrintBP() {
	super();
}
protected ReportPrintBP(String function) {
	super(function);
}
public void checkProperties(java.util.Properties props) {
	props.putAll(printProps);
}
public it.cnr.jada.util.jsp.Button[] createToolbar() {
	return null;
}
public Object getPrintProperty(String key) {
	if (printProps == null)
		return null;
	return printProps.get(key);
}
/**
 * 
 * @return java.util.Properties
 */
public java.util.Properties getPrintProps() {
	return printProps;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 12:04:53)
 * @return java.lang.String
 */
public java.lang.String getReportName() {
	return reportName;
}
/**
 * @see it.cnr.jada.util.action.AbstractPrintBP
 */
public void print(javax.servlet.jsp.PageContext pageContext) throws javax.servlet.ServletException, java.io.IOException, it.cnr.jada.action.BusinessProcessException {
	String appRoot = it.cnr.jada.util.JSPUtils.getAppRoot((javax.servlet.http.HttpServletRequest)pageContext.getRequest());
	pageContext.getOut().println("<applet code=\"com.seagatesoftware.img.ReportViewer.ReportViewer\"");
	pageContext.getOut().print("  codebase=\"");
	pageContext.getOut().print(appRoot);
	pageContext.getOut().println("viewer/JavaViewer\"");
	pageContext.getOut().println("  id=ReportViewer width=100% height=100%>");
	pageContext.getOut().print("	<param name=ReportName value=\"http://");
	pageContext.getOut().print(pageContext.getRequest().getServerName());
	pageContext.getOut().print(':');	
	pageContext.getOut().print(pageContext.getRequest().getServerPort());
	pageContext.getOut().print(appRoot);
	pageContext.getOut().print(it.cnr.jada.action.BusinessProcess.encodeUrl((javax.servlet.http.HttpServletRequest)pageContext.getRequest(),"report"));
	pageContext.getOut().println("&\" >");
	pageContext.getOut().println("	<param name=HasGroupTree value=true>");
	pageContext.getOut().println("	<param name=ShowGroupTree value=true>");
	pageContext.getOut().println("	<param name=HasExportButton value=true>");
	pageContext.getOut().println("	<param name=HasRefreshButton value=false>");
	pageContext.getOut().println("	<param name=HasPrintButton value=true>");
	pageContext.getOut().print("	<param name=cabbase value=\"");
	pageContext.getOut().print(appRoot);
	pageContext.getOut().println("viewer/JavaViewer/ReportViewer.cab\" >");
	pageContext.getOut().print("	<param name=Archive value=\"");
	pageContext.getOut().print(appRoot);
	pageContext.getOut().println("viewer/JavaViewer/ReportViewer.jar\" >");
	pageContext.getOut().println("</applet>");
}
public void setDateReportParameter(int index,java.sql.Timestamp timestamp) {
	setPrintProperty("prompt"+index,DATE_FORMAT.format(timestamp));
}
public Object setPrintProperty(String key, String value) {
	if (printProps == null)
		printProps = new java.util.Properties();
	return printProps.put(key, value);
}
public Object setPrintProperty(String key, java.sql.Date date) {
	return setPrintProperty(key,DATE_FORMAT.format(date));
}
public Object setPrintProperty(String key, java.sql.Timestamp timestamp) {
	return setPrintProperty(key,TIMESTAMP_FORMAT.format(timestamp));
}
/**
 * 
 * @param newPrintProps java.util.Properties
 */
public void setPrintProps(java.util.Properties newPrintProps) {
	printProps = newPrintProps;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 12:04:53)
 * @param newReportName java.lang.String
 */
public void setReportName(java.lang.String newReportName) {
	reportName = newReportName;
}
public void setReportParameter(int index, String value) {
	setPrintProperty("prompt"+index,value);
}
public void setReportParameter(int index,java.sql.Date date) {
	setPrintProperty("prompt"+index,date);
}
public void setReportParameter(int index,java.sql.Timestamp timestamp) {
	setPrintProperty("prompt"+index,timestamp);
}
public void setTimestampReportParameter(int index,java.sql.Timestamp timestamp) {
	setPrintProperty("prompt"+index,TIMESTAMP_FORMAT.format(timestamp));
}
public BulkList getPrintSpoolerParam() {
	return printSpoolerParam;
}
public void setPrintSpoolerParam(BulkList printSpoolerParam) {
	this.printSpoolerParam = printSpoolerParam;
}
public void addToPrintSpoolerParam(Print_spooler_paramBulk bulk){
	getPrintSpoolerParam().add(bulk);	
}
}