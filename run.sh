#! /bin/sh

EXE="Hotel"
BIN_DIR="./bin"
REF_LIB="./drivers/mysql-connector-j-8.0.33.jar"
JR="java -cp $BIN_DIR:$REF_LIB"

filename="$1"
if [ -z $filename ]
then
	filename=$EXE
fi

clear
echo "$JR $filename"
eval "$JR $filename"
