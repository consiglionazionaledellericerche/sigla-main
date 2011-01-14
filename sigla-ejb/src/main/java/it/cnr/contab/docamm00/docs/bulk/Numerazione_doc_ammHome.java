package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Numerazione_doc_ammHome extends BulkHome {
public Numerazione_doc_ammHome(java.sql.Connection conn) {
	super(Numerazione_doc_ammBulk.class,conn);
}
public Numerazione_doc_ammHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Numerazione_doc_ammBulk.class,conn,persistentCache);
}
public Long getNextPg(UserContext userContext, Integer esercizio, String cd_cds, String uo, String tipo_doc, String user) throws PersistencyException, IntrospectionException, OutdatedResourceException, it.cnr.jada.comp.ApplicationException 
{
	Numerazione_doc_ammBulk progressivo = (Numerazione_doc_ammBulk)findByPrimaryKey( new Numerazione_doc_ammKey(cd_cds, tipo_doc, uo, esercizio ));
	//non esiste il record - segnalo errore
	if (progressivo == null)
		throw new it.cnr.jada.comp.ApplicationException("Non e' possibile assegnare progressivi");
	Long pgCorrente = new Long(progressivo.getCorrente().longValue()+1);

	progressivo.setCorrente(pgCorrente);
	progressivo.setUser(user);
	try {
		lock(progressivo);
	}catch (BusyResourceException e) {
		throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");	
	}catch (OutdatedResourceException e) {
	    throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
    }
	update(progressivo, userContext);
	return pgCorrente;
}
public Long getNextTempPg(UserContext userContext,Integer esercizio, String cd_cds, String uo, String tipo_doc, String user) throws	PersistencyException, OutdatedResourceException, BusyResourceException 
{
	Numerazione_doc_ammBulk progressivo =  null;
	Long pgCorrente = null;
	
	String tipoDocTemp = tipo_doc + "$";	
	progressivo = (Numerazione_doc_ammBulk)findByPrimaryKey(new Numerazione_doc_ammKey(cd_cds, tipoDocTemp, uo, esercizio));

	if (progressivo == null) 
	{
		progressivo = new Numerazione_doc_ammBulk();
		progressivo.setUser(user);
		progressivo.setCd_cds(cd_cds);
		progressivo.setCd_unita_organizzativa(uo);
		progressivo.setCd_tipo_documento_amm(tipoDocTemp);
		progressivo.setEsercizio(esercizio);
		
		pgCorrente = new Long(-1);
		progressivo.setCorrente(pgCorrente);
		insert(progressivo, userContext);
		
		return pgCorrente;
	}
	
	pgCorrente = new Long(progressivo.getCorrente().longValue()-1);
	progressivo.setCorrente(pgCorrente);
	progressivo.setUser(user);
	lock(progressivo);
	update(progressivo, userContext);
	
	return pgCorrente;
}
}
