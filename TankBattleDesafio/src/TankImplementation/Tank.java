package TankImplementation;

import java.util.ArrayList;
import TankSimulation.BlocoCenario;
import javaengine.CSprite;

public class Tank {

    private BlocoCenario[][] matrizBlocos = null;
    public CSprite tank = null;
    private ArrayList<BlocoCenario> openHeinecken = new ArrayList<BlocoCenario>();
    private ArrayList<BlocoCenario> checked = new ArrayList<BlocoCenario>();
    private boolean found = false;
    private ArrayList<BlocoCenario> caminho = new ArrayList<BlocoCenario>();
    private BlocoCenario inicio = null;
    private BlocoCenario fim = null;

    public Tank(CSprite sprite, BlocoCenario[][] matrizBlocos) {
        this.matrizBlocos = matrizBlocos;
        this.tank = sprite;
    }

    public ArrayList<BlocoCenario> findPath(BlocoCenario inicio, BlocoCenario fim) {
        clearPath(); // Limpa o caminho anterior antes de iniciar a busca
		setCostOnNodes(matrizBlocos, inicio, fim);
		finder(inicio, fim);
		return caminho;
    }

    public void clearPath() {
        caminho.clear();
        checked.clear();
        openHeinecken.clear();
        found = false;
    }

    public void updateBlockCosts() {
        if (inicio != null && fim != null) {
            for (int i = 0; i < matrizBlocos.length; i++) {
                for (int j = 0; j < matrizBlocos[i].length; j++) {
                    getCost(matrizBlocos[i][j], inicio, fim);
                }
            }
        }
    }

    public void getCost(BlocoCenario atual, BlocoCenario inicio, BlocoCenario fim) {
        int xDistance = Math.abs(atual.coluna - inicio.coluna);
        int yDistance = Math.abs(atual.linha - inicio.linha);

        int gCost = xDistance + yDistance;

        xDistance = Math.abs(atual.coluna - fim.coluna);
        yDistance = Math.abs(atual.linha - fim.linha);

        int hCost = xDistance + yDistance;

        atual.custoEstimadoParaDestino = hCost + gCost;
    }

    private void setCostOnNodes(BlocoCenario[][] matrizBlocos, BlocoCenario inicio, BlocoCenario fim) {
        for (int i = 0; i < matrizBlocos.length; i++) {
            for (int j = 0; j < matrizBlocos[i].length; j++) {
                getCost(matrizBlocos[i][j], inicio, fim);
            }
        }
    }

    private void finder(BlocoCenario atual, BlocoCenario fim) {
		if (atual.equals(fim)) {
			found = true;
			caminho = montaCaminho(atual);
		} else if (found == false) {
			// Não é necessário chamar clearPath() aqui
			int col = atual.coluna;
			int lin = atual.linha;
			checked.add(atual);
			openHeinecken.remove(atual);
			if (lin + 1 < matrizBlocos.length) {
				openBlock(matrizBlocos[lin + 1][col], atual);
			}
			if (lin - 1 >= 0) {
				openBlock(matrizBlocos[lin - 1][col], atual);
			}
			if (col + 1 < matrizBlocos[0].length) {
				openBlock(matrizBlocos[lin][col + 1], atual);
			}
			if (col - 1 >= 0) {
				openBlock(matrizBlocos[lin][col - 1], atual);
			}
			BlocoCenario melhorOp = null;
			for (BlocoCenario vizinho : openHeinecken) {
				if (!checked.contains(vizinho) && vizinho.retornaCustoBloco() != Integer.MAX_VALUE) {
					if (melhorOp == null || vizinho.retornaCustoTotal() < melhorOp.retornaCustoTotal()) {
						melhorOp = vizinho;
					}
				}
			}
			if (melhorOp != null) {
				finder(melhorOp, fim);
			}
		}
	}
	

    private ArrayList<BlocoCenario> montaCaminho(BlocoCenario atual) {
        ArrayList<BlocoCenario> caminho = new ArrayList<BlocoCenario>();
        while (atual.anterior != null) {
            caminho.add(atual);
            atual = atual.anterior;
        }
        return caminho;
    }

    private void openBlock(BlocoCenario b, BlocoCenario ant) {
        if (!checked.contains(b) && !openHeinecken.contains(b) && b.retornaCustoBloco() != Integer.MAX_VALUE) {
            b.anterior = ant;
            openHeinecken.add(b);
        }
    }
}
