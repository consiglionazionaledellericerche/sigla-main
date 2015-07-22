package it.cnr.contab.utenze00.comp;

import java.util.Iterator;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.utenze00.bulk.AccessoBulk;
import it.cnr.contab.utenze00.bulk.AccessoHome;
import it.cnr.contab.utenze00.bulk.AccessoKey;
import it.cnr.contab.utenze00.bulk.AssBpAccessoBulk;
import it.cnr.contab.utenze00.bulk.AssBpAccessoHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * Classe che ridefinisce alcune operazioni di CRUD su CdsBulk e Unita_organizzativaBulk
 */

public class AssBpAccessoComponent extends it.cnr.jada.comp.CRUDComponent {
	public  AssBpAccessoComponent()
	{
		
	}
public java.util.List findAccessoByBP(UserContext userContext, String bp) throws ComponentException {
	try {
		AssBpAccessoHome home = (AssBpAccessoHome)getHome(userContext,AssBpAccessoBulk.class);
		SQLBuilder sql = (SQLBuilder)super.select( userContext,null,new AssBpAccessoBulk());
		sql.addSQLClause( "AND", "business_process", sql.EQUALS, bp);
		List lista = home.fetchAll(sql);
		if (lista != null && !lista.isEmpty()){
			for (Iterator<Object> i= lista.iterator(); i.hasNext();) {
				AssBpAccessoBulk accesso= (AssBpAccessoBulk) i.next();
				AccessoHome accessoHome = (AccessoHome)getHome(userContext, AccessoBulk.class);
				AccessoBulk accessoBulk = (AccessoBulk)accessoHome.findByPrimaryKey(new AccessoBulk(accesso.getCdAccesso()));
				accesso.setAccesso(accessoBulk);
			}
		}

		return lista;
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw new ComponentException(e);
	}
}
}
