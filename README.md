# jenkins-shared-library

Shared library in jenkins helps to eliminate the common patterns of code & can be used to define the common patterns as funtions can be called from your pipelines.

Note: you can have One shared library per Jenkins.

### Here is the structure of your Jenkins-Shared-Library

...
(root)
+- src                     # Groovy source files
|   +- org
|       +- foo
|           +- Bar.groovy  # for org.foo.Bar class
+- vars
|   +- foo.groovy          # for global 'foo' variable
|   +- foo.txt             # help for 'foo' variable
+- resources               # resource files (external libraries only)
|   +- org
|       +- foo
|           +- bar.json    # static helper data for org.foo.Bar

...

The `src`` directory should look like standard Java source directory structure. This directory is added to the classpath when executing Pipelines.

The vars directory hosts script files that are exposed as a variable in Pipelines. The name of the file is the name of the variable in the Pipeline. So if you had a file called vars/log.groovy with a function like def info(message)…​ in it, you can access this function like log.info "hello world" in the Pipeline. You can put as many functions as you like inside this file. Read on below for more examples and options.

# In groovy if we want to declare any variable, we need to use env. (eg: "env.ARGS")

>>> Release Strategy :

---
1) when you push commits to feature branch : Only lintChecks & code scan
2) when changes reach main branch over a PR, that means code it stage and then if we push a tag marking it stable
3) When we run the job against a TAG, only during that time, I want artifacts should be created and should be pushed to nexus. 
---