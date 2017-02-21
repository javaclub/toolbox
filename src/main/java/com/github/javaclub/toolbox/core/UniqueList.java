/*
 * @(#)UniqueList.java	2011-8-8
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core;

import java.util.ArrayList;
import java.util.Collection;

/**
 * UniqueList
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: UniqueList.java 231 2011-08-08 14:24:57Z gerald.chen.hz@gmail.com $
 */
public class UniqueList<T> extends ArrayList<T> {

	private static final long serialVersionUID = -4415279469780082174L;

    public boolean add( T t ){
        if ( contains( t ) )
            return false;
        return super.add( t );
    }
    
    public boolean addAll(Collection<? extends T> c) {
        boolean added = false;
        for ( T t : c )
            added = added || add( t );
        return added;
    }
}
