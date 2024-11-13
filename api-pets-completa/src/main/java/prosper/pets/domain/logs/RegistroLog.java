package prosper.pets.domain.logs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class RegistroLog {
    
    @JsonProperty("username")
    private String usuario;

    @JsonProperty("description")
    private String descricao;

    @JsonProperty("createdAt")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime momento = LocalDateTime.now();

    public RegistroLog(String usuario, String descricao) {
        this.usuario = usuario;
        this.descricao = descricao;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getMomento() {
        return momento;
    }

}
