import java.io.FileInputStream;
import java.util.Scanner;
import java.lang.Exception;
import java.io.PrintStream;
import java.io.File;

public class Main{
    private static int testSize = 100;
	public static void main(String[] args){
        try{
            Scanner input = new Scanner(new FileInputStream(args[0]));
            PrintStream stream = new PrintStream(new File("result.txt"));
            PrintStream streamCV = new PrintStream(new File("result-cv.txt"));


            while(input.hasNextLine()){
                String line = input.nextLine();
                String[] parts = line.split(",");

                int generationSize = Integer.valueOf(parts[0]);
                int killRate = Integer.valueOf(parts[1]);
                int randomSaveRate = Integer.valueOf(parts[2]);
                int chromosomeSize = Integer.valueOf(parts[3]);
                boolean useRB = Integer.valueOf(parts[4]) == 0 ? false: true;

                float sum = 0;
                System.out.println("running " + testSize + " test on GA");
                System.out.println("generation size:  " + generationSize);
                System.out.println("kill rate:        " + killRate);
                System.out.println("random save rate: " + randomSaveRate);
                System.out.println("chromosome size:  " + chromosomeSize);
                System.out.println("use roll back in fitness:  " + useRB);
                for(int j=0; j<testSize; j++){
                    sum += GA.test(j, generationSize, killRate, randomSaveRate, chromosomeSize, useRB);
                    System.out.print("\r                                                                               ");

                }
                System.out.println("\navg generations: " + (float)(sum/(float)testSize));
                System.out.println("===================================================");

                
                stream.println("running " + testSize + " test on GA");
                stream.println("generation size:           " + generationSize);
                stream.println("kill rate:                 " + killRate);
                stream.println("random save rate:          " + randomSaveRate);
                stream.println("chromosome size:           " + chromosomeSize);
                stream.println("use roll back in fitness:  " + useRB);
                stream.println("test case: answer generation count average = " + (float)(sum/(float)testSize));
                stream.println("===========================================================");

                streamCV.println(generationSize + "," + killRate + "," + randomSaveRate + "," + chromosomeSize + "," + useRB + "," + (float)(sum/(float)testSize));
            }
        }catch(Exception e){
            e.printStackTrace();    
        }
	}
}
