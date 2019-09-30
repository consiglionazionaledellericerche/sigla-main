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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;

import java.math.BigDecimal;

public class Linea_attivitaBulk extends it.cnr.jada.bulk.OggettoBulk
{
	private BigDecimal prcImputazioneFin = new BigDecimal(0);

	private it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_att = new it.cnr.contab.config00.latt.bulk.WorkpackageBulk();
	private ObbligazioneBulk obbligazione;
	private AccertamentoBulk accertamento;
public Linea_attivitaBulk() {
	super();
}
/**
 * @return it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
 */
public it.cnr.contab.config00.latt.bulk.WorkpackageBulk getLinea_att() {
	return linea_att;
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk
 */
public ObbligazioneBulk getObbligazione() {
	return obbligazione;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'prcImputazioneFin'
 *
 * @return Il valore della proprietà 'prcImputazioneFin'
 */
public BigDecimal getPrcImputazioneFin() {
	return prcImputazioneFin;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOcd_linea_attivita'
 *
 * @return Il valore della proprietà 'rOcd_linea_attivita'
 */
public boolean isROcd_linea_attivita() 
{
	return 	linea_att.getCrudStatus() == linea_att.NORMAL ;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOPrcImputazioneFin'
 *
 * @return Il valore della proprietà 'rOPrcImputazioneFin'
 */
public boolean isROPrcImputazioneFin() 
{
	// MITODO - controllare questa condizione rispetto all'originale
	if (getObbligazione() != null)
		return getObbligazione().getFl_calcolo_automatico() == null ||
	        !getObbligazione().getFl_calcolo_automatico().booleanValue();
	if (getAccertamento() != null)
		return getAccertamento().getFl_calcolo_automatico() == null ||
			!getAccertamento().getFl_calcolo_automatico().booleanValue();
	return true;
}
/**
 * @param newLinea_att it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
 */
public void setLinea_att(it.cnr.contab.config00.latt.bulk.WorkpackageBulk newLinea_att) {
	linea_att = newLinea_att;
}
/**
 * @param newObbligazione it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk
 */
public void setObbligazione(ObbligazioneBulk newObbligazione) {
	obbligazione = newObbligazione;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'prcImputazioneFin'
 *
 * @param newPrcImputazioneFin	Il valore da assegnare a 'prcImputazioneFin'
 */
public void setPrcImputazioneFin(BigDecimal newPrcImputazioneFin) {
	prcImputazioneFin = newPrcImputazioneFin;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws ValidationException {
	super.validate();

	if ( getObbligazione()!=null&& getObbligazione().getFl_calcolo_automatico()!=null && getObbligazione().getFl_calcolo_automatico().booleanValue() )
	{
		if ( prcImputazioneFin == null )
			throw new ValidationException( "Il campo PERCENTUALE per il nuovo GAE deve essere valorizzato." );
				
		if ( getObbligazione().getPg_obbligazione_ori_riporto() == null )
			if ( prcImputazioneFin.doubleValue() == 0 )
				throw new ValidationException( "Il campo PERCENTUALE per il nuovo GAE deve essere diverso da 0." );
				
		if ( prcImputazioneFin.doubleValue() <0 || prcImputazioneFin.doubleValue() > 100  )
		{
			prcImputazioneFin = new BigDecimal( 0 );
			throw new ValidationException( "Il campo PERCENTUALE per il nuovo GAE deve assumere un valore fra 0 e 100." );
		}
	}	
	if ( getAccertamento()!=null&& getAccertamento().getFl_calcolo_automatico()!=null && getAccertamento().getFl_calcolo_automatico().booleanValue() )
	{
		if ( prcImputazioneFin == null )
			throw new ValidationException( "Il campo PERCENTUALE per il nuovo GAE deve essere valorizzato." );
				
		if ( getAccertamento().getPg_accertamento_ori_riporto() == null )
			if ( prcImputazioneFin.doubleValue() == 0 )
				throw new ValidationException( "Il campo PERCENTUALE per il nuovo GAE deve essere diverso da 0." );
				
		if ( prcImputazioneFin.doubleValue() <0 || prcImputazioneFin.doubleValue() > 100  )
		{
			prcImputazioneFin = new BigDecimal( 0 );
			throw new ValidationException( "Il campo PERCENTUALE per il nuovo GAE deve assumere un valore fra 0 e 100." );
		}
	}	
	if (linea_att.getCrudStatus() != linea_att.NORMAL )
		throw new ValidationException( "Non e' stata effettuata la ricerca del nuovo GAE." );
}
	/**
	 * @return
	 */
	public AccertamentoBulk getAccertamento() {
		return accertamento;
	}

	/**
	 * @param bulk
	 */
	public void setAccertamento(AccertamentoBulk bulk) {
		accertamento = bulk;
	}

}
