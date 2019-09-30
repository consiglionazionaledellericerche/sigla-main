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

package it.cnr.contab.docamm00.comp;

import java.sql.CallableStatement;

import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.GenericComponent;
import it.cnr.jada.persistency.sql.LoggableStatement;

/**
 * Insert the type's description here.
 * Creation date: (04/06/2003 14.11.36)
 * @author: Roberto Peli
 */
public class RiportoDocAmmComponent 
	extends GenericComponent
	implements IRiportoDocAmmMgr {
/**
 * RiportoDocAmmComponent constructor comment.
 */
public RiportoDocAmmComponent() {
	
}
private void callRiportaAvanti(
	UserContext userContext,
	IDocumentoAmministrativoBulk docAmm)
	throws  ComponentException {

	LoggableStatement cs = null;
	try	{
		cs = new LoggableStatement(getConnection(userContext), 
			"{ call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
			"CNRCTB046.riportoEsNextDocAmm(?, ?, ?, ?, ?, ?, ?) }",false,this.getClass());
		
		cs.setInt(1, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue());
		cs.setString(2, docAmm.getCd_tipo_doc_amm());
		cs.setString(3, docAmm.getCd_cds());
		cs.setInt(4, docAmm.getEsercizio().intValue());
		cs.setString(5, docAmm.getCd_uo());
		cs.setLong(6, docAmm.getPg_doc_amm().longValue());
		cs.setString(7, it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));

		cs.executeQuery();
		
	} catch (Throwable e) {
		throw handleException(e);
	} finally {
		try {
			if (cs != null) cs.close();
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		}
 	}
}
private void callRiportaIndietro(
    UserContext userContext,
    IDocumentoAmministrativoBulk docAmm)
    throws ComponentException {

	LoggableStatement cs = null;
    try {
        cs =
            new LoggableStatement(getConnection(userContext),
                "{ call "
                    + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                    + "CNRCTB046.deriportoEsNextDocAmm(?, ?, ?, ?, ?, ?, ?) }",false,this.getClass());

        cs.setInt(1, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue());
        cs.setString(2, docAmm.getCd_tipo_doc_amm());
        cs.setString(3, docAmm.getCd_cds());
        cs.setInt(4, docAmm.getEsercizio().intValue());
        cs.setString(5, docAmm.getCd_uo());
        cs.setLong(6, docAmm.getPg_doc_amm().longValue());
        cs.setString(7, it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));

        cs.executeQuery();

    } catch (Throwable e) {
        throw handleException(e);
    } finally {
        try {
            if (cs != null)
                cs.close();
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }
    }
}
/**
 * Insert the method's description here.
 * Creation date: (04/06/2003 14.16.46)
 * @return java.lang.String
 */
private java.lang.String callVerificaStatoRiporto(
	UserContext userContext, 
	IDocumentoAmministrativoBulk documentoAmministrativo) 
	throws ComponentException {

	LoggableStatement cs = null;
	String status = null;
	try {
		try	{
			cs = new LoggableStatement(getConnection(userContext), 
					"{ ? = call " +
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
					"CNRCTB105.getStatoRiportato(?, ?, ?, ?, ?, ?) }",false,this.getClass());
			
			cs.registerOutParameter( 1, java.sql.Types.VARCHAR);
			cs.setString(2, documentoAmministrativo.getCd_cds());
			cs.setString(3, documentoAmministrativo.getCd_uo());
			cs.setInt(4, documentoAmministrativo.getEsercizio().intValue());
			cs.setLong(5, documentoAmministrativo.getPg_doc_amm().longValue());
			cs.setString(6, documentoAmministrativo.getCd_tipo_doc_amm());
			cs.setInt(7, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue());
			
			cs.executeQuery();
			status = new String(cs.getString(1));
		} catch (Throwable e) {
			throw handleException(e);
		} finally {
			if (cs != null) cs.close();
		}
	} catch (java.sql.SQLException ex) {
		throw handleException(ex);
	}
	return status;		
}
/**
 * Insert the method's description here.
 * Creation date: (04/06/2003 14.16.46)
 * @return java.lang.String
 */
private java.lang.String callVerificaStatoRiportoInScrivania(
	UserContext userContext, 
	IDocumentoAmministrativoBulk documentoAmministrativo) 
	throws ComponentException {

	LoggableStatement cs = null;
	String status = null;
	try {
		try	{
			cs = new LoggableStatement(getConnection(userContext), 
					"{ ? = call " +
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
					"CNRCTB105.getStatoRiportatoInScrivania(?, ?, ?, ?, ?, ?) }",false,this.getClass());
			
			cs.registerOutParameter( 1, java.sql.Types.VARCHAR);
			cs.setString(2, documentoAmministrativo.getCd_cds());
			cs.setString(3, documentoAmministrativo.getCd_uo());
			cs.setInt(4, documentoAmministrativo.getEsercizio().intValue());
			cs.setLong(5, documentoAmministrativo.getPg_doc_amm().longValue());
			cs.setString(6, documentoAmministrativo.getCd_tipo_doc_amm());
			cs.setInt(7, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue());
			
			cs.executeQuery();
			status = new String(cs.getString(1));
		} catch (Throwable e) {
			throw handleException(e);
		} finally {
			if (cs != null) cs.close();
		}
	} catch (java.sql.SQLException ex) {
		throw handleException(ex);
	}
	return status;		
}
public java.lang.String getStatoRiporto(
	UserContext userContext,
	IDocumentoAmministrativoBulk documentoAmministrativo)
	throws ComponentException {

	if (documentoAmministrativo == null)
		return documentoAmministrativo.NON_RIPORTATO;

	String status = callVerificaStatoRiporto(userContext, documentoAmministrativo);
	
	if (status == null)
		throw new ApplicationException("Impossibile ottenere lo stato \"riporto\" per il documento amministrativo dalla procedura database!");
	return status;
}
/**
 * Insert the method's description here.
 * Creation date: (02/11/2004 14.44.21)
 * @return java.lang.String
 */
public java.lang.String getStatoRiportoInScrivania(it.cnr.jada.UserContext userContext, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk documentoAmministrativo) throws it.cnr.jada.comp.ComponentException {

	if (documentoAmministrativo == null)
		return documentoAmministrativo.NON_RIPORTATO;

	String status = callVerificaStatoRiportoInScrivania(userContext, documentoAmministrativo);
	
	if (status == null)
		throw new ApplicationException("Impossibile ottenere lo stato \"riporto in scrivania\" per il documento amministrativo dalla procedura database!");
	return status;
}
/**
 * riportaAvanti method comment.
 */
public IDocumentoAmministrativoBulk riportaAvanti(
	UserContext userContext, 
	IDocumentoAmministrativoBulk docAmm, 
	OptionRequestParameter status) 
	throws ComponentException {

	callRiportaAvanti(userContext, docAmm);
	
	return docAmm;
}
/**
 * riportaIndietro method comment.
 */
public IDocumentoAmministrativoBulk riportaIndietro(
	UserContext userContext, 
	IDocumentoAmministrativoBulk docAmm) 
	throws ComponentException {

	callRiportaIndietro(userContext, docAmm);
	
	return docAmm;
}
}
