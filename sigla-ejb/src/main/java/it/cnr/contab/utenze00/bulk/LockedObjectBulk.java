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
 * Created by BulkGenerator 1.1.0 [15/09/2008]
 * Date 17/09/2008
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.annotation.BulkInfoAnnotation;
import it.cnr.jada.bulk.annotation.ColumnSetAnnotation;
import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;
import it.cnr.jada.bulk.annotation.FormAnnotation;
import it.cnr.jada.bulk.annotation.FormatName;
import it.cnr.jada.bulk.annotation.FreeSearchSetAnnotation;
import it.cnr.jada.bulk.annotation.InputType;
import it.cnr.jada.bulk.annotation.TypeProperty;
import it.cnr.jada.persistency.Persistent;
@BulkInfoAnnotation(shortDescription="Sessioni in lock su Tabelle", 
					longDescription="Sessioni in lock su Tabelle",
					form=@FormAnnotation(value={
							@FieldPropertyAnnotation(name="cds", type=TypeProperty.FormFieldProperty)}),
					freeSearchSet=@FreeSearchSetAnnotation(value={
							@FieldPropertyAnnotation(name="cds", type=TypeProperty.FindFieldProperty)}),
					columnSet=@ColumnSetAnnotation(name="oggetti", value={
							@FieldPropertyAnnotation(name="objectName", type=TypeProperty.ColumnFieldProperty),
							@FieldPropertyAnnotation(name="dacr", type=TypeProperty.ColumnFieldProperty),
							@FieldPropertyAnnotation(name="comments", type=TypeProperty.ColumnFieldProperty)})									)							
public class LockedObjectBulk extends OggettoBulk implements Persistent{
 
//    ID_SESSIONE VARCHAR(100) NOT NULL
	private java.lang.String idSessione;
 
//    SERVER_URL VARCHAR(100) NOT NULL
	private java.lang.String serverUrl;
  
//    OBJECT_NAME VARCHAR(128)
	@FieldPropertyAnnotation(name="objectName",
			inputType=InputType.TEXTAREA,
			cols=60,
			rows=5,
			maxLength=128,
			label="Tabella")	
	private java.lang.String objectName;
 
//    COMMENTS VARCHAR(4000)
	@FieldPropertyAnnotation(name="comments",
			inputType=InputType.TEXTAREA,
			cols=60,
			rows=5,
			maxLength=4000,
			label="Descrizione")	
	private java.lang.String comments;
 
	private UtenteBulk utente = new UtenteBulk();
	
	@FieldPropertyAnnotation(name="cds",
							inputType=InputType.SEARCHTOOL,
							formName="searchtool",
							enabledOnSearch=true,
							label="Centro di Spesa")
	private CdsBulk cds = new CdsBulk();
	
	private BulkList utenti = new BulkList();

	private BulkList oggetti = new BulkList();
	
	/**
	 * Created by BulkGenerator 1.1.0 [15/09/2008]
	 * Table name: LOCKED_OBJECT
	 **/
	public LockedObjectBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 1.1.0 [15/09/2008]
	 * Restituisce il valore di: [idSessione]
	 **/
	public java.lang.String getIdSessione() {
		return idSessione;
	}
	/**
	 * Created by BulkGenerator 1.1.0 [15/09/2008]
	 * Setta il valore di: [idSessione]
	 **/
	public void setIdSessione(java.lang.String idSessione)  {
		this.idSessione=idSessione;
	}
	/**
	 * Created by BulkGenerator 1.1.0 [15/09/2008]
	 * Restituisce il valore di: [serverUrl]
	 **/
	public java.lang.String getServerUrl() {
		return serverUrl;
	}
	/**
	 * Created by BulkGenerator 1.1.0 [15/09/2008]
	 * Setta il valore di: [serverUrl]
	 **/
	public void setServerUrl(java.lang.String serverUrl)  {
		this.serverUrl=serverUrl;
	}	
	/**
	 * Created by BulkGenerator 1.1.0 [15/09/2008]
	 * Setta il valore di: [dacr]
	 **/
	@FieldPropertyAnnotation(name="dacr",
			inputType=InputType.ROTEXT,
			formatName=FormatName.timestamp,
			nullable=false,
			label="Data")
	public void setDacr(java.sql.Timestamp dacr)  {
		this.dacr=dacr;
	}	
	/**
	 * Created by BulkGenerator 1.1.0 [15/09/2008]
	 * Restituisce il valore di: [objectName]
	 **/
	public java.lang.String getObjectName() {
		return objectName;
	}
	/**
	 * Created by BulkGenerator 1.1.0 [15/09/2008]
	 * Setta il valore di: [objectName]
	 **/
	public void setObjectName(java.lang.String objectName)  {
		this.objectName=objectName;
	}
	/**
	 * Created by BulkGenerator 1.1.0 [15/09/2008]
	 * Restituisce il valore di: [comments]
	 **/
	public java.lang.String getComments() {
		return comments;
	}
	/**
	 * Created by BulkGenerator 1.1.0 [15/09/2008]
	 * Setta il valore di: [comments]
	 **/
	public void setComments(java.lang.String comments)  {
		this.comments=comments;
	}
	
	public UtenteBulk getUtente() {
		return utente;
	}
	
	public void setUtente(UtenteBulk utente) {
		this.utente = utente;
	}
	
	public CdsBulk getCds() {
		return cds;
	}
	
	public void setCds(CdsBulk cds) {
		this.cds = cds;
	}
	
	public String getCdCds(){
		if (getCds() == null)
			return null;
		return getCds().getCd_unita_organizzativa();
	}
	
	public void setCdCds(java.lang.String cdCds){
		getCds().setCd_unita_organizzativa(cdCds);
	}
	
	public String getCdUtente(){
		if (getUtente() == null)
			return null;
		return getUtente().getCd_utente();
	}
	
	public void setCdUtente(java.lang.String cdUtente){
		getUtente().setCd_utente(cdUtente);
	}
	
	public int addToUtenti( UtenteBulk utente ){
		utenti.add(utente);
		return utenti.size()-1;
	}
	
	public int addToOggetti( LockedObjectBulk lockedObject ){
		oggetti.add(lockedObject);
		return oggetti.size()-1;
	}

	public UtenteBulk removeFromUtenti(int index){
		return (UtenteBulk)utenti.remove(index);
	}

	public LockedObjectBulk removeFromOggetti(int index){
		return (LockedObjectBulk)oggetti.remove(index);
	}
	public BulkList getUtenti() {
		return utenti;
	}
	public void setUtenti(BulkList utenti) {
		this.utenti = utenti;
	}
	public BulkList getOggetti() {
		return oggetti;
	}
	public void setOggetti(BulkList oggetti) {
		this.oggetti = oggetti;
	}
	
}