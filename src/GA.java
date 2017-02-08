import java.util.Random;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class GA{
    
    public static void main(String[] args){
        test(0, 20000, 10000, 2500, 25);
    }
    public static int test(int tn, int generationSize, int generationKillRate, int saveRate, int chromosomeLength){
        Random random = new Random();
        int[] map = new int[54];
        for(int i=0; i<54; i++)
            map[i] = i/9;

        //for(int i=0; i<40; i++)
        //  roll(map, random.nextInt(12));
        ArrayList<Node> firstGeneration = new ArrayList<Node>();
        createFirstGeneration(firstGeneration, map, generationSize, chromosomeLength);

        
        //for(Node n : firstGeneration)
        //    System.out.println(n);
        int i = 1;
        System.out.print("\rtest " + tn + " generation " + i + ": node count = " + firstGeneration.size());
        
        while(true){
            maxFitnessInCurrentGeneration = Integer.MIN_VALUE;
            completeGeneration(firstGeneration, map, generationKillRate, saveRate);
            if(checkIfWon(firstGeneration, map)){
                break;
            }
            //System.out.println("===================");
            System.out.print("\rtest " + tn + " generation " + (i) + " : node count = " + firstGeneration.size() + " ft: " + maxFitnessInCurrentGeneration);
            //for(Node n : firstGeneration)
            //    System.out.println(n);
            i++;
        }
        //System.out.println("\nreached goal after " + i + " generations");
        return i;
    }
    /*
    * =============================
    * === complete ================
    * =========== generation ======
    * ================== progress =
    * =============================
    */
    public static void completeGeneration(ArrayList<Node> generationList, int[] startPosition, int kill, int rndSave){
        crossOver(generationList, kill/2, startPosition);

        kill(generationList, kill, rndSave, startPosition);

        for(int i=0; i<generationList.size()/2; i++){
            mute(generationList.get(i), startPosition);
        }

        sort(generationList);

        //System.out.println("max Fitness in current generation: " + maxFitnessInCurrentGeneration);
        //System.out.println("max Fitness total: " + maxFitnessTotal);
    }
    /* ============================ */
    public static void createFirstGeneration(ArrayList<Node> emptyGeneration,int[] baseMap, int size, int chromosomeLength){
        Random random = new Random();
        for(int i=0; i<size; i++){
            int[] chromosome = new int[chromosomeLength];
            for(int j=0; j<chromosomeLength; j++)
                chromosome[j] = random.nextInt(13) -1;

            emptyGeneration.add(new Node(chromosome, baseMap));
        }
    }
    /*
    * =============================
    * === calculate ===============
    * ======== if = any = node ====
    * ============ if not = good ==
    * ====== for = genertions =====
    */
    private static int calculateHoverBackScore(int[] chromosome){
        ArrayList<Integer> moveList = new ArrayList<Integer>();
        Stack<Integer> lastSameGroupMoves = new Stack<Integer>();

        for(int i=0; i<chromosome.length; i++)
            if(chromosome[i] != -1){
                moveList.add(chromosome[i]);
            }

        int groupId = -1;

        int[] groupCount = new int[4];
        int lastMove = -1;
        boolean hasFaltch = false;
        int result = 0;
        for(int j=moveList.size(); j>=0; j--){
            
            while(!lastSameGroupMoves.isEmpty())
                lastSameGroupMoves.pop();

            for(int i=0; i<j; i++){
                int move = moveList.get(i);
                if(groupId == -1){
                    groupId = move/4;
                    lastSameGroupMoves.push(move%4);
                }else{
                    if(groupId != move/4){
                        groupId = move/4;
                        
                        while(!lastSameGroupMoves.isEmpty())
                            lastSameGroupMoves.pop();

                        lastSameGroupMoves.push(move%4);
                    }else{
                        lastSameGroupMoves.push(move%4);
                    }
                }
            }
            if(!lastSameGroupMoves.isEmpty()){
                if(hasFaltch(lastSameGroupMoves))
                    result -= 3;
            }
        }
        return result;
    }
    /*
    * ------------------------------------------
    * -----mutation-----------------------------
    * --------------function--------------------
    * -----------------------method-------------
    */
    public static void mute(Node n, int[] baseMap){
        Random random =new Random();
        n.chromosome[random.nextInt(n.chromosome.length)] = (random.nextInt(13)) - 1; // => [-1 ... 11]
        n.fitness = fitness(baseMap, n.chromosome);
    }
	

    public static void printMap(int[] map){
        String[] faces = {"DOWN", "UP", "FRONT", "BACK","LEFT", "RIGHT"};
        for(int i=0; i<6; i++){
            System.out.println(faces[i]);
            for(int j=0; j<3; j++){
                for(int k=0; k<3; k++)
                    System.out.print(map[i*9 + j*3 + k] + "\t");
                System.out.println();
            }
            System.out.println("==================");
        }
    }

    private static void sort(ArrayList<Node> generation){
        Collections.sort(generation, new Comparator<Node>() {
                public int compare(Node obj1,Node obj2){

                    if(obj1.fitness < obj2.fitness)
                        return -1;
                    else if(obj1.fitness > obj2.fitness)
                        return 1;
                    else
                        return 0;

                }
            });
    }
    public static void kill(ArrayList<Node> nodeGroup, int killNumber, int randomSaveNumber, int[] startPosition){
        int saveNumber = nodeGroup.size() - killNumber;
        boolean[] kills = new boolean[nodeGroup.size()];

        
        sort(nodeGroup);
        for(int i=0; i<killNumber; i++)
            kills[i] = true;
        for(int i=killNumber; i<kills.length; i++)
            kills[i] = false;

        Random random = new Random();
        for(int i=0; i<randomSaveNumber; i++){
            int saveIndex = random.nextInt(killNumber);
            
            while(!kills[saveIndex])
                saveIndex = random.nextInt(killNumber);

            int removeIndex = random.nextInt(saveNumber);
            while(kills[removeIndex])
                removeIndex = random.nextInt(saveNumber) + killNumber;
            
            kills[saveIndex] = false;
            kills[removeIndex] = true;
            
        }

        for(int i=kills.length-1; i>=0; i--)
            if(kills[i]){
                nodeGroup.remove(i);
                //System.out.print("\rkilled " + i);
            }
        //System.out.println("count after killing : " + nodeGroup.size());
    }
    public static void crossOver(ArrayList<Node> nodeGroup /* sorted */, int size, int[] baseMap){
        Random random = new Random();

        for(int i=0; i<size; i++){

            Node[] pair0 = getPair(nodeGroup);
            Node[] pair1 = getPair(nodeGroup);

            int singlePointer = random.nextInt(pair0[0].chromosome.length);
            produce(pair0[0], pair1[0], singlePointer, nodeGroup, baseMap);
            //produce(pair0[1], pair1[1], singlePointer, nodeGroup, baseMap);
        }
    }
    private static void produce(Node p0, Node p1, int pointer, ArrayList<Node> nodeGroup, int[] baseMap){
        int[] chromosome0 = new int[p0.chromosome.length];
        int[] chromosome1 = new int[p0.chromosome.length];

        for(int i=0; i<pointer; i++){
            chromosome0[i] = p0.chromosome[i];
            chromosome1[i] = p1.chromosome[i];
        }
        for(int i=pointer; i<chromosome0.length; i++){
            chromosome0[i] = p1.chromosome[i];
            chromosome1[i] = p0.chromosome[i];
        }

        nodeGroup.add(new Node(chromosome0, baseMap));
        nodeGroup.add(new Node(chromosome1, baseMap));
    }
    private static Node[] getPair(ArrayList<Node> nodeGroup){
        Random random = new Random();
        Node[] pair = new Node[2];
        pair[0] = nodeGroup.get(random.nextInt(nodeGroup.size()));
        while(pair[1] == null || pair[1].fitness == pair[0].fitness)
            pair[1] = nodeGroup.get(random.nextInt(nodeGroup.size()));

        if(pair[0].fitness < pair[1].fitness){
            Node t = pair[0];
            pair[0] = pair[1];
            pair[1] = t;
        }

        return pair;
    }

    private static int maxFitnessInCurrentGeneration = Integer.MIN_VALUE;
    private static int maxFitnessTotal = Integer.MIN_VALUE;
    public static int fitness(int[] baseMap, int[] chromosome){

        int[] map = new int[baseMap.length];
        for(int i=0; i<baseMap.length; i++)
            map[i] = baseMap[i];

        for(int i=0; i<chromosome.length; i++)
            roll(map, chromosome[i]);
        int result = 0;
        for(int i=0; i<map.length; i++){
            if(map[i] == i/9)
                result++;
        }
        
        result += calculateHoverBackScore(chromosome);

        return result;
    }
    private static boolean hasFaltch(Stack<Integer> moves){
        int n0 = moves.pop();
        if(moves.isEmpty())
            return false;
        int n1 = moves.pop();

        switch(n0){
            case 0:
                switch(n1){
                    case 0:
                        while(!moves.isEmpty())
                            if(moves.pop() == 0)
                                return true;
                        return false;
                    case 1:
                        return true;
                    case 2:
                    case 3:
                        return false;
                }
                break;
            case 1:
                return true;
            case 2:
                switch(n1){
                    case 0:
                        return false;
                    case 1:
                        return false;
                    case 2:
                        while(!moves.isEmpty())
                            if(moves.pop() == 2)
                                return true;
                        return false;
                    case 3:
                        return true;
                }
                break;
            case 3:
                switch(n1){
                    case 0:
                        return false;
                    case 1:
                        return false;
                    case 2:
                        return true;
                    case 3:
                        return true;
                }
                break;
        }
        return false;
    }
    /*
    * ------------------------------------------
    * ----fitness function----------------------
    * ---------------------section--------------
    * ------------------------------------------
    */
    private static void roll(int[] map /* [0 to 53] */, int rollId){
        int t = -1;
        switch(rollId){
            case -1:
                // nothing
                break;
            case 0:
                /*
                * =======    U    ========
                */
                t = map[53];
                map[53] = map[33];
                map[33] = map[36];
                map[36] = map[20];
                map[20] = t;

                t = map[50];
                map[50] = map[34];
                map[34] = map[39];
                map[39] = map[19];
                map[19] = t;

                t = map[47];
                map[47] = map[35];
                map[35] = map[42];
                map[42] = map[18];
                map[18] = t;

                t = map[9];
                map[9] = map[15];
                map[15] = map[17];
                map[17] = map[11];
                map[11] = t;

                t = map[10];
                map[10] = map[12];
                map[12] = map[16];
                map[16] = map[14];
                map[14] = t;
                break;
            case 1:
                /*
                * =======    U'   ========
                */
                t = map[20];
                map[20] = map[36];
                map[36] = map[33];
                map[33] = map[53];
                map[53] = t;

                t = map[19];
                map[19] = map[39];
                map[39] = map[34];
                map[34] = map[50];
                map[50] = t;

                t = map[18];
                map[18] = map[42];
                map[42] = map[35];
                map[35] = map[47];
                map[47] = t;

                t = map[11];
                map[11] = map[17];
                map[17] = map[15];
                map[15] = map[9];
                map[9] = t;

                t = map[14];
                map[14] = map[16];
                map[16] = map[12];
                map[12] = map[10];
                map[10] = t;
                break;
            case 2:
                /*
                * =======    D    ========
                */
                t = map[0];
                map[0] = map[2];
                map[2] = map[8];
                map[8] = map[6];
                map[6] = t;

                t = map[3];
                map[3] = map[1];
                map[1] = map[5];
                map[5] = map[7];
                map[7] = t;

                t = map[45];
                map[45] = map[29];
                map[29] = map[44];
                map[44] = map[24];
                map[24] = t;

                t = map[48];
                map[48] = map[28];
                map[28] = map[41];
                map[41] = map[25];
                map[25] = t;

                t = map[51];
                map[51] = map[27];
                map[27] = map[38];
                map[38] = map[26];
                map[26] = t;
                break;
            case 3:
                /*
                * =======    D'   ========
                */
                t = map[6];
                map[6] = map[8];
                map[8] = map[2];
                map[2] = map[0];
                map[0] = t;

                t = map[7];
                map[7] = map[5];
                map[5] = map[1];
                map[1] = map[3];
                map[3] = t;

                t = map[24];
                map[24] = map[44];
                map[44] = map[29];
                map[29] = map[45];
                map[45] = t;

                t = map[25];
                map[25] = map[41];
                map[41] = map[28];
                map[28] = map[48];
                map[48] = t;

                t = map[26];
                map[26] = map[38];
                map[38] = map[27];
                map[27] = map[51];
                map[51] = t;
                break;
            case 4:
                /*
                * =======    L    ========
                */
                t = map[51];
                map[51] = map[53];
                map[53] = map[47];
                map[47] = map[45];
                map[45] = t;

                t = map[52];
                map[52] = map[50];
                map[50] = map[46];
                map[46] = map[48];
                map[48] = t;

                t = map[9];
                map[9] = map[35];
                map[35] = map[8];
                map[8] = map[26];
                map[26] = t;

                t = map[12];
                map[12] = map[32];
                map[32] = map[5];
                map[5] = map[23];
                map[23] = t;

                t = map[15];
                map[15] = map[29];
                map[29] = map[2];
                map[2] = map[20];
                map[20] = t;
                break;
            case 5:
                /*
                * =======    L'   ========
                */
                t = map[45];
                map[45] = map[47];
                map[47] = map[53];
                map[53] = map[51];
                map[51] = t;

                t = map[48];
                map[48] = map[46];
                map[46] = map[50];
                map[50] = map[52];
                map[52] = t;

                t = map[26];
                map[26] = map[8];
                map[8] = map[35];
                map[35] = map[9];
                map[9] = t;

                t = map[23];
                map[23] = map[5];
                map[5] = map[32];
                map[32] = map[12];
                map[12] = t;

                t = map[20];
                map[20] = map[2];
                map[2] = map[29];
                map[29] = map[15];
                map[15] = t;
                break;
            case 6:
                /*
                * =======    R    ========
                */
                t = map[36];
                map[36] = map[42];
                map[42] = map[44];
                map[44] = map[38];
                map[38] = t;

                t = map[39];
                map[39] = map[43];
                map[43] = map[41];
                map[41] = map[37];
                map[37] = t;

                t = map[6];
                map[6] = map[24];
                map[24] = map[11];
                map[11] = map[33];
                map[33] = t;

                t = map[3];
                map[3] = map[21];
                map[21] = map[14];
                map[14] = map[30];
                map[30] = t;

                t = map[0];
                map[0] = map[18];
                map[18] = map[17];
                map[17] = map[27];
                map[27] = t;
                break;
            case 7:
                /*
                * =======    R'   ========
                */
                t = map[38];
                map[38] = map[44];
                map[44] = map[42];
                map[42] = map[36];
                map[36] = t;

                t = map[37];
                map[37] = map[41];
                map[41] = map[43];
                map[43] = map[39];
                map[39] = t;

                t = map[33];
                map[33] = map[11];
                map[11] = map[24];
                map[24] = map[6];
                map[6] = t;

                t = map[30];
                map[30] = map[14];
                map[14] = map[21];
                map[21] = map[3];
                map[3] = t;

                t = map[27];
                map[27] = map[17];
                map[17] = map[18];
                map[18] = map[0];
                map[0] = t;
                break;
            case 8:
                /*
                * =======    B    ========
                */
                t = map[18];
                map[18] = map[24];
                map[24] = map[26];
                map[26] = map[20];
                map[20] = t;

                t = map[19];
                map[19] = map[21];
                map[21] = map[25];
                map[25] = map[23];
                map[23] = t;

                t = map[0];
                map[0] = map[45];
                map[45] = map[9];
                map[9] = map[36];
                map[36] = t;

                t = map[1];
                map[1] = map[46];
                map[46] = map[10];
                map[10] = map[37];
                map[37] = t;

                t = map[2];
                map[2] = map[47];
                map[47] = map[11];
                map[11] = map[38];
                map[38] = t;
                break;
            case 9:
                /*
                * =======    B'   ========
                */
                t = map[20];
                map[20] = map[26];
                map[26] = map[24];
                map[24] = map[18];
                map[18] = t;

                t = map[23];
                map[23] = map[25];
                map[25] = map[21];
                map[21] = map[19];
                map[19] = t;

                t = map[36];
                map[36] = map[9];
                map[9] = map[45];
                map[45] = map[0];
                map[0] = t;

                t = map[37];
                map[37] = map[10];
                map[10] = map[46];
                map[46] = map[1];
                map[1] = t;

                t = map[38];
                map[38] = map[11];
                map[11] = map[47];
                map[47] = map[2];
                map[2] = t;
                break;
            case 10:
                /*
                * =======    F    ========
                */
                t = map[27];
                map[27] = map[33];
                map[33] = map[35];
                map[35] = map[29];
                map[29] = t;

                t = map[28];
                map[28] = map[30];
                map[30] = map[34];
                map[34] = map[32];
                map[32] = t;

                t = map[44];
                map[44] = map[17];
                map[17] = map[51];
                map[51] = map[6];
                map[6] = t;

                t = map[43];
                map[43] = map[16];
                map[16] = map[52];
                map[52] = map[7];
                map[7] = t;

                t = map[42];
                map[42] = map[15];
                map[15] = map[53];
                map[53] = map[8];
                map[8] = t;
                break;
            case 11:
                /*
                * =======    F'   ========
                */
                t = map[29];
                map[29] = map[35];
                map[35] = map[33];
                map[33] = map[27];
                map[27] = t;

                t = map[32];
                map[32] = map[34];
                map[34] = map[30];
                map[30] = map[28];
                map[28] = t;

                t = map[6];
                map[6] = map[51];
                map[51] = map[17];
                map[17] = map[44];
                map[44] = t;

                t = map[7];
                map[7] = map[52];
                map[52] = map[16];
                map[16] = map[43];
                map[43] = t;

                t = map[8];
                map[8] = map[53];
                map[53] = map[15];
                map[15] = map[42];
                map[42] = t;
                break;
        }
    }

    private static boolean checkIfWon(ArrayList<Node> generation, int[] baseMap){
        for(int i=0; i<generation.size(); i++){

            if(maxFitnessInCurrentGeneration < generation.get(i).fitness)
                maxFitnessInCurrentGeneration = generation.get(i).fitness;
            if(maxFitnessTotal < generation.get(i).fitness)
                maxFitnessTotal = generation.get(i).fitness;

            if(checkIfComplete(baseMap, generation.get(i).chromosome))
                return true;
        }
        return false;
    }
    private static boolean checkIfComplete(int[] baseMap, int[] chromosome){
        int[] map = new int[baseMap.length];
        for(int i=0; i<map.length; i++)
            map[i] = baseMap[i];

        for(int i=0; i<chromosome.length; i++)
            roll(map, chromosome[i]);

        for(int i=0; i<map.length; i++)
            if(map[i] != i/9)
                return false;
        return true;
    }
    /*
    * =============================
    * == when = found = answer ====
    * =============================
    */
    private static void foundAnswer(int[] chromosome, int[] baseMap){
        System.out.println("found answer:");
        for(int i=0; i<chromosome.length; i++)
            if(chromosome[i] != -1)
                System.out.print(chromosome[i] + " ");
        System.out.println();
    }
}