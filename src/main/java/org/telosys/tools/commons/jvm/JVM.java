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
package org.telosys.tools.commons.jvm;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.RuntimeMXBean;
import java.util.Locale;
import java.util.StringJoiner;

public class JVM {

	private static final long MB = 1024L * 1024L;

	private JVM() {
	}
	
	public static String getBasicReport() {
		StringJoiner status = new StringJoiner("\n");

		status.add("JVM version: " + System.getProperty("java.version"));

		// Compilation info
		CompilationMXBean compiler = ManagementFactory.getCompilationMXBean();
		status.add("JIT Compiler: '" + compiler.getName() + "'  (total compilation time: " + compiler.getTotalCompilationTime() + " ms)");
		
		// Class loading info
		ClassLoadingMXBean classLoading = ManagementFactory.getClassLoadingMXBean();
	    status.add("Classes: loaded = " + classLoading.getLoadedClassCount() + ", total loaded = " +  classLoading.getTotalLoadedClassCount() + ", unloaded = " + classLoading.getUnloadedClassCount() );

		// Command-Line Arguments
		status.add("Command-Line Arguments:");
		RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
		for (String arg : runtimeMxBean.getInputArguments()) {
			status.add(" " + arg);
		}

		// Memory pool info  ( "-Xmx" => impact on heap memory pools "getMax", "-Xms" => impact on heap memory pools "getCommitted()" )
		// Only one Survivor pool exposed (not the 2 "survivor" pools)
		// there are always 2 Survivor spaces used in generational garbage collectors that implement a Young Generation, regardless of Java version (from Java 1.4 through Java 21+).
	    status.add("Memory Pools: " );
	    long totalHeapMax = 0 ;
		for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            long used = pool.getUsage().getUsed();
            long max = pool.getUsage().getMax(); // may be -1 (undefined)
            String freePercentStr = "";
            if ( max > 0 ) {
    			double freePercent = ((double)(max - used) / max) * 100 ;
    			freePercentStr = String.format(Locale.US, "%d Mb  %.2f%% free", max/MB,  freePercent);
            }
            else {
            	freePercentStr = "max is undefined";
            }
            // heap size
            String heap = "" ; 
            if ( pool.getType() == MemoryType.HEAP ) {
                heap = "(HEAP)"; 
                totalHeapMax = totalHeapMax + pool.getUsage().getMax();
            }
            // "survivor" pool ?
            String survivor = "";
			if ( pool.getName().contains("Survivor") || pool.getName().contains("survivor") ) {
	            survivor = "(survivor x 2)";
                totalHeapMax = totalHeapMax + pool.getUsage().getMax();
			}
		    status.add(" . '" + pool.getName() + "' " + heap + ": " + freePercentStr + " " + survivor );
		    status.add("    " + pool.getUsage());
		}
	    status.add(" Max total size of 'heap' pools = " + totalHeapMax + " (" + totalHeapMax/MB + " Mb)");

		// GC info
	    status.add("Garbage Collectors: " );
		for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
		    status.add(" . '" + gc.getName() + "':  collection count = " + gc.getCollectionCount() + ", collection time = " + gc.getCollectionTime() + " ms");
		}

		return status.toString();
	}
}
