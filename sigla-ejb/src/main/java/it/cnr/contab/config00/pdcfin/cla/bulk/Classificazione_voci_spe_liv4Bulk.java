/*
 * Created on Aug 29, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.pdcfin.cla.bulk;

public class Classificazione_voci_spe_liv4Bulk extends Classificazione_voci_spe_liv3Bulk {

	public Classificazione_voci_spe_liv4Bulk() {
		super();
	}
	public Classificazione_voci_spe_liv4Bulk(java.lang.Integer id_classificazione) {
		super(id_classificazione);
	}
	public Classificazione_voci_spe_liv4Bulk(String cd_livello1, String cd_livello2, String cd_livello3, String cd_livello4) {
		super(cd_livello1, cd_livello2, cd_livello3);
		setCd_livello4(cd_livello4);
	}
}
