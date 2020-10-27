package br.com.aula.exception;

public class NumeroDeContaInvalidoException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public NumeroDeContaInvalidoException(String msg) {
		super(msg);
	}

}
