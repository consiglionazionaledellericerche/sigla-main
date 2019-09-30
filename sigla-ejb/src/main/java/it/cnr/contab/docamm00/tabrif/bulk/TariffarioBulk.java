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

package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Adatta e implementa: {@link TariffarioBase }
 * 		perchè si ottengano e si settino gli oggetti complessi.
 * 
 * @author: Bisquadro Vincenzo
 */

public class TariffarioBulk extends TariffarioBase {

	private Voce_ivaBulk voce_iva = new Voce_ivaBulk();
	private java.util.Collection voci_iva;
/**
 * Costruttore standard di Pdg_aggregato_spe_det_inizialeBulk.
 */
public TariffarioBulk() {
	super();
}
/**
 * Costruttore di TariffarioBulk cui vengono passati in ingresso:
 * 		cd_tariffario, cd_unita_organizzativa, dt_ini_validita 
 *
 * @param cd_tariffario java.lang.String
 * @param cd_unita_organizzativa java.lang.String
 * @param dt_ini_validita java.sql.Timestamp
 */
public TariffarioBulk(java.lang.String cd_tariffario,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_ini_validita) {
	super(cd_tariffario,cd_unita_organizzativa,dt_ini_validita);
}
/**
 * Insert the method's description here.
 * Creation date: (27/03/2002 11.38.59)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFineValidita() {

	if ( (getDt_fine_validita()!=null) && (getDt_fine_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO)))
		return null;
	return getDt_fine_validita();
}
/**
 * Insert the method's description here.
 * Creation date: (17/10/2001 17.31.24)
 *
 * Restituisce l'istanza voce_iva di Voce_ivaBulk
 *
 * @return it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
 */
public Voce_ivaBulk getVoce_iva() {
	return voce_iva;
}
/**
 * Insert the method's description here.
 * Creation date: (17/10/2001 17.31.24)
 *
 * Restituisce la collezione di voci_iva
 *
 * @return java.util.Collection
 */
public java.util.Collection getVoci_iva() {
	return voci_iva;
}
/**
 * Metodo che inizializza la pagina Html con l'unità organizzativa 
 * Creation date: (17/10/2001 17.31.24)
 *
 * @return it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk
 */
protected OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setCd_unita_organizzativa(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());
	return super.initialize(bp,context);
}
/**
 * Metodo che inizializza la pagina Html con l'unità organizzativa 
 * Creation date: (17/10/2001 17.31.24)
 *
 * @return it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	TariffarioBulk obj = (TariffarioBulk)super.initializeForInsert(bp,context);
	obj.setVoce_iva(new Voce_ivaBulk());

	return obj;
}
public boolean isROCd_voce_iva() {
	
	return getVoce_iva() == null ||
			getVoce_iva().getCrudStatus() == OggettoBulk.NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (27/03/2002 11.39.45)
 * @param newData java.sql.Timestamp
 */
public void setDataFineValidita(java.sql.Timestamp newData) {

	this.setDt_fine_validita(newData);
}
/**
 * Setta l'istanza voce_iva di Voce_ivaBulk
 * Creation date: (17/10/2001 17.31.24)
 * @param newVoce_iva it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
 */
public void setVoce_iva(Voce_ivaBulk newVoce_iva) {
	voce_iva = newVoce_iva;
}
/**
 * Setta la collezione voci_iva.
 * Creation date: (17/10/2001 17.32.19)
 * @param newVoci_iva java.util.Collection
 */
public void setVoci_iva(java.util.Collection newVoci_iva) {
	voci_iva = newVoci_iva;
}
public void validate() throws ValidationException {

	if(getCd_tariffario() == null)
				throw new ValidationException("Attenzione, il Codice del tariffario è obbligatorio!");
	if((getIm_tariffario() == null) || getIm_tariffario().compareTo(new java.math.BigDecimal(0)) == 0)
		   		throw new ValidationException("Attenzione, il Prezzo Unitario deve essere maggiore di zero!");
		   
}
}
