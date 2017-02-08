public class Main{
    private static int[] generationSize = {
        1000,   1000,   1000,   1000,   2000,   2000,   2000,   2000,   5000,   5000,   5000,   5000,   10000,   10000,   10000,   10000
    };
    private static int[] killRate = {
        100,    100,    200,    200,    100,    100,    200,    200,    100,    100,    200,    200,    100,    100,    200,    200
    };
    private static int[] randomSaveRate = {
        10,     20,     10,     20,     10,     20,     10,     20,     10,     20,     10,     20,     10,     20,     20,     10
    };
    private static int testSize = 60;
	public static void main(String[] args){

        for(int i=0; i<generationSize.length; i++){
            float sum = 0;
    		System.out.println("running " + testSize + " test on GA");
            System.out.println("generation size:  " + generationSize[i]);
            System.out.println("kill rate:        " + killRate[i]);
            System.out.println("random save rate: " + randomSaveRate[i]);
            for(int j=0; j<testSize; j++){
                //System.out.println("test " + j);
                sum += GA.test(j, generationSize[i], killRate[i], randomSaveRate[i], 20);
                System.out.print("\r                                                                               ");

            }
            System.out.println("\navg generations: " + (float)(sum/(float)testSize));
            System.out.println("===================================================");
        }
	}
}
