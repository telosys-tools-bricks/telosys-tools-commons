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
package org.telosys.tools.commons.exception;

import java.io.File;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * @author Laurent GUERIN
 * 
 */
public class TelosysYamlException extends Exception  {
	
	private static final long serialVersionUID = 1L;
	
	private final String     yamlFile;
	private final Exception  exception;
	private final String     yamlError;

    /**
     * Constructor
     * @param yamlFile
     * @param exception
     */
    public TelosysYamlException(File yamlFile, Exception exception) {
        super("Error with YAML file " + yamlFile.getName(), exception);
        this.yamlFile = yamlFile.getAbsolutePath() ;
        this.exception = exception ;
        if ( exception instanceof YAMLException ) {
            this.yamlError = buildYamlError((YAMLException) exception);
        }
        else {
            this.yamlError = exception.getMessage() ;
        }
    }

    public String getYamlFile() {
		return yamlFile;
	}

	public Exception getException() {
		return exception;
	}

	public String getYamlError() {
		return yamlError;
	}

	/*
     * SNAKE YAML EXCEPTIONS HIERARCHY :
     * RuntimeException
     *     YAMLException
     *         EmitterException (message only)
     *         MarkedYAMLException(String context, Mark contextMark, String problem, Mark problemMark)
     *             ComposerException
     *             ConstructorException
     *                 DuplicateKeyException
     *             ParserException
     *             ScannerException
     *         SerializerException (message only)   
     */
    private String buildYamlError(YAMLException yamlException) {
    	StringBuilder sb = new StringBuilder();
    	Mark mark = getMark(yamlException);
    	if ( mark != null ) {
    		int line = mark.getLine() + 1;
        	sb.append("line " + line);
    	}
    	return sb.toString();    	
    }
    
    private Mark getMark(YAMLException yamlException) {
		if ( yamlException instanceof MarkedYAMLException ) {
			MarkedYAMLException mye = (MarkedYAMLException) yamlException ;
			return mye.getProblemMark();
		}
		else {
			Throwable cause = yamlException.getCause();
			if ( cause instanceof YAMLException) { // instanceof returns false for null
				return getMark((YAMLException) cause);
			}
		}
		return null;
    }

}