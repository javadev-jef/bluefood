package br.com.bluefood.application.test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.bluefood.domain.cliente.Cliente;
import br.com.bluefood.domain.cliente.ClienteRepository;
import br.com.bluefood.domain.estados.EstadosRepository;
import br.com.bluefood.domain.endereco.EnderecoRepository;
import br.com.bluefood.domain.estados.EstadosBrasileiros;
import br.com.bluefood.domain.pedido.Pedido;
import br.com.bluefood.domain.pedido.PedidoRepository;
import br.com.bluefood.domain.pedido.Pedido.Status;
import br.com.bluefood.domain.restaurante.CategoriaRepository;
import br.com.bluefood.domain.restaurante.CategoriaRestaurante;
import br.com.bluefood.domain.restaurante.ItemCardapio;
import br.com.bluefood.domain.restaurante.ItemCardapioRepository;
import br.com.bluefood.domain.restaurante.Restaurante;
import br.com.bluefood.domain.restaurante.RestauranteRepository;
import br.com.bluefood.utils.StringUtils;

/**
 * INSERT DATA FOR TEST
 */
@Component
public class InsertDataForTesting 
{
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private EstadosRepository estadosRepository;

    @Autowired
	private ItemCardapioRepository itemCardapioRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;

	// TRUE/FALSE INSERT DATA
    @EventListener(condition = "true")
    public void onApplicationEvevnt(ContextRefreshedEvent event)
    {
        Cliente[] clientes = clientes();
        Restaurante[] restaurantes = restaurantes();
		itensCardapio(restaurantes);
		
		Pedido p = new Pedido();
		p.setData(LocalDateTime.now());
		p.setCliente(clientes[0]);
		p.setRestaurante(restaurantes[0]);
		p.setStatus(Status.Producao);
		p.setSubtotal(BigDecimal.valueOf(10));
		p.setTaxaEntrega(BigDecimal.valueOf(2));
		p.setTotal(BigDecimal.valueOf(12));
		pedidoRepository.save(p);
    }

    private Restaurante[] restaurantes() 
    {
		List<Restaurante> restaurantes = new ArrayList<>(); 
		
		CategoriaRestaurante categoriaPizza = categoriaRepository.findById(2).orElseThrow();
		CategoriaRestaurante categoriaSalada = categoriaRepository.findById(5).orElseThrow();
		CategoriaRestaurante categoriaSobremesa = categoriaRepository.findById(14).orElseThrow();
		CategoriaRestaurante categoriaJaponesa = categoriaRepository.findById(1).orElseThrow();
		CategoriaRestaurante categoriaHamburger = categoriaRepository.findById(4).orElseThrow();
		CategoriaRestaurante categoriaSorvete = categoriaRepository.findById(7).orElseThrow();
		CategoriaRestaurante categoriaBebida = categoriaRepository.findById(6).orElseThrow();
		CategoriaRestaurante categoriaSanduiche = categoriaRepository.findById(15).orElseThrow();
		CategoriaRestaurante categoriaAcai = categoriaRepository.findById(10).orElseThrow();
        
        EstadosBrasileiros estado01 = estadosRepository.findById(1).orElseThrow();
        EstadosBrasileiros estado02 = estadosRepository.findById(2).orElseThrow();
        EstadosBrasileiros estado03 = estadosRepository.findById(5).orElseThrow();
        EstadosBrasileiros estado04 = estadosRepository.findById(10).orElseThrow();
		
		Restaurante r = new Restaurante();
		r.setRazao("Bubger King");
		r.setCnpj("16480763000117");
		r.setEmail("r1@bluefood.com.br");
        r.setTelefone("31988888888");
        r.getEndereco().setLogradouro("Rua Trinta");
		r.getEndereco().setBairro("Centro");
		r.getEndereco().setCidade("Ipatinga");
        r.getEndereco().setCep("38300084");
        r.getEndereco().setNumero("154C");
        r.getEndereco().setUf(estado01);
		r.setSenha(StringUtils.encrypt("r"));
		r.setTaxaEntrega(BigDecimal.valueOf(3.2));
		r.getCategorias().add(categoriaHamburger);
		r.getCategorias().add(categoriaSorvete);
		r.setLogotipo("0001-logo.png");
		r.setBanner("0001-banner.jpg");
        r.setTempoEntregaBase(30);
        enderecoRepository.save(r.getEndereco());
		restauranteRepository.save(r);
		restaurantes.add(r);
		
		r = new Restaurante();
		r.setRazao("Mc Naldo's");
		r.setEmail("r2@bluefood.com.br");
		r.setSenha(StringUtils.encrypt("r"));
		r.setCnpj("61634781000160");
		r.setTaxaEntrega(BigDecimal.valueOf(4.5));
        r.setTelefone("33977777777");
        r.getEndereco().setLogradouro("Rua Zeca Gusmão");
		r.getEndereco().setBairro("Matinha");
		r.getEndereco().setCidade("Cel. Fabriciano");
        r.getEndereco().setCep("39801398");
        r.getEndereco().setNumero("154M");
        r.getEndereco().setUf(estado02);
		r.getCategorias().add(categoriaBebida);
		r.getCategorias().add(categoriaSobremesa);
		r.setLogotipo("0002-logo.png");
		r.setBanner("0002-banner.jpg");
        r.setTempoEntregaBase(25);
        enderecoRepository.save(r.getEndereco());
		restauranteRepository.save(r);
		restaurantes.add(r);

		r = new Restaurante();
		r.setRazao("Bar e Lanchonete Ipatinga");
		r.setEmail("ipa@bluefood.com.br");
		r.setSenha(StringUtils.encrypt("r"));
		r.setCnpj("94302579000190");
		r.setTaxaEntrega(BigDecimal.valueOf(0.000));
        r.setTelefone("3155555555");
        r.getEndereco().setLogradouro("Rua dos Salmos");
		r.getEndereco().setBairro("Canaã");
		r.getEndereco().setCidade("Ipatinga");
        r.getEndereco().setCep("35164208");
        r.getEndereco().setNumero("257");
        r.getEndereco().setUf(estado02);
		r.getCategorias().add(categoriaSanduiche);
		r.getCategorias().add(categoriaHamburger);
		r.setLogotipo("0006-logo.png");
        r.setTempoEntregaBase(30);
        enderecoRepository.save(r.getEndereco());
		restauranteRepository.save(r);
		restaurantes.add(r);
		
		r = new Restaurante();
		r.setRazao("Sbubby");
		r.setEmail("r3@bluefood.com.br");
		r.setSenha(StringUtils.encrypt("r"));
		r.setCnpj("40501464000104");
		r.setTaxaEntrega(BigDecimal.valueOf(12.2));
        r.setTelefone("3133333333");
        r.getEndereco().setLogradouro("Av. José Gualberto do Nascimento");
		r.getEndereco().setBairro("Conjunto Habitacional Vale do Jatobá (Barreiro)");
		r.getEndereco().setCidade("Ipaba");
        r.getEndereco().setCep("30666140");
        r.getEndereco().setNumero("154S");
        r.getEndereco().setUf(estado03);
		r.getCategorias().add(categoriaSanduiche);
		r.getCategorias().add(categoriaSalada);
		r.setLogotipo("0003-logo.png");
		r.setBanner("0003-banner.jpg");
        r.setTempoEntregaBase(38);
        enderecoRepository.save(r.getEndereco());
		restauranteRepository.save(r);
		restaurantes.add(r);
		
		r = new Restaurante();
		r.setRazao("Pizza Brut");
		r.setEmail("r4@bluefood.com.br");
		r.setSenha(StringUtils.encrypt("r"));
		r.setCnpj("24446467000128");
		r.setTaxaEntrega(BigDecimal.valueOf(9.8));
        r.setTelefone("3122222222");
        r.getEndereco().setLogradouro("Passarela Samuel Moura");
		r.getEndereco().setBairro("Centro");
		r.getEndereco().setCidade("Ipatinga");
        r.getEndereco().setCep("37500056");
        r.getEndereco().setNumero("93");
        r.getEndereco().setUf(estado04);
		r.getCategorias().add(categoriaPizza);
		r.getCategorias().add(categoriaAcai);
		r.setLogotipo("0004-logo.png");
		r.setBanner("0004-banner.jpg");
        r.setTempoEntregaBase(22);
        enderecoRepository.save(r.getEndereco());
		restauranteRepository.save(r);
		restaurantes.add(r);
		
		r = new Restaurante();
		r.setRazao("Wiki Japa");
		r.setEmail("r5@bluefood.com.br");
		r.setSenha(StringUtils.encrypt("r"));
		r.setCnpj("73204514000132");
		r.setTaxaEntrega(BigDecimal.valueOf(14.9));
        r.setTelefone("31984654215");
        r.getEndereco().setLogradouro("Rua Sucupira");
		r.getEndereco().setBairro("Vila Alecrim");
		r.getEndereco().setCidade("São Paulo");
        r.getEndereco().setCep("65605663");
        r.getEndereco().setNumero("154W");
        r.getEndereco().setUf(estado03);
		r.getCategorias().add(categoriaJaponesa);
		r.setLogotipo("0005-logo.png");
        r.setTempoEntregaBase(19);
        enderecoRepository.save(r.getEndereco());
		restauranteRepository.save(r);
		restaurantes.add(r);
		
		Restaurante[] array = new Restaurante[restaurantes.size()]; 
		return restaurantes.toArray(array);
    }
    
    private Cliente[] clientes() 
    {
        EstadosBrasileiros estado01 = estadosRepository.findById(10).orElseThrow();
        EstadosBrasileiros estado02 = estadosRepository.findById(15).orElseThrow();

		List<Cliente> clientes = new ArrayList<>(); 
		
		Cliente c = new Cliente();
		c.setNome("João Silva");
		c.setEmail("joao@bluefood.com.br");
		c.setSenha(StringUtils.encrypt("c"));
		c.setCpf("68605806079");
        c.setTelefone("31978541235");
        c.getEndereco().setLogradouro("Rua Via Pedestre");
		c.getEndereco().setBairro("Parque das Águas");
		c.getEndereco().setCidade("Ipatinga");
        c.getEndereco().setCep("35164636");
        c.getEndereco().setNumero("1504");
        c.getEndereco().setUf(estado01);
        clientes.add(c);
        enderecoRepository.save(c.getEndereco());
		clienteRepository.save(c);
		
		c = new Cliente();
		c.setNome("Maria Torres");
		c.setEmail("maria@bluefood.com.br");
		c.setSenha(StringUtils.encrypt("c"));
		c.getEndereco().setLogradouro("Rua Vanderlúcio Luiz da Silva");
		c.getEndereco().setBairro("Vila Real (Justinópolis)");
		c.getEndereco().setCidade("São Paulo");
        c.getEndereco().setCep("33940160");
        c.getEndereco().setNumero("1148");
        c.getEndereco().setUf(estado02);
		c.setCpf("49953671010");
		c.setTelefone("33996754284");
        clientes.add(c);
        enderecoRepository.save(c.getEndereco());
		clienteRepository.save(c);
		
		Cliente[] array = new Cliente[clientes.size()]; 
		return clientes.toArray(array);
    }
    
	private void itensCardapio(Restaurante[] restaurantes) 
	{

		CategoriaRestaurante categoriaPizza = categoriaRepository.findById(2).orElseThrow();
		CategoriaRestaurante categoriaSalada = categoriaRepository.findById(5).orElseThrow();
		CategoriaRestaurante categoriaSobremesa = categoriaRepository.findById(14).orElseThrow();
		CategoriaRestaurante categoriaJaponesa = categoriaRepository.findById(1).orElseThrow();
		CategoriaRestaurante categoriaHamburger = categoriaRepository.findById(4).orElseThrow();
		CategoriaRestaurante categoriaSorvete = categoriaRepository.findById(7).orElseThrow();
		CategoriaRestaurante categoriaBebida = categoriaRepository.findById(6).orElseThrow();
		CategoriaRestaurante categoriaSanduiche = categoriaRepository.findById(15).orElseThrow();
		CategoriaRestaurante categoriaAcai = categoriaRepository.findById(10).orElseThrow();

		ItemCardapio ic = new ItemCardapio();
		ic.setCategoria(categoriaHamburger);
		ic.setDescricao("Delicioso hamburger com molho");
		ic.setNome("Double Cheese Burger Special");
		ic.setPreco(BigDecimal.valueOf(23.8));
		ic.setRestaurante(restaurantes[0]);
		ic.setImagem("comida-0001.jpg");
		itemCardapioRepository.save(ic);
		
		ic = new ItemCardapio();
		ic.setCategoria(categoriaSorvete);
		ic.setDescricao("Soverte geladinho e com a melhor qualidade");
		ic.setNome("Sundae Chocolate Ice");
		ic.setPreco(BigDecimal.valueOf(17.8));
		ic.setRestaurante(restaurantes[0]);
		ic.setImagem("comida-0002.jpg");
		itemCardapioRepository.save(ic);
		
		ic = new ItemCardapio();
		ic.setCategoria(categoriaBebida);
		ic.setDescricao("Refrigerante com gás");
		ic.setNome("Refrigerante Tradicional");
		ic.setPreco(BigDecimal.valueOf(9));
		ic.setRestaurante(restaurantes[1]);
		ic.setImagem("comida-0003.jpg");
		itemCardapioRepository.save(ic);
		
		ic = new ItemCardapio();
		ic.setCategoria(categoriaBebida);
		ic.setDescricao("Suco natural e docinho");
		ic.setNome("Suco de Laranja");
		ic.setPreco(BigDecimal.valueOf(9));
		ic.setRestaurante(restaurantes[1]);
		ic.setImagem("comida-0004.jpg");
		itemCardapioRepository.save(ic);

		ic = new ItemCardapio();
		ic.setCategoria(categoriaSobremesa);
		ic.setDescricao("Sorvete 2 bolas, sem cobertura");
		ic.setNome("Sorvete de Chocolate");
		ic.setPreco(BigDecimal.valueOf(3.6));
		ic.setRestaurante(restaurantes[1]);
		ic.setImagem("comida-0005.jpg");
		itemCardapioRepository.save(ic);

		ic = new ItemCardapio();
		ic.setCategoria(categoriaSanduiche);
		ic.setDescricao("Sanduíche natural com peito de peru");
		ic.setNome("Sanduíche Natural da Casa");
		ic.setPreco(BigDecimal.valueOf(11.8));
		ic.setRestaurante(restaurantes[2]);
		ic.setImagem("comida-0006.jpg");
		itemCardapioRepository.save(ic);

		ic = new ItemCardapio();
		ic.setCategoria(categoriaHamburger);
		ic.setDescricao("Hamburger simples sem salada");
		ic.setNome("Hamburger comun");
		ic.setPreco(BigDecimal.valueOf(4.75));
		ic.setRestaurante(restaurantes[2]);
		ic.setImagem("comida-0007.jpg");
		itemCardapioRepository.save(ic);

		ic = new ItemCardapio(); 
		ic.setCategoria(categoriaSanduiche);
		ic.setDescricao("Sanduíche stacker com cheddar e muito mais");
		ic.setNome("Sanduíche Hoper Stacker");
		ic.setPreco(BigDecimal.valueOf(14.9));
		ic.setRestaurante(restaurantes[3]);
		ic.setImagem("comida-0008.jpg");
		itemCardapioRepository.save(ic);

		ic = new ItemCardapio();
		ic.setCategoria(categoriaSalada);
		ic.setDescricao("Salada levíssima, totalmente natural e sem agrotóxicos");
		ic.setNome("Salada Mista");
		ic.setPreco(BigDecimal.valueOf(13.29));
		ic.setRestaurante(restaurantes[3]);
		ic.setImagem("comida-0009.jpg");
		itemCardapioRepository.save(ic);
		
		ic = new ItemCardapio();
		ic.setCategoria(categoriaPizza);
		ic.setDescricao("Pizza saborosa com cebola");
		ic.setNome("Pizza de Calabresa");
		ic.setPreco(BigDecimal.valueOf(38.9));
		ic.setRestaurante(restaurantes[4]);
		ic.setImagem("comida-0010.jpg");
		itemCardapioRepository.save(ic);

		ic = new ItemCardapio();
		ic.setCategoria(categoriaAcai);
		ic.setDescricao("Açai com banana, morango, leite em pó, e muito mais");
		ic.setNome("Açai Twist");
		ic.setPreco(BigDecimal.valueOf(14.99));
		ic.setRestaurante(restaurantes[4]);
		ic.setImagem("comida-0011.jpg");
		itemCardapioRepository.save(ic);
		
		ic = new ItemCardapio();
		ic.setCategoria(categoriaJaponesa);
		ic.setDescricao("Delicioso Uramaki tradicional");
		ic.setNome("Uramaki");
		ic.setPreco(BigDecimal.valueOf(16.8));
		ic.setRestaurante(restaurantes[5]);
		ic.setImagem("comida-0012.jpg");
		itemCardapioRepository.save(ic);
	}
}