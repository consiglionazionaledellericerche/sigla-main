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

import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.jsp.Button;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.StringTokenizer;
/**
 * Insert the type's description here.
 * Creation date: (11/04/2002 14:37:49)
 * @author: CNRADM
 */
public class OfflineReportPrintBP extends ReportPrintBP {
	private it.cnr.contab.reports.bulk.Print_spoolerBulk model;
	private int serverPriority;

	/**
	 * OfflineReportPrintBP constructor comment.
	 */
	public OfflineReportPrintBP() {
		super();
	}
	/**
	 * OfflineReportPrintBP constructor comment.
	 */
	public OfflineReportPrintBP(String function) {
		super(function);
	}
	public it.cnr.contab.reports.ejb.OfflineReportComponentSession createComponentSession() throws BusinessProcessException {
		return
				(it.cnr.contab.reports.ejb.OfflineReportComponentSession)createComponentSession(
						"BREPORTS_EJB_OfflineReportComponentSession",
						it.cnr.contab.reports.ejb.OfflineReportComponentSession.class);
	}
	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		Button[] toolbar = new Button[2];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.print");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.close");
		return toolbar;
	}
	public boolean fillModel(ActionContext context) throws FillException {
		getModel().setUser(context.getUserInfo().getUserid());
		if (this.getRepotWhithDsOffLine())
			getModel().setStato( Print_spoolerBulk.STATO_IN_CODA_WAITDS);
		return getModel().fillFromActionContext(context,"main",it.cnr.jada.util.action.FormController.EDIT,getFieldValidationMap());
	}
	public BulkInfo getBulkInfo() {
		return BulkInfo.getBulkInfo(Print_spoolerBulk.class);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/04/2002 16:13:17)
	 * @return it.cnr.contab.reports.bulk.Print_spoolBulk
	 */
	public it.cnr.contab.reports.bulk.Print_spoolerBulk getModel() {
		return model;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (14/05/2002 10:51:51)
	 * @return int
	 */
	public int getServerPriority() {
		return serverPriority;
	}
	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		super.init(config,context);
		String s = config.getInitParameter("serverPriority");
		if (s != null)
			try {
				serverPriority = Integer.parseInt(config.getInitParameter("serverPriority"));
			} catch(Throwable e) {
				throw new BusinessProcessException("Errore: priorità del server di stampa non valida. "+s);
			}
		model = new Print_spoolerBulk();
		model.setTiVisibilita(model.TI_VISIBILITA_UTENTE);
	}
	public void setId_report_generico(java.math.BigDecimal newLong) {
		model.setIdReportGenerico(newLong);
	}
	public void controllaCampiEMail() throws ValidationException{
		if (isEMailEnabled()){
			if (getModel().getEmailA()==null)
				throw new it.cnr.jada.bulk.ValidationException("Specificare il destinatario della E-Mail.");
			if (getModel().getEmailSubject()==null)
				throw new it.cnr.jada.bulk.ValidationException("Specificare l'oggetto della E-Mail.");
			try {
				StringTokenizer st = new StringTokenizer(getModel().getEmailA(),",");
				while (st.hasMoreTokens()) {
					new InternetAddress(st.nextToken()).validate();
				}
			} catch (AddressException e) {
				throw new it.cnr.jada.bulk.ValidationException("Indirizzo E-Mail del destinatario non valido!");
			}
			if (getModel().getEmailCc()!=null){
				try {
					StringTokenizer st = new StringTokenizer(getModel().getEmailCc(),",");
					while (st.hasMoreTokens()) {
						new InternetAddress(st.nextToken()).validate();
					}
				} catch (AddressException e) {
					throw new it.cnr.jada.bulk.ValidationException("Indirizzo E-Mail del destinatario per conoscenza non valido!");
				}
			}
			if (getModel().getEmailCcn()!=null){
				try {
					StringTokenizer st = new StringTokenizer(getModel().getEmailCcn(),",");
					while (st.hasMoreTokens()) {
						new InternetAddress(st.nextToken()).validate();
					}
				} catch (AddressException e) {
					throw new it.cnr.jada.bulk.ValidationException("Indirizzo E-Mail del destinatario per conoscenza nacosta non valido!");
				}
			}
		}
	}
	public boolean isEMailVisible(){
		if (getReportName().endsWith("jasper"))
			return true;
		return false;
	}
	public boolean isEMailEnabled(){
		if (getModel().getFlEmail() != null && getModel().getFlEmail())
			return true;
		return false;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (14/05/2002 10:51:51)
	 * @param newServerPriority int
	 */
	public void setServerPriority(int newServerPriority) {
		serverPriority = newServerPriority;
	}
	public void writeFormField(javax.servlet.jsp.JspWriter out,String name) throws java.io.IOException {
		getBulkInfo().writeFormField(out,getModel(),null,name,"main",1,1,it.cnr.jada.util.action.FormController.EDIT,false,getFieldValidationMap(), getParentRoot().isBootstrap());
	}
	public void writeFormInput(javax.servlet.jsp.JspWriter out,String name) throws java.io.IOException {
		getBulkInfo().writeFormInput(out,getModel(),null,name,false,null,null,"main",it.cnr.jada.util.action.FormController.EDIT,getFieldValidationMap(), getParentRoot().isBootstrap());
	}
	public void writeFormLabel(javax.servlet.jsp.JspWriter out,String name) throws java.io.IOException {
		getBulkInfo().writeFormLabel(out,getModel(),null,name,null, this.getParentRoot().isBootstrap());
	}
	public void initCdServizioPEC(String cdServizioPEC) {
		((Print_spoolerBulk) getModel()).setCd_servizio_pec(cdServizioPEC);
	}
	public void initDsOggettoPEC(String dsOggettoPEC) {
		((Print_spoolerBulk) getModel()).setDs_oggetto_pec(dsOggettoPEC);
	}
	public void initDsNumregPEC(String dsNumregPEC) {
		((Print_spoolerBulk) getModel()).setDs_numreg_pec(dsNumregPEC);
	}
}