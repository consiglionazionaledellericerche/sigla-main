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

import it.cnr.contab.consultazioni.bulk.ConsObbligazioniBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.SelectionListener;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsObbligazioniBP extends ConsultazioniRestBP
	implements SelectionListener, SearchProvider {

	private String componentSessioneName;
	private Class bulkClass;
	//private BulkInfo bulkInfo;
	private CompoundFindClause findclause;
	private CompoundFindClause baseclause;
	private int navPosition = 0;

	public ConsObbligazioniBP() {
		super();
	}

	public ConsObbligazioniBP(String function) {
		super(function);
	}

	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			super.init(config,context);

			//CompoundFindClause clauses = new CompoundFindClause();
			
//			clauses.addClause("AND", "cd_centro_responsabilita", SQLBuilder.STARTSWITH, ((it.cnr.contab.utenze00.bulk.CNRUserInfo)context.getUserInfo()).getCdr().getCd_cds());
			//clauses.addClause("AND", "pg_obbligazione", SQLBuilder.EQUALS, new Integer("2674"));
	//		clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bulk.CNRUserInfo)context.getUserInfo()).getEsercizio(context));
			
		//	setBaseclause(clauses);

			//setMultiSelection(true);
			setPageSize( 15 );	
			setBulkClassName(config.getInitParameter("bulkClassName"));	
			setComponentSessioneName(config.getInitParameter("componentSessionName"));		
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}		

	public void openIterator(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try	{	
			ConsObbligazioniBulk model = new ConsObbligazioniBulk();

			it.cnr.jada.util.RemoteIterator ri =
				it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator
				(context, createComponentSession().cerca(context.getUserContext(),CompoundFindClause.and(getBaseclause(),getFindclause()),model));
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
	
		try {
			clauses = CompoundFindClause.and(clauses,getBaseclause());
			it.cnr.jada.util.RemoteIterator ri =
				it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator
				(context, createComponentSession().cerca(context.getUserContext(),clauses,model));
			//this.setIterator(context,ri);
			return ri;			
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	
	public OggettoBulk createEmptyModelForFreeSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			return createComponentSession().inizializzaBulkPerRicercaLibera(context.getUserContext(),new ConsObbligazioniBulk());
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
		setColumns(getBulkInfo().getColumnFieldPropertyDictionary());
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

}
