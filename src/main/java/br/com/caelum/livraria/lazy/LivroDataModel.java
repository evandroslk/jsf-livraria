package br.com.caelum.livraria.lazy;

import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.caelum.livraria.dao.LivroDAO;
import br.com.caelum.livraria.entities.Livro;

@Named
@ViewScoped
public class LivroDataModel extends LazyDataModel<Livro> {

	private static final long serialVersionUID = 1L;

	@Inject
	private LivroDAO dao;

	@Override
	public List<Livro> load(int inicio, int quantidade, String campoOrdenacao, SortOrder sentidoOrdenacao, Map<String, Object> filtros) {
		super.setRowCount(dao.quantidadeDeElementosFiltrados(filtros));
		return dao.listaTodosPaginada(inicio, quantidade, filtros, campoOrdenacao, sentidoOrdenacao);
	}

}
