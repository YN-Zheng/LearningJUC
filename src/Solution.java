import java.util.HashMap;

class Solution {

    /*
        Symbol       Value
        I             1
        V             5
        X             10
        L             50
        C             100
        D             500
        M             1000
     */
    HashMap<Character, Integer> map = new HashMap<>();

    {
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);
    }

    public int romanToInt(String s) {
        int ret = 0;
        int i = 0;
        int count = 0;
        while (i < s.length()) {
            char temp = s.charAt(i);
            while (++i < s.length() && map.get(s.charAt(i)).equals(map.get(temp))) {
                count++;
            }
            if (i >= s.length()) {
                return ret += count * map.get(temp);
            } else if (count == 1 && map.get(temp) > map.get(s.charAt(i))) {
                ret += map.get(temp) - map.get(s.charAt(i));
                i++;
            } else {
                ret += map.get(temp) * count;
            }
        }
        return ret;
    }

}