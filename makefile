PROJECT = Mfun

$(PROJECT).jar: ./bin/
	jar -cfve $(PROJECT).jar main.$(PROJECT) -C ./bin .

./bin:
	mkdir ./bin
	$(MAKE) -C ./src/main compile

clean:
	$(RM) *.jar
	$(RM) -r ./bin/
	$(RM) ./output/*
	$(MAKE) -C ./src/parser clean
