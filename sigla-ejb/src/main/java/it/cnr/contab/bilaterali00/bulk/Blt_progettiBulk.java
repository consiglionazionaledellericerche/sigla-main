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
* Created by Generator 1.0
* Date 19/10/2005
*/
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
public class Blt_progettiBulk extends Blt_progettiBase {

	private Blt_accordiBulk bltAccordo;

	private TerzoBulk responsabileIta;

	private TerzoBulk responsabileStr;

	private CdrBulk centro_responsabilitaIta;
	
	private BulkList bltProgrammaVisiteItaColl = new BulkList();

	private BulkList bltProgrammaVisiteStrColl = new BulkList();

	private BulkList bltAutorizzatiItaColl = new BulkList();

	private BulkList bltAutorizzatiStrColl = new BulkList();

	private ComuneBulk comuneEnteResponsIta;

	private java.util.Collection caps_comune;
	
	private BltIstitutiBulk bltIstituto;

	public Blt_progettiBulk() {
		super();
	}
	public Blt_progettiBulk(java.lang.String cd_accordo, java.lang.String cd_progetti) {
		super(cd_accordo, cd_progetti);
	}

	public Blt_accordiBulk getBltAccordo() {
		return bltAccordo;
	}
	
	public void setBltAccordo(Blt_accordiBulk blt_accordo) {
		this.bltAccordo = blt_accordo;
	}
	
	@Override
	public String getCd_accordo() {
		if (this.getBltAccordo()!=null)
			return this.getBltAccordo().getCd_accordo();
		return null;
		
	}
	@Override
	public void setCd_accordo(String cd_accordo) {
		if (this.getBltAccordo()!=null)
			this.getBltAccordo().setCd_accordo(cd_accordo);
	}
	public TerzoBulk getResponsabileIta() {
		return responsabileIta;
	}
	public void setResponsabileIta(TerzoBulk responsabileIta) {
		this.responsabileIta = responsabileIta;
	}
	@Override
	public Integer getCd_respons_ita() {
		if (this.getResponsabileIta()!=null)
			return this.getResponsabileIta().getCd_terzo();
		return null;
	}
	@Override
	public void setCd_respons_ita(Integer cd_respons_ita) {
		if (this.getResponsabileIta()!=null)
			this.getResponsabileIta().setCd_terzo(cd_respons_ita);
	}
	public TerzoBulk getResponsabileStr() {
		return responsabileStr;
	}
	public void setResponsabileStr(TerzoBulk responsabileStr) {
		this.responsabileStr = responsabileStr;
	}
	@Override
	public Integer getCd_respons_str() {
		if (this.getResponsabileStr()!=null)
			return this.getResponsabileStr().getCd_terzo();
		return null;
	}
	@Override
	public void setCd_respons_str(Integer cd_respons_str) {
		if (this.getResponsabileStr()!=null)
			this.getResponsabileStr().setCd_terzo(cd_respons_str);
	}

	public CdrBulk getCentro_responsabilitaIta() {
		return centro_responsabilitaIta;
	}
	public void setCentro_responsabilitaIta(CdrBulk centro_responsabilitaIta) {
		this.centro_responsabilitaIta = centro_responsabilitaIta;
	}
	@Override
	public String getCd_cdr_respons_ita() {
		if (this.getCentro_responsabilitaIta()!=null)
			return this.getCentro_responsabilitaIta().getCd_centro_responsabilita();
		return null;
	}
	@Override
	public void setCd_cdr_respons_ita(String cd_cdr_respons_ita) {
		if (this.getCentro_responsabilitaIta()!=null)
			this.getCentro_responsabilitaIta().setCd_centro_responsabilita(cd_cdr_respons_ita);
	}

	public BulkList getBltProgrammaVisiteItaColl() {
		return bltProgrammaVisiteItaColl;
	}
	public void setBltProgrammaVisiteItaColl(BulkList bltProgrammaVisiteItaColl) {
		this.bltProgrammaVisiteItaColl = bltProgrammaVisiteItaColl;
	}

	public BulkList getBltProgrammaVisiteStrColl() {
		return bltProgrammaVisiteStrColl;
	}
	public void setBltProgrammaVisiteStrColl(BulkList bltProgrammaVisiteStrColl) {
		this.bltProgrammaVisiteStrColl = bltProgrammaVisiteStrColl;
	}

	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] { 
				getBltProgrammaVisiteItaColl(), getBltProgrammaVisiteStrColl(),
				getBltAutorizzatiItaColl(), getBltAutorizzatiStrColl() };
	}

	public int addToBltProgrammaVisiteItaColl(Blt_programma_visiteBulk dett) {
		dett.setBltProgetti(this);
		dett.setFlStraniero(Boolean.FALSE);
		dett.setNumVisiteAutorizzate(Utility.ZERO.intValue());
		dett.setNumVisiteUtilizzate(Utility.ZERO.intValue());
		getBltProgrammaVisiteItaColl().add(dett);
		return getBltProgrammaVisiteItaColl().size()-1;
	}	
	public Blt_programma_visiteBulk removeFromBltProgrammaVisiteItaColl(int index) {
		Blt_programma_visiteBulk dett = (Blt_programma_visiteBulk)getBltProgrammaVisiteItaColl().remove(index);
		return dett;
	}

	public int addToBltProgrammaVisiteStrColl(Blt_programma_visiteBulk dett) {
		dett.setBltProgetti(this);
		dett.setFlStraniero(Boolean.TRUE);
		dett.setNumVisiteAutorizzate(Utility.ZERO.intValue());
		dett.setNumVisiteUtilizzate(Utility.ZERO.intValue());
		getBltProgrammaVisiteStrColl().add(dett);
		return getBltProgrammaVisiteStrColl().size()-1;
	}	
	public Blt_programma_visiteBulk removeFromBltProgrammaVisiteStrColl(int index) {
		Blt_programma_visiteBulk dett = (Blt_programma_visiteBulk)getBltProgrammaVisiteStrColl().remove(index);
		return dett;
	}

	public BulkList getBltAutorizzatiItaColl() {
		return bltAutorizzatiItaColl;
	}
	public void setBltAutorizzatiItaColl(BulkList bltAutorizzatiItaColl) {
		this.bltAutorizzatiItaColl = bltAutorizzatiItaColl;
	}
	public int addToBltAutorizzatiItaColl(Blt_autorizzatiBulk dett) {
		dett.setBltProgetti(this);
		dett.setTiItalianoEstero(NazioneBulk.ITALIA);
		dett.setFlAssimilatoDip(Boolean.FALSE);
		dett.setFlAssociato(Boolean.FALSE);
		getBltAutorizzatiItaColl().add(dett);
		return getBltAutorizzatiItaColl().size()-1;
	}	
	public Blt_autorizzatiBulk removeFromBltAutorizzatiItaColl(int index) {
		Blt_autorizzatiBulk dett = (Blt_autorizzatiBulk)getBltAutorizzatiItaColl().remove(index);
		return dett;
	}

	public BulkList getBltAutorizzatiStrColl() {
		return bltAutorizzatiStrColl;
	}
	public void setBltAutorizzatiStrColl(BulkList bltAutorizzatiStrColl) {
		this.bltAutorizzatiStrColl = bltAutorizzatiStrColl;
	}
	public int addToBltAutorizzatiStrColl(Blt_autorizzatiBulk dett) {
		dett.setBltProgetti(this);
		dett.setTiItalianoEstero(NazioneBulk.EXTRA_CEE);
		dett.setFlAssimilatoDip(Boolean.FALSE);
		dett.setFlAssociato(Boolean.FALSE);
		getBltAutorizzatiStrColl().add(dett);
		return getBltAutorizzatiStrColl().size()-1;
	}	
	public Blt_autorizzatiBulk removeFromBltAutorizzatiStrColl(int index) {
		Blt_autorizzatiBulk dett = (Blt_autorizzatiBulk)getBltAutorizzatiStrColl().remove(index);
		return dett;
	}
	public boolean isROdsComuneEnteResponsIta() {
		return getComuneEnteResponsIta() == null || getComuneEnteResponsIta().getCrudStatus() == OggettoBulk.NORMAL;
	}
	public ComuneBulk getComuneEnteResponsIta() {
		return comuneEnteResponsIta;
	}
	public void setComuneEnteResponsIta(ComuneBulk comuneEnteResponsIta) {
		this.comuneEnteResponsIta = comuneEnteResponsIta;
	}

	@Override
	public Long getPg_comune_ente_respons_ita() {
		ComuneBulk comuneEnteResponsIta = this.getComuneEnteResponsIta();
		if (comuneEnteResponsIta == null)
			return null;
		return getComuneEnteResponsIta().getPg_comune();
	}
	@Override
	public void setPg_comune_ente_respons_ita(Long pgComuneEnteResponsIta) {
		this.getComuneEnteResponsIta().setPg_comune(pgComuneEnteResponsIta);
	}
	public java.util.Collection getCaps_comune() {
		return caps_comune;
	}
	public void setCaps_comune(java.util.Collection caps_comune) {
		this.caps_comune = caps_comune;
	}
	public BltIstitutiBulk getBltIstituto() {
		return bltIstituto;
	}
	public void setBltIstituto(BltIstitutiBulk bltIstituto) {
		this.bltIstituto = bltIstituto;
	}
	
}