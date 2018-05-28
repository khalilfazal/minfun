Parser.java: parser.jj
	javacc $<

parser.jj: parser.jjt
	jjtree $<

clean:
	$(RM) *.java
	$(RM) parser.jj