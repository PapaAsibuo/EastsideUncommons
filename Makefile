all:
	javac pka224.java
	jar cfmv pka224.jar Manifest.txt pka224.class
	java -jar pka224.jar