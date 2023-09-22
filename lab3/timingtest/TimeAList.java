package timingtest;
import edu.princeton.cs.algs4.Stopwatch;
/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<>();
        buildNs(Ns);
        AList<Double> times;
        times = computeTime(Ns);
        AList<Integer> opCounts;
        opCounts = Ns;
        
        printTimingTable(Ns, times, opCounts);
    }
    
    /** To build a Number list quickly. */
    private static void buildNs(AList<Integer> Ns) {
        int init = 1000;
        while ((init *= 2) <= 128000)
            Ns.addLast(init);
    }
    
    private static AList<Double> computeTime(AList<Integer> Ns) {
        AList<Double> times = new AList<>();
        
        for (int i = 0; i < Ns.size(); i ++ ) {
            int loopcount = Ns.get(i);

            AList<Integer> storedata = new AList<>();
            /* Start Timing */
            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < loopcount; j ++ ) {
                storedata.addLast(j);
            }
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
        }
        return times;
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }
}
