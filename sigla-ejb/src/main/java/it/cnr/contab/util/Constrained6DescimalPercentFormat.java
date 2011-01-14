package it.cnr.contab.util;

/**
 * Insert the type's description here.
 * Creation date: (12/17/2002 10:37:33 AM)
 * @author: CNRADM
 */
public class Constrained6DescimalPercentFormat extends ConstrainedPercentFormat {

	private static final java.text.Format sixDecimalFormat = new java.text.DecimalFormat("0.000000");
/**
 * Constrained6DescimalPercentFormat constructor comment.
 */
public Constrained6DescimalPercentFormat() {
	super();
}
public java.text.Format getFormat() {
	
	return sixDecimalFormat;
}
public int getPrecision() {
	return 6;
}
}
