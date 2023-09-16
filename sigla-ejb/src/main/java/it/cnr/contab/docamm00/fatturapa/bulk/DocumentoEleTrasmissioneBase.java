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
 * Date 25/02/2015
 */
package it.cnr.contab.docamm00.fatturapa.bulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util.SIGLAStoragePropertyNames;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.persistency.Keyed;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;

import java.util.List;
import java.util.Optional;

public class DocumentoEleTrasmissioneBase extends DocumentoEleTrasmissioneKey implements Keyed {
	
	private java.lang.String progressivoInvio;
	
//    FORMATO_TRASMISSIONE VARCHAR(5) NOT NULL
	private java.lang.String formatoTrasmissione;
 
//    CODICE_DESTINATARIO VARCHAR(6)
	private java.lang.String codiceDestinatario;

	//  CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cdUnitaOrganizzativa;

 
//    TRASMITTENTE_TELEFONO VARCHAR(12)
	private java.lang.String trasmittenteTelefono;
 
//    TRASMITTENTE_EMAIL VARCHAR(256)
	private java.lang.String trasmittenteEmail;
 
//    PRESTATORE_PAESE VARCHAR(256)
	private java.lang.String prestatorePaese;
 
//    PRESTATORE_CODICE VARCHAR(256)
	private java.lang.String prestatoreCodice;
 
//    PRESTATORE_CODICEFISCALE VARCHAR(256)
	private java.lang.String prestatoreCodicefiscale;
 
//    PRESTATORE_DENOMINAZIONE VARCHAR(256)
	private java.lang.String prestatoreDenominazione;
 
//    PRESTATORE_NOME VARCHAR(256)
	private java.lang.String prestatoreNome;
 
//    PRESTATORE_COGNOME VARCHAR(256)
	private java.lang.String prestatoreCognome;
 
//    PRESTATORE_TITOLO VARCHAR(256)
	private java.lang.String prestatoreTitolo;
 
//    PRESTATORE_CODEORI VARCHAR(256)
	private java.lang.String prestatoreCodeori;
 
//    ALBOPROFESSIONALE VARCHAR(256)
	private java.lang.String alboprofessionale;
 
//    PROVINCIAALBO VARCHAR(256)
	private java.lang.String provinciaalbo;
//  PROVINCIAALBO VARCHAR(256)
	private java.lang.String numeroalbo;
	
//    DATAISCRIZIONEALBO TIMESTAMP(7)
	private java.sql.Timestamp dataiscrizionealbo;
 
//    REGIMEFISCALE VARCHAR(256)
	private java.lang.String regimefiscale;
 
//    PRESTATORE_INDIRIZZO VARCHAR(256)
	private java.lang.String prestatoreIndirizzo;
 
//    PRESTATORE_NUMEROCIVICO VARCHAR(256)
	private java.lang.String prestatoreNumerocivico;
 
//    PRESTATORE_CAP VARCHAR(256)
	private java.lang.String prestatoreCap;
 
//    PRESTATORE_COMUNE VARCHAR(256)
	private java.lang.String prestatoreComune;
 
//    PRESTATORE_PROVINCIA VARCHAR(256)
	private java.lang.String prestatoreProvincia;
 
//    PRESTATORE_NAZIONE VARCHAR(256)
	private java.lang.String prestatoreNazione;
 
//    PRESTATORE_CD_TERZO DECIMAL(8,0)
	private java.lang.Integer prestatoreCdTerzo;

//  PRESTATORE_CD_ANAG DECIMAL(8,0)
	private java.lang.Integer prestatoreCdAnag;
	
//    STABILEORG_INDIRIZZO VARCHAR(256)
	private java.lang.String stabileorgIndirizzo;
 
//    STABILEORG_NUMEROCIVICO VARCHAR(256)
	private java.lang.String stabileorgNumerocivico;
 
//    STABILEORG_CAP VARCHAR(256)
	private java.lang.String stabileorgCap;
 
//    STABILEORG_COMUNE VARCHAR(256)
	private java.lang.String stabileorgComune;
 
//    STABILEORG_PROVINCIA VARCHAR(256)
	private java.lang.String stabileorgProvincia;
 
//    STABILEORG_NAZIONE VARCHAR(256)
	private java.lang.String stabileorgNazione;
 
//    REA_UFFICIO VARCHAR(256)
	private java.lang.String reaUfficio;
 
//    REA_NUMEROREA VARCHAR(256)
	private java.lang.String reaNumerorea;
 
//    REA_CAPITALESOCIALE DECIMAL(17,2)
	private java.math.BigDecimal reaCapitalesociale;
 
//    REA_SOCIOUNICO VARCHAR(256)
	private java.lang.String reaSociounico;
 
//    REA_STATOLIQUIDAZIONE VARCHAR(256)
	private java.lang.String reaStatoliquidazione;
 
//    PRESTATORE_TELEFONO VARCHAR(256)
	private java.lang.String prestatoreTelefono;
 
//    PRESTATORE_FAX VARCHAR(256)
	private java.lang.String prestatoreFax;
 
//    PRESTATORE_EMAIL VARCHAR(256)
	private java.lang.String prestatoreEmail;
 
//    RIFERIMENTO_AMMINISTRAZIONE VARCHAR(256)
	private java.lang.String riferimentoAmministrazione;
 
//    RAPPRESENTANTE_PAESE VARCHAR(256)
	private java.lang.String rappresentantePaese;
 
//    RAPPRESENTANTE_CODICE VARCHAR(256)
	private java.lang.String rappresentanteCodice;
 
//    RAPPRESENTANTE_CODICEFISCALE VARCHAR(256)
	private java.lang.String rappresentanteCodicefiscale;
 
//    RAPPRESENTANTE_DENOMINAZIONE VARCHAR(256)
	private java.lang.String rappresentanteDenominazione;
 
//    RAPPRESENTANTE_NOME VARCHAR(256)
	private java.lang.String rappresentanteNome;
 
//    RAPPRESENTANTE_COGNOME VARCHAR(256)
	private java.lang.String rappresentanteCognome;
 
//    RAPPRESENTANTE_TITOLO VARCHAR(256)
	private java.lang.String rappresentanteTitolo;
 
//    RAPPRESENTANTE_CODEORI VARCHAR(256)
	private java.lang.String rappresentanteCodeori;
 
//    RAPPRESENTANTE_CD_TERZO DECIMAL(8,0)
	private java.lang.Integer rappresentanteCdTerzo;

//  RAPPRESENTANTE_CD_ANAG DECIMAL(8,0)
	private java.lang.Integer rappresentanteCdAnag;
	
//    COMMITTENTE_PAESE VARCHAR(256)
	private java.lang.String committentePaese;
 
//    COMMITTENTE_CODICE VARCHAR(256)
	private java.lang.String committenteCodice;
 
//    COMMITTENTE_CODICEFISCALE VARCHAR(256)
	private java.lang.String committenteCodicefiscale;
 
//    COMMITTENTE_DENOMINAZIONE VARCHAR(256)
	private java.lang.String committenteDenominazione;
 
//    COMMITTENTE_NOME VARCHAR(256)
	private java.lang.String committenteNome;
 
//    COMMITTENTE_COGNOME VARCHAR(256)
	private java.lang.String committenteCognome;
 
//    COMMITTENTE_TITOLO VARCHAR(256)
	private java.lang.String committenteTitolo;
 
//    COMMITTENTE_CODEORI VARCHAR(256)
	private java.lang.String committenteCodeori;
 
//    COMMITTENTE_INDIRIZZO VARCHAR(256)
	private java.lang.String committenteIndirizzo;
 
//    COMMITTENTE_NUMEROCIVICO VARCHAR(256)
	private java.lang.String committenteNumerocivico;
 
//    COMMITTENTE_CAP VARCHAR(256)
	private java.lang.String committenteCap;
 
//    COMMITTENTE_COMUNE VARCHAR(256)
	private java.lang.String committenteComune;
 
//    COMMITTENTE_PROVINCIA VARCHAR(256)
	private java.lang.String committenteProvincia;
 
//    COMMITTENTE_NAZIONE VARCHAR(256)
	private java.lang.String committenteNazione;
 
//    COMMITTENTE_CD_TERZO DECIMAL(8,0)
	private java.lang.Integer committenteCdTerzo;
 
//    INTERMEDIARIO_PAESE VARCHAR(256)
	private java.lang.String intermediarioPaese;
 
//    INTERMEDIARIO_CODICE VARCHAR(256)
	private java.lang.String intermediarioCodice;
 
//    INTERMEDIARIO_CODICEFISCALE VARCHAR(256)
	private java.lang.String intermediarioCodicefiscale;
 
//    INTERMEDIARIO_DENOMINAZIONE VARCHAR(256)
	private java.lang.String intermediarioDenominazione;
 
//    INTERMEDIARIO_NOME VARCHAR(256)
	private java.lang.String intermediarioNome;
 
//    INTERMEDIARIO_COGNOME VARCHAR(256)
	private java.lang.String intermediarioCognome;
 
//    INTERMEDIARIO_TITOLO VARCHAR(256)
	private java.lang.String intermediarioTitolo;
 
//    INTERMEDIARIO_CODEORI VARCHAR(256)
	private java.lang.String intermediarioCodeori;
 
//    INTERMEDIARIO_CD_TERZO DECIMAL(8,0)
	private java.lang.Integer intermediarioCdTerzo;

//  INTERMEDIARIO_CD_ANAG DECIMAL(8,0)
	private java.lang.Integer intermediarioCdAnag;
	
//    SOGGETTO_EMITTENTE VARCHAR(256)
	private java.lang.String soggettoEmittente;
 
//    CODICE_UNIVOCO_SDI DECIMAL(22,0)
	private java.lang.Long codiceUnivocoSdi;
 
//    DATA_RICEZIONE TIMESTAMP(7)
	private java.sql.Timestamp dataRicezione;
 
//    ANOMALIE_RICEZIONE VARCHAR(2000)
	private java.lang.String anomalieRicezione;
 
	private java.lang.Boolean flCompletato;

//  CMIS_NODE_REF VARCHAR(100)
	private java.lang.String cmisNodeRef;
	
//  NOME_FILE VARCHAR(100)
	private java.lang.String nomeFile;
	
//  REPLY_TO VARCHAR(200)
	private java.lang.String replyTo;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_TRASMISSIONE
	 **/
	public DocumentoEleTrasmissioneBase() {
		super();
	}
	public DocumentoEleTrasmissioneBase(java.lang.String idPaese, java.lang.String idCodice, java.lang.Long identificativoSdi) {
		super(idPaese, idCodice, identificativoSdi);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [formatoTrasmissione]
	 **/
	public java.lang.String getFormatoTrasmissione() {
		return formatoTrasmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [formatoTrasmissione]
	 **/
	public void setFormatoTrasmissione(java.lang.String formatoTrasmissione)  {
		this.formatoTrasmissione=formatoTrasmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceDestinatario]
	 **/
	public java.lang.String getCodiceDestinatario() {
		return codiceDestinatario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceDestinatario]
	 **/
	public void setCodiceDestinatario(java.lang.String codiceDestinatario)  {
		this.codiceDestinatario=codiceDestinatario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Unità organizzativa]
	 **/
	public java.lang.String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Unità organizzativa]
	 **/
	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa)  {
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
	}	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [trasmittenteTelefono]
	 **/
	public java.lang.String getTrasmittenteTelefono() {
		return trasmittenteTelefono;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [trasmittenteTelefono]
	 **/
	public void setTrasmittenteTelefono(java.lang.String trasmittenteTelefono)  {
		this.trasmittenteTelefono=trasmittenteTelefono;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [trasmittenteEmail]
	 **/
	public java.lang.String getTrasmittenteEmail() {
		return trasmittenteEmail;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [trasmittenteEmail]
	 **/
	public void setTrasmittenteEmail(java.lang.String trasmittenteEmail)  {
		this.trasmittenteEmail=trasmittenteEmail;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatorePaese]
	 **/
	public java.lang.String getPrestatorePaese() {
		return prestatorePaese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatorePaese]
	 **/
	public void setPrestatorePaese(java.lang.String prestatorePaese)  {
		this.prestatorePaese=prestatorePaese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreCodice]
	 **/
	public java.lang.String getPrestatoreCodice() {
		return prestatoreCodice;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreCodice]
	 **/
	public void setPrestatoreCodice(java.lang.String prestatoreCodice)  {
		this.prestatoreCodice=prestatoreCodice;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreCodicefiscale]
	 **/
	public java.lang.String getPrestatoreCodicefiscale() {
		return prestatoreCodicefiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreCodicefiscale]
	 **/
	public void setPrestatoreCodicefiscale(java.lang.String prestatoreCodicefiscale)  {
		this.prestatoreCodicefiscale=prestatoreCodicefiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreDenominazione]
	 **/
	public java.lang.String getPrestatoreDenominazione() {
		return prestatoreDenominazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreDenominazione]
	 **/
	public void setPrestatoreDenominazione(java.lang.String prestatoreDenominazione)  {
		this.prestatoreDenominazione=prestatoreDenominazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreNome]
	 **/
	public java.lang.String getPrestatoreNome() {
		return prestatoreNome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreNome]
	 **/
	public void setPrestatoreNome(java.lang.String prestatoreNome)  {
		this.prestatoreNome=prestatoreNome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreCognome]
	 **/
	public java.lang.String getPrestatoreCognome() {
		return prestatoreCognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreCognome]
	 **/
	public void setPrestatoreCognome(java.lang.String prestatoreCognome)  {
		this.prestatoreCognome=prestatoreCognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreTitolo]
	 **/
	public java.lang.String getPrestatoreTitolo() {
		return prestatoreTitolo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreTitolo]
	 **/
	public void setPrestatoreTitolo(java.lang.String prestatoreTitolo)  {
		this.prestatoreTitolo=prestatoreTitolo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreCodeori]
	 **/
	public java.lang.String getPrestatoreCodeori() {
		return prestatoreCodeori;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreCodeori]
	 **/
	public void setPrestatoreCodeori(java.lang.String prestatoreCodeori)  {
		this.prestatoreCodeori=prestatoreCodeori;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [alboprofessionale]
	 **/
	public java.lang.String getAlboprofessionale() {
		return alboprofessionale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [alboprofessionale]
	 **/
	public void setAlboprofessionale(java.lang.String alboprofessionale)  {
		this.alboprofessionale=alboprofessionale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [provinciaalbo]
	 **/
	public java.lang.String getProvinciaalbo() {
		return provinciaalbo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [provinciaalbo]
	 **/
	public void setProvinciaalbo(java.lang.String provinciaalbo)  {
		this.provinciaalbo=provinciaalbo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataiscrizionealbo]
	 **/
	public java.sql.Timestamp getDataiscrizionealbo() {
		return dataiscrizionealbo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataiscrizionealbo]
	 **/
	public void setDataiscrizionealbo(java.sql.Timestamp dataiscrizionealbo)  {
		this.dataiscrizionealbo=dataiscrizionealbo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [regimefiscale]
	 **/
	public java.lang.String getRegimefiscale() {
		return regimefiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [regimefiscale]
	 **/
	public void setRegimefiscale(java.lang.String regimefiscale)  {
		this.regimefiscale=regimefiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreIndirizzo]
	 **/
	public java.lang.String getPrestatoreIndirizzo() {
		return prestatoreIndirizzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreIndirizzo]
	 **/
	public void setPrestatoreIndirizzo(java.lang.String prestatoreIndirizzo)  {
		this.prestatoreIndirizzo=prestatoreIndirizzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreNumerocivico]
	 **/
	public java.lang.String getPrestatoreNumerocivico() {
		return prestatoreNumerocivico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreNumerocivico]
	 **/
	public void setPrestatoreNumerocivico(java.lang.String prestatoreNumerocivico)  {
		this.prestatoreNumerocivico=prestatoreNumerocivico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreCap]
	 **/
	public java.lang.String getPrestatoreCap() {
		return prestatoreCap;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreCap]
	 **/
	public void setPrestatoreCap(java.lang.String prestatoreCap)  {
		this.prestatoreCap=prestatoreCap;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreComune]
	 **/
	public java.lang.String getPrestatoreComune() {
		return prestatoreComune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreComune]
	 **/
	public void setPrestatoreComune(java.lang.String prestatoreComune)  {
		this.prestatoreComune=prestatoreComune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreProvincia]
	 **/
	public java.lang.String getPrestatoreProvincia() {
		return prestatoreProvincia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreProvincia]
	 **/
	public void setPrestatoreProvincia(java.lang.String prestatoreProvincia)  {
		this.prestatoreProvincia=prestatoreProvincia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreNazione]
	 **/
	public java.lang.String getPrestatoreNazione() {
		return prestatoreNazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreNazione]
	 **/
	public void setPrestatoreNazione(java.lang.String prestatoreNazione)  {
		this.prestatoreNazione=prestatoreNazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreCdTerzo]
	 **/
	public java.lang.Integer getPrestatoreCdTerzo() {
		return prestatoreCdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreCdTerzo]
	 **/
	public void setPrestatoreCdTerzo(java.lang.Integer prestatoreCdTerzo)  {
		this.prestatoreCdTerzo=prestatoreCdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stabileorgIndirizzo]
	 **/
	public java.lang.String getStabileorgIndirizzo() {
		return stabileorgIndirizzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stabileorgIndirizzo]
	 **/
	public void setStabileorgIndirizzo(java.lang.String stabileorgIndirizzo)  {
		this.stabileorgIndirizzo=stabileorgIndirizzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stabileorgNumerocivico]
	 **/
	public java.lang.String getStabileorgNumerocivico() {
		return stabileorgNumerocivico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stabileorgNumerocivico]
	 **/
	public void setStabileorgNumerocivico(java.lang.String stabileorgNumerocivico)  {
		this.stabileorgNumerocivico=stabileorgNumerocivico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stabileorgCap]
	 **/
	public java.lang.String getStabileorgCap() {
		return stabileorgCap;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stabileorgCap]
	 **/
	public void setStabileorgCap(java.lang.String stabileorgCap)  {
		this.stabileorgCap=stabileorgCap;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stabileorgComune]
	 **/
	public java.lang.String getStabileorgComune() {
		return stabileorgComune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stabileorgComune]
	 **/
	public void setStabileorgComune(java.lang.String stabileorgComune)  {
		this.stabileorgComune=stabileorgComune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stabileorgProvincia]
	 **/
	public java.lang.String getStabileorgProvincia() {
		return stabileorgProvincia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stabileorgProvincia]
	 **/
	public void setStabileorgProvincia(java.lang.String stabileorgProvincia)  {
		this.stabileorgProvincia=stabileorgProvincia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stabileorgNazione]
	 **/
	public java.lang.String getStabileorgNazione() {
		return stabileorgNazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stabileorgNazione]
	 **/
	public void setStabileorgNazione(java.lang.String stabileorgNazione)  {
		this.stabileorgNazione=stabileorgNazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [reaUfficio]
	 **/
	public java.lang.String getReaUfficio() {
		return reaUfficio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [reaUfficio]
	 **/
	public void setReaUfficio(java.lang.String reaUfficio)  {
		this.reaUfficio=reaUfficio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [reaNumerorea]
	 **/
	public java.lang.String getReaNumerorea() {
		return reaNumerorea;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [reaNumerorea]
	 **/
	public void setReaNumerorea(java.lang.String reaNumerorea)  {
		this.reaNumerorea=reaNumerorea;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [reaCapitalesociale]
	 **/
	public java.math.BigDecimal getReaCapitalesociale() {
		return reaCapitalesociale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [reaCapitalesociale]
	 **/
	public void setReaCapitalesociale(java.math.BigDecimal reaCapitalesociale)  {
		this.reaCapitalesociale=reaCapitalesociale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [reaSociounico]
	 **/
	public java.lang.String getReaSociounico() {
		return reaSociounico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [reaSociounico]
	 **/
	public void setReaSociounico(java.lang.String reaSociounico)  {
		this.reaSociounico=reaSociounico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [reaStatoliquidazione]
	 **/
	public java.lang.String getReaStatoliquidazione() {
		return reaStatoliquidazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [reaStatoliquidazione]
	 **/
	public void setReaStatoliquidazione(java.lang.String reaStatoliquidazione)  {
		this.reaStatoliquidazione=reaStatoliquidazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreTelefono]
	 **/
	public java.lang.String getPrestatoreTelefono() {
		return prestatoreTelefono;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreTelefono]
	 **/
	public void setPrestatoreTelefono(java.lang.String prestatoreTelefono)  {
		this.prestatoreTelefono=prestatoreTelefono;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreFax]
	 **/
	public java.lang.String getPrestatoreFax() {
		return prestatoreFax;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreFax]
	 **/
	public void setPrestatoreFax(java.lang.String prestatoreFax)  {
		this.prestatoreFax=prestatoreFax;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreEmail]
	 **/
	public java.lang.String getPrestatoreEmail() {
		return prestatoreEmail;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreEmail]
	 **/
	public void setPrestatoreEmail(java.lang.String prestatoreEmail)  {
		this.prestatoreEmail=prestatoreEmail;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [riferimentoAmministrazione]
	 **/
	public java.lang.String getRiferimentoAmministrazione() {
		return riferimentoAmministrazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [riferimentoAmministrazione]
	 **/
	public void setRiferimentoAmministrazione(java.lang.String riferimentoAmministrazione)  {
		this.riferimentoAmministrazione=riferimentoAmministrazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rappresentantePaese]
	 **/
	public java.lang.String getRappresentantePaese() {
		return rappresentantePaese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rappresentantePaese]
	 **/
	public void setRappresentantePaese(java.lang.String rappresentantePaese)  {
		this.rappresentantePaese=rappresentantePaese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rappresentanteCodice]
	 **/
	public java.lang.String getRappresentanteCodice() {
		return rappresentanteCodice;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rappresentanteCodice]
	 **/
	public void setRappresentanteCodice(java.lang.String rappresentanteCodice)  {
		this.rappresentanteCodice=rappresentanteCodice;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rappresentanteCodicefiscale]
	 **/
	public java.lang.String getRappresentanteCodicefiscale() {
		return rappresentanteCodicefiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rappresentanteCodicefiscale]
	 **/
	public void setRappresentanteCodicefiscale(java.lang.String rappresentanteCodicefiscale)  {
		this.rappresentanteCodicefiscale=rappresentanteCodicefiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rappresentanteDenominazione]
	 **/
	public java.lang.String getRappresentanteDenominazione() {
		return rappresentanteDenominazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rappresentanteDenominazione]
	 **/
	public void setRappresentanteDenominazione(java.lang.String rappresentanteDenominazione)  {
		this.rappresentanteDenominazione=rappresentanteDenominazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rappresentanteNome]
	 **/
	public java.lang.String getRappresentanteNome() {
		return rappresentanteNome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rappresentanteNome]
	 **/
	public void setRappresentanteNome(java.lang.String rappresentanteNome)  {
		this.rappresentanteNome=rappresentanteNome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rappresentanteCognome]
	 **/
	public java.lang.String getRappresentanteCognome() {
		return rappresentanteCognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rappresentanteCognome]
	 **/
	public void setRappresentanteCognome(java.lang.String rappresentanteCognome)  {
		this.rappresentanteCognome=rappresentanteCognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rappresentanteTitolo]
	 **/
	public java.lang.String getRappresentanteTitolo() {
		return rappresentanteTitolo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rappresentanteTitolo]
	 **/
	public void setRappresentanteTitolo(java.lang.String rappresentanteTitolo)  {
		this.rappresentanteTitolo=rappresentanteTitolo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rappresentanteCodeori]
	 **/
	public java.lang.String getRappresentanteCodeori() {
		return rappresentanteCodeori;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rappresentanteCodeori]
	 **/
	public void setRappresentanteCodeori(java.lang.String rappresentanteCodeori)  {
		this.rappresentanteCodeori=rappresentanteCodeori;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rappresentanteCdTerzo]
	 **/
	public java.lang.Integer getRappresentanteCdTerzo() {
		return rappresentanteCdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rappresentanteCdTerzo]
	 **/
	public void setRappresentanteCdTerzo(java.lang.Integer rappresentanteCdTerzo)  {
		this.rappresentanteCdTerzo=rappresentanteCdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [committentePaese]
	 **/
	public java.lang.String getCommittentePaese() {
		return committentePaese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [committentePaese]
	 **/
	public void setCommittentePaese(java.lang.String committentePaese)  {
		this.committentePaese=committentePaese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [committenteCodice]
	 **/
	public java.lang.String getCommittenteCodice() {
		return committenteCodice;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [committenteCodice]
	 **/
	public void setCommittenteCodice(java.lang.String committenteCodice)  {
		this.committenteCodice=committenteCodice;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [committenteCodicefiscale]
	 **/
	public java.lang.String getCommittenteCodicefiscale() {
		return committenteCodicefiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [committenteCodicefiscale]
	 **/
	public void setCommittenteCodicefiscale(java.lang.String committenteCodicefiscale)  {
		this.committenteCodicefiscale=committenteCodicefiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [committenteDenominazione]
	 **/
	public java.lang.String getCommittenteDenominazione() {
		return committenteDenominazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [committenteDenominazione]
	 **/
	public void setCommittenteDenominazione(java.lang.String committenteDenominazione)  {
		this.committenteDenominazione=committenteDenominazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [committenteNome]
	 **/
	public java.lang.String getCommittenteNome() {
		return committenteNome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [committenteNome]
	 **/
	public void setCommittenteNome(java.lang.String committenteNome)  {
		this.committenteNome=committenteNome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [committenteCognome]
	 **/
	public java.lang.String getCommittenteCognome() {
		return committenteCognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [committenteCognome]
	 **/
	public void setCommittenteCognome(java.lang.String committenteCognome)  {
		this.committenteCognome=committenteCognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [committenteTitolo]
	 **/
	public java.lang.String getCommittenteTitolo() {
		return committenteTitolo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [committenteTitolo]
	 **/
	public void setCommittenteTitolo(java.lang.String committenteTitolo)  {
		this.committenteTitolo=committenteTitolo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [committenteCodeori]
	 **/
	public java.lang.String getCommittenteCodeori() {
		return committenteCodeori;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [committenteCodeori]
	 **/
	public void setCommittenteCodeori(java.lang.String committenteCodeori)  {
		this.committenteCodeori=committenteCodeori;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [committenteIndirizzo]
	 **/
	public java.lang.String getCommittenteIndirizzo() {
		return committenteIndirizzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [committenteIndirizzo]
	 **/
	public void setCommittenteIndirizzo(java.lang.String committenteIndirizzo)  {
		this.committenteIndirizzo=committenteIndirizzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [committenteNumerocivico]
	 **/
	public java.lang.String getCommittenteNumerocivico() {
		return committenteNumerocivico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [committenteNumerocivico]
	 **/
	public void setCommittenteNumerocivico(java.lang.String committenteNumerocivico)  {
		this.committenteNumerocivico=committenteNumerocivico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [committenteCap]
	 **/
	public java.lang.String getCommittenteCap() {
		return committenteCap;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [committenteCap]
	 **/
	public void setCommittenteCap(java.lang.String committenteCap)  {
		this.committenteCap=committenteCap;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [committenteComune]
	 **/
	public java.lang.String getCommittenteComune() {
		return committenteComune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [committenteComune]
	 **/
	public void setCommittenteComune(java.lang.String committenteComune)  {
		this.committenteComune=committenteComune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [committenteProvincia]
	 **/
	public java.lang.String getCommittenteProvincia() {
		return committenteProvincia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [committenteProvincia]
	 **/
	public void setCommittenteProvincia(java.lang.String committenteProvincia)  {
		this.committenteProvincia=committenteProvincia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [committenteNazione]
	 **/
	public java.lang.String getCommittenteNazione() {
		return committenteNazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [committenteNazione]
	 **/
	public void setCommittenteNazione(java.lang.String committenteNazione)  {
		this.committenteNazione=committenteNazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [committenteCdTerzo]
	 **/
	public java.lang.Integer getCommittenteCdTerzo() {
		return committenteCdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [committenteCdTerzo]
	 **/
	public void setCommittenteCdTerzo(java.lang.Integer committenteCdTerzo)  {
		this.committenteCdTerzo=committenteCdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [intermediarioPaese]
	 **/
	public java.lang.String getIntermediarioPaese() {
		return intermediarioPaese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [intermediarioPaese]
	 **/
	public void setIntermediarioPaese(java.lang.String intermediarioPaese)  {
		this.intermediarioPaese=intermediarioPaese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [intermediarioCodice]
	 **/
	public java.lang.String getIntermediarioCodice() {
		return intermediarioCodice;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [intermediarioCodice]
	 **/
	public void setIntermediarioCodice(java.lang.String intermediarioCodice)  {
		this.intermediarioCodice=intermediarioCodice;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [intermediarioCodicefiscale]
	 **/
	public java.lang.String getIntermediarioCodicefiscale() {
		return intermediarioCodicefiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [intermediarioCodicefiscale]
	 **/
	public void setIntermediarioCodicefiscale(java.lang.String intermediarioCodicefiscale)  {
		this.intermediarioCodicefiscale=intermediarioCodicefiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [intermediarioDenominazione]
	 **/
	public java.lang.String getIntermediarioDenominazione() {
		return intermediarioDenominazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [intermediarioDenominazione]
	 **/
	public void setIntermediarioDenominazione(java.lang.String intermediarioDenominazione)  {
		this.intermediarioDenominazione=intermediarioDenominazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [intermediarioNome]
	 **/
	public java.lang.String getIntermediarioNome() {
		return intermediarioNome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [intermediarioNome]
	 **/
	public void setIntermediarioNome(java.lang.String intermediarioNome)  {
		this.intermediarioNome=intermediarioNome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [intermediarioCognome]
	 **/
	public java.lang.String getIntermediarioCognome() {
		return intermediarioCognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [intermediarioCognome]
	 **/
	public void setIntermediarioCognome(java.lang.String intermediarioCognome)  {
		this.intermediarioCognome=intermediarioCognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [intermediarioTitolo]
	 **/
	public java.lang.String getIntermediarioTitolo() {
		return intermediarioTitolo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [intermediarioTitolo]
	 **/
	public void setIntermediarioTitolo(java.lang.String intermediarioTitolo)  {
		this.intermediarioTitolo=intermediarioTitolo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [intermediarioCodeori]
	 **/
	public java.lang.String getIntermediarioCodeori() {
		return intermediarioCodeori;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [intermediarioCodeori]
	 **/
	public void setIntermediarioCodeori(java.lang.String intermediarioCodeori)  {
		this.intermediarioCodeori=intermediarioCodeori;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [intermediarioCdTerzo]
	 **/
	public java.lang.Integer getIntermediarioCdTerzo() {
		return intermediarioCdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [intermediarioCdTerzo]
	 **/
	public void setIntermediarioCdTerzo(java.lang.Integer intermediarioCdTerzo)  {
		this.intermediarioCdTerzo=intermediarioCdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [soggettoEmittente]
	 **/
	public java.lang.String getSoggettoEmittente() {
		return soggettoEmittente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [soggettoEmittente]
	 **/
	public void setSoggettoEmittente(java.lang.String soggettoEmittente)  {
		this.soggettoEmittente=soggettoEmittente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceUnivocoSdi]
	 **/
	public java.lang.Long getCodiceUnivocoSdi() {
		return codiceUnivocoSdi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceUnivocoSdi]
	 **/
	public void setCodiceUnivocoSdi(java.lang.Long codiceUnivocoSdi)  {
		this.codiceUnivocoSdi=codiceUnivocoSdi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataRicezione]
	 **/
	public java.sql.Timestamp getDataRicezione() {
		return dataRicezione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataRicezione]
	 **/
	public void setDataRicezione(java.sql.Timestamp dataRicezione)  {
		this.dataRicezione=dataRicezione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [anomalieRicezione]
	 **/
	public java.lang.String getAnomalieRicezione() {
		return anomalieRicezione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [anomalieRicezione]
	 **/
	public void setAnomalieRicezione(java.lang.String anomalieRicezione)  {
		this.anomalieRicezione=anomalieRicezione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [progressivoInvio]
	 **/
	public void setProgressivoInvio(java.lang.String progressivoInvio)  {
		this.progressivoInvio=progressivoInvio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [progressivoInvio]
	 **/
	public java.lang.String getProgressivoInvio() {
		return progressivoInvio;
	}
	public java.lang.String getNumeroalbo() {
		return numeroalbo;
	}
	public void setNumeroalbo(java.lang.String numeroalbo) {
		this.numeroalbo = numeroalbo;
	}
	public java.lang.Integer getPrestatoreCdAnag() {
		return prestatoreCdAnag;
	}
	public void setPrestatoreCdAnag(java.lang.Integer prestatoreCdAnag) {
		this.prestatoreCdAnag = prestatoreCdAnag;
	}
	public java.lang.Integer getRappresentanteCdAnag() {
		return rappresentanteCdAnag;
	}
	public void setRappresentanteCdAnag(java.lang.Integer rappresentanteCdAnag) {
		this.rappresentanteCdAnag = rappresentanteCdAnag;
	}
	public java.lang.Integer getIntermediarioCdAnag() {
		return intermediarioCdAnag;
	}
	public void setIntermediarioCdAnag(java.lang.Integer intermediarioCdAnag) {
		this.intermediarioCdAnag = intermediarioCdAnag;
	}
	public java.lang.Boolean getFlCompletato() {
		return flCompletato;
	}
	public void setFlCompletato(java.lang.Boolean flCompletato) {
		this.flCompletato = flCompletato;
	}
	
	public java.lang.String getCmisNodeRef() {
		return Optional.ofNullable(cmisNodeRef)
				.orElseGet(() -> {
					final StoreService storeService = SpringUtil.getBean("storeService", StoreService.class);
					StringBuffer query = new StringBuffer("select cmis:objectId from sigla_fatture:fatture_passive");
					query.append(" where ").append("sigla_fatture:identificativoSdI").append(" = ").append(getIdentificativoSdi());
					List<StorageObject> storageObjects = storeService.search(query.toString());
					return storeService.search(query.toString()).stream()
							.map(StorageObject::getKey)
							.findAny().orElseThrow(() -> new ApplicationRuntimeException("Fattura non trovata sulla base documentale!"));
				});
	}
	public void setCmisNodeRef(java.lang.String cmisNodeRef) {
		this.cmisNodeRef = cmisNodeRef;
	}
	
	public java.lang.String getNomeFile() {
		return nomeFile;
	}
	public void setNomeFile(java.lang.String nomeFile) {
		this.nomeFile = nomeFile;
	}
	
	public String getDenominzionePrestatore() {
		return prestatoreDenominazione != null ? prestatoreDenominazione : (prestatoreCognome + " " + prestatoreNome);		
	}
	public java.lang.String getReplyTo() {
		return replyTo;
	}
	public void setReplyTo(java.lang.String replyTo) {
		this.replyTo = replyTo;
	}
}