package br.com.pwatraining;

/*==================================================================================
********************* Projeto Sistema Carrinho de Compras Com ICMS  ****************
************************Projeto Conceitual e Educativo *****************************
************************************************************************************
***************** Autor: Anderson Fabio da Silva - PWA TRAINING ********************
*##################################################################################*
* Aprendizado : Conceitos de Sistema Comercial Utilizando 8 Comandos               *
* Data        : 06/01/2022                                                         *
* Site        : http://www.pwatraining.com.br                                      *
* Facebook    : https://www.facebook.com/ead.pwatraining                           *
* Instagram   : https://www.instagram.com/pwatraining                              *
* Youtube     : https://www.youtube.com/@pwatraining                               *
* WhatsApp    : https://wa.me/5513991119722                                        *
* Sala de Aula: https://classroom.google.com/c/NTQwOTk0Mzc3NDM5?cjc=i2nmfim        *
* 																				   *
* Autorizado a Copia e Distribuicao Mantendo Este Quadro Informativo               *
*==================================================================================*/

//Inicio da Importacao de Recursos
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
//Fim da Importação de Recursos


public class Principal {

	public static void main(String[] args) {
		
		//   Desenvolver um carrinho de compras com calculo de icms
		//   Entidades: 
		// 
		//   Modelo Relatorio de Saida
		//
		//   ====================================================================
		//   Produto                       QTD       PU        ICMS      SUBTOTAL
		//   ====================================================================
		//   Boné Crocodilo                1         289.0     18.0      341.0     
		//   Camiseta Crocodilo            1         199.0     16.0      230.8     
		//   ====================================================================
		//   TOTAL: 571.8     
		//   ====================================================================
		
		
		// Inicio do seu espaco de trabalho
		
		Coste lacoste = new Coste();

		lacoste.incluirNcm("111", 18d);
		lacoste.incluirNcm("222", 16d);
		
		lacoste.incluirProduto(1l, "Bone Crocodilo", "111", 289d);
		lacoste.incluirProduto(2l, "Camiseta Crocodilo", "222",199d);
		
		List<ItemCompra> carrinhoCompras = new ArrayList<ItemCompra>();

		int quantidadeItens = lacoste.digitarQuantidadeItens();

       	carrinhoCompras = lacoste.digitarItens(quantidadeItens);
		
		lacoste.imprimirCompra(carrinhoCompras);
		
		// Final do seu espaço de trabalho
	}
}

class Coste {
	
	private HashMap<String,Ncm> mapNcm = new HashMap<String,Ncm>();
	private HashMap<Long, Produto> mapProduto = new HashMap<Long, Produto>();
	private Scanner digitar = new Scanner(System.in);
	
	

	
	// *************************************************************** FERRAMENTAS
	
	public int digitarInteiro() {
		digitar = new Scanner(System.in);
		return digitar.nextInt();
	}

	public double digitarDecimal() {
		digitar = new Scanner(System.in);
		return digitar.nextDouble();
	}

	public String digitarTexto() {
		digitar = new Scanner(System.in);
		return digitar.nextLine();
	}
	
	public Long digitarLong() {
		digitar = new Scanner(System.in);
		return digitar.nextLong();
	}
    
	public void imprimir(String mensagem, boolean pularLinha) {
		if(pularLinha) {
			System.out.println(mensagem);
		} else {
			System.out.print(mensagem);
		}
	}
	
	public void pularLinha() {
		System.out.println("");
	}
    
    public String ajusteTamanho(String texto, int tamanho) {
    	String novoTexto = "";
    	if(texto.contains(".")) {
    		texto = texto.substring(0,texto.indexOf(".")+2);
    	}
    	for(int i=0;i<tamanho;i++) {
    		if(texto.length() > i ) {
    			novoTexto = novoTexto + texto.substring(i,i+1);
    		} else {
    			novoTexto = novoTexto + " ";
    		}
    	}
    	return novoTexto;
    }
	
	//*********************************************************************** ITEM COMPRA
	public List<ItemCompra> digitarItens(int quantidadeItens) {
		List<ItemCompra> listaItemCompra = new ArrayList<ItemCompra>();
		
		for(int i=1; i <= quantidadeItens; i++) {
			
			
			pularLinha();
			
			ItemCompra item = new ItemCompra();
		
			imprimir("Produto-"+i+" Informe o Codigo do Produto: ",false);
			while(true) {
				Long idProduto = digitarLong();
				Produto produto = getProduto(idProduto);
				if(produto != null) {
					item.setIdProduto(idProduto);
					imprimir(produto.getDescricao(),true);
					pularLinha();
					break;
				} else {
					pularLinha();
					imprimir("Produto-"+i+" Informe o Codigo do Produto: ",false);
				}
			}
			
			imprimir("Produto-"+i+" Informe a Quantidade: ",false);
			item.setQuantidade(digitarLong());
			pularLinha();
			listaItemCompra.add(item);
		}
		return listaItemCompra;
	}
	
	public int digitarQuantidadeItens() {
		imprimir("Informe a Quantidade de Produtos: ",false);
		int quantidadeItens = digitarInteiro();
		pularLinha();
		return quantidadeItens;
	}
	
	// ***************************************************************************************** COMPRA
	
	public void imprimirCompra(List<ItemCompra> compra) {
    	imprimir("====================================================================",true);
    	imprimir("Produto                       QTD       PU        ICMS      SUBTOTAL",true);
    	imprimir("====================================================================",true);

    	Double total = 0d;

		for(ItemCompra item : compra) {
			Produto produto = getProduto(item.getIdProduto());
			Ncm ncm = getNcm(produto.getNcm());
			
		    imprimir(ajusteTamanho(produto.getDescricao(),30),false);
		    imprimir(ajusteTamanho(item.getQuantidade().toString(),10).toString(),false);
		    imprimir(ajusteTamanho(produto.getPreco().toString(),10),false);
		    imprimir(ajusteTamanho(ncm.getPercentualIcms().toString(),10),false);
		    imprimir(ajusteTamanho(getValorProduto(item).toString(),10),false);
		    total = total + getValorProduto(item);
		    pularLinha();
		}
	    imprimir("====================================================================",true);
		imprimir("TOTAL: "+ajusteTamanho(total.toString(),10),true);
	    imprimir("====================================================================",true);
    }
	
	//*************************************************************************************** NCM
	
	public void incluirNcm(String ncmNovo, Double percentualIcms) {
		Ncm ncm = new Ncm();
		ncm.setNcm(ncmNovo);
		ncm.setPercentualIcms(percentualIcms);
		
		mapNcm.put(ncm.getNcm(), ncm);
	}
	
	public Ncm getNcm(String ncm) {
		return mapNcm.get(ncm);
	}
	//*************************************************************************************** PRODUTO
	public Double getValorProduto(ItemCompra itemCompra) {
		Produto produto = getProduto(itemCompra.getIdProduto());
		Ncm ncm = getNcm(produto.getNcm());
		return ((itemCompra.getQuantidade()*produto.getPreco())*(ncm.getPercentualIcms()/100+1));
	}
	
	public void incluirProduto(Long id, String descricao, String ncm, Double preco) {
		Produto produto = new Produto();
		produto.setId(id);
		produto.setDescricao(descricao);
		produto.setNcm(ncm);
		produto.setPreco(preco);
		
		mapProduto.put(produto.getId(), produto);
	}

	
	public Produto getProduto(Long idProduto) {
		if(mapProduto.get(idProduto) == null) {
			imprimir("Produto Nao Encontrado !",true);
		} 
		return mapProduto.get(idProduto);
	}
}

class Produto {
	
	private Long id;
	private String descricao;
	private String ncm;
	private Double preco;

	public Double getPreco() {
		return preco;
	}
	public void setPreco(Double preco) {
		this.preco = preco;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getNcm() {
		return ncm;
	}
	public void setNcm(String ncm) {
		this.ncm = ncm;
	}
	
}

class Ncm {
	private String ncm;
	private Double percentualIcms;
	
    public String getNcm() {
		return ncm;
	}
	public void setNcm(String ncm) {
		this.ncm = ncm;
	}
	public Double getPercentualIcms() {
		return percentualIcms;
	}
	public void setPercentualIcms(Double percentualIcms) {
		this.percentualIcms = percentualIcms;
	}
}

class ItemCompra {
  
	private Long idProduto;
  	private Long quantidade;
  
	public Long getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}
	public Long getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}
}

/*
* Teoria
* 
* Identacao
* ;
* {}
* Comentarios
* 
* 
* */

/*  
	FERRAMENTAS
	
	1 - Como armazeno uma informacao na memoria
		1 - Para textos		==> String 	nomeDesejado = "Conteudo Desejado";
		2 - Para valores	==>	Float	nomeDesejado = 9f;
	 	
	2 - Como imprimir uma mensagem
		System.out.print("Mensagem a Ser Impressa");  

	3 - Como Solicitar Informacao ao Usuario
		1 - Importar Recurso  			==> import java.util.Scanner;
		2 - Criar Recurso	  			==> Scanner input = new Scanner(System.in);
		3 - Informar o Recurso Desejado	==>	System.out.print("Informe seu Nome: ");
		4 - Acionar Recurso				==> String nome = input.nextLine();
		5 - Destruir Recurso			==> input.close();

	4 - Como crio uma lista de informacoes
	
	    USANDO LIST
	    
		1 - Importar Recurso			==> import java.util.ArrayList;
		2 - Importar Recurso			==>	import java.util.List;
		3 - Criar Recurso				==> List<TipodeInformacao> nomeLista = new ArrayList<TipoInformacao>();
		4 - Adicionar Valores a Lista	==> nomeLista.add("Valor Desejado");
		
		USANDO MAP
		
		1 - Importar Recurso           ==>  import java.util.HashMap;
		2 - Criar Recurso			   ==> HashMap<Chave,Objeto> nomeMap = new HashMap<Chave,Objeto>();
		3 - Adicionar Objeto na Lista  ==> nomeMap.put(Objeto);
		4 - Localizar Objeto Por Chave ==> objeto = nomeMap.get(chave);

	5 - Condicional
		int x = 10;
		if(x == 10) {
			System.out.print("Igual a 10");
		} else {
			System.out.print("Diferente de 10");
		}
		
		Em portugues ficaria
		
		se(x == 10) {
			System.out.print("Igual a 10");
		} senao {
			System.out.print("Diferente de 10");
		}


	6 - Como construo um bloco de repeticao
	
		POR LISTA
		
		1 - Informar Inicio da Repeticao==> for(TipoLista nomeItem : nomeLista)
		2 - Exemplo imprimir uma lista:
		for(String item : lista) {
			System.out.print(item);
		}
		
		POR INCREMENTO
		1 - Informar os parametros, inicio, termino e avanco
		  Ex: for(int i = 1; i <= 10; i++) {
				System.out.print("Estou na posicao: "+i);
		  }
		  
		INDEFINIDO
		
		int item = 1;
		while(true) {
			if(item > 10) {
				break;
			} 
			System.out.print("Continuo na repeticao");	
		}
		
	7 - Criando uma Classe 
	
		class NomeClasse {
		
			// Atributos sempre private para gerar encapsulamento
			
			//private tipo nomeAtributo;
			
			//EX:
	
			private Long id;
			private String nome;
			
			// Atributos privados precisam de metodos GET SET Publicos para manipular os valores do lado externo.
			// EX:
			
			public Long getId() {
				return id;
			}
	
			public void setId(Long id) {
				this.id = id;
			}
		
			public String getNome() {
				return this.nome;
			}
	
			public void setNome(String nome) {
				this.nome = nome;
			}
		}
	
	Instanciando (Utilizando) uma classe ==> Criar um objeto a partir de uma classe.
	
		NomeClasse nomeObjeto = new NomeClasse();	// Criou
		
		nomeObjeto.setNome("Anderson");				// Setou valores nos atributos internos privados com metodo publico
		
		System.out.print(nomeObjeto.getNome());		// Recuperou valores dos atributos e apresentou no console
	

	8 - Criando um metodo
	
		public retorno nomeMetodo(Tipo nomeParametro) {
			return nomeParametro;
		}
		
		Chamando este método
		
		String meuNome = nomeMetodo("Anderson");
		
		meuNome passa a ser Anderson, pois passe como parametro e o método retornou o proprio parametro.
		
		Ex Metodo:
		
		public String retornarNome(String nome, String sobreNome) {
		
			return nome+" "+sobreNome;
		}
		
		Uso:
		
		String meuNome = retornarNome("Anderson","Silva");
		
		System.out.print(meuNome);
		
		// Imprime Anderson Silva
	
	

*/



