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
package org.telosys.tools.commons.javatypes;

import java.util.LinkedList;

//import org.objectweb.telosys.plugin.commons.MsgBox;

/**
 * List of association "text" - "type" ( list of JavaType instances )
 * @author L. Guerin
 *
 */
public class JavaTypes {
	
	private LinkedList<JavaType> _list = new LinkedList<JavaType>() ;
	
	/**
	 * @param text
	 * @param type
	 */
	public JavaTypes() {
		super();
	}

	public void add(String text, String type, String defaultValue)
	{
		_list.add( new JavaType(text,type,defaultValue) );
	}
	
	/**
	 * Returns the text to print in the "UI view" ( ie "Boolean", "Date (sql)", ... )
	 * @param i the index of the type in the list
	 * @return
	 */
	public String getText(int i)
	{
		if ( i >= 0 && i < _list.size() )
		{
			JavaType t = (JavaType) _list.get(i) ;
			return t.getText();
		}
		else
		{
			//MsgBox.error("getText("+i+") : invalid index !");
			return "xxx";
		}
	}
	
	/**
	 * Returns the full Java type ( ie "java.lang.Boolean", "boolean", ... )
	 * @param i the index of the type in the list
	 * @return
	 */
	public String getType(int i)
	{
		if ( i >= 0 && i < _list.size() )
		{
			JavaType t = (JavaType) _list.get(i) ;
			return t.getType();
		}
		else
		{
			return "xxx";
		}
	}

	/**
	 * Returns the short Java type ( ie "Boolean", "boolean", "Integer", "int", ... )
	 * @param i the index of the type in the list
	 * @return
	 */
	public String getShortType(int i)
	{
		if ( i >= 0 && i < _list.size() )
		{
			JavaType t = (JavaType) _list.get(i) ;
			return t.getShortType();
		}
		else
		{
			return "xxx";
		}
	}

	/**
	 * Returns an array containing all the texts to print in the "UI view"
	 * @return
	 */
	public String[] getTexts()
	{
		int size = _list.size();
		String[] array = new String[size];
		for ( int i = 0 ; i < size ; i++ )
		{
			array[i] = getText(i);
		}
		return array ;
	}

	/**
	 * Returns the index of the given java type in the list 
	 * @param sType
	 * @return index or -1 if not found
	 */
	public int getTypeIndex( String sType )
	{
		int size = _list.size();
		for ( int i = 0 ; i < size ; i++ )
		{
			if ( sType.equals( getType(i) ) )
			{
				return i ;
			}
		}
		return -1 ;
	}
	
	private JavaType getJavaTypeObjectForFullType( String sFullType )
	{
		if ( sFullType == null ) return null ;
		for ( JavaType javaType : _list ) {
			if ( sFullType.equals( javaType.getType() ) )
			{
				return javaType ;
			}
		}
		return null ;
	}
	
	/**
	 * Returns the text associated with the given full java type 
	 * @param sFullType ( "java.util.Date" )
	 * @return the text to show to the user (or null if not found)
	 */
	public String getTextForType( String sFullType )
	{
//		if ( sFullType == null ) return null ;
//		Iterator iter = _list.iterator();
//		while ( iter.hasNext() )
//		{
//			JavaType t = (JavaType) iter.next() ;
//			if ( sFullType.equals( t.getType() ) )
//			{
//				return t.getText() ;
//			}
//		}
//		return null ;
		
		JavaType t = getJavaTypeObjectForFullType( sFullType );
		if ( t != null ) return t.getText() ;
		return null ;
	}
	
	/**
	 * Returns the default value for the given full java type 
	 * @param sFullType ( "java.util.Date" )
	 * @return the default value if defined ( else null )
	 */
	public String getDefaultValueForType( String sFullType )
	{
		JavaType t = getJavaTypeObjectForFullType( sFullType );
		if ( t != null ) return t.getDefaultValue() ;
		return null ;
	}
	
	/**
	 * Returns the "full Java type" corresponding to the given "short type"
	 * e.g. : returns "java.util.Date" for "Date"
	 * @param sShortType ( "Date" )
	 * @return
	 */
	public String getTypeForShortType( String sShortType )
	{
		if ( sShortType == null ) return null ;
		for ( JavaType t : _list ) {
			if ( sShortType.equals( t.getShortType() ) )
			{
				return t.getType() ;
			}
		}
		return null ;
	}
	
	/**
	 * Returns the list size
	 * @return
	 */
	public int size()
	{
		return _list.size();
	}
}
