all:
	ecj -g -cp lib/javassist-3.1/javassist.jar \
    -nowarn src/main/java/ -d bin -Xemacs \
    -log errors
