@echo off
chcp 65001
javac -encoding UTF-8 *.java
java Main
type output.txt
pause