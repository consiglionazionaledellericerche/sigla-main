package it.cnr.contab.incarichi00.ejb;

import javax.ejb.Remote;

@Remote
public interface IncarichiEstrazioneFpComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	public it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk completaIncaricoElencoFP(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk param1) throws it.cnr.jada.comp.ComponentException;
	public void aggiornaIncarichiComunicatiFP(it.cnr.jada.UserContext param0, java.util.List<it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk> param1) throws it.cnr.jada.comp.ComponentException;
	public void aggiornaIncarichiComunicatiFP(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk param1) throws it.cnr.jada.comp.ComponentException;
	public it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk getIncarichiComunicatiAggFP(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk param1) throws it.cnr.jada.comp.ComponentException;	
	public it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk getIncarichiComunicatiAggFP(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk param1) throws it.cnr.jada.comp.ComponentException;	
	public java.util.List<it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk> getIncarichiComunicatiEliminatiFP(it.cnr.jada.UserContext param0, Integer param1, Integer param2) throws it.cnr.jada.comp.ComponentException;
	public java.util.List<it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fp_detBulk> getPagatoPerSemestre(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param1) throws it.cnr.jada.comp.ComponentException;
}
