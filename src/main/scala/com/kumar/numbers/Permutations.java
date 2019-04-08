package com.kumar.numbers;

import java.util.*;

public class Permutations {
    private static void permutations(List<List<Integer>> result, int K, List<Integer> current, List<Integer> remaining) {
        if(current.size() == K) {
            result.add(current);
            return;
        }

        if(remaining.isEmpty()) return;

        for(int i = 0; i < remaining.size(); ++i) {
            List<Integer> cc = new ArrayList<>(current);
            cc.add(remaining.get(i));

            List<Integer> rr = new ArrayList<>();
            rr.addAll(remaining.subList(0, i));
            rr.addAll(remaining.subList(i + 1, remaining.size()));

            permutations(result, K, cc, rr);
        }
    }

    public static List<List<Integer>> permutations(List<Integer> input, int k) {
        if(k > input.size()) throw new IllegalArgumentException("Cannot choose more than available input!");

        List<List<Integer>> result = new ArrayList<>();
        permutations(result, k, new ArrayList<>(), input);

        return result;
    }

    public static void main(String args[]) {
        try {
            System.err.println("Hello Permuations!");

            List<Integer> N = new ArrayList<>();
            N.add(1);
            N.add(2);
            N.add(3);

            List<List<Integer>> results = permutations(N, 3);
            System.err.println("Result size: " + results.size());
            for(List<Integer> result : results) {
                System.err.println(result);
            }

            System.err.println("All Done!");
        } catch(Throwable t) {
            System.err.println("Exception: " + t.getMessage());
            t.printStackTrace(System.err);
        }
    }
}
