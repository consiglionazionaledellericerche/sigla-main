package it.cnr.contab.gestiva00.bp;

/**
 * Insert the type's description here.
 * Creation date: (3/26/2003 11:17:29 AM)
 * @author: CNRADM
 */
public class VisualizzaLiquidazioneDefinitivaIvaBP extends LiquidazioneDefinitivaIvaBP {
/**
 * RistampaLiquidazioneDefinitivaIvaBP constructor comment.
 */
public VisualizzaLiquidazioneDefinitivaIvaBP() {
	super();
}
/**
 * RistampaLiquidazioneDefinitivaIvaBP constructor comment.
 * @param function java.lang.String
 */
public VisualizzaLiquidazioneDefinitivaIvaBP(String function) {
	super(function);
}
public boolean isStartSearchButtonEnabled() {
	return false;
}
public boolean isStartSearchButtonHidden() {
	return true;
}
}
