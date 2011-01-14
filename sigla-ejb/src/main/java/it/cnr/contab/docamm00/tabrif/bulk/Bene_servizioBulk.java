package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.Dictionary;

public class Bene_servizioBulk extends Bene_servizioBase {

	protected final static java.lang.Boolean TRUE= java.lang.Boolean.TRUE;
	protected final static java.lang.Boolean FALSE= java.lang.Boolean.FALSE;

	public final static String BENE_SERVIZIO = "*";
	public final static String SERVIZIO = "S";
	public final static String BENE = "B";

	public final static Dictionary BENI_SERVIZI;

	static {
		
		BENI_SERVIZI = new it.cnr.jada.util.OrderedHashtable();
		BENI_SERVIZI.put(BENE,"Bene");
		BENI_SERVIZI.put(SERVIZIO,"Servizio");
//		BENI_SERVIZI.put(BENE_SERVIZIO,"Entrambi");
	}
	
	protected Voce_ivaBulk voce_iva;
	protected Categoria_gruppo_inventBulk categoria_gruppo;
public Bene_servizioBulk() {
	super();
}
public Bene_servizioBulk(java.lang.String cd_bene_servizio) {
	super(cd_bene_servizio);
}
/**
 * Insert the method's description here.
 * Creation date: (10/09/2002 11.35.25)
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
public java.lang.String getCd_voce_iva() {
	it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk voce_iva = this.getVoce_iva();
	if (voce_iva == null)
		return null;
	return voce_iva.getCd_voce_iva();
}
public Dictionary getTi_bene_servizioKeys() {
	
	return BENI_SERVIZI;
}
/**
 * Insert the method's description here.
 * Creation date: (10/09/2002 11.35.35)
 * @return it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
 */
public Voce_ivaBulk getVoce_iva() {
	return voce_iva;
}
/**
 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>.
 * Questo metodo viene invocato da {@link #initializeForEdit}, {@link #initializeForInsert},
 * {@link #initializeForSearch} e {@link #initializeForFreeSearch} e può contenere
 * inizializzazioni comuni ai 4 stati del <code>FormController</code>
 */
protected OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	
	voce_iva = new Voce_ivaBulk();
	categoria_gruppo = new Categoria_gruppo_inventBulk();
	//for (java.util.Iterator i = childrenController.values().iterator();i.hasNext();)	
	//this.get
	
	if (getFl_gestione_magazzino()==null)
		setFl_gestione_magazzino(FALSE);
	if (getFl_gestione_inventario()==null)
		setFl_gestione_inventario(FALSE);
	if (getFl_obb_intrastat_acq()==null)
		setFl_obb_intrastat_acq(TRUE);	
	if (getFl_obb_intrastat_ven()==null)
		setFl_obb_intrastat_ven(TRUE);	
	if (getFl_valido()==null)
		setFl_valido(TRUE);
	if (getFl_autofattura()==null)
		setFl_autofattura(FALSE);	
	
	return super.initialize(bp,context);
}
/**
 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>.
 * Questo metodo viene invocato da {@link #initializeForEdit}, {@link #initializeForInsert},
 * {@link #initializeForSearch} e {@link #initializeForFreeSearch} e può contenere
 * inizializzazioni comuni ai 4 stati del <code>FormController</code>
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	Bene_servizioBulk bulk = (Bene_servizioBulk)super.initializeForInsert(bp,context);
	
	setTi_bene_servizio(SERVIZIO);

	return bulk;
}
public boolean isROcategoria_gruppo() {
	
	return getCategoria_gruppo() == null ||
			getCategoria_gruppo().getCrudStatus() == OggettoBulk.NORMAL;
}
public boolean isROTi_bene_servizio() {
	
	return false;
}
public boolean isROvoce_iva() {
	
	return getVoce_iva() == null ||
			getVoce_iva().getCrudStatus() == OggettoBulk.NORMAL;
}
public void setCategoria_gruppo(Categoria_gruppo_inventBulk catben) {
	categoria_gruppo = catben;
	if (catben!=null)
		setCd_categoria_gruppo(categoria_gruppo.getCd_categoria_gruppo());
}
public void setCd_categoria_gruppo(java.lang.String cd_categoria_gruppo) {
	this.getCategoria_gruppo().setCd_categoria_gruppo(cd_categoria_gruppo);
}
public void setCd_voce_iva(java.lang.String cd_voce_iva) {
	this.getVoce_iva().setCd_voce_iva(cd_voce_iva);
}
public void setVoce_iva(Voce_ivaBulk vi) {
	voce_iva = vi;
	if (vi!=null)
		super.setCd_voce_iva(voce_iva.getCd_voce_iva());
}
public void validate() throws ValidationException {
	super.validate();
	
	if (getFl_gestione_magazzino()==null)
		setFl_gestione_magazzino(FALSE);
	if (getFl_gestione_inventario()==null)
		setFl_gestione_inventario(FALSE);
	if (getFl_gestione_inventario().booleanValue() && getFl_gestione_magazzino().booleanValue())
		throw new ValidationException("Attenzione: non è possibile che un bene servizio sia a magazzino e ad inventario");
	if (getFl_gestione_inventario().booleanValue() && (getCategoria_gruppo()==null || getCategoria_gruppo().getDs_categoria_gruppo()==null))
		throw new ValidationException("Inserire un gruppo inventariale");
}
}
