package br.com.bluefood.utils;

import me.pagar.model.Transaction;

/**
 * PagarMeUtils
 */
public class PagarMeUtils 
{
    public static String verifyTransactionStatus(Transaction transaction)
    {
        StringBuilder statusPagamento = new StringBuilder();
        switch (transaction.getStatus()) 
        {
            case PROCESSING: 
            {
                statusPagamento.append("Transação sendo processada");
                break;
            }
            case AUTHORIZED: 
            {
                statusPagamento.append("Transação autorizada. Pagamento em processamento");
                break;
            }
            case PAID: 
            {
                statusPagamento.append("Transação autorizada. Pagamento realizado com sucesso.");
                break;
            }
            case REFUNDED:
            {
                statusPagamento.append("Transação estornada.");
                break;
            }
            case WAITING_PAYMENT:
            {
                statusPagamento.append("Transação aguardando pagamento realizado via boleto");
                break;
            }
            case PENDING_REFUND:
            {
                statusPagamento.append("Transação paga com boleto aguardando para ser estornada.");
                break;
            }
            case REFUSED:
            {
                statusPagamento.append("Transação não autorizada.");
                checkResponseCodePayment(transaction.getAcquirerResponseCode(), statusPagamento);
                break;
            }
            case CHARGEDBACK:
            {
                statusPagamento.append("Transação sofreu chargeback.");
                break;
            }
        }

        return statusPagamento.toString();
    }

    private static void checkResponseCodePayment(String responseCode, StringBuilder statusPagamento)
    {
        statusPagamento.append(" ");
        if(responseCode.equals("1001"))
        {
            statusPagamento.append("Cartão vencido");
            return;
        }

        if(responseCode.equals("1004"))
        {
            statusPagamento.append("Cartão com restrição");
            return;
        }

        if(responseCode.equals("1006"))
        {
            statusPagamento.append("Tentativas de senha excedidas");
            return;
        }

        if(responseCode.equals("1007") || responseCode.equals("1008"))
        {
            statusPagamento.append("Rejeitado emissor");
            return;
        }

        if(responseCode.equals("1010"))
        {
            statusPagamento.append("Valor inválido");
            return;
        }

        if(responseCode.equals("1011"))
        {
            statusPagamento.append("Cartão inválido");
            return;
        }

        if(responseCode.equals("1013"))
        {
            statusPagamento.append("Transação não autorizada pelo banco emissor do cartão");
            return;
        }

        if(responseCode.equals("1015"))
        {
            statusPagamento.append("Função não suportada");
            return;
        }

        if(responseCode.equals("1016"))
        {
            statusPagamento.append("Saldo insuﬁciente");
            return;
        }

        if(responseCode.equals("1017"))
        {
            statusPagamento.append("Senha inválida");
            return;
        }

        if(responseCode.equals("1019") || responseCode.equals("1020"))
        {
            statusPagamento.append("Transação não permitida");
            return;
        }

        if(responseCode.equals("1025"))
        {
            statusPagamento.append("Cartão bloqueado");
            return;
        }

        if(responseCode.equals("1027"))
        {
            statusPagamento.append("Excedida a quantidade de transações para o cartão.");
            return;
        }

        if(responseCode.equals("1045"))
        {
            statusPagamento.append("Código de segurança inválido.");
            return;
        }

        if(responseCode.equals("5003") || responseCode.equals("5003"))
        {
            statusPagamento.append("Erro interno.");
            return;
        }

        if(responseCode.equals("9999"))
        {
            statusPagamento.append("Erro não especiﬁcado.");
            return;
        }
    }
}