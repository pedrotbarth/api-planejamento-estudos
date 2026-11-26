package br.edu.unisatc.estudos.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail tratarErroDaApi(ResponseStatusException erro) {
        return erro.getBody();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail tratarValidacao(MethodArgumentNotValidException erro) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Corrija os campos indicados."
        );

        var campos = erro.getBindingResult().getFieldErrors().stream()
                .map(campo -> new CampoInvalido(campo.getField(), campo.getDefaultMessage()))
                .toList();

        problema.setProperty("campos", campos);
        return problema;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail tratarCorpoInvalido() {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Confira o formato do JSON e os valores enviados."
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail tratarParametroInvalido(MethodArgumentTypeMismatchException erro) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Valor inválido para o parâmetro " + erro.getName() + "."
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail tratarConflito() {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "A operação viola uma restrição do banco. Verifique os registros vinculados."
        );
    }

    private record CampoInvalido(String campo, String mensagem) {
    }
}