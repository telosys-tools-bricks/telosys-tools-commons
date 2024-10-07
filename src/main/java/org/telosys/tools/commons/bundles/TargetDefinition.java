/**
 *  Copyright (C) 2008-2017  Telosys project org. ( http://www.telosys.org/ )
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
package org.telosys.tools.commons.bundles;


/**
 * A generation target definition : <br>
 * what file to generate, where, with which template, etc...
 * 
 * @author Laurent Guerin
 *
 */
public class TargetDefinition 
{

	private final String  name  ;

	private final String  file  ;
	
	private final String  folder ;

	private final String  template ; // or resource ( since v 2.0.7 )

	private final String  type ; // "1", "R", "*" or ""

	//-----------------------------------------------------------------------
	/**
	 * Constructor 
	 * @param name the target name (to be displayed in the UI)
	 * @param file the file to be generated ( ie "${BEANNAME}Data.java" )
	 * @param folder the folder where to generate the file ( ie "src/org/demo/screen/${BEANNAME_LC}" )
	 * @param template the template to use ( ie "vo_screen_data.vm" )
	 * @param type the template type : "1" for "ONCE", "R" for "resource", else standard entity target (can be VOID if none)
	 */
	public TargetDefinition(String name, String file, String folder, String template, String type ) 
	{
		super();
		this.name = name;
		this.file = file;
		this.folder = folder;
		this.template = template;
		this.type = ( type != null ? type.trim() : "" ) ;
	}
	
	//-----------------------------------------------------------------------
	/**
	 * Returns a string that can be used as a unique id for a target
	 * @return
	 */
	public String getId()
	{
		return template + ":" + type + ":" + folder + ":" + file ;
	}
	
	//-----------------------------------------------------------------------
	/**
	 * Returns the target name ( the text displayed on the screen )
	 * @return
	 */
	public String getName()
	{
		return name ;
	}
	
	//-----------------------------------------------------------------------
	/**
	 * Returns the target file name ( file to be generated )
	 * Can contains a generic variable BEANNAME, BEANNAME_UC, BEANNAME_LC
	 * if the target if generic and "applyBeanClassName" as not been called
	 * @return
	 */
	public String getFile()
	{
		return file ;
	}
	
	//-----------------------------------------------------------------------
	/**
	 * Returns the folder where to generate the file
	 * Can contains a generic variable BEANNAME, BEANNAME_UC, BEANNAME_LC
	 * if the target if generic and "applyBeanClassName" as not been called
	 * @return
	 */
	public String getFolder()
	{
		return folder ;
	}
	
	//-----------------------------------------------------------------------
	public String getFullFileName()
	{
		if ( folder.endsWith("/") || folder.endsWith("\\") )
		{
			return folder + file ;
		}
		return folder + "/" + file ;
	}
	
	//-----------------------------------------------------------------------
	/**
	 * Returns the template 
	 * @return
	 */
	public String getTemplate()
	{
		return template ;
	}	
	
	//-----------------------------------------------------------------------
	/**
	 * Returns true if the target is for just "once" generation (not linked to an entity)
	 * @return
	 */
	public boolean isOnce()
	{
		return "1".equals(type) ;
	}

	//-----------------------------------------------------------------------
	/**
	 * Returns true if the target is for a resource file or a resource folder (not linked to an entity)
	 * @return
	 */
	public boolean isResource()
	{
		return "R".equals(type) ;
	}

	//-----------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return name + " : '" + file  + "' in '" + folder + "' ( " + template + " " + type + " )" ;
	}
}
