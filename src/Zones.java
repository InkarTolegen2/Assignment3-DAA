import java.util.*;

public class Zones {

    // Mission 4: Kosaraju
    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();
        int[][] out = Graphs.slidesFrom(s);
        int[][] in = Graphs.slidesInto(s);

        // pass 1: DFS on the slides, remember the order of finishing
        boolean[] seen = new boolean[n];
        int[] idx = new int[n];
        int[] order = new int[n];
        int ordCnt = 0;
        int[] stack = new int[n];

        for (int st = 0; st < n; st++) {
            if (seen[st]) continue;
            int top = 0;
            stack[top++] = st;
            seen[st] = true;
            
            while (top > 0) {
                int u = stack[top - 1];
                if (idx[u] < out[u].length) {
                    int v = s.to()[out[u][idx[u]++]];
                    if (!seen[v]) { seen[v] = true; stack[top++] = v; }
                } else {
                    order[ordCnt++] = u;          // u is finished
                    top--;
                }
            }
        }

        // pass 2: reversed slides, from the last finished cell to the first
        int[] zoneOf = new int[n];
        Arrays.fill(zoneOf, -1);
        int zones = 0;
        for (int i = n - 1; i >= 0; i--) {
            int st = order[i];
            if (zoneOf[st] != -1) continue;
            int top = 0;
            stack[top++] = st;
            zoneOf[st] = zones;
            while (top > 0) {
                int u = stack[--top];
                for (int e : in[u]) {
                    int v = s.from()[e];
                    if (zoneOf[v] == -1) { zoneOf[v] = zones; stack[top++] = v; }
                }
            }
            zones++;
        }
        return zoneOf;
    }

    // Mission 5: one pass over the zones in the order of their numbers
    public static long[] routes(Input.Slides s, int[] zoneOf, int zoneCount) {
        int m = s.count();
        // group the slides by the zone they start in
        int[] start = new int[zoneCount + 1];
        for (int i = 0; i < m; i++) start[zoneOf[s.from()[i]] + 1]++;
        for (int z = 0; z < zoneCount; z++) start[z + 1] += start[z];
        int[] byZone = new int[m];
        int[] fill = Arrays.copyOf(start, zoneCount);
        for (int i = 0; i < m; i++) byZone[fill[zoneOf[s.from()[i]]]++] = i;

        long INF = Long.MAX_VALUE / 4;
        long[] dist = new long[zoneCount];
        int[] stages = new int[zoneCount];
        Arrays.fill(dist, INF);
        Arrays.fill(stages, 1);
        dist[zoneOf[0]] = 0;

        for (int z = 0; z < zoneCount; z++) {
            for (int j = start[z]; j < start[z + 1]; j++) {
                int i = byZone[j];
                int y = zoneOf[s.to()[i]];
                if (y == z) continue;             
                // inside a zone: free, not an edge
                stages[y] = Math.max(stages[y], stages[z] + 1);
                if (dist[z] < INF) dist[y] = Math.min(dist[y], dist[z] + s.time()[i]);
            }
        }
        long best = 0;
        for (int z = 0; z < zoneCount; z++) best = Math.max(best, stages[z]);
        return new long[]{best, dist[zoneOf[s.n() - 1]]};
    }
}
