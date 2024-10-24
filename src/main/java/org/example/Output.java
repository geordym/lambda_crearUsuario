package org.example;

public class Output {
    private String message;
    private Usuario usuarioCreado;

    public Output(String message, Usuario usuarioCreado) {
        this.message = message;
        this.usuarioCreado = usuarioCreado;
    }

    public Usuario getUsuarioCreado() {
        return usuarioCreado;
    }

    public void setUsuarioCreado(Usuario usuarioCreado) {
        this.usuarioCreado = usuarioCreado;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
