package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;

import NetworkConstruction.NeuralNet;
import geom.Rectangle;

public class GeneticAlgorithm {
	public double mutateRate;
	private int bestPopIndex;
	private ArrayList<Rectangle> squares;
	public int maxPop;
	public int numTopPop;
	private int scaleFactor = 200;
	private int bestIndex;
	public float bestFitness;
	public double gene;
	//ArrayList<Rectangle> winnerArr;
	private float width;
	private float height;
	private int generation;


	public GeneticAlgorithm(int maxUnits,int topPerformingUnits, ArrayList<Rectangle> squares2){
		this.squares = squares2;
		maxPop = maxUnits;
		numTopPop = topPerformingUnits;
		mutateRate = 1;
		if(maxPop < numTopPop){
			numTopPop = maxPop;
		}
	}
	public void clearGeneticAlgorithm(ArrayList<Rectangle> squares){
		this.squares = squares;
		mutateRate = 1;

		bestPopIndex = -1;
		bestFitness = -1;
	}
	public ArrayList<Rectangle> createNewPopulation(float width, float height, int generation, int populationSize){
		this.width = width;
		this.height = height;
		generation = this.generation;
		ArrayList <Rectangle> arrPop = new ArrayList<Rectangle>();
		for (int i = 0; i < populationSize; i++) {
			arrPop.add(new Rectangle(width / 2, height - 15, 15, 15, width, height, Color.magenta, "Blue", generation,
					new NeuralNet(), 1));
		}
		return arrPop;
	}
	public ArrayList<Rectangle> createMutatedPopulation(ArrayList<Rectangle> population){
		
		return EvolvePop(population);
	}
	
	public ArrayList<Rectangle> EvolvePop(ArrayList<Rectangle> population){
		//ArrayList<Rectangle> winners = selection(population);
		ArrayList<Rectangle> winners = new ArrayList<>();
		
		if(population.get(population.size() - 1).getFitness() < 250){
			//System.out.println("TEST");
			createNewPopulation(width, height, generation, population.size());// If the best unit from the initial population has a negative fitness ;// If the best unit from the initial population has a negative fitness 
		}
		//System.out.println(population.get(population.size() - 1).getFitness());
		else {
			setMutateRate(.2);
		}
		for(int i = 0; i < population.size() - 1; i++){
			Rectangle mom; Rectangle dad; Rectangle offspring;
			Random rand = new Random();
			
			if (population.get(i).getFitness() < 250) {
				population.get(i).setNet(new NeuralNet());
				offspring = new Rectangle(population.get(i));
				offspring.setName("GoodBoi1");
				//offspring = mutation(offspring);
			}
			
			if(i > getNumTopPop() ) {
				 mom = population.get(population.size() - 1);
				 dad = population.get(population.size() - 2);
				 offspring = new Rectangle(crossOver(mom,dad));
				 offspring.setName("GoodBoi2");
				 //offspring = mutation(offspring);
				 System.out.println(offspring.getNet().getTotalOutput());
			}
			else if (i == getNumTopPop() ) {
				offspring = new Rectangle(population.get(population.size() - 1));
				offspring.setName("GoodestBoi");
				//offspring = mutation(offspring);
				System.out.println(offspring.getNet().getTotalOutput());
			}
			else //if(i < getMaxPop() - 2){
			{
				 mom = population.get(population.size() - 1) ;
				 dad = population.get(rand.nextInt(population.size()));
				 offspring = new Rectangle(crossOver(mom,dad));
				 offspring.setName("GoodBoi4");
				 offspring = mutation(offspring);
				 System.out.println(offspring.getNet().getTotalOutput());
			}
//			else{
//				 offspring = winners.get(rand.nextInt(winners.size()-1));
//			}
			
			//offspring = mutation(offspring);
			
			//Rectangle a = new Rectangle(offspring);
			//a.setIndex(i);
			winners.add(offspring);
			
			//population.set(i, a);
		}
		for (Rectangle r : winners) {
			r.setFitness(0);
		}
		//System.out.println(winners);
//		if(winners.get(0).getFitness() > bestFitness){
//			bestFitness = winners.get(0).getFitness();
//		});
		return winners;
	}
	
	public ArrayList<Rectangle> sortFitness(ArrayList<Rectangle> preSort){
		ArrayList<Rectangle> postSort = preSort;
		
		for(int i = 0; i < postSort.size() - 1; i++){
			int min = i;
			for(int x = i + 1; x < postSort.size(); x++){
				if(postSort.get(x).getFitness() < postSort.get(min).getFitness()){
					min = x;
				}
			}
			Rectangle temp = postSort.get(min);
			postSort.set(min, postSort.get(i));
			postSort.set(i,temp);
			
		}
		return postSort;
	}
	public List<Rectangle> sortIndex(List<Rectangle> squares2){
		List<Rectangle> postSort = squares2;
		
		for(int i = 0; i < postSort.size() - 1; i++){
			int min = i;
			for(int x = i + 1; x < postSort.size(); x++){
				if(postSort.get(x).getIndex() < postSort.get(min).getIndex()){
					min = x;
				}
			}
			Rectangle temp = postSort.get(min);
			postSort.set(min, postSort.get(i));
			postSort.set(i,temp);
			
		}
		return postSort;
	}
	
	public Rectangle crossOver(Rectangle mom, Rectangle dad) {
	    Random rand = new Random();
		 int neuronStartingPoint = rand.nextInt(mom.getNet().getAllNeurons().size() - 1);
		 for (int i = neuronStartingPoint; i < mom.getNet().getAllNeurons().size(); i++) {
			 double biasFromMom = mom.getNet().getAllNeurons().get(i).getBias();
			 mom.getNet().getAllNeurons().get(i).setBias(dad.getNet().getAllNeurons().get(i).getBias());
			 dad.getNet().getAllNeurons().get(i).setBias(biasFromMom);
		 }
		 Random rand2 = new Random();
		 //System.out.println("crossOver Test: " + mom + " " + dad);
		 return rand2.nextInt(1) == 1 ? mom : dad;
	}
	
	public ArrayList<Rectangle> selection(ArrayList<Rectangle> winnerArr){ //sorts by highest fitness marks top as winners and adds winners to winnersArr
		winnerArr = sortFitness(winnerArr);
//		for(int i = 1; i < numTopPop; i++){ //marks top units as winner
//			squares.get(i).setWinner(true);
//		}
		ArrayList<Rectangle> a = new ArrayList<Rectangle>();
		for(int i = 0; i < 4; i++){
			a.add(winnerArr.get(i));
		}
		return a;
	}
	public Rectangle mutation(Rectangle offspring){
		for (int i = 0; i < offspring.getNet().getAllNeurons().size(); i++) {
			offspring.getNet().getAllNeurons().get(i).setBias(geneMutation(offspring.getNet().getAllNeurons().get(i).getBias()));
		}
		
		for (int i = 0; i < offspring.getNet().getAllConnections().size(); i++) {
			offspring.getNet().getAllConnections().get(i).setWeight(geneMutation(offspring.getNet().getAllConnections().get(i).getWeight()));
		}
		return offspring;
	}
	public double geneMutation(double gene){
		Random rand = new Random();
		//gene = 0;
		if(rand.nextDouble() < mutateRate){
			double mutateFactor = .95;
			 gene = gene * mutateFactor;
		}
		return gene;
	}
	public double getMutateRate() {
		return mutateRate;
	}
	public void setMutateRate(double mutateRate) {
		this.mutateRate = mutateRate;
	}
	public int getBestPopIndex() {
		return bestPopIndex;
	}
	public void setBestPopIndex(int bestPopIndex) {
		this.bestPopIndex = bestPopIndex;
	}
	public List<Rectangle> getSquares() {
		return squares;
	}
	public void setPopulationArr(ArrayList<Rectangle> squares) {
		this.squares = squares;
	}
	public void add(Rectangle square){
		squares.add(square);
	}
	public int getScaleFactor() {
		return scaleFactor;
	}
	public void setScaleFactor(int scaleFactor) {
		this.scaleFactor = scaleFactor;
	}
	public int getBestIndex() {
		return bestIndex;
	}
	public void setBestIndex(int bestIndex) {
		this.bestIndex = bestIndex;
	}
	public float getBestFitness() {
		return bestFitness;
	}
	public void setBestFitness(long l) {
		this.bestFitness = (int) l;
	}
	public int getMaxPop() {
		return maxPop;
	}
	public void setMaxPop(int maxPop) {
		this.maxPop = maxPop;
	}
	public int getNumTopPop() {
		return numTopPop;
	}
	public void setNumTopPop(int numTopPop) {
		this.numTopPop = numTopPop;
	}
}
