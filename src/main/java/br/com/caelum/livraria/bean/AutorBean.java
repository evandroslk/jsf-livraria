package br.com.caelum.livraria.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.entities.Autor;
import br.com.caelum.livraria.util.RedirectView;

@ManagedBean
@ViewScoped
public class AutorBean {

	private Autor autor;

	private Integer autorId;

	public Autor getAutor() {
		return autor;
	}

	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public void carregarAutorPeloId() {
		this.autor = new DAO<Autor>(Autor.class).buscaPorId(autorId);
		if (autor == null) {
			this.autor = new Autor();
		}
	}

	public RedirectView gravar() {
		System.out.println("Gravando autor " + this.autor.getNome());
		new DAO<Autor>(Autor.class).adiciona(this.autor);
		autor = new Autor();
		return new RedirectView("livro");
	}

}
