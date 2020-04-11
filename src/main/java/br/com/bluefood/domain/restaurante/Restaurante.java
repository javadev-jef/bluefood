package br.com.bluefood.domain.restaurante;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import br.com.bluefood.domain.usuario.Usuario;
import br.com.bluefood.infrastructure.web.validator.UploadConstraint;
import br.com.bluefood.utils.FileType;
import br.com.bluefood.utils.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * RESTAURANT MODEL
 */
@SuppressWarnings("serial")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "TBL_RESTAURANTE")
public class Restaurante extends Usuario
{
    private Integer id;

    @NotBlank(message = "Razão Social não informada")
    @Size(max = 80, message = "A Razão Social é muito grande")
    @Column(length = 80, nullable = false)
    private String razao;

    @NotBlank(message = "O CNPJ não pode ser vazio")
    @Pattern(regexp = "[0-9]{14}", message = "O CNPJ possui o formato inválido")
    @Column(length = 14, nullable = false)
    private String cnpj;

    @Size(max = 80)
    private String logotipo;

    @UploadConstraint(acceptedTypes = {FileType.PNG, FileType.JPG}, message = "Extensão do arquivo inválida")
    private transient MultipartFile logotipoFile;

    @Size(max = 80)
    private String banner;

    @UploadConstraint(acceptedTypes = {FileType.PNG, FileType.JPG}, message = "Extensão do arquivo inválida")
    private transient MultipartFile bannerFile;

    @NotNull(message = "Taxa de entrega em branco")
    @Min(0) @Max(99)
    private BigDecimal taxaEntrega;

    @NotNull(message = "Tempo de entrega em branco")
    @Min(0) @Max(120)
    private Integer tempoEntregaBase;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "TBL_RESTAURANTE_CATEGORIA",
        joinColumns = @JoinColumn(name = "id_restaurante"),
        inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    @Size(min = 1, message = "Nenhuma categoria padrão foi selecionada")
    @ToString.Exclude
    private Set<CategoriaRestaurante> categorias = new HashSet<>(0);

    @OneToMany(mappedBy = "restaurante")
    private Set<ItemCardapio> itensCardapio = new HashSet<>(0);

    public void setLogotipoFileName()
    {
        if(this.id == null)
        {
            throw new IllegalStateException("É preciso primeiro gravar o registro");
        }

        this.logotipo = String.format("restaurante-logo-%04d.%s", this.id, FileType.of(logotipoFile.getContentType()).getExtension());
    }

    public void setBannerFileName()
    {
        if(this.id == null)
        {
            throw new IllegalStateException("É preciso primeiro gravar o registro");
        }

        this.banner = String.format("restaurante-banner-%04d.%s", this.id, FileType.of(bannerFile.getContentType()).getExtension());
    }

    public String getCategoriasAsText()
    {
        final Set<String> strings = new LinkedHashSet<>();
        for(final CategoriaRestaurante categoria : categorias)
        {
            strings.add(categoria.getDescricao());
        }

        return StringUtils.concatenate(strings);
    }

    public Integer calcularTempoEntrega(final String cep)
    {
        int soma = 0;
        
        for(final char c : this.getEndereco().getCep().toCharArray())
        {
            final int v = Character.getNumericValue(c);
            soma += v;
        }

        soma /= 2;

        return tempoEntregaBase + soma;
    }
}