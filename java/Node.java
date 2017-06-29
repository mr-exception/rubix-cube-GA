public class Node{
    public int[] chromosome = new int[20];
    public int fitness = 0;
    
    public Node(int[] chromosome, int[] startPosition, boolean useRB){
        this.fitness = GA.fitness(startPosition ,chromosome, useRB);
        this.chromosome = chromosome;
    }

    public String toString(){
        String result = "[" + (this.chromosome[0] == -1 ? 
                            ("##") : 
                            ((this.chromosome[0] > 9) ? 
                                this.chromosome[0] : 
                                " " + this.chromosome[0]));
        for(int i=1; i<chromosome.length; i++)
            result += this.chromosome[i] == -1 ? (", ##"):((this.chromosome[i] > 9) ? ", " + this.chromosome[i] : ",  " + this.chromosome[i]);
        result += "] : f: " + this.fitness;
        return result;
    }
}
