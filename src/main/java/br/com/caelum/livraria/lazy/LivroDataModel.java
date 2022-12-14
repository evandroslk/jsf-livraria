package br.com.caelum.livraria.lazy;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.entities.Livro;

public class LivroDataModel extends LazyDataModel<Livro> {

	private static final long serialVersionUID = 1L;

	DAO<Livro> dao = new DAO<Livro>(Livro.class);

	@Override
	public List<Livro> load(int inicio, int quantidade, String campoOrdenacao, SortOrder sentidoOrdenacao, Map<String, Object> filtros) {
		super.setRowCount(dao.quantidadeDeElementosFiltrados(filtros));
		return dao.listaTodosPaginada(inicio, quantidade, filtros, campoOrdenacao, sentidoOrdenacao);
	}

}
