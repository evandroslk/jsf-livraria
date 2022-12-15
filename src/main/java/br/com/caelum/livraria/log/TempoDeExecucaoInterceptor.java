package br.com.caelum.livraria.log;

import java.io.Serializable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Log
public class TempoDeExecucaoInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;

	@AroundInvoke
	public Object log(InvocationContext contexto) throws Exception {

		long inicio = System.currentTimeMillis();
		String metodo = contexto.getMethod().getName();
		Object proceder = contexto.proceed();
		long fim = System.currentTimeMillis();
		long resultado = fim - inicio;
		System.out.println("Método executado: " + metodo + " Tempo execução: " + resultado);
		return proceder;
	}

}
