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

import java.io.Serializable;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

/**
 * Insert the type's description here.
 * Creation date: (28/02/2002 16.22.07)
 * @author: Roberto Fantino
 */
public class NumerazioneTempDocAmmComponent extends it.cnr.jada.comp.CRUDComponent implements ICRUDMgr,INumerazioneTempDocAmmMgr,Cloneable,Serializable {
/**
 * NumerazioneTempDocAmmComponent constructor comment.
 */
public NumerazioneTempDocAmmComponent() {
	super();
}
//^^@@
/** 
  *  tutti i controlli superati.
  *    PreCondition:
  *      Il progressivo è stato generato senza errori.
  *    PostCondition:
  *      Viene consentita la registrazione del progressivo.
  *  validazione generazione consuntivo.
  *    PreCondition:
  *      Rilevata una condizione di errore.
  *    PostCondition:
  *      Negato il consenso alla registrazione del progressivo.
 */
//^^@@
public Long getNextTempPG (UserContext userContext,Numerazione_doc_ammBulk progressivo) 
	throws ComponentException {


	try {
		String cds = progressivo.getCd_cds();
		String cdUnitaOrg = progressivo.getCd_unita_organizzativa();
		String tipoDocTemp = progressivo.getCd_tipo_documento_amm() + "$";
		Integer es = progressivo.getEsercizio();

		progressivo.setCd_tipo_documento_amm(tipoDocTemp);
		
		Numerazione_doc_ammHome home = (Numerazione_doc_ammHome)getHome(userContext, progressivo);
		progressivo = (Numerazione_doc_ammBulk)home.findByPrimaryKey(progressivo);
		Long pgCorrente = null;
		if (progressivo == null) {
			progressivo = new Numerazione_doc_ammBulk();
			progressivo.setCd_cds(cds);
			progressivo.setCd_tipo_documento_amm(tipoDocTemp);
			progressivo.setCd_unita_organizzativa(cdUnitaOrg);
			progressivo.setEsercizio(es);
			progressivo.setUser(userContext.getUser());
			pgCorrente = new Long(-1);
			progressivo.setCorrente(pgCorrente);
			home.insert(progressivo, userContext);
			return pgCorrente;
		}
		pgCorrente = new Long(progressivo.getCorrente().longValue()-1);
		progressivo.setCorrente(pgCorrente);
		progressivo.setUser(userContext.getUser());
		home.lock(progressivo);
		home.update(progressivo, userContext);
		return pgCorrente;
	} catch(Exception e) {
		throw handleException(progressivo, e);
	}
}
}
