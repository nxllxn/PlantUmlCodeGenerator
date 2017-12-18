package com.icekredit.utils;

import com.icekredit.entities.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by icekredit on 12/24/16.
 */
public class ContactUtil {
    public static String join(List identifiers, String delimiter){
        if(identifiers == null || identifiers.size() == 0){
            return "";
        }

        List<Integer> nonDuplicateList = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for(Object object:identifiers){
            if(nonDuplicateList.contains(object.toString().hashCode())){
                continue;
            } else {
                nonDuplicateList.add(object.toString().hashCode());
            }

            if(!isFirst){
                builder.append(delimiter);
            }

            builder.append(((Identifier)object).show());

            isFirst = false;
        }

        return builder.toString();
    }
}
