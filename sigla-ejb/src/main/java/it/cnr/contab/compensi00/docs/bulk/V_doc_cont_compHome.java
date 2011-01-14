package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_doc_cont_compHome extends BulkHome {
public V_doc_cont_compHome(java.sql.Connection conn) {
	super(V_doc_cont_compBulk.class,conn);
}
public V_doc_cont_compHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_doc_cont_compBulk.class,conn,persistentCache);
}
/**
  * Viene caricata la lista dei Documenti Contabili
  * (Mandati e Reversali) associati al compenso
  *
  * @param	compenso	il compenso di cui si vuole caricare i Documenti Contabili
  * @return	La lista dei Documenti Contabili associati al compenso
  *
**/
public java.util.List loadAllDocCont(CompensoBulk compenso) throws PersistencyException {

	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND","cd_cds_compenso",sql.EQUALS,compenso.getCd_cds());
	sql.addClause("AND","cd_uo_compenso",sql.EQUALS,compenso.getCd_unita_organizzativa());
	sql.addClause("AND","esercizio_compenso",sql.EQUALS,compenso.getEsercizio());
	sql.addClause("AND","pg_compenso",sql.EQUALS,compenso.getPg_compenso());

	java.util.List l = fetchAll(sql);
	for (java.util.Iterator i = l.iterator();i.hasNext();){
		V_doc_cont_compBulk docContComp = (V_doc_cont_compBulk)i.next();
		docContComp.setManRev(loadDocCont(docContComp));
	}
	return l;
}
/**
  * Viene caricata la lista dei Documenti Contabili
  * (Mandati e Reversali) associati al compenso
  *
  * @param	compenso	il compenso di cui si vuole caricare i Documenti Contabili
  * @return	La lista dei Documenti Contabili associati al compenso
  *
**/
private IManRevBulk loadDocCont(V_doc_cont_compBulk docContComp) throws PersistencyException{

	IManRevBulk manRev = null;
	if (docContComp.TIPO_DOC_CONT_MANDATO.equals(docContComp.getTipo_doc_cont())){
		manRev = loadMandato(docContComp);
	}

	if (docContComp.TIPO_DOC_CONT_REVERSALE.equals(docContComp.getTipo_doc_cont())){
		manRev = loadReversale(docContComp);
	}

	return manRev;
}
/**
  * Viene caricata la lista dei Documenti Contabili
  * (Mandati e Reversali) associati al compenso
  *
  * @param	compenso	il compenso di cui si vuole caricare i Documenti Contabili
  * @return	La lista dei Documenti Contabili associati al compenso
  *
**/
private IManRevBulk loadMandato(V_doc_cont_compBulk docContComp) throws PersistencyException{

	MandatoIHome manHome = (MandatoIHome)getHomeCache().getHome(MandatoIBulk.class);
	SQLBuilder sql = manHome.createSQLBuilder();
	sql.addClause("AND","cd_cds",sql.EQUALS,docContComp.getCd_cds_doc_cont());
	sql.addClause("AND","esercizio",sql.EQUALS,docContComp.getEsercizio_doc_cont());
	sql.addClause("AND","pg_mandato",sql.EQUALS,docContComp.getPg_doc_cont());

	Broker broker = createBroker(sql);
	IManRevBulk manRev = null;
	if (broker.next())
		manRev = (IManRevBulk)manHome.fetch(broker);
	broker.close();

	return manRev;
}
/**
  * Viene caricata la lista dei Documenti Contabili
  * (Mandati e Reversali) associati al compenso
  *
  * @param	compenso	il compenso di cui si vuole caricare i Documenti Contabili
  * @return	La lista dei Documenti Contabili associati al compenso
  *
**/
private IManRevBulk loadReversale(V_doc_cont_compBulk docContComp) throws PersistencyException{

	ReversaleIHome revHome = (ReversaleIHome)getHomeCache().getHome(ReversaleIBulk.class);
	SQLBuilder sql = revHome.createSQLBuilder();
	sql.addClause("AND","cd_cds", sql.EQUALS, docContComp.getCd_cds_doc_cont());
	sql.addClause("AND","esercizio", sql.EQUALS, docContComp.getEsercizio_doc_cont());
	sql.addClause("AND","pg_reversale", sql.EQUALS, docContComp.getPg_doc_cont());

	Broker broker = createBroker(sql);
	IManRevBulk manRev = null;
	if (broker.next())
		manRev = (IManRevBulk)revHome.fetch(broker);
	broker.close();

	return manRev;
}
}
