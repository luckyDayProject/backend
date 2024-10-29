package io.swyp.luckybackend;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class Temp {
    public static void main(String[] args) throws Exception {
        ArrayList<int[]> list = new ArrayList<>();

        // 쌍을 추가
        list.add(new int[]{1, 3});
        list.add(new int[]{2, 5});
        list.add(new int[]{2, 3});
        list.add(new int[]{3, 4});
        list.add(new int[]{4, 5});
        list.add(new int[]{1, 5});
        list.add(new int[]{1, 2});
        list.add(new int[]{2, 4});

        HashMap<Integer, HashSet<Integer>> map = new HashMap<>();
        int count = 0;

        try {
            // 입력받은 값을 맵으로 전환
            // (나는 입력 어캐 받는지 모르겠어서 걍 위처럼 깡 리스트로 만들어버림..)
            for (int[] pair : list) {
                int a = pair[0];
                int b = pair[1];
                map.computeIfAbsent(a, k -> new HashSet<>()).add(b);
                map.computeIfAbsent(b, k -> new HashSet<>()).add(a);
            }

            // 모든 키를 순회하면서 비교 작업 수행
            for (int key : map.keySet()) {
                // key 값들에 대해 비교
                for (Integer value : map.get(key)) {
                    // value가 키인 항목에서 키 값을 제외한 나머지 원소들을 비교
                    for (Integer nextValue : map.get(value)) {
                        if (nextValue == key) {
                            continue; // 키값이 바로 나오는 경우 제외
                        }
                        // nextValue 키의 원소들을 확인하여 키 값이 있으면 카운트 증가
                        if (map.get(nextValue).contains(key)) {
                            count++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 리스트가 비어있으면 count를 0으로 설정
            count = 0;
        }
        // 중복되는 경우의 수 제거
        count /= 6;
        System.out.println("Total count: " + count);
    }
}


