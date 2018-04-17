package rColt;

import java.util.Arrays;

public class listSearcher {

    public listSearcher() {
    }

    public int binarySearch(int search, int[] array) {
        bubbleSort(array);
        int low = 0;
        int high = array.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int midVal = array[mid];
            if (midVal < search)
                low = mid + 1;
            else if (midVal > search)
                high = mid - 1;
            else
                // key found
                return mid;
        }
        return -1;
    }

    public int binarySearch___original(int search, int[] array) {
        Arrays.sort(array);
        int start = 0;
        int end = array.length - 1;

        while (start <= end) {

            int middle = (start + end) / 2;

            if (search < array[middle]) {
                end = middle - 1;
            }

            if (search > array[middle]) {
                start = middle + 1;
            }

            if (search == array[middle]) {
                return middle;
            }
        }
        return -1;
    }

    public void bubbleSort(int[] array) {
        boolean swapped = true;
        int j = 0;
        int tmp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < array.length - j; i++) {
                if (array[i] > array[i + 1]) {
                    tmp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = tmp;
                    swapped = true;
                }
            }
        }
    }
}
