package com.david.interview.transfer.dao;

import com.david.interview.transfer.model.Handout;
import com.david.interview.transfer.model.Transfer;
import org.springframework.stereotype.Repository;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class HandoutDao {
    //mock db
    public static Map<String, Handout> map = new ConcurrentHashMap<>();

    public Handout create(Handout e) {
        synchronized (this) {
            if (map.get(e.getId()) != null) {
                throw new RuntimeException("重复记录");
            }
            map.put(e.getId(), e);
            return e;
        }
    }

    public Handout update(Handout e) {
        map.put(e.getId(), e);
        return e;
    }

    public Handout load(String id) {
        return map.get(id);
    }
}
