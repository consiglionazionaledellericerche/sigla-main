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

package it.cnr.contab.doccont00.comp;

import java.io.Serializable;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contHome;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;

public class NumTempDocContComponent extends it.cnr.jada.comp.CRUDComponent implements INumerazioneTemporaneaDocCont, ICRUDMgr,Cloneable,Serializable
{



//@@>> setComponentContext

//@@<< CONSTRUCTORCST
    public  NumTempDocContComponent()
    {
//>>

//<< CONSTRUCTORCSTL
        /*Default constructor*/
//>>

//<< CONSTRUCTORCSTT

    }
//^^@@
/** 
  *  tutti i controlli superati.
  *    PreCondition:
  *      Il progressivo temporaneo è stato generato senza errori.
  *    PostCondition:
  *      Viene consentita la registrazione del progressivo temporaneo.
  *  validazione
  *    PreCondition:
  *      Rilevata una condizione di errore.
  *    PostCondition:
  *      Negato il consenso alla registrazione del progressivo temporaneo.
  *  esistenza della numerazione
  *    PreCondition:
  *      Non esiste la numerazione temporanea per il tipo di documento contabile
  *    PostCondition:
  *      Viene inserita una nuova numerazione temporanea per il tipo documento contabile, CDS e esercizio correnti
  *  esistenza della numerazione definitiva
  *    PreCondition:
  *      Non esiste la numerazione definitiva per il tipo di documento contabile
  *    PostCondition:
  *      Negato il consenso alla registrazione del progressivo temporaneo.
  */
//^^@@

public java.lang.Long getNextTempPg(
	it.cnr.jada.UserContext userContext,
	it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento) 
	throws ComponentException {
	
	if (accertamento == null) return null;
	Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache(userContext).getHome( Numerazione_doc_contBulk.class );
	try {
		return numHome.getNextTempPg(userContext,
								accertamento.getEsercizio(),
								accertamento.getCd_cds(), 
								accertamento.getCd_tipo_documento_cont(),
								accertamento.getUser());
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	} catch (OutdatedResourceException e) {
		throw handleException(e);
	} catch (BusyResourceException e) {
		throw handleException(e);
	}
}
//^^@@
/** 
  *  tutti i controlli superati.
  *    PreCondition:
  *      Il progressivo temporaneo è stato generato senza errori.
  *    PostCondition:
  *      Viene consentita la registrazione del progressivo temporaneo.
  *  validazione
  *    PreCondition:
  *      Rilevata una condizione di errore.
  *    PostCondition:
  *      Negato il consenso alla registrazione del progressivo temporaneo.
  *  esistenza della numerazione
  *    PreCondition:
  *      Non esiste la numerazione temporanea per il tipo di documento contabile
  *    PostCondition:
  *      Viene inserita una nuova numerazione temporanea per il tipo documento contabile, CDS e esercizio correnti
  *  esistenza della numerazione definitiva
  *    PreCondition:
  *      Non esiste la numerazione definitiva per il tipo di documento contabile
  *    PostCondition:
  *      Negato il consenso alla registrazione del progressivo temporaneo.
  */
//^^@@

public java.lang.Long getNextTempPg(
	it.cnr.jada.UserContext userContext,
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione)
	throws ComponentException {
	
	if (obbligazione == null) return null;
	Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache(userContext).getHome( Numerazione_doc_contBulk.class );
	try {
		return numHome.getNextTempPg(userContext,
								obbligazione.getEsercizio(), 
								obbligazione.getCd_cds(), 
								obbligazione.getCd_tipo_documento_cont(), 
								obbligazione.getUser());
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	} catch (OutdatedResourceException e) {
		throw handleException(e);
	} catch (BusyResourceException e) {
		throw handleException(e);
	}
}

public java.lang.Long getNextTempPg(
	it.cnr.jada.UserContext userContext,
	it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaBulk obbligazione_modifica)
	throws ComponentException {
		
	if (obbligazione_modifica == null) return null;
		Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache(userContext).getHome( Numerazione_doc_contBulk.class );
		try {
			return numHome.getNextTempPg(userContext,
									obbligazione_modifica.getEsercizio(), 
									obbligazione_modifica.getCd_cds(), 
									obbligazione_modifica.getCd_tipo_documento_cont(), 
									obbligazione_modifica.getUser());
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(e);
		} catch (OutdatedResourceException e) {
			throw handleException(e);
		} catch (BusyResourceException e) {
			throw handleException(e);
		}
	}
public java.lang.Long getNextTempPg(
	it.cnr.jada.UserContext userContext,
	it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk accertamento_modifica)
	throws ComponentException {
		
	if (accertamento_modifica == null) return null;
		Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache(userContext).getHome( Numerazione_doc_contBulk.class );
		try {
			return numHome.getNextTempPg(userContext,
									accertamento_modifica.getEsercizio(), 
									accertamento_modifica.getCd_cds(), 
									accertamento_modifica.getCd_tipo_documento_cont(), 
									accertamento_modifica.getUser());
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(e);
		} catch (OutdatedResourceException e) {
			throw handleException(e);
		} catch (BusyResourceException e) {
			throw handleException(e);
		}
	}
}
