package br.edu.infnet.ecommerce.pedido.controller;

import br.edu.infnet.ecommerce.pedido.domain.Pedido;
import br.edu.infnet.ecommerce.pedido.api.CriarPedidoRequest;
import br.edu.infnet.ecommerce.pedido.application.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> listar() {
        return pedidoService.listar();
    }

    @GetMapping("/{id}")
    public Pedido buscar(@PathVariable Long id) {
        return pedidoService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido criar(@Valid @RequestBody CriarPedidoRequest request) {
        return pedidoService.criar(request);
    }
}
