package com.ly.sjyxt;

public class suans {
  public static void main(String[] args) {
    int arr[][] = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
    int sum = 0;
    int i = 0, j = 0;
    System.out.println(arr[0].length);
    for (i = 0;i<arr.length;i++ ) {
      for (j=0;j<arr[i].length ;j++) {
        sum += arr[i][j];
        System.out.println("sum=" + sum);
      }
      System.out.println("很快就会开花看--");

    }
  }
}