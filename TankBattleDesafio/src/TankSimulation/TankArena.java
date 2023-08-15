package TankSimulation;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import TankImplementation.Tank;
import javaengine.CFase;
import javaengine.CGerenciadorCentral;
import javaengine.CQuadro;
import javaengine.CSprite;
import javaengine.CVetor2D;


public class TankArena extends CFase
{
	final int QUANTIDADE_BLOCOS = 10;
	final int TAMANHO_QUADRO = 70;
	BlocoCenario[][] matrizBlocos = new BlocoCenario[QUANTIDADE_BLOCOS][QUANTIDADE_BLOCOS];
	CSprite mouse = null;
	Tank tank = null;
	
	TankArena(CGerenciadorCentral pManager)
	{
		super(pManager);
	}
	
	private void executaCaminho() {
		ArrayList<BlocoCenario> caminho =  tank.findPath(matrizBlocos[0][0], matrizBlocos[9][9]);
		for (BlocoCenario bloco: caminho) {
			bloco.imagemBloco.configuraAnimacaoAtual(2);
		}
	}
	
	public void inicializa()
	{
		criaCena();
		criaMouse();
		tank = new Tank(null, matrizBlocos);
	}
	
	private void limpaCaminho() {

		for (int linha = 0; linha < QUANTIDADE_BLOCOS; linha ++) {
			for (int coluna = 0; coluna < QUANTIDADE_BLOCOS; coluna++) { 
				
				if (matrizBlocos[linha][coluna].imagemBloco.getIndiceAnimacaoAtual() == 2) {
					matrizBlocos[linha][coluna].imagemBloco.configuraAnimacaoAtual(1);
				}
			
			}
		}
	}
	
	private void criaCena()
	{
		
		for (int linha = 0; linha < QUANTIDADE_BLOCOS; linha ++) {
			for (int coluna = 0; coluna < QUANTIDADE_BLOCOS; coluna++) { 
				matrizBlocos[linha][coluna] = new BlocoCenario(linha, coluna);
				matrizBlocos[linha][coluna].imagemBloco = criaSpriteCenario();
				matrizBlocos[linha][coluna].imagemBloco.posicao.setXY(TAMANHO_QUADRO * coluna, TAMANHO_QUADRO * linha);
			}
		}
	}
	
	private void criaMouse() {
		CQuadro[] quads0 = new CQuadro[1];
		quads0[0] = new CQuadro();
		quads0[0].setQuadro(0);
				
		//Cria o sprite
		mouse =  this.criaSprite(pGerenciador, null, new CVetor2D(35,35), true);
		mouse.configuraImagemSprite(getClass().getResource("imagens/hand.png"));
		mouse.criaAnimacao(1,true,quads0);
		mouse.configuraAnimacaoAtual(0);
	}
	
	private CSprite criaSpriteCenario() {
		
		CQuadro[] bloco0 = new CQuadro[1];
		bloco0[0] = new CQuadro();
		bloco0[0].setQuadro(0);
		
		CQuadro[] bloco1 = new CQuadro[1];
		bloco1[0] = new CQuadro();
		bloco1[0].setQuadro(1);
		
		CQuadro[] bloco2 = new CQuadro[1];
		bloco2[0] = new CQuadro();
		bloco2[0].setQuadro(2);
		
		CSprite sprite = this.criaSprite(pGerenciador, null, new CVetor2D(70,70), true);
		sprite.configuraImagemSprite(getClass().getResource("imagens/tiles.png"));
		sprite.criaAnimacao(1,true,bloco0);
		sprite.criaAnimacao(1,true,bloco1);
		sprite.criaAnimacao(1,true,bloco2);
		sprite.configuraAnimacaoAtual(1);
		return sprite;
	}
	
	private void atualizaTeclas() {
		if (pGerenciador.gerenciadorDispositivos.teclaJaPressionada(KeyEvent.VK_C)) {
			executaCaminho();
		}
		else if (pGerenciador.gerenciadorDispositivos.teclaJaPressionada(KeyEvent.VK_L)) {
			limpaCaminho();
		}
	}
	
	private void atualizaMouse() {
		mouse.posicao.setXY(this.pGerenciador.gerenciadorDispositivos.getPosicaoMouse().getX() , this.pGerenciador.gerenciadorDispositivos.getPosicaoMouse().getY());
		
		int linha = (int)mouse.posicao.getY() / TAMANHO_QUADRO;
		int coluna = (int)mouse.posicao.getX() / TAMANHO_QUADRO;
		
		if (linha >= QUANTIDADE_BLOCOS || coluna >= QUANTIDADE_BLOCOS) {
			return;
		}
		
		if (pGerenciador.gerenciadorDispositivos.teclaJaPressionada(KeyEvent.VK_P)) {
			matrizBlocos[linha][coluna].imagemBloco.configuraAnimacaoAtual(0);
			imprimeMatrizCustos();
		}
		else if (pGerenciador.gerenciadorDispositivos.teclaJaPressionada(KeyEvent.VK_G)) {
			matrizBlocos[linha][coluna].imagemBloco.configuraAnimacaoAtual(1);
			imprimeMatrizCustos();
		}
	}

	public void executa()
	{
		atualizaMouse();
		atualizaTeclas();
	}
	
	
	public void liberaRecursos()
	{
		super.liberaRecursos();
	}
	
	private void imprimeMatrizCustos() {
		for (int linha = 0; linha < QUANTIDADE_BLOCOS; linha++) {
			for (int coluna = 0; coluna < QUANTIDADE_BLOCOS; coluna++) { 
				System.out.print(matrizBlocos[linha][coluna].retornaCustoBloco() + "    ");
			}
			System.out.println();
		}
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}
}
