package br.com.bluefood.domain.restaurante;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import br.com.bluefood.infrastructure.web.validator.UploadConstraint;
import br.com.bluefood.utils.FileType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * MENU ITEM
 */
@Entity
@Getter @Setter
@SuppressWarnings("serial")
@Table(name = "TBL_ITEM_CARDAPIO")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemCardapio implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nome não pode ser vazio")
    @Size(max = 50)
    private String nome;

    @ManyToOne
    @ToString.Exclude
    @NotNull(message = "Nenhuma categoria padrão foi selecionada")
    private CategoriaRestaurante categoria; 

    @NotBlank(message = "Descrição não pode ser vazio")
    @Size(max = 80)
    private String descricao;

    @Size(max = 50)
    private String imagem;

    @Size(max = 50)
    private String capa;

    @NotNull(message = "O preço não pode ser vazio")
    @Min(0)
    private BigDecimal preco;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_restaurante")
    private Restaurante restaurante;

    @UploadConstraint(acceptedTypes = {FileType.PNG, FileType.JPG}, message = "O arquivo com extensão inválida")
    private transient MultipartFile imagemFile;

    @UploadConstraint(acceptedTypes = {FileType.PNG, FileType.JPG}, message = "O arquivo com extensão inválida")
    private transient MultipartFile capaFile;

    public void setImagemFileName()
    {
        if(this.id == null)
        {
            throw new IllegalStateException("O objeto precisa primeiro ser criado");
        }

        this.imagem = String.format("comida-%04d.%s", this.id, FileType.of(imagemFile.getContentType()).getExtension());
    }

    public void setCapaFileName()
    {
        if(this.id == null)
        {
            throw new IllegalStateException("O objeto precisa primeiro ser criado");
        }

        this.capa = String.format("capa-%04d.%s", this.id, FileType.of(capaFile.getContentType()).getExtension());
    }
}