import java.util.*;

public class Maze {

    // Mission 1
    public static int[] breakWalls(Input.Walls w) {
        DisjointSet dsu = new DisjointSet(w.cells());
        int[] broken = new int[w.cells() - 1];
        int cnt = 0;
        for (int k = 0; k < w.count(); k++) {
            if (dsu.union(w.a()[k], w.b()[k])) broken[cnt++] = k;
        }
        return Arrays.copyOf(broken, cnt);
    }


    // Mission 2: Kruskal
    public static int[] cheapestMaze(Input.Walls w) {
        int m = w.count();
        long[] keys = new long[m];
        for (int k = 0; k < m; k++) keys[k] = ((long) w.cost()[k] << 32) | k;
        Arrays.sort(keys);                      
        // by cost, then by index
        DisjointSet dsu = new DisjointSet(w.cells());
        int[] broken = new int[w.cells() - 1];
        int cnt = 0;
        for (long key : keys) {
            int k = (int) (key & 0xFFFFFFFFL);
            if (dsu.union(w.a()[k], w.b()[k])) broken[cnt++] = k;
        }
        return Arrays.copyOf(broken, cnt);
    }

    // Mission 2: Prim with a priority queue
    public static long primCost(Input.Walls w) {
        int n = w.cells(), m = w.count();
        int[] deg = new int[n];
        for (int k = 0; k < m; k++) { deg[w.a()[k]]++; deg[w.b()[k]]++; }
        int[][] adj = new int[n][];                
        // adj[v] = indexes of walls at cell v
        for (int v = 0; v < n; v++) adj[v] = new int[deg[v]];
        int[] fill = new int[n];
        for (int k = 0; k < m; k++) {
            adj[w.a()[k]][fill[w.a()[k]]++] = k;
            adj[w.b()[k]][fill[w.b()[k]]++] = k;
        }
        
        boolean[] in = new boolean[n];
        PriorityQueue<Long> pq = new PriorityQueue<>();
        in[0] = true;
        for (int k : adj[0]) pq.add(((long) w.cost()[k] << 32) | k);
        long total = 0;
        int added = 1;
        while (!pq.isEmpty() && added < n) {
            int k = (int) (pq.poll() & 0xFFFFFFFFL);
            int v;
            if (!in[w.a()[k]]) v = w.a()[k];
            else if (!in[w.b()[k]]) v = w.b()[k];
            else continue;                         
            // both ends already in the tree
            in[v] = true;
            added++;
            total += w.cost()[k];
            for (int e : adj[v]) {
                if (!in[w.a()[e]] || !in[w.b()[e]]) pq.add(((long) w.cost()[e] << 32) | e);
            }
        }

        return total;
    }
}
