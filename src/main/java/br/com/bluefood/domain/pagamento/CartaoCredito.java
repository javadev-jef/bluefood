package br.com.bluefood.domain.pagamento;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.CreditCardNumber;

import lombok.Getter;
import lombok.Setter;

/**
 * CREDIT CARD MODEL
 */
@Getter @Setter
public class CartaoCredito
{
    @Getter
    public enum FormaPagamento
    {
        Avista("A Vista");
        String legenda;

        FormaPagamento(String legenda)
        {
            this.legenda = legenda;
        }
    }

    @NotNull(message = "Forma de pagamento vazio")
    private FormaPagamento formaPagamento;

    @NotBlank(message = "Nome do cartão vázio")
    private String nomeCartao;

    @CreditCardNumber(message = "O número de cartão informado não é válido")
    @NotBlank(message = "Número do cartão vázio")
    private String numero;

    @NotBlank(message = "Mês de vencimento do cartão vázio")
    private String mes;

    @NotBlank(message = "Ano de vencimento do cartão vázio")
    private String ano; 

    @NotNull(message = "Código de segurança vázio")
    private Integer codigoSeguranca;

    @NotBlank(message = "CPF/CNPJ vázio")
    private String cpfCnpj;
}