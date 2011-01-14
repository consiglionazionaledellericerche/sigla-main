/*
 * Created on Aug 29, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.pdcfin.cla.bulk;

public class Classificazione_voci_spe_liv6Bulk extends Classificazione_voci_spe_liv5Bulk {

	public Classificazione_voci_spe_liv6Bulk() {
		super();
	}
	public Classificazione_voci_spe_liv6Bulk(java.lang.Integer id_classificazione) {
		super(id_classificazione);
	}
	public Classificazione_voci_spe_liv6Bulk(String cd_livello1, String cd_livello2, String cd_livello3, String cd_livello4, String cd_livello5, String cd_livello6) {
		super(cd_livello1, cd_livello2, cd_livello3, cd_livello4, cd_livello5);
		setCd_livello6(cd_livello6);
	}
}
