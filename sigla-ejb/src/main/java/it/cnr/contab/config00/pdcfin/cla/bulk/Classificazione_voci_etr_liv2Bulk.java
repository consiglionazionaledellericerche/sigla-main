/*
 * Created on Aug 29, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.pdcfin.cla.bulk;

public class Classificazione_voci_etr_liv2Bulk extends Classificazione_voci_etr_liv1Bulk {

	public Classificazione_voci_etr_liv2Bulk() {
		super();
	}
	public Classificazione_voci_etr_liv2Bulk(java.lang.Integer id_classificazione) {
		super(id_classificazione);
	}
	public Classificazione_voci_etr_liv2Bulk(String cd_livello1, String cd_livello2) {
		super(cd_livello1);
		setCd_livello2(cd_livello2);
	}
}
