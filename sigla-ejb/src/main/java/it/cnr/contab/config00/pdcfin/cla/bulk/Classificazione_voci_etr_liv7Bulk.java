/*
 * Created on Aug 29, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.pdcfin.cla.bulk;

public class Classificazione_voci_etr_liv7Bulk extends Classificazione_voci_etr_liv6Bulk {
	public Classificazione_voci_etr_liv7Bulk() {
		super();
	}
	public Classificazione_voci_etr_liv7Bulk(java.lang.Integer id_classificazione) {
		super(id_classificazione);
	}
	public Classificazione_voci_etr_liv7Bulk(String cd_livello1, String cd_livello2, String cd_livello3, String cd_livello4, String cd_livello5, String cd_livello6, String cd_livello7) {
		super(cd_livello1, cd_livello2, cd_livello3, cd_livello4, cd_livello5, cd_livello6);
		setCd_livello7(cd_livello7);
	}
}
