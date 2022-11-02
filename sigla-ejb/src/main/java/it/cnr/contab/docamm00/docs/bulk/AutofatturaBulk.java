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

import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class AutofatturaBulk extends AutofatturaBase {

	public final static String STATO_INIZIALE = "I";
	public final static String STATO_CONTABILIZZATO = "C";
	public final static String STATO_PARZIALE = "Q";
	public final static String STATO_PAGATO = "P";

	public final static String STATO_IVA_A = "A";
	public final static String STATO_IVA_B = "B";
	public final static String STATO_IVA_C = "C";

	Fattura_passivaBase fattura_passiva = null;
	Tipo_sezionaleBulk tipo_sezionale = null;

	private java.lang.Boolean fl_extra_ue;
	private java.lang.Boolean fl_san_marino_con_iva;
	private java.lang.Boolean fl_san_marino_senza_iva;
	private java.lang.String ti_bene_servizio = null;
	private boolean autofatturaNeeded = false;
public AutofatturaBulk() {
	super();
}
public AutofatturaBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_autofattura) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_autofattura);
}
/**
 * Insert the method's description here.
 * Creation date: (2/12/2002 4:10:01 PM)
 */
public void completeFrom(Fattura_passivaBulk fatturaPassiva) {

	setFattura_passiva(fatturaPassiva);

	setCd_cds_origine(fatturaPassiva.getCd_cds_origine());
	setCd_uo_origine(fatturaPassiva.getCd_uo_origine());

	setDt_registrazione(fatturaPassiva.getDt_registrazione());
	setData_esigibilita_iva(getDt_registrazione());
	setStato_cofi(AutofatturaBulk.STATO_CONTABILIZZATO);
	setStato_coge(Fattura_passivaBulk.NON_PROCESSARE_IN_COGE);
	
	setFl_liquidazione_differita(Boolean.FALSE);
	setFl_intra_ue(fatturaPassiva.getFl_intra_ue());
	setFl_extra_ue(fatturaPassiva.getFl_extra_ue());
	setFl_san_marino_con_iva(fatturaPassiva.getFl_san_marino_con_iva());
	setFl_san_marino_senza_iva(fatturaPassiva.getFl_san_marino_senza_iva());
	setFl_split_payment(fatturaPassiva.getFl_split_payment());
	setTi_bene_servizio(fattura_passiva.getTi_bene_servizio());

	if (fatturaPassiva.getFl_split_payment())
		setFl_autofattura(fatturaPassiva.getFl_autofattura());
	else
		setFl_autofattura(Boolean.TRUE);
	
	setAutofatturaNeeded(
		fatturaPassiva.isCommerciale() &&
		((getFl_intra_ue() != null && Boolean.TRUE.equals(getFl_intra_ue())) ||
		(getFl_san_marino_senza_iva() != null && Boolean.TRUE.equals(getFl_san_marino_senza_iva())))
	);

	setEsercizio(fatturaPassiva.getEsercizio());
	setProtocollo_iva(fatturaPassiva.getProtocollo_iva());
	setProtocollo_iva_generale(fatturaPassiva.getProtocollo_iva_generale());
	setTi_istituz_commerc(fatturaPassiva.getTi_istituz_commerc());
	setTi_fattura(fatturaPassiva.getTi_fattura());
	setToBeCreated();
}
public java.lang.String getCd_cds_ft_passiva() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBase fattura_passiva = this.getFattura_passiva();
	if (fattura_passiva == null)
		return null;
	return fattura_passiva.getCd_cds();
}
public java.lang.String getCd_tipo_sezionale() {
	it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk tipo_sezionale = this.getTipo_sezionale();
	if (tipo_sezionale == null)
		return null;
	return tipo_sezionale.getCd_tipo_sezionale();
}
public java.lang.String getCd_uo_ft_passiva() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBase fattura_passiva = this.getFattura_passiva();
	if (fattura_passiva == null)
		return null;
	return fattura_passiva.getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (2/12/2002 4:06:00 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk
 */
public Fattura_passivaBase getFattura_passiva() {
	return fattura_passiva;
}
/* 
 * Getter dell'attributo fl_intra_ue
 */
public java.lang.Boolean getFl_extra_ue() {
	return fl_extra_ue;
}
/**
 * Insert the method's description here.
 * Creation date: (9/20/2002 10:08:36 AM)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_san_marino_con_iva() {
	return fl_san_marino_con_iva;
}
/**
 * Insert the method's description here.
 * Creation date: (9/20/2002 10:08:36 AM)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_san_marino_senza_iva() {
	return fl_san_marino_senza_iva;
}
public java.lang.Long getPg_fattura_passiva() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBase fattura_passiva = this.getFattura_passiva();
	if (fattura_passiva == null)
		return null;
	return fattura_passiva.getPg_fattura_passiva();
}
public String getStatoIVA() {

	return (getProtocollo_iva() == null ||
			getProtocollo_iva_generale() == null) ?
				"A" : "B";
}
/**
 * Insert the method's description here.
 * Creation date: (10/14/2002 5:22:33 PM)
 * @return java.lang.String
 */
public java.lang.String getTi_bene_servizio() {
	return ti_bene_servizio;
}
/**
 * Insert the method's description here.
 * Creation date: (2/12/2002 4:06:00 PM)
 * @return it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk
 */
public it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk getTipo_sezionale() {
	return tipo_sezionale;
}
/**
 * Insert the method's description here.
 * Creation date: (4/8/2003 3:38:07 PM)
 * @return boolean
 */
public boolean isAutofatturaDiBeni() {
	return Bene_servizioBulk.BENE.equals(getTi_bene_servizio());
}
/**
 * Insert the method's description here.
 * Creation date: (4/8/2003 3:38:07 PM)
 * @return boolean
 */
public boolean isAutofatturaNeeded() {
	return autofatturaNeeded;
}
public boolean isStampataSuRegistroIVA() {

	return STATO_IVA_B.equalsIgnoreCase(getStatoIVA()) ||
			STATO_IVA_C.equalsIgnoreCase(getStatoIVA());
}
/**
 * Insert the method's description here.
 * Creation date: (4/8/2003 3:38:07 PM)
 * @param newAutofatturaNeeded boolean
 */
public void setAutofatturaNeeded(boolean newAutofatturaNeeded) {
	autofatturaNeeded = newAutofatturaNeeded;
}
public void setCd_cds_ft_passiva(java.lang.String cd_cds_ft_passiva) {
	this.getFattura_passiva().setCd_cds(cd_cds_ft_passiva);
}
public void setCd_tipo_sezionale(java.lang.String cd_tipo_sezionale) {
	this.getTipo_sezionale().setCd_tipo_sezionale(cd_tipo_sezionale);
}
public void setCd_uo_ft_passiva(java.lang.String cd_uo_ft_passiva) {
	this.getFattura_passiva().setCd_unita_organizzativa(cd_uo_ft_passiva);
}
/**
 * Insert the method's description here.
 * Creation date: (2/12/2002 4:06:00 PM)
 * @param newFattura_passiva it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk
 */
public void setFattura_passiva(Fattura_passivaBase newFattura_passiva) {
	fattura_passiva = newFattura_passiva;
}
/* 
 * Setter dell'attributo fl_intra_ue
 */
public void setFl_extra_ue(java.lang.Boolean fl_extra_ue) {
	this.fl_extra_ue = fl_extra_ue;
}
/**
 * Insert the method's description here.
 * Creation date: (9/20/2002 10:08:36 AM)
 * @param newFl_san_marino_con_iva java.lang.Boolean
 */
public void setFl_san_marino_con_iva(java.lang.Boolean newFl_san_marino_con_iva) {
	fl_san_marino_con_iva = newFl_san_marino_con_iva;
}
/**
 * Insert the method's description here.
 * Creation date: (9/20/2002 10:08:36 AM)
 * @param newFl_san_marino_senza_iva java.lang.Boolean
 */
public void setFl_san_marino_senza_iva(java.lang.Boolean newFl_san_marino_senza_iva) {
	fl_san_marino_senza_iva = newFl_san_marino_senza_iva;
}
public void setPg_fattura_passiva(java.lang.Long pg_fattura_passiva) {
	this.getFattura_passiva().setPg_fattura_passiva(pg_fattura_passiva);
}
/**
 * Insert the method's description here.
 * Creation date: (10/14/2002 5:22:33 PM)
 * @param newTi_bene_servizio java.lang.String
 */
public void setTi_bene_servizio(java.lang.String newTi_bene_servizio) {
	ti_bene_servizio = newTi_bene_servizio;
}
/**
 * Insert the method's description here.
 * Creation date: (2/12/2002 4:06:00 PM)
 * @param newTipo_sezionale it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk
 */
public void setTipo_sezionale(it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk newTipo_sezionale) {
	tipo_sezionale = newTipo_sezionale;
}
}
