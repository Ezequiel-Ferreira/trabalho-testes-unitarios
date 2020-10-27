package br.com.aula;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import br.com.aula.exception.ContaJaExistenteException;
import br.com.aula.exception.ContaNaoExistenteException;
import br.com.aula.exception.ContaSemSaldoException;
import br.com.aula.exception.NumeroDeContaInvalidoException;
import br.com.aula.exception.ValorNegativoException;

public class BancoTest {

	@Test
	public void deveCadastrarConta() throws ContaJaExistenteException, NumeroDeContaInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, 123, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta);

		// Verificação
		assertEquals(1, banco.obterContas().size());
	}

	
	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaNumeroRepetido() throws ContaJaExistenteException, NumeroDeContaInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta conta2 = new Conta(cliente2, 123, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		Assert.fail();
	}

	@Test(expected = NumeroDeContaInvalidoException.class)
	public void naoDevePossuirNumeroInvalido() throws ContaJaExistenteException, NumeroDeContaInvalidoException {

		Cliente cliente = new Cliente("Carla");
		Conta conta1 = new Conta(cliente, 12345673, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);

		Assert.fail();
	}

	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaComNomeExistente()
			throws ContaJaExistenteException, NumeroDeContaInvalidoException {

		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta conta2 = new Conta(cliente2, 123, 0, TipoConta.POUPANCA);

		Cliente cliente3 = new Cliente("Maria");
		Conta conta3 = new Conta(cliente3, 123, 0, TipoConta.CORRENTE);

		Banco banco = new Banco();

		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);
		banco.cadastrarConta(conta3);

		Assert.fail();

	}

	@Test
	public void deveEfetuarTransferenciaContasCorrentes() throws ContaSemSaldoException, ContaNaoExistenteException, ValorNegativoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(-100, contaOrigem.getSaldo());
		assertEquals(100, contaDestino.getSaldo());
	}
	
	@Test
	public void deveEfetuarTransferenciaContasPoupanca() throws ContaSemSaldoException, ContaNaoExistenteException, ValorNegativoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 1000, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 1000, TipoConta.POUPANCA);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(900, contaOrigem.getSaldo());
		assertEquals(1100, contaDestino.getSaldo());
	}


	@Test(expected = ContaNaoExistenteException.class)
	public void deveVerificarAContaOrigemNoBanco() throws ContaNaoExistenteException, ContaSemSaldoException, ValorNegativoException {
		
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 450, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 81, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));
		
		banco.efetuarTransferencia(111, contaDestino.getNumeroConta(), 110 );
		
		Assert.fail();	
	}
	
	@Test(expected = ContaNaoExistenteException.class)
	public void deveVerificarAContaDestinoNoBanco() throws ContaNaoExistenteException, ContaSemSaldoException, ValorNegativoException {
		
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 99, TipoConta.CORRENTE);

	
		
		Banco banco = new Banco(Arrays.asList(contaOrigem));
		
		banco.efetuarTransferencia(123, 654, 12 );
		
		Assert.fail();	
	}
	
	@Test(expected = ContaSemSaldoException.class)
	public void contaPoupancaNaoDeveFicarComSaldoNegativo() throws ContaNaoExistenteException, ContaSemSaldoException, ValorNegativoException {
		
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 99, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 1, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));
		
		banco.efetuarTransferencia(123, 456, 100);
		
		Assert.fail();
	}
	@Test(expected = ValorNegativoException.class)
public void valorTransferidoNaoPodeserNegativo() throws ContaNaoExistenteException, ContaSemSaldoException, ValorNegativoException {
		
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 99, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 1, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));
		
		banco.efetuarTransferencia(123, 456, -1);
		
		Assert.fail();
	}
	
	
	
}
