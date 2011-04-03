/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctp.cdi.query.util;

import java.text.MessageFormat;

/**
 *
 * @author thomashug
 */
public abstract class QueryUtils {
    
    private static final String KEYWORD_SPLITTER = "({0})(?=[A-Z])";
    
    public static String[] splitByKeyword(String query, String keyword) {
        return query.split(MessageFormat.format(KEYWORD_SPLITTER, keyword));
    }
    
}
