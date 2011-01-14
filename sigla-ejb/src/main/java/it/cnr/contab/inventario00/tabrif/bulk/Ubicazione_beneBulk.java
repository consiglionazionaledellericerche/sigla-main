package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.contab.utenze00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.action.*;


public class Ubicazione_beneBulk extends Ubicazione_beneBase {

	private Ubicazione_beneBulk nodoPadre;

	// CD_CDS e CD_UNITA_ORGANIZZATIVA che identificano le Ubicazioni fittizie
	public static final String CD_CDS_FITTIZIO = "999";
	public static final String CD_UO_FITTIZIO = "999.000";
public Ubicazione_beneBulk() {
	super();
}
public Ubicazione_beneBulk(java.lang.String cd_cds,java.lang.String cd_ubicazione,java.lang.String cd_unita_organizzativa) {
	super(cd_cds,cd_ubicazione,cd_unita_organizzativa);
}
public java.lang.String getCd_ubicazione_padre() {
	it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk nodoPadre = this.getNodoPadre();
	if (nodoPadre == null)
		return null;
	return nodoPadre.getCd_ubicazione();
}
/**
 * Insert the method's description here.
 * Creation date: (27/11/2001 12.44.59)
 * @return it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk
 */
public Ubicazione_beneBulk getNodoPadre() {
	return nodoPadre;
}
public OggettoBulk initialize(CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	this.setCd_cds(uo.getCd_unita_padre());
	this.setCd_unita_organizzativa(uo.getCd_unita_organizzativa());

	return this;
}

public boolean isROnodo_padre() {
	
	return getNodoPadre() == null ||
			getNodoPadre().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL;
}
public void setCd_ubicazione_padre(java.lang.String cd_ubicazione_padre) {
	this.getNodoPadre().setCd_ubicazione(cd_ubicazione_padre);
}
/**
 * Insert the method's description here.
 * Creation date: (27/11/2001 12.44.59)
 * @param newNodoPadre it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk
 */
public void setNodoPadre(Ubicazione_beneBulk newNodoPadre) {
	nodoPadre = newNodoPadre;
}
public java.lang.String getCd_cds_padre() {
	it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk nodoPadre = this.getNodoPadre();
	if (nodoPadre == null)
		return null;
	return nodoPadre.getCd_cds();
}
public void setCd_cds_padre(java.lang.String new_cd_cds) {
	this.getNodoPadre().setCd_cds(new_cd_cds);
}
public java.lang.String getCd_uo_padre() {
	it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk nodoPadre = this.getNodoPadre();
	if (nodoPadre == null)
		return null;
	return nodoPadre.getCd_unita_organizzativa();
}

public void setCd_uo_padre(java.lang.String new_cd_uo) {
	this.getNodoPadre().setCd_unita_organizzativa(new_cd_uo);
}
public OggettoBulk initializeForInsert(CRUDBP crudbp,ActionContext actioncontext) {
		setFl_ubicazione_default(Boolean.FALSE);
		return super.initializeForInsert(crudbp, actioncontext);
	}

}
