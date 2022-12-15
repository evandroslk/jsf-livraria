package br.com.caelum.livraria.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.primefaces.model.SortOrder;

import br.com.caelum.livraria.entities.Livro;
import br.com.caelum.livraria.log.Log;

public class LivroDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	private DAO<Livro> dao;

	@PostConstruct
	void init() {
		this.dao = new DAO<Livro>(this.em, Livro.class);
	}

	public Livro buscaPorId(Integer livroId) {
		return this.dao.buscaPorId(livroId);
	}

	public void adiciona(Livro livro) {
		this.dao.adiciona(livro);
	}

	public void atualiza(Livro livro) {
		this.dao.atualiza(livro);
	}

	public void remove(Livro livro) {
		this.dao.remove(livro);
	}


	public List<Livro> listaTodos() {
		return this.dao.listaTodos();
	}

	public int quantidadeDeElementosFiltrados(Map<String, Object> filtros) {
		return this.dao.quantidadeDeElementosFiltrados(filtros);
	}

	@Log
	public List<Livro> listaTodosPaginada(int inicio, int quantidade, Map<String, Object> filtros, String campoOrdenacao, SortOrder sentidoOrdenacao) {
		return this.dao.listaTodosPaginada(inicio, quantidade, filtros, campoOrdenacao, sentidoOrdenacao);
	}

}