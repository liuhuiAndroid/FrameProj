package com.lh.frameproj.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by we-win on 2017/4/5.
 */

public class MapSortUtil {
    public static void main(String[] args) {


        //  需求：对hashmap的value的值的大小进行逆序排序
        // 创建一个HashMap 然后填充数据
        HashMap<String, Object> oldhMap = new HashMap<>();
        oldhMap.put("a", 12);
        oldhMap.put("b", 53);
        oldhMap.put("c", 41);
        oldhMap.put("d", 24);

        HashMap<String, Object> newMap = sortMap(oldhMap);
        printMap(oldhMap, newMap);
        // ===================== 测试
        HashMap<String, Object> stringObjectMap = new HashMap<>();
//        stringObjectMap.put("app_type", "android");
//        stringObjectMap.put("device_id", "2345");
//        stringObjectMap.put("OS_type", "23456");
        long l = System.currentTimeMillis();

        System.out.println("timestamp = "+l);
        stringObjectMap.put("timestamp", l+"");
        stringObjectMap.put("key", "B272F43387B8504D");
        stringObjectMap.put("mobile", "15300936557");
        stringObjectMap.put("codeType", "1");

        HashMap<String, Object> stringObjectHashMap = MapSortUtil.sortMap(stringObjectMap);
        Iterator iterator = stringObjectHashMap.entrySet().iterator();
        String result = "";
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            result += key.toString();
            result += val.toString();
        }
        result += "70BAE8B491362AB39042B77C7653199C";
        try {
            System.out.println("result = "+result);
            String generateDigest = SecurityUtil.generateDigest(result, l+"");
            System.out.println("generateDigest = "+generateDigest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 对map集合进行逆序排序
     * @param oldhMap
     * @return
     */
    public static HashMap<String, Object> sortMap(Map<String, Object> oldhMap) {

        TreeMap<String,Object> treeMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return rhs.compareTo(lhs);
            }
        });
        Iterator iterator = oldhMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            Object val = entry.getValue();
            treeMap.put(key,val);
        }
        //创建一个map
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        Iterator<Map.Entry<String, Object>> iterator1 = treeMap.entrySet().iterator();
        while (iterator1.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator1.next();
            map.put((String) entry.getKey(), entry.getValue());
        }

        return map;
    }


    /**
     * 打印map集合
     * @param oldhMap 老集合
     * @param newMap   排序后的新集合
     */
    private static void printMap(HashMap<String, Object> oldhMap,
                                 HashMap<String, Object> newMap) {

        System.out.println(oldhMap.toString());
        System.out.println(newMap.toString());
    }

}
