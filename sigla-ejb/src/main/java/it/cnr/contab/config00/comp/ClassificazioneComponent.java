package it.cnr.contab.config00.comp;

import java.io.Serializable;
import java.sql.SQLException;

import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;

/**
 * @author aimprota
 *
 */
public class ClassificazioneComponent extends CRUDComponent implements Cloneable,Serializable
{

    /**
     * 
     */
    public ClassificazioneComponent()
    {
        super();
    }
    
			public SQLBuilder selectClassificazione_entrateByClause (UserContext userContext,
												  OggettoBulk bulk,
												  Classificazione_entrateBulk classificazione,
												  CompoundFindClause clause)
			throws ComponentException, PersistencyException
			{
					if (clause == null) 
					  clause = classificazione.buildFindClauses(null);
					SQLBuilder sql = getHome(userContext, classificazione).createSQLBuilder();
					sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio());
				    sql.addSQLClause("AND", "FL_MASTRINO", sql.EQUALS,"N" );
					if (clause != null) 
					  sql.addClause(clause);
					return sql;
			}

			public SQLBuilder selectClassificazione_speseByClause (UserContext userContext,
												  OggettoBulk bulk,
												  Classificazione_speseBulk classificazione,
												  CompoundFindClause clause)
			throws ComponentException, PersistencyException
			{
					if (clause == null) 
					  clause = classificazione.buildFindClauses(null);
					SQLBuilder sql = getHome(userContext, classificazione).createSQLBuilder();
					sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio());
					sql.addSQLClause("AND", "FL_MASTRINO", sql.EQUALS,"N" );
					if (clause != null) 
					  sql.addClause(clause);
					return sql;
			}

	/**
	 * Pre:  Controllo Mastrino=true e esistono record figli
	 * Post: Segnalazione "La Classificazione in esame non può essere un Mastrino poichè è già Classificazione padre di altre."
	 */  			
	public OggettoBulk modificaConBulk(UserContext uc, OggettoBulk bulk) throws ComponentException {
	 
	 /*Entrata*/
	 if (bulk instanceof Classificazione_entrateBulk)
	 {	 
		if ( ((Classificazione_entrateBulk) bulk).getFl_mastrino().booleanValue() )
		{
			SQLBuilder sql = getHome(uc, (Classificazione_entrateBulk)bulk).createSQLBuilder();
			sql.addSQLClause("AND", "ESERCIZIO_PADRE", sql.EQUALS, ((Classificazione_entrateBulk)bulk).getEsercizio());
			sql.addSQLClause("AND", "CODICE_CLA_E_PADRE", sql.EQUALS, ((Classificazione_entrateBulk)bulk).getCodice_cla_e());
			try {
				if (sql.executeCountQuery(getConnection(uc))>0)
				throw new it.cnr.jada.comp.ApplicationException("La Classificazione di entrata in esame non può essere un Mastrino \npoichè è già Classificazione padre di altre.");							 
				} catch (java.sql.SQLException e) {
				   throw handleSQLException(e);				
			    } 
						
		} 
		else
		{
		  return super.modificaConBulk(uc, bulk);
		}
	 } 
	 else if (bulk instanceof Classificazione_speseBulk)
	 /*Spesa*/
	 {
		if ( ((Classificazione_speseBulk) bulk).getFl_mastrino().booleanValue() )
		{
			SQLBuilder sql = getHome(uc, (Classificazione_speseBulk)bulk).createSQLBuilder();
			sql.addSQLClause("AND", "ESERCIZIO_PADRE", sql.EQUALS, ((Classificazione_speseBulk)bulk).getEsercizio());
			sql.addSQLClause("AND", "CODICE_CLA_S_PADRE", sql.EQUALS, ((Classificazione_speseBulk)bulk).getCodice_cla_s());
			try {
				if (sql.executeCountQuery(getConnection(uc))>0)							
				throw new it.cnr.jada.comp.ApplicationException("La Classificazione di spesa in esame non può essere un Mastrino \npoichè è già Classificazione padre di altre.");						 
				} catch (java.sql.SQLException e) {
				   throw handleSQLException(e);				
				} 
						
		} 
		else
		{
		  return super.modificaConBulk(uc, bulk);
		}	 	
	 }		  
	     return super.modificaConBulk(uc, bulk);

 }
	
}
	
