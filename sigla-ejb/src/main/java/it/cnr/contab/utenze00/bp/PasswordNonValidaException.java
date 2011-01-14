package it.cnr.contab.utenze00.bp;

public class PasswordNonValidaException extends Exception {
    public PasswordNonValidaException()
    {
    }

    public PasswordNonValidaException(String s)
    {
        super(s);
    }
}
