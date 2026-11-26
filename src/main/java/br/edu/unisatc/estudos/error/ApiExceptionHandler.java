package br.edu.unisatc.estudos.error;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler {
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ProblemDetail recursoNaoEncontrado(RecursoNaoEncontradoException erro) {
        return problema(HttpStatus.NOT_FOUND, "Recurso não encontrado", erro.getMessage());
    }

    @ExceptionHandler(RequisicaoInvalidaException.class)
    public ProblemDetail requisicaoInvalida(RequisicaoInvalidaException erro) {
        return problema(HttpStatus.BAD_REQUEST, "Requisição inválida", erro.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail camposInvalidos(MethodArgumentNotValidException erro) {
        ProblemDetail resposta = problema(HttpStatus.BAD_REQUEST, "Dados inválidos",
                "Corrija os campos indicados e envie a requisição novamente.");
        List<ErroCampo> campos = erro.getBindingResult().getFieldErrors().stream()
                .map(campo -> new ErroCampo(campo.getField(), campo.getDefaultMessage()))
                .toList();
        resposta.setProperty("erros", campos);
        return resposta;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail conflito() {
        return problema(HttpStatus.CONFLICT, "Conflito de integridade",
                "A operação entra em conflito com registros vinculados ou com uma restrição do banco.");
    }

    private ProblemDetail problema(HttpStatus status, String titulo, String detalhe) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(status, detalhe);
        problema.setTitle(titulo);
        return problema;
    }

    private record ErroCampo(String campo, String mensagem) {
    }
}
