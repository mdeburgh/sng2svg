cd ../gem
cd ../gensrc
javac -sourcepath ../src *.java
cd ../src
javac -sourcepath ../gensrc *.java
copy *.class ..\gensrc
cd ../make
