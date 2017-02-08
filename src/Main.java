public class Main{
    private static int[] generationSize = {
        100000, 100000, 100000, 200000, 200000, 200000
    };
    private static int[] killRate = {
        50000, 20000, 70000, 100000, 50000, 150000
    };
    private static int[] randomSaveRate = {
        1000, 500, 2000, 3000, 2000, 1000
    };
    private static int[] chromosomeSize = {
        25, 20, 20, 25, 25, 25
    };
    private static int[][] fitness = new int[6][200];
    private static int testSize = 50;
	public static void main(String[] args){

        for(int i=0; i<generationSize.length; i++){
            float sum = 0;
    		System.out.println("running " + testSize + " test on GA");
            System.out.println("generation size:  " + generationSize[i]);
            System.out.println("kill rate:        " + killRate[i]);
            System.out.println("random save rate: " + randomSaveRate[i]);
            System.out.println("chromosome size:  " + chromosomeSize[i]);
            for(int j=0; j<testSize; j++){
                System.out.println("test " + j);
                GA.test(chromosomeSize[i], generationSize[i], killRate[i], randomSaveRate[i], fitness[i]);
                System.out.println("=======================================");
            }
            System.out.println("avg generations: " + sum/testSize);
            System.out.println("*\t*\t*\t*\t*\t*\t*\t*\t*");
        }
	}
}
