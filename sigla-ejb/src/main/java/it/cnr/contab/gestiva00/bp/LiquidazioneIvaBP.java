package it.cnr.contab.gestiva00.bp;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.util.action.CompoundPropertyController;


public class LiquidazioneIvaBP extends StampaRegistriIvaBP {

	private int status = INSERT;
	private boolean liquidato = false;
	private final CompoundPropertyController dettaglio = new CompoundPropertyController("liquidazione_iva", Liquidazione_ivaBulk.class,"liquidazione_iva",this);

public LiquidazioneIvaBP() {
	super();
}

public LiquidazioneIvaBP(String function) {
	super(function);
}

/**
 * Restituisce il valore della propriet� 'dettaglio'
 *
 * @return Il valore della propriet� 'dettaglio'
 */
public final CompoundPropertyController getDettaglio() {
	return dettaglio;
}

/**
 * Restituisce il valore della propriet� 'liquidato'
 *
 * @return Il valore della propriet� 'liquidato'
 */
public boolean isLiquidato() {
	return liquidato;
}

/**
 * Imposta il valore della propriet� 'liquidato'
 *
 * @param newLiquidato	Il valore da assegnare a 'liquidato'
 */
public void setLiquidato(boolean newLiquidato) {
	liquidato = newLiquidato;
}
public void inizializzaMese(ActionContext context) throws BusinessProcessException {
}
}