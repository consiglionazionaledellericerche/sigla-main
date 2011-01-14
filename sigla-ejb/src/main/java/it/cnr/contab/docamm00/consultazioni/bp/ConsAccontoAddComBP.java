/*
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.docamm00.consultazioni.bp;

import java.io.IOException;
import java.util.BitSet;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.SelectionListener;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

/**
 * @author Matilde D'Urso
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsAccontoAddComBP extends ConsultazioniBP {

	private String componentSessioneName;
	private String multiSelezione;
	private Integer recordPerPagina;
	private Class bulkClass;
	private CompoundFindClause findclause;
	private CompoundFindClause baseclause;
	private java.lang.String searchResultColumnSet;
	private java.lang.String freeSearchSet;
	private java.lang.String archiveEnabled;	

	private int navPosition = 0;

	public ConsAccontoAddComBP(String s) {
		super(s);
	}

	public ConsAccontoAddComBP() {
		super();
	}

	public void openIterator(it.cnr.jada.action.ActionContext context, OggettoBulk model) throws it.cnr.jada.action.BusinessProcessException {
		try	{	

			it.cnr.jada.util.RemoteIterator ri =
				it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator
				(context, createComponentSession().cerca(context.getUserContext(),CompoundFindClause.and(getBaseclause(),getFindclause()),model));
			this.setIterator(context,ri);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

	public boolean isRemoveFilterButtonHidden() 
	{
			return true;
	}
	
	public boolean isFilterButtonHidden() 
	{
			return true;
	}
}
