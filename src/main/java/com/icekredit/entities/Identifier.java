package com.icekredit.entities;

import java.util.List;

/**
 * Created by icekredit on 12/24/16.
 */
public interface Identifier {
    int MODIFIER_TYPE_PUBLIC = 1;
    int MODIFIER_TYPE_PRIVATE = 2;
    int MODIFIER_TYPE_PROTECTED = 4;
    int MODIFIER_TYPE_STATIC = 8;
    int MODIFIER_TYPE_FINAL = 16;

    String show();
}
