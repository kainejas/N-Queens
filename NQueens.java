import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NQueens {
	
	ArrayList<Integer[]> population;
	ArrayList<Integer> fitnesses;
	int n;
	Random rand = new Random();
	
	
	public static void main(String[] args) {
		NQueens tsp = new NQueens(200);
		
	}
	
	public NQueens(int n) {
		this.n = n;
		population = new ArrayList<Integer[]>();
		fitnesses = new ArrayList<Integer>();
		
		
		for(int i = 0; i < 100; i++) {
			population.add(new Integer[n]);
			for(int j = 0; j < n; j ++) {
				population.get(i)[j] = j;
			}
			int temp = rand.nextInt(20);
			for(int k = 0; k < temp; k++) {
				mutate(i);
			}
		}
		
		 
		simulate();
		
	}
	
	public void mutate(int index) {
		
		int index1 = rand.nextInt(n);
		int index2 = rand.nextInt(n);
		
		int val1 = population.get(index)[index1];
		int val2 = population.get(index)[index2];
		
		population.get(index)[index2] = val1;
		population.get(index)[index1] = val2;
		
		
		
	}
	
	public void simulate() {
		int count = 0;
		

	while(!solutionFound() && count < population.size()*1000000){

	//	System.out.println(count + "\tAvg:" + averageFitness());
		//System.out.println(count);
	
		if(count % 1000 == 0) {
			System.out.println("Crossovers: " + (count) + "\tAvg Fitness:" + averageFitness());
		}
	
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
		
	
		
		int bestFit = Integer.MAX_VALUE;
		int bestIndex = -1;
		
		for(int i = 0; i < 10; i ++) {
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
		
		int crossOverPoint = 0;//rand.nextInt(n);
		
		int probability = rand.nextInt(3);
		if(probability == 0) {
			crossOverPoint = n/2 -1;
		}
		else if(probability == 1) {
			crossOverPoint = n/2;
		}
		else if(probability == 2) {
			crossOverPoint = n/2+1;
		}
		
		
		
	
		
		ArrayList<Integer> child1 = new ArrayList<Integer>(n);
		ArrayList<Integer> child2 = new ArrayList<Integer>(n);
		
		//0 to crossoverpoint from first
		// crossoverpoint to n
		
		for(int i = 0; i < crossOverPoint; i++) {
			child1.add(parent1[i]);
			child2.add( parent2[i]);
		}
		
		
		
		for(int j = crossOverPoint; j < n; j++) {
			if(!child1.contains(parent2[j])){
			child1.add(parent2[j]);
			}
			if(!child2.contains(parent1[j])){
			child2.add(parent1[j]);
			}
		}
		
		for(int k = 0; k < crossOverPoint; k++) {
			if(!child1.contains(parent2[k]))
				child1.add(parent2[k]);
			
			if(!child2.contains(parent1[k]))
				child2.add(parent1[k]);
		}
		
		
		Integer[] tempArr1 = Arrays.copyOf(child1.toArray(), child1.toArray().length, Integer[].class);
		Integer[] tempArr2 = Arrays.copyOf(child2.toArray(), child1.toArray().length, Integer[].class);
		population.add(tempArr1);
		population.add(tempArr2);
		
		findWorstTwo();
		
	}
	public void findWorstTwo(){
		int maxIndex = -1;
		int maxFitness = -1;
		
		int nextIndex = -1;
		int nextFitness = -1;
		for(int i = 0; i < population.size(); i++) {
			
			double probability = rand.nextDouble();
			if(probability <= 0.03) {
				mutate(i);
			}
			
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