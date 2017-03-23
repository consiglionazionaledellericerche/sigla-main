/*
 * Created on Aug 29, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.pdcep.cla.bulk;

public class Classificazione_voci_ep_eco_liv8Bulk extends Classificazione_voci_ep_eco_liv7Bulk {
	public Classificazione_voci_ep_eco_liv8Bulk() {
		super();
	}
	public Classificazione_voci_ep_eco_liv8Bulk(java.lang.Integer id_classificazione) {
		super(id_classificazione);
	}
	public Classificazione_voci_ep_eco_liv8Bulk(String cd_livello1, String cd_livello2, String cd_livello3,
	 String cd_livello4, String cd_livello5, String cd_livello6, String cd_livello7,String cd_livello8) {
		super(cd_livello1, cd_livello2, cd_livello3, cd_livello4, cd_livello5, cd_livello6,cd_livello7);
		setCd_livello8(cd_livello8);
	}
}
