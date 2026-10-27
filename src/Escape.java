public class Escape {

    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells(), rows = w.rows(), cols = w.cols();
        int[][] g = Graphs.open(w, broken);

        int deadEnds = 0;
        for (int i = 0; i < n; i++) if (g[i].length == 1) deadEnds++;

        int exit = n - 1;
        boolean[] seen = new boolean[n];
        int[] parent = new int[n];
        int[] dir = new int[n];                  
        // next direction to try in each cell: 0 up, 1 right, 2 down, 3 left
        int[] stack = new int[n];
        int top = 0, visited = 1;
        seen[0] = true;
        parent[0] = -1;
        stack[top++] = 0;

        while (top > 0 && !seen[exit]) {
            int u = stack[top - 1];
            if (dir[u] == 4) { top--; continue; }   
            // all directions tried: go back
            int d = dir[u]++;
            int r = u / cols, c = u % cols;
            int v = -1;
            if (d == 0 && r > 0) v = u - cols;
            else if (d == 1 && c < cols - 1) v = u + 1;
            else if (d == 2 && r < rows - 1) v = u + cols;
            else if (d == 3 && c > 0) v = u - 1;
            if (v < 0 || seen[v] || !contains(g[u], v)) continue;
            seen[v] = true;
            visited++;
            parent[v] = u;
            stack[top++] = v;
        }

        int len = 0;
        for (int x = exit; x != -1; x = parent[x]) len++;
        int[] path = new int[len];
        for (int x = exit, i = len - 1; x != -1; x = parent[x], i--) path[i] = x;
        return new Result(path, visited, deadEnds);
    }

    private static boolean contains(int[] list, int x) {
        for (int y : list) if (y == x) return true;
        return false;
    }
}
