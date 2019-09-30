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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/10/2018
 */
package it.cnr.contab.doccont00.intcass.giornaliera;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
public class FlussoGiornaleDiCassaBulk extends FlussoGiornaleDiCassaBase {
	
	private BulkList<InformazioniContoEvidenzaBulk>  infoConto=new BulkList<InformazioniContoEvidenzaBulk>();
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: FLUSSO_GIORNALE_DI_CASSA
	 **/
	public FlussoGiornaleDiCassaBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: FLUSSO_GIORNALE_DI_CASSA
	 **/
	public FlussoGiornaleDiCassaBulk(java.lang.Integer esercizio, java.lang.String identificativoFlusso) {
		super(esercizio, identificativoFlusso);
	}
	public BulkList<InformazioniContoEvidenzaBulk> getInfoConto() {
		return infoConto;
	}
	public void setInfoConto(BulkList<InformazioniContoEvidenzaBulk> infoConto) {
		this.infoConto = infoConto;
	}
	public int addToInfoConto( InformazioniContoEvidenzaBulk doc ) {
		infoConto.add(doc);
		doc.setFlussoGiornaleDiCassa(this);		
		return infoConto.size()-1;
	}
	@Override
	public BulkCollection[] getBulkLists() {
		// Metti solo le liste di oggetti che devono essere resi persistenti
		return new it.cnr.jada.bulk.BulkCollection[] { 
				infoConto				
		};
	}
}