package sorting;

import java.util.ArrayList;

public class QuickSort {

	public static int Partition(int[] a,int left, int right) {
		int X = a[left];
		int i = left+1;
		int j = right;
		int t;
		
		do {
			while ((i<=j)&&(a[i] <= X))
				i++;
			while ((i<=j)&&(a[j] > X))
			j--;
			
			//doing swap here
			if (i<j) {
				
				t = a[i];
				a[i]= a[j];
				a[j]=t; 
			}
			
		}while (i<=j);
		
		t = a[left];
		a[left] = a[j];
		a[j] =t;
		
		return j;
	}
	
	public static int Partition2(int a[],int left, int right) {
		int X = a[left];
		int i = left+1;
		int j = right;
		int t;
		
		do {
			while ((i<=j)&&(a[i] >= X))
				i++;
			while ((i<=j)&&(a[j] < X))
			j--;
			
			//doing swap here
			if (i<j) {
				
				t = a[i];
				a[i]= a[j];
				a[j]=t; 
			}
			
		}while (i<=j);
		
		t = a[left];
		a[left] = a[j];
		a[j] =t;
		
		return j;
	}
	

	
	
	public static void quicksort(int[]a,int left,int right) {
		
		int k ;
		if (left < right) {
			
			k = Partition(a, left, right);
			quicksort(a,left,k-1);
			quicksort(a,k+1,right);
		}
	}
	
	
public static void quicksortReverse(int[] a,int left,int right) {
		
		int k ;
		if (left < right) {
			
			k = Partition2(a, left, right);
			quicksortReverse(a,left,k-1);
			quicksortReverse(a,k+1,right);
		}
	}


}
