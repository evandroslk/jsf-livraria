package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.entities.Autor;
import br.com.caelum.livraria.util.RedirectView;

@Named
@ViewScoped
public class AutorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Autor autor = new Autor();

	private Integer autorId;

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public List<Autor> getAutores() {
		return new DAO<Autor>(Autor.class).listaTodos();
	}

	public void remover(Autor autor) {
		new DAO<Autor>(Autor.class).remove(autor);
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
