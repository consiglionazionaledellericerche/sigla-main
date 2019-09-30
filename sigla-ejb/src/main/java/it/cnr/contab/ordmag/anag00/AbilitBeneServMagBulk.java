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

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
public class AbilitBeneServMagBulk extends AbilitBeneServMagBase {
	/**
	 * [MAGAZZINO Rappresenta i magazzini utilizzati in gestione ordine e magazzino.]
	 **/
	private MagazzinoBulk magazzino =  new MagazzinoBulk();
	/**
	 * [CATEGORIA_GRUPPO_INVENT Definisce le categorie ed i relativi gruppi di beni associabili ad un record della tabella BENE_SERVIZIO. Tale gestione si applica

L'entità è definita su due livelli obbligatori:
1 livello = Categoria inventariale
2 livello = Gruppo di appartenenza del bene nell'ambito di una data categoria inventariale

Ogni categoria deve essere associata ad un capitolo del piano dei conti  economico-finanziario (qualsiasi livello anche il solo titolo) per l'individuazione del range delle possibili voci contabili associabili ad un documento che si riferisce a beni inventariabili. Tale associazione rileva solo in sede di collegamento delle righe di una fattura passiva ad una scadenza di obbligazione; le voci contabili presenti in OBBLIGAZIONE_SCAD_VOCE (collegata a OBBLIGAZIONE_SCADENZARIO) devono essere compatibili con quanto indicato sulla categoria inventariale. Si intende che le componenti il capitolo associabili sono solo quelle di spesa riferite ad un CdS non C.N.R.
]
	 **/
	private Categoria_gruppo_inventBulk categoriaGruppoInvent =  new Categoria_gruppo_inventBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ABILIT_BENE_SERV_MAG
	 **/
	public AbilitBeneServMagBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ABILIT_BENE_SERV_MAG
	 **/
	public AbilitBeneServMagBulk(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.String cdCategoriaGruppo) {
		super(cdCds, cdMagazzino, cdCategoriaGruppo);
		setMagazzino( new MagazzinoBulk(cdCds,cdMagazzino) );
		setCategoriaGruppoInvent( new Categoria_gruppo_inventBulk(cdCategoriaGruppo) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta i magazzini utilizzati in gestione ordine e magazzino.]
	 **/
	public MagazzinoBulk getMagazzino() {
		return magazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta i magazzini utilizzati in gestione ordine e magazzino.]
	 **/
	public void setMagazzino(MagazzinoBulk magazzino)  {
		this.magazzino=magazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Definisce le categorie ed i relativi gruppi di beni associabili ad un record della tabella BENE_SERVIZIO. Tale gestione si applica

L'entità è definita su due livelli obbligatori:
1 livello = Categoria inventariale
2 livello = Gruppo di appartenenza del bene nell'ambito di una data categoria inventariale

Ogni categoria deve essere associata ad un capitolo del piano dei conti  economico-finanziario (qualsiasi livello anche il solo titolo) per l'individuazione del range delle possibili voci contabili associabili ad un documento che si riferisce a beni inventariabili. Tale associazione rileva solo in sede di collegamento delle righe di una fattura passiva ad una scadenza di obbligazione; le voci contabili presenti in OBBLIGAZIONE_SCAD_VOCE (collegata a OBBLIGAZIONE_SCADENZARIO) devono essere compatibili con quanto indicato sulla categoria inventariale. Si intende che le componenti il capitolo associabili sono solo quelle di spesa riferite ad un CdS non C.N.R.
]
	 **/
	public Categoria_gruppo_inventBulk getCategoriaGruppoInvent() {
		return categoriaGruppoInvent;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Definisce le categorie ed i relativi gruppi di beni associabili ad un record della tabella BENE_SERVIZIO. Tale gestione si applica

L'entità è definita su due livelli obbligatori:
1 livello = Categoria inventariale
2 livello = Gruppo di appartenenza del bene nell'ambito di una data categoria inventariale

Ogni categoria deve essere associata ad un capitolo del piano dei conti  economico-finanziario (qualsiasi livello anche il solo titolo) per l'individuazione del range delle possibili voci contabili associabili ad un documento che si riferisce a beni inventariabili. Tale associazione rileva solo in sede di collegamento delle righe di una fattura passiva ad una scadenza di obbligazione; le voci contabili presenti in OBBLIGAZIONE_SCAD_VOCE (collegata a OBBLIGAZIONE_SCADENZARIO) devono essere compatibili con quanto indicato sulla categoria inventariale. Si intende che le componenti il capitolo associabili sono solo quelle di spesa riferite ad un CdS non C.N.R.
]
	 **/
	public void setCategoriaGruppoInvent(Categoria_gruppo_inventBulk categoriaGruppoInvent)  {
		this.categoriaGruppoInvent=categoriaGruppoInvent;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		MagazzinoBulk magazzino = this.getMagazzino();
		if (magazzino == null)
			return null;
		return getMagazzino().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getMagazzino().setCdCds(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public java.lang.String getCdMagazzino() {
		MagazzinoBulk magazzino = this.getMagazzino();
		if (magazzino == null)
			return null;
		return getMagazzino().getCdMagazzino();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(java.lang.String cdMagazzino)  {
		this.getMagazzino().setCdMagazzino(cdMagazzino);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCategoriaGruppo]
	 **/
	public java.lang.String getCdCategoriaGruppo() {
		Categoria_gruppo_inventBulk categoriaGruppoInvent = this.getCategoriaGruppoInvent();
		if (categoriaGruppoInvent == null)
			return null;
		return getCategoriaGruppoInvent().getCd_categoria_gruppo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCategoriaGruppo]
	 **/
	public void setCdCategoriaGruppo(java.lang.String cdCategoriaGruppo)  {
		this.getCategoriaGruppoInvent().setCd_categoria_gruppo(cdCategoriaGruppo);
	}
}