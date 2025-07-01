import java.io.*;
import java.util.*;

public class FixKeyword {
    static class Node {
        String w;
        String wk;
        Node(String w, String wk) { this.w = w; this.wk = wk; }
    }

    static class Node2 implements Comparable<Node2> {
        String name;
        double point;
        Node2(String name, double point) { this.name = name; this.point = point; }
        @Override
        public int compareTo(Node2 other) {
            return Double.compare(this.point, other.point);
        }
    }

    static final int MAX_LEN = 100;
    static long UZ = 9;

    @SuppressWarnings("unchecked")
    static List<Node2>[] weekBest = new ArrayList[7]; //월 ~ 금

    @SuppressWarnings("unchecked")
    static List<Node2>[] twoBest = new ArrayList[2]; //평일, 주말

    public static int levenshtein(String a, String b) {
        int lenA = a.length();
        int lenB = b.length();
        int[][] d = new int[MAX_LEN + 1][MAX_LEN + 1];
        for (int i = 0; i <= lenA; i++) d[i][0] = i;
        for (int j = 0; j <= lenB; j++) d[0][j] = j;
        for (int i = 1; i <= lenA; i++) {
            for (int j = 1; j <= lenB; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    d[i][j] = d[i - 1][j - 1];
                } else {
                    d[i][j] = 1 + Math.min(Math.min(d[i - 1][j], d[i][j - 1]), d[i - 1][j - 1]);
                }
            }
        }
        return d[lenA][lenB];
    }

    public static boolean similer(String a, String b) {
        if (a.isEmpty() && b.isEmpty()) return true;
        if (a.isEmpty() || b.isEmpty()) return false;
        int dist = levenshtein(a, b);
        int maxLen = Math.max(a.length(), b.length());
        double similarity = 1.0 - (double) dist / maxLen;
        int score = 1 + (int) (similarity * 99);
        return score >= 80;
    }

    public static String input2(String w, String wk) {
        UZ++;
        int index = 0;
        if (wk.equals("monday")) index = 0;
        else if (wk.equals("tuesday")) index = 1;
        else if (wk.equals("wednesday")) index = 2;
        else if (wk.equals("thursday")) index = 3;
        else if (wk.equals("friday")) index = 4;
        else if (wk.equals("saturday")) index = 5;
        else if (wk.equals("sunday")) index = 6;

        int index2 = (index >= 0 && index <= 4) ? 0 : 1;
        double point = UZ;
        double max1 = 0, max2 = 0;
        boolean flag = false;

        for (Node2 node : weekBest[index]) {
            if (node.name.equals(w)) {
                max1 = node.point + node.point * 0.1;
                node.point += node.point * 0.1;
                flag = true;
                break;
            }
        }
        for (Node2 node : twoBest[index2]) {
            if (node.name.equals(w)) {
                max2 = node.point + node.point * 0.1;
                node.point += node.point * 0.1;
                break;
            }
        }

        if (UZ >= 2100000000 || max1 >= 2100000000 || max2 >= 2100000000) {
            UZ = 9;
            for (int i = 0; i < 5; i++) {
                int num = 1;
                for (Node2 node : weekBest[i]) {
                    node.point = num++;
                }
            }
            for (int i = 0; i < 2; i++) {
                int num = 1;
                for (Node2 node : twoBest[i]) {
                    node.point = num++;
                }
            }
        }

        if (flag) return w;

        for (Node2 node : weekBest[index]) {
            if (similer(node.name, w)) return node.name;
        }
        for (Node2 node : twoBest[index2]) {
            if (similer(node.name, w)) return node.name;
        }

        if (weekBest[index].size() < 10) {
            weekBest[index].add(new Node2(w, point));
            Collections.sort(weekBest[index]);
        }
        if (twoBest[index2].size() < 10) {
            twoBest[index2].add(new Node2(w, point));
            Collections.sort(twoBest[index2]);
        }

        if (weekBest[index].size() == 10) {
            if (weekBest[index].get(9).point < point) {
                weekBest[index].remove(9);
                weekBest[index].add(new Node2(w, point));
                Collections.sort(weekBest[index]);
            }
        }
        if (twoBest[index2].size() == 10) {
            if (twoBest[index2].get(9).point < point) {
                twoBest[index2].remove(9);
                twoBest[index2].add(new Node2(w, point));
                Collections.sort(twoBest[index2]);
            }
        }

        return w;
    }

    public static void input() throws IOException {
        String path = "src/main/resources/keyword_weekday_500.txt";
        BufferedReader br = new BufferedReader(new FileReader(path));
        for (int i = 0; i < 500; i++) {
            String line = br.readLine();
            if (line == null) break;
            String[] parts = line.split("\\s+");
            String t1 = parts[0], t2 = parts[1];
            System.out.println(input2(t1, t2));
        }
    }

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 7; i++) weekBest[i] = new ArrayList<>();
        for (int i = 0; i < 2; i++) twoBest[i] = new ArrayList<>();
        input();
    }
}
