@echo off
chcp 65001
javac -encoding UTF-8 *.java
java Main error_sample.bhasha
type output.txt
pause