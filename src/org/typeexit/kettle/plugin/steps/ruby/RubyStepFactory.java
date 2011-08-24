package org.typeexit.kettle.plugin.steps.ruby;

import org.jruby.CompatVersion;
import org.jruby.RubyInstanceConfig.CompileMode;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;
import org.typeexit.kettle.plugin.steps.ruby.RubyStepMeta.RubyVersion;

import java.lang.System;
import java.util.Hashtable;

public class RubyStepFactory {

    private static Hashtable<String,ScriptingContainer> containers = new Hashtable();

	synchronized public static ScriptingContainer createScriptingContainer(boolean withPersistentLocalVars, RubyVersion rubyVersion, String runtimeKey){
	    System.out.println("***** calling createScriptingContainer " + runtimeKey);	

        if (containers.get(runtimeKey) == null) { //TODO or force new runtime anyway based on some option.
        System.out.println("***** new ScriptingContainer");
        ScriptingContainer c = new ScriptingContainer(LocalContextScope.SINGLETHREAD, LocalVariableBehavior.TRANSIENT);//FIXME: won't work with LocalVariableBehavior.PERSISTENT, but is that bad?
        containers.put(runtimeKey, c);

		switch(rubyVersion){
		case RUBY_1_8:
			c.setCompatVersion(CompatVersion.RUBY1_8);
			break;
		case RUBY_1_9:
			c.setCompatVersion(CompatVersion.RUBY1_9);
			break;
		}
		
		c.setCompileMode(CompileMode.JIT);

		c.setRunRubyInProcess(false);
		ClassLoader loader = ScriptingContainer.class.getClassLoader();
		c.setClassLoader(loader);
		
		// does it make sense to include more in the class path? 
		
//		List<String> paths = new ArrayList<String>();
//		paths.add(c.getHomeDirectory());
//		paths.add(ScriptingContainer.class.getProtectionDomain().getCodeSource().getLocation().toString());
//		c.setLoadPaths(paths); 
        }
				
        return containers.get(runtimeKey);
		
	}
	
}
