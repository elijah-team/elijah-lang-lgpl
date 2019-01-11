
TEST_IN = $(shell ls *.os)
TEST_OUT = $(TEST_IN:.os=.os.TEST-OUT)

%.os.TEST-OUT : %.os ../Out.class 
	jre -cp .. Main $< >$@ 2>&1

all: $(TEST_OUT)

clean:
	-rm $(TEST_OUT)

X:
	-rm ../Out.class
	make -C ..
	make clean all

../Out.class: ../Out.java
	make -C ..

cleantest: clean all

distclean: clean
