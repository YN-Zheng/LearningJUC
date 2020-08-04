class Solution {
    public String longestCommonPrefix(String[] strs) {
        int minSize=Integer.MAX_VALUE;
        int n = strs.length;
        for (int i = 0; i < strs.length; i++) {
            minSize = Math.min(minSize,strs[i].length());
        }
        for (int i = 0; i < minSize; i++) {
            for (int j = 0; j < n-1; j++) {
                if (strs[j].charAt(i) != strs[j].charAt(i+1)){
                    return strs[j].substring(0,i);
                }
            }
        }
        return strs[0].substring(0,minSize);
    }
}