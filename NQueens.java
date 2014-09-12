import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NQueens {
	
	ArrayList<Integer[]> population;
	ArrayList<Integer> fitnesses;
	int n;
	
	
	
	public static void main(String[] args) {
		NQueens queens = new NQueens(14);
		
	}
	
	public NQueens(int n) {
		this.n = n;
		population = new ArrayList<Integer[]>();
		fitnesses = new ArrayList<Integer>();
		
		Random rand = new Random();
		for(int i = 0; i < 2000; i++) {
			population.add(new Integer[n]);
			for(int j = 0; j < n; j ++) {
				population.get(i)[j] = rand.nextInt(n);
			}
		}
		
		
		simulate();
		
	}
	
	public void simulate() {
		int count = 0;
		

	while(!solutionFound() && count < 1000000){

		System.out.println(count + "\tAvg:" + averageFitness());
		crossOver();
		count++;
	}

	if(solutionFound()){
		
		for(int i = 0; i < population.size(); i++) {
			if(fitness(population.get(i)) == 0) {
				System.out.println("Solution Found!");
				for(int j = 0; j < n; j ++) {
					System.out.print(population.get(i)[j] + ", ");
				}
				System.out.println();
			}
		}
		
		
		
	}
	else {
		System.out.println("No Solutions Found!");
	}
		
		
		
	}
	
	public double averageFitness() {
		
		int sum = 0;
		for(int i = 0; i < population.size(); i++) {
			sum += fitness(population.get(i));
		}
		
		return (sum*1.0)/population.size();
		
	}
	
public int[] findParents() {
		
		
		List<Integer[]> parentsList = new ArrayList<Integer[]>();
		
	
		Random rand = new Random();
		
		int bestFit = Integer.MAX_VALUE;
		int bestIndex = -1;
		
		for(int i = 0; i < 5; i ++) {
			int index = rand.nextInt(population.size());
			Integer[] tempArr = {fitness(population.get(index)) , index};
			parentsList.add(tempArr);
			if(tempArr[0] < bestFit) {
				bestFit = tempArr[0];
				bestIndex = tempArr[1];
			}
		}
		
		int[] parents = new int[2];
		parents[0] = bestIndex;
		
		int oldIndex = bestIndex;
		bestFit =  Integer.MAX_VALUE;
		bestIndex = -1;
		for(int j = 0; j < parentsList.size(); j++) {
			if(parentsList.get(j)[0] < bestFit && parentsList.get(j)[1] != oldIndex) {
				
				bestFit = parentsList.get(j)[0];
				bestIndex = parentsList.get(j)[1];
				
			}
		}
		
		parents[1] = bestIndex;

		
		return parents;
		
		
	}
	
	
	public boolean solutionFound() {
		int sum = 0;
		for(int i = 0; i < population.size(); i++) {
			if(fitness(population.get(i)) == 0) {
				return true;
			}
		}
		
		return false;
		
	}
	
	
	public void crossOver() {
		int[] indexes = findParents();
		
		Integer[] parent1 = population.get(indexes[0]);
		Integer[] parent2 = population.get(indexes[1]);
		
		Random rand = new Random();
		int crossOverPoint = rand.nextInt(n);
		
		Integer[] child1 = new Integer[n];
		Integer[] child2 = new Integer[n];
		
		//0 to crossoverpoint from first
		// crossoverpoint to n
		
		for(int i = 0; i < crossOverPoint; i++) {
			child1[i] = parent1[i];
			child2[i] = parent2[i];
		}
		
		for(int j = crossOverPoint; j < n; j++) {
			child1[j] = parent2[j];
			child2[j] = parent1[j];
		}
		
		population.add(child1);
		population.add(child2);
		
		findWorstTwo();
		
	}
	public void findWorstTwo(){
		int maxIndex = -1;
		int maxFitness = -1;
		
		int nextIndex = -1;
		int nextFitness = -1;
		for(int i = 0; i < population.size(); i++) {
			int fit = fitness(population.get(i));
			if(fit > maxFitness) {
				maxFitness = fit;
				maxIndex = i;
			}
		}
		
		population.remove(maxIndex);
		
		
		
		
		for(int j = 0; j < population.size(); j++) {
			int fit = fitness(population.get(j));
			if(fit > nextFitness && j != maxIndex ){
				nextFitness = fit;
				nextIndex = j;
			}
		}
		
	
	population.remove(nextIndex);	
		
	}
	
	
	
	public int fitness(Integer[] individual) {
		int checks= 0;
		for(int i = 0; i < individual.length; i++) {
			checks+= countChecks(i, individual);
		}
		return checks;
	}
	
	public int checkForStraights(int index, Integer[] individual) {
		int val = individual[index];
		int sum = 0;
		for(int i = 0; i < individual.length; i++) {
			if(i != index && individual[i] == val) {
				sum++;
			}
		}
		return sum;
	}

	public int checkForDiagonals(int index, Integer[] individual) {
		int val = individual[index]; // 3
		int sum = 0;
		if(index > 0 && index < n-1) {

			int forward = index+1;
			int backward = index-1;

			while(forward < n || backward >= 0) {
				if(forward < n) {
					int diff = forward-index; //2- 1 = 1
					int checkSpace = val+diff; // 3 + 1 = 4
					
				if(individual[forward] == checkSpace) {
					//System.out.println("Diagonal at " + forward);
					sum++;
				}
				checkSpace = val - diff; //3 - 1 = 2
				if(individual[forward] == checkSpace) {
					//System.out.println("Diagonal at " + forward);
					sum++;
				}
				
				forward++;
				}//end forward

				if(backward > -1) {
					int diff = index - backward; // 2 - 1 = 1
					int checkSpace = val + diff; //5 + 1 = 6 
					if(individual[backward] == checkSpace) {
						//System.out.println("Diagonal at " + backward);
						sum++;
					}
					checkSpace = val - diff; //5 - 1
					if(individual[backward] == checkSpace) {
						//System.out.println("Diagonal at " + backward);
						sum++;
					}
					
					backward--;
				}//end backward
				
			}//end while



		}//end check index
		return sum;
	}
	
	public int countChecks(int index, Integer[] individual) {
		return checkForDiagonals(index, individual) + checkForStraights(index, individual);
	}
	

}
