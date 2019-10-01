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
 * Date 06/06/2014
 */
package it.cnr.contab.anagraf00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class IpaAmministrazioniBase extends IpaAmministrazioniKey implements Keyed {
//    DES_AMM VARCHAR(1000)
	private java.lang.String desAmm;
 
//    COMUNE VARCHAR(100)
	private java.lang.String comune;
 
//    NOME_RESP VARCHAR(50)
	private java.lang.String nomeResp;
 
//    COGN_RESP VARCHAR(50)
	private java.lang.String cognResp;
 
//    CAP VARCHAR(5)
	private java.lang.String cap;
 
//    PROVINCIA VARCHAR(4)
	private java.lang.String provincia;
 
//    REGIONE VARCHAR(50)
	private java.lang.String regione;
 
//    SITO_ISTITUZIONALE VARCHAR(100)
	private java.lang.String sitoIstituzionale;
 
//    INDIRIZZO VARCHAR(200)
	private java.lang.String indirizzo;
 
//    TITOLO_RESP VARCHAR(100)
	private java.lang.String titoloResp;
 
//    TIPOLOGIA_ISTAT VARCHAR(150)
	private java.lang.String tipologiaIstat;
 
//    TIPOLOGIA_AMM VARCHAR(140)
	private java.lang.String tipologiaAmm;
 
//    ACRONIMO VARCHAR(100)
	private java.lang.String acronimo;
 
//    CF_VALIDATO CHAR(1)
	private java.lang.String cfValidato;
 
//    CF VARCHAR(11)
	private java.lang.String cf;
 
//    MAIL1 VARCHAR(100)
	private java.lang.String mail1;
 
//    TIPO_MAIL1 VARCHAR(20)
	private java.lang.String tipoMail1;
 
//    MAIL2 VARCHAR(100)
	private java.lang.String mail2;
 
//    TIPO_MAIL2 VARCHAR(20)
	private java.lang.String tipoMail2;
 
//    MAIL3 VARCHAR(100)
	private java.lang.String mail3;
 
//    TIPO_MAIL3 VARCHAR(20)
	private java.lang.String tipoMail3;
 
//    MAIL4 VARCHAR(100)
	private java.lang.String mail4;
 
//    TIPO_MAIL4 VARCHAR(20)
	private java.lang.String tipoMail4;
 
//    MAIL5 VARCHAR(100)
	private java.lang.String mail5;
 
//    TIPO_MAIL5 VARCHAR(20)
	private java.lang.String tipoMail5;
 
//    URL_FACEBOOK VARCHAR(1024)
	private java.lang.String urlFacebook;
 
//    URL_TWITTER VARCHAR(1024)
	private java.lang.String urlTwitter;
 
//    URL_GOOGLEPLUS VARCHAR(1024)
	private java.lang.String urlGoogleplus;
 
//    URL_YOUTUBE VARCHAR(1024)
	private java.lang.String urlYoutube;
 
//    LIV_ACCESSIBILI DECIMAL(11,0)
	private java.lang.Long livAccessibili;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: IPA_AMMINISTRAZIONI
	 **/
	public IpaAmministrazioniBase() {
		super();
	}
	public IpaAmministrazioniBase(java.lang.String codAmm) {
		super(codAmm);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [desAmm]
	 **/
	public java.lang.String getDesAmm() {
		return desAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [desAmm]
	 **/
	public void setDesAmm(java.lang.String desAmm)  {
		this.desAmm=desAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [comune]
	 **/
	public java.lang.String getComune() {
		return comune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [comune]
	 **/
	public void setComune(java.lang.String comune)  {
		this.comune=comune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nomeResp]
	 **/
	public java.lang.String getNomeResp() {
		return nomeResp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nomeResp]
	 **/
	public void setNomeResp(java.lang.String nomeResp)  {
		this.nomeResp=nomeResp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cognResp]
	 **/
	public java.lang.String getCognResp() {
		return cognResp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cognResp]
	 **/
	public void setCognResp(java.lang.String cognResp)  {
		this.cognResp=cognResp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cap]
	 **/
	public java.lang.String getCap() {
		return cap;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cap]
	 **/
	public void setCap(java.lang.String cap)  {
		this.cap=cap;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [provincia]
	 **/
	public java.lang.String getProvincia() {
		return provincia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [provincia]
	 **/
	public void setProvincia(java.lang.String provincia)  {
		this.provincia=provincia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [regione]
	 **/
	public java.lang.String getRegione() {
		return regione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [regione]
	 **/
	public void setRegione(java.lang.String regione)  {
		this.regione=regione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [sitoIstituzionale]
	 **/
	public java.lang.String getSitoIstituzionale() {
		return sitoIstituzionale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [sitoIstituzionale]
	 **/
	public void setSitoIstituzionale(java.lang.String sitoIstituzionale)  {
		this.sitoIstituzionale=sitoIstituzionale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [indirizzo]
	 **/
	public java.lang.String getIndirizzo() {
		return indirizzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [indirizzo]
	 **/
	public void setIndirizzo(java.lang.String indirizzo)  {
		this.indirizzo=indirizzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [titoloResp]
	 **/
	public java.lang.String getTitoloResp() {
		return titoloResp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [titoloResp]
	 **/
	public void setTitoloResp(java.lang.String titoloResp)  {
		this.titoloResp=titoloResp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipologiaIstat]
	 **/
	public java.lang.String getTipologiaIstat() {
		return tipologiaIstat;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipologiaIstat]
	 **/
	public void setTipologiaIstat(java.lang.String tipologiaIstat)  {
		this.tipologiaIstat=tipologiaIstat;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipologiaAmm]
	 **/
	public java.lang.String getTipologiaAmm() {
		return tipologiaAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipologiaAmm]
	 **/
	public void setTipologiaAmm(java.lang.String tipologiaAmm)  {
		this.tipologiaAmm=tipologiaAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [acronimo]
	 **/
	public java.lang.String getAcronimo() {
		return acronimo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [acronimo]
	 **/
	public void setAcronimo(java.lang.String acronimo)  {
		this.acronimo=acronimo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cfValidato]
	 **/
	public java.lang.String getCfValidato() {
		return cfValidato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cfValidato]
	 **/
	public void setCfValidato(java.lang.String cfValidato)  {
		this.cfValidato=cfValidato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cf]
	 **/
	public java.lang.String getCf() {
		return cf;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cf]
	 **/
	public void setCf(java.lang.String cf)  {
		this.cf=cf;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [mail1]
	 **/
	public java.lang.String getMail1() {
		return mail1;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [mail1]
	 **/
	public void setMail1(java.lang.String mail1)  {
		this.mail1=mail1;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoMail1]
	 **/
	public java.lang.String getTipoMail1() {
		return tipoMail1;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoMail1]
	 **/
	public void setTipoMail1(java.lang.String tipoMail1)  {
		this.tipoMail1=tipoMail1;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [mail2]
	 **/
	public java.lang.String getMail2() {
		return mail2;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [mail2]
	 **/
	public void setMail2(java.lang.String mail2)  {
		this.mail2=mail2;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoMail2]
	 **/
	public java.lang.String getTipoMail2() {
		return tipoMail2;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoMail2]
	 **/
	public void setTipoMail2(java.lang.String tipoMail2)  {
		this.tipoMail2=tipoMail2;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [mail3]
	 **/
	public java.lang.String getMail3() {
		return mail3;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [mail3]
	 **/
	public void setMail3(java.lang.String mail3)  {
		this.mail3=mail3;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoMail3]
	 **/
	public java.lang.String getTipoMail3() {
		return tipoMail3;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoMail3]
	 **/
	public void setTipoMail3(java.lang.String tipoMail3)  {
		this.tipoMail3=tipoMail3;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [mail4]
	 **/
	public java.lang.String getMail4() {
		return mail4;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [mail4]
	 **/
	public void setMail4(java.lang.String mail4)  {
		this.mail4=mail4;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoMail4]
	 **/
	public java.lang.String getTipoMail4() {
		return tipoMail4;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoMail4]
	 **/
	public void setTipoMail4(java.lang.String tipoMail4)  {
		this.tipoMail4=tipoMail4;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [mail5]
	 **/
	public java.lang.String getMail5() {
		return mail5;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [mail5]
	 **/
	public void setMail5(java.lang.String mail5)  {
		this.mail5=mail5;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoMail5]
	 **/
	public java.lang.String getTipoMail5() {
		return tipoMail5;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoMail5]
	 **/
	public void setTipoMail5(java.lang.String tipoMail5)  {
		this.tipoMail5=tipoMail5;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [urlFacebook]
	 **/
	public java.lang.String getUrlFacebook() {
		return urlFacebook;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [urlFacebook]
	 **/
	public void setUrlFacebook(java.lang.String urlFacebook)  {
		this.urlFacebook=urlFacebook;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [urlTwitter]
	 **/
	public java.lang.String getUrlTwitter() {
		return urlTwitter;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [urlTwitter]
	 **/
	public void setUrlTwitter(java.lang.String urlTwitter)  {
		this.urlTwitter=urlTwitter;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [urlGoogleplus]
	 **/
	public java.lang.String getUrlGoogleplus() {
		return urlGoogleplus;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [urlGoogleplus]
	 **/
	public void setUrlGoogleplus(java.lang.String urlGoogleplus)  {
		this.urlGoogleplus=urlGoogleplus;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [urlYoutube]
	 **/
	public java.lang.String getUrlYoutube() {
		return urlYoutube;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [urlYoutube]
	 **/
	public void setUrlYoutube(java.lang.String urlYoutube)  {
		this.urlYoutube=urlYoutube;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [livAccessibili]
	 **/
	public java.lang.Long getLivAccessibili() {
		return livAccessibili;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [livAccessibili]
	 **/
	public void setLivAccessibili(java.lang.Long livAccessibili)  {
		this.livAccessibili=livAccessibili;
	}
}