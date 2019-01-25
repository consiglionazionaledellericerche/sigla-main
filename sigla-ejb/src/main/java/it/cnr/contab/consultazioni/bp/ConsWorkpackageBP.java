/*
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.consultazioni.bp;

import java.io.IOException;
import java.util.BitSet;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.pdg00.ejb.StampaSituazioneSinteticaGAEComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.SelectionListener;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsWorkpackageBP extends SelezionatoreListaBP 
	implements SelectionListener, SearchProvider {

	private String componentSessioneName;
	private Class bulkClass;
	private CompoundFindClause findclause;
	private CompoundFindClause baseclause;
	private int navPosition = 0;
	private boolean flNuovoPdg = false;
	
	private java.math.BigDecimal pg_stampa = null;
	private java.math.BigDecimal currentSequence = null;
	
	public ConsWorkpackageBP() {
		super();
	}

	public ConsWorkpackageBP(String function) {
		super(function);
	}

	public it.cnr.jada.ejb.CRUDComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException, BusinessProcessException {
	
		return (it.cnr.jada.ejb.CRUDComponentSession)createComponentSession("JADAEJB_CRUDComponentSession", it.cnr.jada.ejb.CRUDComponentSession.class);
	}
	
	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			super.init(config,context);
			Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(), CNRUserContext.getEsercizio(context.getUserContext())); 
			setFlNuovoPdg(parCnr.getFl_nuovo_pdg().booleanValue());
			CompoundFindClause clauses = new CompoundFindClause();
			
			clauses.addClause(FindClause.AND, "esercizio_inizio", SQLBuilder.LESS_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
			clauses.addClause(FindClause.AND, "esercizio_fine", SQLBuilder.GREATER_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
			clauses.addClause(FindClause.AND, "ti_gestione", SQLBuilder.NOT_EQUALS, WorkpackageBulk.TI_GESTIONE_ENTRATE);

			setBaseclause(clauses);

			setMultiSelection(true);
			setPageSize(10);	
			setBulkClassName(config.getInitParameter("bulkClassName"));	
			setComponentSessioneName(config.getInitParameter("componentSessionName"));
			
			openIterator(context);
								
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}		

	public void openIterator(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try	{	
			it.cnr.jada.util.RemoteIterator ri = ((StampaSituazioneSinteticaGAEComponentSession)EJBCommonServices.createEJB(
					"CNRPDG00_EJB_StampaSituazioneSinteticaGAEComponentSession",
					StampaSituazioneSinteticaGAEComponentSession.class)).selezionaGae( context.getUserContext(), getBaseclause());
			this.setIterator(context,ri);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

	public it.cnr.jada.util.jsp.Button[] createNavigatorToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[6];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.filter");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.removeFilter");
		setNavPosition(2);
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Navigator.previousFrame");
		toolbar[i-1].setSeparator(true);
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Navigator.previous");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Navigator.next");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Navigator.nextFrame");
	
		return toolbar;
	}

	public Button[] createToolbar()
	{
		Button abutton[] = null;
		if (getName().equalsIgnoreCase("StampaSituazioneSinteticaDispGAEBP")
				||getName().equalsIgnoreCase("StampaSituazioneSinteticaRendGAEBP")){
			abutton = new Button[4];
			int i = 0;
			abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.print");
			abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.excel");
			abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.selectAll");
			abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.deselectAll");
		}
		else
		{
			abutton = new Button[5];
			int i = 0;
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.print");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.excel");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.selectAll");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.deselectAll");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.obbligazioni");
		abutton[i-1].setSeparator(true);
		}
		
		//abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.freeSearch");		
		return abutton;
	}

	public void writeHTMLNavigator(JspWriter jspwriter)
		throws IOException, ServletException
	{
		Button abutton[] = getNavigatorToolbar();
		jspwriter.println("<div class=\"Toolbar\">");
		jspwriter.println("<table cellspacing=\"0\" cellpadding=\"0\">");
		jspwriter.println("<tr align=center valign=middle>");
		for (int i = 0; i<getNavPosition();i++) {
			jspwriter.print("<td>");
			abutton[i].write(jspwriter, this, this.getParentRoot().isBootstrap());
			jspwriter.println("</td>");
		}
		jspwriter.print("<td");
		if(abutton[getNavPosition()].hasSeparator())
			jspwriter.print(" class=\"VSeparator\"");
		jspwriter.print(">");
		abutton[getNavPosition()].write(jspwriter, this, this.getParentRoot().isBootstrap());
		jspwriter.println("</td>");
		jspwriter.print("<td>");
		abutton[getNavPosition()+1].write(jspwriter, this, this.getParentRoot().isBootstrap());
		jspwriter.println("</td>");
		getLastPage();
		for(int i = getFirstPage(); i < getLastPage(); i++)
		{
			jspwriter.print("<td width=\"16\">");
			if(getCurrentPage() != i)
				JSPUtils.button(jspwriter, null, String.valueOf(i), "javascript:submitForm('doGotoPage(" + i + ")')", this.getParentRoot().isBootstrap());
			else
				JSPUtils.button(jspwriter, null, String.valueOf(i), null, "background: Highlight;color: HighlightText;", this.getParentRoot().isBootstrap());
			jspwriter.println("</td>");
		}

		jspwriter.print("<td>");
		abutton[getNavPosition()+2].write(jspwriter, this, this.getParentRoot().isBootstrap());
		jspwriter.println("</td>");
		jspwriter.print("<td>");
		abutton[getNavPosition()+3].write(jspwriter, this, this.getParentRoot().isBootstrap());
		jspwriter.println("</td>");
		for(int j = getNavPosition()+4; j < abutton.length; j++)
		{
			jspwriter.print("<td>");
			abutton[getNavPosition()+j].write(jspwriter, this, this.getParentRoot().isBootstrap());
			jspwriter.println("</td>");
		}

		jspwriter.println("</tr>");
		jspwriter.println("</table>");
		jspwriter.println("</div>");
	}

	public RemoteIterator search(
		ActionContext actioncontext,
		CompoundFindClause compoundfindclause,
		OggettoBulk oggettobulk)
		throws BusinessProcessException {
			/*
			 * Mi conservo la findClause per poi utilizzarla
			 * nel selectAll
			 */
			setFindclause(compoundfindclause);
			return findFreeSearch(actioncontext,
								  compoundfindclause,
								  oggettobulk);
	}
	public it.cnr.jada.util.RemoteIterator findFreeSearch(
		ActionContext context,
		it.cnr.jada.persistency.sql.CompoundFindClause clauses,
		OggettoBulk model) 
		throws it.cnr.jada.action.BusinessProcessException {
		it.cnr.jada.util.RemoteIterator ri;
		try {
			clauses = CompoundFindClause.and(clauses,getBaseclause());
//			if ((getName().equalsIgnoreCase("StampaSituazioneSinteticaDispGAEBP")||getName().equalsIgnoreCase("StampaSituazioneSinteticaRendGAEBP")) ){
				 ri = ((StampaSituazioneSinteticaGAEComponentSession)EJBCommonServices.createEJB(
						"CNRPDG00_EJB_StampaSituazioneSinteticaGAEComponentSession",
						StampaSituazioneSinteticaGAEComponentSession.class)).selezionaGae( context.getUserContext(), clauses);
	/*		}
			else{
			 ri =
				it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator
				(context, createComponentSession().cerca(context.getUserContext(),clauses,model));
			}*/
			return ri;			
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	public OggettoBulk createEmptyModelForFreeSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			return createComponentSession().inizializzaBulkPerRicercaLibera(context.getUserContext(),new WorkpackageBulk());
		} catch(Exception e) {
			throw handleException(e);
		}
	}	

	/**
	 * @return
	 */
	public String getComponentSessioneName() {
		return componentSessioneName;
	}

	/**
	 * @param string
	 */
	public void setComponentSessioneName(String string) {
		componentSessioneName = string;
	}
	/**
	 * @return java.lang.Class
	 */
	public java.lang.Class getBulkClass() {
		return bulkClass;
	}
	/**
	 * @param newBulkClass java.lang.Class
	 */
	public void setBulkClass(java.lang.Class newBulkClass) {
		bulkClass = newBulkClass;
	}
	/**
	 * Imposta il valore della proprietà 'bulkClassName'
	 *
	 * @param bulkClassName	Il valore da assegnare a 'bulkClassName'
	 * @throws ClassNotFoundException	
	 */
	public void setBulkClassName(java.lang.String bulkClassName) throws ClassNotFoundException {
		bulkClass = getClass().getClassLoader().loadClass(bulkClassName);
		setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(bulkClass));
		setColumns(getBulkInfo().getColumnFieldPropertyDictionary(this.isFlNuovoPdg()?"prg_liv2":null));
	}

	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.SelectionListener#deselectAll(it.cnr.jada.action.ActionContext)
	 */
	public void deselectAll(ActionContext actioncontext) {}
	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.SelectionListener#getSelection(it.cnr.jada.action.ActionContext, it.cnr.jada.bulk.OggettoBulk[], java.util.BitSet)
	 */
	public BitSet getSelection(ActionContext actioncontext, OggettoBulk[] aoggettobulk, BitSet bitset) throws BusinessProcessException {
		return bitset;
	}
	
	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.SelectionListener#initializeSelection(it.cnr.jada.action.ActionContext)
	 */
	public void initializeSelection(ActionContext actioncontext) throws BusinessProcessException {
		// TODO Auto-generated method stub
		int dummy = 0;
		
	}

	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.SelectionListener#setSelection(it.cnr.jada.action.ActionContext, it.cnr.jada.bulk.OggettoBulk[], java.util.BitSet, java.util.BitSet)
	 */
	public BitSet setSelection(ActionContext actioncontext, OggettoBulk[] aoggettobulk, BitSet bitset, BitSet bitset1) throws BusinessProcessException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setFindclause(CompoundFindClause clause) {
		findclause = clause;
	}

	public CompoundFindClause getFindclause() {
		return findclause;
	}

	public CompoundFindClause getBaseclause() {
		return baseclause;
	}

	public void setBaseclause(CompoundFindClause clause) {
		baseclause = clause;
	}

	public void addToBaseclause(CompoundFindClause clause) {
		baseclause = CompoundFindClause.and(baseclause, clause);
	}

	
	public boolean isFilterButtonHidden() 
	{
		if (getFindclause() == null)
			return false;
		else
			return true;
	}

	public boolean isRemoveFilterButtonHidden() 
	{
		if (isFilterButtonHidden())
			return false;
		else
			return true;
	}

	public boolean isObbligazioniButtonHidden()
	{
		return false;
	}
	
	public int getNavPosition() {
		return navPosition;
	}

	public void setNavPosition(int i) {
		navPosition = i;
	}

	
	public void setCurrentSequence(java.math.BigDecimal newCurrentSequence) {
		currentSequence = newCurrentSequence;
	}
	
	public java.math.BigDecimal getCurrentSequence() {
		return currentSequence;
	}
	
	public java.math.BigDecimal getPg_stampa() {
		return pg_stampa;
	}
	
	public void setPg_stampa(java.math.BigDecimal newPg_stampa) {
		pg_stampa = newPg_stampa;
	}
	
	public void resetIdReport(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
			setPg_stampa(null);
			setCurrentSequence(null);
	}
	
	public void selectedGae(it.cnr.jada.action.ActionContext context, java.util.List gae_selezionate)
		throws it.cnr.jada.action.BusinessProcessException {
		try {
			StampaSituazioneSinteticaGAEComponentSession session = 
				(StampaSituazioneSinteticaGAEComponentSession)
						createComponentSession("CNRPDG00_EJB_StampaSituazioneSinteticaGAEComponentSession",StampaSituazioneSinteticaGAEComponentSession.class);
			setPg_stampa(session.getPgStampa(context.getUserContext()));
			session.inserisciRecord(context.getUserContext(),pg_stampa, gae_selezionate);
		} catch(it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch(java.rmi.RemoteException e) {
			throw handleException(e);
		}
	}
	public boolean isFlNuovoPdg() {
		return flNuovoPdg;
	}
	private void setFlNuovoPdg(boolean flNuovoPdg) {
		this.flNuovoPdg = flNuovoPdg;
	}
}
