package br.edu.infnet.ecommerce.usuario.application;

import br.edu.infnet.ecommerce.usuario.domain.Usuario;
import br.edu.infnet.ecommerce.shared.exception.RecursoNaoEncontradoException;
import br.edu.infnet.ecommerce.usuario.infrastructure.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário não encontrado: " + id
                ));
    }

    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario dados) {
        Usuario usuario = buscar(id);
        usuario.setNome(dados.getNome());
        usuario.setEmail(dados.getEmail());
        usuario.setAtivo(dados.isAtivo());
        return usuarioRepository.save(usuario);
    }

    public void excluir(Long id) {
        usuarioRepository.delete(buscar(id));
    }
}
