package it.cnr.contab.docamm00.tabrif.bulk;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
public class Categoria_gruppo_voceBulk extends Categoria_gruppo_voceBase {

	Categoria_gruppo_inventBulk categoria_gruppo;
	Elemento_voceBulk voce_f;

public Categoria_gruppo_voceBulk() {
	super();
}

public Categoria_gruppo_voceBulk(java.lang.String cd_categoria_gruppo,java.lang.String cd_elemento_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_categoria_gruppo,cd_elemento_voce,esercizio,ti_appartenenza,ti_gestione);
}

/**
 * Insert the method's description here.
 * Creation date: (07/05/2002 18.20.20)
 * @param newVoce_f it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk
 */
public void basicSetAssociazioneConVoce_F(it.cnr.jada.action.ActionContext context) {
	this.setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	this.setTi_appartenenza("D");
	this.setTi_gestione("S");
}

/**
 * Insert the method's description here.
 * Creation date: (07/05/2002 17.14.16)
 * @return it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk
 */
public Categoria_gruppo_inventBulk getCategoria_gruppo() {
	return categoria_gruppo;
}

public java.lang.String getCd_categoria_gruppo() {
	it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk categoria_gruppo = this.getCategoria_gruppo();
	if (categoria_gruppo == null)
		return null;
	return categoria_gruppo.getCd_categoria_gruppo();
}

public java.lang.String getCd_elemento_voce() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk voce_f = this.getVoce_f();
	if (voce_f == null)
		return null;
	return voce_f.getCd_elemento_voce();
}

public java.lang.String getCd_voce() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk voce_f = this.getVoce_f();
	if (voce_f == null)
		return null;
	return voce_f.getCd_elemento_voce();
}

public java.lang.Integer getEsercizio() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk voce_f = this.getVoce_f();
	if (voce_f == null)
		return null;
	return voce_f.getEsercizio();
}

public java.lang.String getTi_appartenenza() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk voce_f = this.getVoce_f();
	if (voce_f == null)
		return null;
	return voce_f.getTi_appartenenza();
}

public java.lang.String getTi_gestione() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk voce_f = this.getVoce_f();
	if (voce_f == null)
		return null;
	return voce_f.getTi_gestione();
}

/**
 * Insert the method's description here.
 * Creation date: (07/05/2002 18.20.20)
 * @return it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk
 */
public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getVoce_f() {
	return voce_f;
}

/**
 * Insert the method's description here.
 * Creation date: (07/05/2002 18.20.20)
 * @param newVoce_f it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk
 */
public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
    super.initializeForEdit(bp, context);
    this.setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
    this.setTi_appartenenza("D");
    this.setTi_gestione("S");
    return this;
}

/**
 * Insert the method's description here.
 * Creation date: (07/05/2002 18.20.20)
 * @param newVoce_f it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk
 */
public OggettoBulk initializeForEdit(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
    super.initializeForEdit(bp, context);
    this.setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
    this.setTi_appartenenza("D");
    this.setTi_gestione("S");
    return this;
}

/**
 * Insert the method's description here.
 * Creation date: (07/05/2002 18.20.20)
 * @param newVoce_f it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
    super.initializeForInsert(bp, context);
    this.setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
    this.setTi_appartenenza("D");
    this.setTi_gestione("S");
    return this;
}

/**
 * Insert the method's description here.
 * Creation date: (07/05/2002 17.14.16)
 * @param newCategoria_gruppo it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk
 */
public void setCategoria_gruppo(Categoria_gruppo_inventBulk newCategoria_gruppo) {
	categoria_gruppo = newCategoria_gruppo;
}

public void setCd_categoria_gruppo(java.lang.String cd_categoria_gruppo) {
	this.getCategoria_gruppo().setCd_categoria_gruppo(cd_categoria_gruppo);
}

public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.getVoce_f().setCd_elemento_voce(cd_elemento_voce);
}

public void setCd_voce(java.lang.String cd_voce) {
	if (this.getVoce_f()!=null)
		this.getVoce_f().setCd_elemento_voce(cd_voce);
	else
		super.setCd_elemento_voce(cd_voce);	
}

public void setEsercizio(java.lang.Integer esercizio) {
	this.getVoce_f().setEsercizio(esercizio);
}

public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.getVoce_f().setTi_appartenenza(ti_appartenenza);
}

public void setTi_gestione(java.lang.String ti_gestione) {
	this.getVoce_f().setTi_gestione(ti_gestione);
}

/**
 * Insert the method's description here.
 * Creation date: (07/05/2002 18.20.20)
 * @param newVoce_f it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk
 */
public void setVoce_f(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk newVoce_f) {
	voce_f = newVoce_f;
}
}