package rColt.symbv;

import rColt.listSearcher;

public class rColt___listSearcher___binarySearch {
    public static void main(String args[]) {
        rColt___listSearcher___binarySearch t = new rColt___listSearcher___binarySearch();
        t.run(0, new int[4]);
    }
    public void run(int search, int[] array) {
        listSearcher patched = new listSearcher();
        listSearcher original = new listSearcher();

        int patchedResult = patched.binarySearch(search, array);
        int originalResult = original.binarySearch___original(search, array);

        if (originalResult == patchedResult) {
            throw new Error();
        }
    }
}
