package com.jomeuan.unibbs.valid;

import java.util.Collection;

import com.jomeuan.unibbs.vo.R;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * key_order:key包括(time,likes,collects,comments),order包括(asc,desc);
 * 例如time_desc按发表时间晚早排序,likes_asc按照点赞数少多排序, // TODO :目前仅支持单一key
 */
@Data
@AllArgsConstructor
public class SortRule {
    SortType key;
    boolean isAsc;

    public static enum SortType {
        TIME("time"), LIKE("like"), COLLECT("collect");

        String type;

        SortType(String key) {
            this.type = key.toLowerCase();
        }

        @Override
        public String toString() {
            return this.type;
        }

        public boolean equals(String key) {
            return this.type.equals(key);
        }
    }

    public static SortRule parseSortRule(String sortruleString) {

        String[] sortRuleTokens = sortruleString.split("_");
        if (sortRuleTokens.length != 2) {
            throw new IllegalArgumentException("Invalid sort rule: " + sortruleString);
        }

        boolean isAsc;
        if (sortRuleTokens[1].equals("asc")) {
            isAsc = true;
        } else if (sortRuleTokens[1].equals("desc")) {
            isAsc = false;
        } else {
            throw new IllegalArgumentException("Invalid sort roder: " + sortruleString);
        }

        for (SortType st : SortType.values()) {
            if (st.equals(sortRuleTokens[1])) {
                return new SortRule(st, isAsc);
            }
        }
        throw new IllegalArgumentException("Invalid sort rule: " + sortruleString);
    }
}
