/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 20/01/2007
 */
package it.cnr.contab.prevent01.bulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Ass_dipartimento_areaBulk extends Ass_dipartimento_areaBase {
	private DipartimentoBulk dipartimento;
	private CdsBulk area;
	public Ass_dipartimento_areaBulk() {
		super();
	}
	public Ass_dipartimento_areaBulk(java.lang.Integer esercizio, java.lang.String cd_dipartimento, java.lang.String cd_cds_area) {
		super(esercizio, cd_dipartimento, cd_cds_area);
		setDipartimento(new DipartimentoBulk(cd_dipartimento));
		setArea(new CdsBulk(cd_cds_area));
	}
	public DipartimentoBulk getDipartimento() {
		return dipartimento;
	}
	public void setDipartimento(DipartimentoBulk dipartimento) {
		this.dipartimento = dipartimento;
	}
	public String getCd_dipartimento() {
		DipartimentoBulk dipartimento=this.getDipartimento();
		if(dipartimento==null)
			return null;
		return dipartimento.getCd_dipartimento();
	}
	public void setCd_dipartimento(String cd_dipartimento) {
		dipartimento.setCd_dipartimento(cd_dipartimento);
	}
	public java.lang.String getCd_cds_area () {
		if(getArea() != null)
		  return getArea().getCd_unita_organizzativa();
		return null;  
	}
	public void setCd_cds_area(java.lang.String cd_cds_area)  {
		if(getArea() != null)
		   getArea().setCd_unita_organizzativa(cd_cds_area);
		super.setCd_cds_area(cd_cds_area);   
	}
	public CdsBulk getArea() {
		return area;
	}
	public void setArea(CdsBulk bulk) {
		area = bulk;
	}
}