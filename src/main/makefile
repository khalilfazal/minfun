PROJECT = Mfun
CLASSPATH = ../../lib/jscience-4.3.1-bin.jar:../../lib/guava-14.0.jar:../../lib/javatuples-1.3.jar

compile: ../parser/Parser.java
	javac $(PROJECT).java -sourcepath .. -cp $(CLASSPATH) -d ../../bin/

../parser/Parser.java:
	$(MAKE) -C ../parser
