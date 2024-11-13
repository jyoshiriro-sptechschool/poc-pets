package prosper.pets.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.br.CPF;
import prosper.pets.domain.racas.TipoRaca;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30)
    private String nome;

    @NotBlank
    private String nomeDono;

    @Size(max = 50)
    @Email
    private String emailDono;

    @CPF
    private String cpfDono;

    @NotBlank
    @Size(max = 15)
    @Pattern(regexp = "(\\(?\\d{2}\\)?\\s)?(\\d{4,5}\\-\\d{4})", message = "Deve ser um número telefônico válido")
    private String telefoneDono;

    @DecimalMin("0.2")
    @NotNull
    private Double peso;

    @PositiveOrZero
    private Integer filhotes;

    @PastOrPresent
    private LocalDate nascimento;

    @NotNull
    private TipoRaca tipo;

    @Transient
    private String raca;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long idRaca;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime criacao;

    @UpdateTimestamp
    private LocalDateTime atualizacao;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getIdade() {
        if (nascimento == null) {
            return null;
        }

        Period periodo = nascimento.until(LocalDate.now());
        int anos = periodo.getYears();
        int meses = periodo.getMonths();
        int dias = periodo.getDays();
        String textoAnos = "ano"+(anos>1?"s":"");
        String textoMeses = (meses>1?"meses":"mês");
        String textoDias = "dia"+(dias>1?"s":"");
        return String.format("%d %s, %d %s e %d %s", anos, textoAnos, meses, textoMeses, dias, textoDias);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono;
    }

    public String getEmailDono() {
        return emailDono;
    }

    public void setEmailDono(String emailDono) {
        this.emailDono = emailDono;
    }

    public String getCpfDono() {
        return cpfDono;
    }

    public void setCpfDono(String cpfDono) {
        this.cpfDono = cpfDono;
    }

    public String getTelefoneDono() {
        return telefoneDono;
    }

    public void setTelefoneDono(String telefoneDono) {
        this.telefoneDono = telefoneDono;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Integer getFilhotes() {
        return filhotes;
    }

    public void setFilhotes(Integer filhotes) {
        this.filhotes = filhotes;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public TipoRaca getTipo() {
        return tipo;
    }

    public void setTipo(TipoRaca tipo) {
        this.tipo = tipo;
    }

    public Long getIdRaca() {
        return idRaca;
    }

    public void setIdRaca(Long idRaca) {
        this.idRaca = idRaca;
    }

    public LocalDateTime getCriacao() {
        return criacao;
    }

    public LocalDateTime getAtualizacao() {
        return atualizacao;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }
}
