package it.cnr.contab.doccont00.comp;

public interface IStampaSingoloContoMgr extends it.cnr.jada.comp.ICRUDMgr {
void annullaModificaSelezione(
	it.cnr.jada.UserContext userContext, 
	it.cnr.contab.doccont00.singconto.bulk.V_voce_f_sing_contoBulk filtro)
	throws it.cnr.jada.comp.ComponentException;
void associaTutti(
	it.cnr.jada.UserContext userContext, 
	it.cnr.contab.doccont00.singconto.bulk.V_voce_f_sing_contoBulk filtro,
	java.math.BigDecimal pg_stampa)
	throws it.cnr.jada.comp.ComponentException;
java.math.BigDecimal getPgStampa(
	it.cnr.jada.UserContext userContext)
	throws it.cnr.jada.comp.ComponentException;
void inizializzaSelezionePerModifica(
	it.cnr.jada.UserContext userContext, 
	it.cnr.contab.doccont00.singconto.bulk.V_voce_f_sing_contoBulk filtro)
	throws it.cnr.jada.comp.ComponentException;
java.math.BigDecimal modificaSelezione(
	it.cnr.jada.UserContext userContext, 
	it.cnr.contab.doccont00.singconto.bulk.V_voce_f_sing_contoBulk filtro,
	it.cnr.jada.bulk.OggettoBulk[] bulks,
	java.util.BitSet oldSelection,
	java.util.BitSet newSelection,
	java.math.BigDecimal pg_stampa,
	java.math.BigDecimal currentSequence)
	throws it.cnr.jada.comp.ComponentException;
}
