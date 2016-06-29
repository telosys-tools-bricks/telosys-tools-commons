/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.commons.langtypes;

public final class NeutralType {

    private NeutralType(){}

    // Neutral type list of predefined names
    public static final String STRING    = "string";
    
    public static final String BYTE      = "byte"; // Added
    public static final String SHORT     = "short"; // Added
//    public static final String INTEGER   = "integer"; 
    public static final String INTEGER   = "int"; // Changed
    public static final String LONG      = "long"; // Added
    
    public static final String DECIMAL   = "decimal";
    public static final String FLOAT     = "float";
    public static final String DOUBLE    = "double";
    
    public static final String BOOLEAN   = "boolean";
    
    public static final String DATE      = "date";
    public static final String TIME      = "time";
    public static final String TIMESTAMP = "timestamp";
    
    public static final String BINARY   = "binary";   // BLOB
    public static final String LONGTEXT = "longtext"; // CLOB

}
