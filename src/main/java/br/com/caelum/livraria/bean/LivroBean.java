package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.entities.Autor;
import br.com.caelum.livraria.entities.Livro;
import br.com.caelum.livraria.lazy.LivroDataModel;

@Named
@ViewScoped
public class LivroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Livro livro = new Livro();

	private Integer autorId;

	private Integer livroId;

	private List<Livro> livros;

	private LivroDataModel livroDataModel = new LivroDataModel();

	public void gravar() {
		System.out.println("Gravando livro " + this.livro.getTitulo());

		if (livro.getAutores().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage("autor", new FacesMessage("Livro deve ter pelo menos um autor"));
			return;
		}

		DAO<Livro> dao = new DAO<Livro>(Livro.class);

		if (this.livro.getId() == null) {
			dao.adiciona(this.livro);
			this.livros = dao.listaTodos();
		} else {
			dao.atualiza(this.livro);
		}

		this.livro = new Livro();

	}

	public boolean precoEhMenor(Object valorColuna, Object filtroDigitado, Locale locale) {

		String textoDigitado = (filtroDigitado == null) ? null : filtroDigitado.toString().trim();

		System.out.println("Filtrando pelo " + textoDigitado + ", Valor do elemento: " + valorColuna);

		if (textoDigitado == null || textoDigitado.equals("")) {
			return true;
		}

		if (valorColuna == null) {
			return false;
		}

		try {
			Double precoDigitado = Double.valueOf(textoDigitado);
			Double precoColuna = (Double) valorColuna;

			// comparando os valores, compareTo devolve um valor negativo se o value é menor do que o filtro
			return precoColuna.compareTo(precoDigitado) < 0;
		} catch (NumberFormatException e) {
			return false;
		}

	}

	public void gravarAutor() {
		Autor autor = new DAO<Autor>(Autor.class).buscaPorId(autorId);
		livro.adicionaAutor(autor);
	}

	public void comecaComDigitoUm(FacesContext facesContext, UIComponent component, Object value) throws ValidatorException {
		String valor = value.toString();
		if (!valor.startsWith("1")) {
			throw new ValidatorException(new FacesMessage("ISBN deveria começar com 1"));
		}
	}

	public void removerAutorDoLivro(Autor autor) {
		this.livro.removeAutor(autor);
	}

	public void carregaLivroPeloId() {
		this.livro = new DAO<Livro>(Livro.class).buscaPorId(this.livroId);
		if (livro == null) {
			this.livro = new Livro();
		}
	}

	public void remover(Livro livro) {
		new DAO<Livro>(Livro.class).remove(livro);
	}

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public Integer getLivroId() {
		return livroId;
	}

	public void setLivroId(Integer livroId) {
		this.livroId = livroId;
	}

	public List<Livro> getLivros() {
		DAO<Livro> dao = new DAO<Livro>(Livro.class);

		if (this.livros == null) {
			this.livros = dao.listaTodos();
		}

		return livros;
	}

	public List<Autor> getAutores() {
		return new DAO<Autor>(Autor.class).listaTodos();
	}

	public List<Autor> getAutoresDoLivro() {
		return livro.getAutores();
	}

	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public LivroDataModel getLivroDataModel() {
		return livroDataModel;
	}

	public List<String> getGeneros() {
		return Arrays.asList("Ação", "Drama", "Romance");
	}

}
