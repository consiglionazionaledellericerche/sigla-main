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

/*
* Created by Generator 1.0
* Date 09/05/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import java.util.Dictionary;

import it.cnr.contab.config00.file.bulk.Gruppo_fileBulk;
import it.cnr.contab.util.ICancellatoLogicamente;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.OrderedHashtable;
public class Procedure_amministrativeBulk extends Procedure_amministrativeBase implements ICancellatoLogicamente{
	
	public final static Dictionary ti_proc_ammKeys;
	
	static public final String TIPO_FORNITURA_SERVIZI 		= "FS";
	static public final String TIPO_AFFIDAMENTO_INCARICHI	= "AI";
	static public final String TIPO_INDIVIDUAZIONE_BENEFICIARIO	= "MI";
	static public final String TIPO_GENERICA 	= "GN";	
	
	static
	{
		ti_proc_ammKeys = new OrderedHashtable();
		ti_proc_ammKeys.put(TIPO_FORNITURA_SERVIZI, "Fornitura Servizi");
		ti_proc_ammKeys.put(TIPO_AFFIDAMENTO_INCARICHI, "Affidamento Incarichi");
		ti_proc_ammKeys.put(TIPO_GENERICA, "Generica");
	}

	private Gruppo_fileBulk gruppo_file = new Gruppo_fileBulk();

	public Procedure_amministrativeBulk() {
		super();
	}
	public Procedure_amministrativeBulk(java.lang.String cd_proc_amm) {
		super(cd_proc_amm);
	}
	/**
	 * Inizializza per l'inserimento i flag
	 */
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setFl_cancellato(new Boolean(false));
		return super.initializeForInsert(bp,context);
	}		
	/* (non-Javadoc)
	 * @see it.cnr.contab.util.ICancellatoLogicamente#isCancellatoLogicamente()
	 */
	public boolean isCancellatoLogicamente() {
		return getFl_cancellato().booleanValue();
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.util.ICancellatoLogicamente#cancellaLogicamente()
	 */
	public void cancellaLogicamente() {
		setFl_cancellato(new Boolean(true));		
	}

	public Gruppo_fileBulk getGruppo_file() {
		return gruppo_file;
	}
	public void setGruppo_file(Gruppo_fileBulk gruppo_file) {
		this.gruppo_file = gruppo_file;
	}
	@Override
	public String getCd_gruppo_file() {
		if (getGruppo_file()==null)
			return null;
		return getGruppo_file().getCd_gruppo_file();
	}
	@Override
	public void setCd_gruppo_file(String cd_gruppo_file) {
		if (getGruppo_file()!=null)
			getGruppo_file().setCd_gruppo_file(cd_gruppo_file);
	}
	public boolean isMeramenteOccasionaleRequired(){
		return getFl_ricerca_incarico()!=null && 
	       	   getFl_ricerca_incarico().booleanValue() &&
			   getIncarico_ric_giorni_pubbl()!=null &&
		  	   getIncarico_ric_giorni_pubbl().compareTo(new Integer(0))==0;
	}
	public boolean isMeramenteOccasionaleEnabled(){
		return getFl_meramente_occasionale()!=null && 
		       getFl_meramente_occasionale().booleanValue();
	}
}