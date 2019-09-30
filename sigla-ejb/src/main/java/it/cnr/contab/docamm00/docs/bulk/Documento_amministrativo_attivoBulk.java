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

package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
/**
 * Insert the type's description here.
 * Creation date: (3/22/2002 1:06:56 PM)
 * @author: Roberto Peli
 */
public class Documento_amministrativo_attivoBulk 
	extends Fattura_attivaBulk
	implements IDocumentoAmministrativoGenericoBulk {
/**
 * Documento_amministrativo_passivoBulk constructor comment.
 */
public Documento_amministrativo_attivoBulk() {
	super();
}
/**
 * Documento_amministrativo_passivoBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param cd_unita_organizzativa java.lang.String
 * @param esercizio java.lang.Integer
 * @param pg_fattura_passiva java.lang.Long
 */
public Documento_amministrativo_attivoBulk(String cd_cds, String cd_unita_organizzativa, Integer esercizio, Long pg_fattura_attiva) {
	super(cd_cds, cd_unita_organizzativa, esercizio, pg_fattura_attiva);
}
/**
 * Insert the method's description here.
 * Creation date: (3/22/2002 1:06:56 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
 */
public Class getChildClass() {
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (3/22/2002 1:06:56 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
 */
public String getManagerName() {

	if (getTi_fattura() != null) {
		if (Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO.equalsIgnoreCase(getTi_fattura()))
			return "CRUDNotaDiCreditoAttivaBP";
		else if (Fattura_attivaBulk.TIPO_NOTA_DI_DEBITO.equalsIgnoreCase(getTi_fattura()))
			return "CRUDNotaDiDebitoAttivaBP";
	}
	return "CRUDFatturaAttivaBP";
}
/**
 * Insert the method's description here.
 * Creation date: (3/22/2002 2:53:16 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
 */
public java.lang.String getManagerOptions() {

	// NON CANCELLARE QUESTO COMMENTO: E' DA ABILITARE AL POSTO DEL RESTO NEL CASO DI APERTURA
	// IN MODIFICA
	//if (getTi_fattura() != null &&
		//(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO.equalsIgnoreCase(getTi_fattura()) ||
			//Fattura_attivaBulk.TIPO_NOTA_DI_DEBITO.equalsIgnoreCase(getTi_fattura())))
			//return "VTh";
	//return "MTh";
	return "VTh";
}
/**
 * Insert the method's description here.
 * Creation date: (3/22/2002 2:53:16 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
 * @param docAmmGen it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoGenericoBulk
 */
public IDocumentoAmministrativoBulk getSpecializedInstance() {

	if (getTi_fattura() != null) {
		if (Fattura_attivaBulk.TIPO_FATTURA_ATTIVA.equalsIgnoreCase(getTi_fattura()))
			return new Fattura_attiva_IBulk(
								getCd_cds(),
								getCd_unita_organizzativa(),
								getEsercizio(),
								getPg_fattura_attiva());
		else if (Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO.equalsIgnoreCase(getTi_fattura()))
						return new Nota_di_credito_attivaBulk(
								getCd_cds(),
								getCd_unita_organizzativa(),
								getEsercizio(),
								getPg_fattura_attiva());
		else if (Fattura_attivaBulk.TIPO_NOTA_DI_DEBITO.equalsIgnoreCase(getTi_fattura()))
						return new Nota_di_debito_attivaBulk(
								getCd_cds(),
								getCd_unita_organizzativa(),
								getEsercizio(),
								getPg_fattura_attiva());
	}
	return null;	
}
/**
 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>
 * in stato <code>SEARCH</code>.
 * Questo metodo viene invocato automaticamente da un 
 * <code>it.cnr.jada.util.action.CRUDBP</code> quando viene inizializzato
 * per la ricerca di un OggettoBulk.
 */
public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,ActionContext context) {

	initialize(bp, context);

	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	setCd_cds_origine(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
	
	setTi_fattura(null);
	setFl_intra_ue(null);
	setFl_extra_ue(null);
	setFl_san_marino(null);
	setFl_liquidazione_differita(null);

	return this;
}
}
