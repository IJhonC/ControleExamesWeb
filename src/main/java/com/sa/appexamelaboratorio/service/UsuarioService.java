package com.sa.appexamelaboratorio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sa.appexamelaboratorio.model.Usuario;
import com.sa.appexamelaboratorio.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario salvarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeStartingWithIgnoreCase(nome);
    }

    public Usuario buscarPorId(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.get();
    }

    public Long contarUsuarios() {
        return usuarioRepository.count();
    }

    public List<Usuario> listarUsuaios() {
        return usuarioRepository.findAll();
    }

    public void deletarPorId(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Long contarPorGenero(String genero) {
        return usuarioRepository.countBySexo(genero);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));

        // Transforme as roles do banco de dados em uma String
        String role = usuario.getRole();

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles(role)
                .build();
    }

    public Long buscarIdPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(Usuario::getId)
                .orElse(null);
    }
}
