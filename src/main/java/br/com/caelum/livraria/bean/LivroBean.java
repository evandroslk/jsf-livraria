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
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.livraria.dao.AutorDAO;
import br.com.caelum.livraria.dao.LivroDAO;
import br.com.caelum.livraria.entities.Autor;
import br.com.caelum.livraria.entities.Livro;
import br.com.caelum.livraria.lazy.LivroDataModel;
import br.com.caelum.livraria.tx.Transacional;

@Named
@ViewScoped
public class LivroBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private AutorDAO autorDAO;

	@Inject
	private LivroDAO livroDAO;

	@Inject
	private FacesContext context;

	private Livro livro = new Livro();

	private Integer autorId;

	private Integer livroId;

	private List<Livro> livros;

	@Inject
	private LivroDataModel livroDataModel;

	@Transacional
	public void gravar() {
		System.out.println("Gravando livro " + this.livro.getTitulo());

		if (livro.getAutores().isEmpty()) {
			context.addMessage("autor", new FacesMessage("Livro deve ter pelo menos um autor"));
			return;
		}

		if (this.livro.getId() == null) {
			livroDAO.adiciona(this.livro);
			this.livros = livroDAO.listaTodos();
		} else {
			livroDAO.atualiza(this.livro);
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
		Autor autor = autorDAO.buscaPorId(autorId);
		livro.adicionaAutor(autor);
	}

	public void comecaComDigitoUm(FacesContext facesContext, UIComponent component, Object value) throws ValidatorException {
		String valor = value.toString();
		if (!valor.startsWith("1")) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "ISBN deveria começar com 1", null));
		}
	}

	public void removerAutorDoLivro(Autor autor) {
		this.livro.removeAutor(autor);
	}

	public void carregaLivroPeloId() {
		this.livro = livroDAO.buscaPorId(this.livroId);
		if (livro == null) {
			this.livro = new Livro();
		}
	}

	public void carregar(Livro livro) {
		this.livro = this.livroDAO.buscaPorId(livro.getId());
	}

	@Transacional
	public void remover(Livro livro) {
		livroDAO.remove(livro);
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
		if (this.livros == null) {
			this.livros = livroDAO.listaTodos();
		}

		return livros;
	}

	public List<Autor> getAutores() {
		return autorDAO.listaTodos();
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

	public List<String> getGeneros() {
		return Arrays.asList("Ação", "Drama", "Romance");
	}

	public LivroDataModel getLivroDataModel() {
		return livroDataModel;
	}

}
