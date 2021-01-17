package br.com.softblue.bluefood.infraestructure.web.security;

import java.util.List;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.softblue.bluefood.domain.cliente.Cliente;
import br.com.softblue.bluefood.domain.restaurante.Restaurante;
import br.com.softblue.bluefood.domain.usuario.Usuario;
import br.com.softblue.bluefood.util.CollectionUtil;

@SuppressWarnings("serial")
public class LoggedUser implements UserDetails {
	
	private Usuario usuario;
	private Role role;
	private Collection<? extends GrantedAuthority> roles;
	
	

	public LoggedUser(Usuario usuario) {

		this.usuario = usuario;
		Role role;
		
		if (usuario instanceof Cliente) {
			role = Role.CLIENTE;
		}else if (usuario instanceof Restaurante) {
			role = Role.RESTAURANTE;
			
		}else {
			throw new IllegalStateException ("O tipo de usuário não é válido");
		}
		
		this.role = role;
		this.roles = CollectionUtil.listOf(new SimpleGrantedAuthority("ROLE_" + role));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return roles;
	}

	@Override
	public String getPassword() {
		return usuario.getSenha();
	}

	@Override
	public String getUsername() {
		return usuario.getNome();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public Role getRole() {
		return role;
	}
	

}
