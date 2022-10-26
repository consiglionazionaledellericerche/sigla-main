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

package it.cnr.contab.missioni00.docs.bulk;

import it.cnr.contab.coepcoan00.core.bulk.IDocumentoCogeBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.docs.bulk.TipoDocumentoEnum;

import java.sql.Timestamp;

public class RimborsoBulk extends RimborsoBase implements IDocumentoCogeBulk {
    // Stati documento riportato
    public final static java.util.Dictionary STATI_RIPORTO;

    static {
        STATI_RIPORTO = new it.cnr.jada.util.OrderedHashtable();
        STATI_RIPORTO.put(IDocumentoAmministrativoBulk.NON_RIPORTATO, "Non riportata");
        STATI_RIPORTO.put(IDocumentoAmministrativoBulk.PARZIALMENTE_RIPORTATO, "Parzialmente riportata");
        STATI_RIPORTO.put(IDocumentoAmministrativoBulk.COMPLETAMENTE_RIPORTATO, "Completamente riportata");
    }

    AnticipoBulk anticipo;
    private java.lang.String riportata = IDocumentoAmministrativoBulk.NON_RIPORTATO;
    private java.lang.String riportataInScrivania = IDocumentoAmministrativoBulk.NON_RIPORTATO;
	private Scrittura_partita_doppiaBulk scrittura_partita_doppia;

    public RimborsoBulk() {
        super();
    }

    public RimborsoBulk(java.lang.String cd_cds, java.lang.String cd_unita_organizzativa, java.lang.Integer esercizio, java.lang.Long pg_rimborso) {
        super(cd_cds, cd_unita_organizzativa, esercizio, pg_rimborso);
    }

    /**
     * Insert the method's description here.
     * Creation date: (24/06/2002 18.23.22)
     *
     * @return it.cnr.contab.missioni00.docs.bulk.AnticipoBulk
     */
    public AnticipoBulk getAnticipo() {
        return anticipo;
    }

    /**
     * Insert the method's description here.
     * Creation date: (24/06/2002 18.23.22)
     *
     * @param newAnticipo it.cnr.contab.missioni00.docs.bulk.AnticipoBulk
     */
    public void setAnticipo(AnticipoBulk newAnticipo) {
        anticipo = newAnticipo;
    }

    /**
     * Insert the method's description here.
     * Creation date: (21/07/2003 12.01.34)
     *
     * @return java.lang.String
     */
    public java.lang.String getRiportata() {
        return riportata;
    }

    /**
     * Insert the method's description here.
     * Creation date: (02/11/2004 14.39.32)
     *
     * @return java.lang.String
     */
    public java.lang.String getRiportataInScrivania() {
        return riportataInScrivania;
    }

    public boolean isRiportata() {
        return !IDocumentoAmministrativoBulk.NON_RIPORTATO.equals(riportata);
    }

    /**
     * Insert the method's description here.
     * Creation date: (21/07/2003 12.01.34)
     *
     * @param newRiportata java.lang.String
     */
    public void setRiportata(java.lang.String newRiportata) {
        riportata = newRiportata;
    }

    public boolean isRiportataInScrivania() {
        return !IDocumentoAmministrativoBulk.NON_RIPORTATO.equals(riportataInScrivania);
    }

    /**
     * Insert the method's description here.
     * Creation date: (02/11/2004 14.39.32)
     *
     * @param newRiportataInScrivania java.lang.String
     */
    public void setRiportataInScrivania(java.lang.String newRiportataInScrivania) {
        riportataInScrivania = newRiportataInScrivania;
    }

    @Override
    public java.lang.String getCd_tipo_doc() {
        return Numerazione_doc_ammBulk.TIPO_RIMBORSO;
    }

    @Override
    public String getCd_uo() {
        return this.getCd_unita_organizzativa();
    }

    @Override
    public Long getPg_doc() {
        return this.getPg_rimborso();
    }

    @Override
    public TipoDocumentoEnum getTipoDocumentoEnum() {
        return TipoDocumentoEnum.fromValue(this.getCd_tipo_doc());
    }

	public Scrittura_partita_doppiaBulk getScrittura_partita_doppia() {
		return scrittura_partita_doppia;
	}

	public void setScrittura_partita_doppia(Scrittura_partita_doppiaBulk scrittura_partita_doppia) {
		this.scrittura_partita_doppia = scrittura_partita_doppia;
	}

    /**
     * Ritorna sempre valore null in quanto campo valido solo per liquidazioni
     */
    @Override
    public Timestamp getDtInizioLiquid() {
        return null;
    }

    /**
     * Ritorna sempre valore null in quanto campo valido solo per liquidazioni
     */
    @Override
    public Timestamp getDtFineLiquid() {
        return null;
    }

    /**
     * Ritorna sempre valore null in quanto campo valido solo per liquidazioni
     */
    @Override
    public String getTipoLiquid() {
        return null;
    }

    /**
     * Ritorna sempre valore null in quanto campo valido solo per liquidazioni
     */
    @Override
    public Long getReportIdLiquid() {
        return null;
    }

    @Override
    public Timestamp getDt_contabilizzazione() {
        return this.getDt_registrazione();
    }
}
