package br.com.aula.exception;

public class ValorNegativoException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ValorNegativoException(String msg) {
		super(msg);
	}

}
