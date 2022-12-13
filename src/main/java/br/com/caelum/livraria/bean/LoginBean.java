package br.com.caelum.livraria.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.caelum.livraria.dao.UsuarioDAO;
import br.com.caelum.livraria.entities.Usuario;

@ManagedBean
@ViewScoped
public class LoginBean {

	private Usuario usuario = new Usuario();

	public String efetuaLogin() {
		boolean existe = new UsuarioDAO().existe(this.usuario);
		if (existe) {
			return "livro?faces-redirect=true";
		}
		return null;
	}

	public Usuario getUsuario() {
		return usuario;
	}

}
